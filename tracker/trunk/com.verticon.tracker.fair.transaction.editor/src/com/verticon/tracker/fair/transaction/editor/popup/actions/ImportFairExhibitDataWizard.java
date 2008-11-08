package com.verticon.tracker.fair.transaction.editor.popup.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.verticon.tracker.TrackerPackage;
import com.verticon.tracker.fair.Fair;
import com.verticon.tracker.fair.FairPackage;


/**
 * 
 * Wizard for importing Fair Exhibit data into a Fair model. Pages:
 * <ul>
 * <li>Prompt for mapping spreadsheet column names to Fair and Tracker Features</li>
 * <li>Prompt for Lot.</li>
 * </ul>
 * @author jconlon
 *
 */
public class ImportFairExhibitDataWizard extends Wizard  {

	/**
	 * slf4j Logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ImportFairExhibitDataWizard.class);
	
	private static final String WINDOW_TITLE = "ImportExhibitData";
	
	/**
	 * Reference to the list of {@link EStructuralFeature}s that
	 * will be shown offered as features for mapping to spreadsheet
	 * columns.
	 */
	public static final List<EStructuralFeature> featuresList ;
	
	/**
	 * Active workbench
	 */
	private IWorkbench workbench;
	
	/**
	 * Reference to the workSheet that is the source of the import data.
	 */
	private HSSFSheet workSheet;
	
	/**
	 * Reference to the Fair which will be the target for import data.
	 */
	private Fair fair = null;
	
	/**
	 * 
	 */
	private ImportFairDataColumnMappingWizardPage importFairDataColumnMappingPage;
	
	private BaseConfigureExhibitWizardPage exhibitPage;
	
	private int processedRows = 0;
	
	private ExecutableProcreator procreator = null;
	
    static{
    	List<EStructuralFeature> list = new ArrayList<EStructuralFeature>(); 
		initializeTheFeaturesToMap(list);
		featuresList = Collections.unmodifiableList(list);
	}

	/**
	 * Called by the {@link ImportFairExhibitDataWizardDelegate} to initialize the wizard.
	 * @param workbench
	 * @param selection
	 * @param fair
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection, Fair fair) {
		this.workbench=workbench;
		File file = getSelectedResource( selection);
		workSheet = getWorkSheet(file);
		this.fair =fair;
		setNeedsProgressMonitor(true);
	}

	/**
	 * Add the default sequence of pages
	 */
	@Override
	public void addPages() {
		setWindowTitle(WINDOW_TITLE);
		importFairDataColumnMappingPage = new ImportFairDataColumnMappingWizardPage();
		addPage(importFairDataColumnMappingPage);
		exhibitPage = new BaseConfigureExhibitWizardPage("selectLot", fair);
		exhibitPage.setDescription("Select the Lot to use for creating all new Exhibits");
		exhibitPage.setTitle("Select a Lot");
		addPage(exhibitPage);
		
	}
	
	@Override
	public boolean canFinish() {
		return importFairDataColumnMappingPage.mapContainsCompleteDeptClassLot() || 
		(exhibitPage!=null && exhibitPage.getSelectedLot()!=null);
	}
	
	@Override
	public boolean performFinish() {

		if(importFairDataColumnMappingPage.mapContainsCompleteDeptClassLot()){
			procreator = ExecutableProcreators.newDivisionProcreator();
		}else{
			procreator = ExecutableProcreators.newExhibitProcreator();
		}
		
		IRunnableWithProgress op = new
			WorkspaceModifyDelegatingOperation(getRunnable());

		try {
			getContainer().run(false, true, op);
			Exception e = procreator.getError();
			
			if(e !=null){
				procreator.dispose();
				MessageDialog.openError(workbench.getDisplay().getActiveShell(),
						"Fair Data Import Failed", e.getMessage());
				return false;
				
			}else{
				String message = "Imported " +
				procreator.getStatus()+
				"from "+processedRows+" spreadsheet rows.";
				procreator.dispose();

				MessageDialog.openInformation(workbench.getDisplay().getActiveShell(),
						"Fair Data Import", message);
				return true;
			}

			
			
		} catch (InvocationTargetException e) {
			exhibitPage.setErrorMessage("Fair Data Import Error. "+e.getMessage());
		} catch (InterruptedException e) {
			// Restore the interrupted status
			Thread.currentThread().interrupt();
		}
		return false;
	}

    @Override
	public IWizardPage getNextPage(IWizardPage page) {
    	if(page==importFairDataColumnMappingPage && importFairDataColumnMappingPage.mapContainsCompleteDeptClassLot()){
    		return null;
    	}
		return super.getNextPage(page);
	}

	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		// TODO Auto-generated method stub
		return super.getPreviousPage(page);
	}

    HSSFSheet getWorkSheet() {
		return workSheet;
	}

	List<EStructuralFeature> getFeaturesToMap() {
		return featuresList;
	}
	
	private IRunnableWithProgress getRunnable(){
		return new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				monitor.beginTask("Processing worksheet...", workSheet.getLastRowNum()+10);
				IEditorPart editorPart = workbench.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
				EditingDomain editingDomain = getEditingDomain(editorPart);
				
				processedRows = processWorksheet(workSheet, fair, 
						importFairDataColumnMappingPage.getHeaderRow()+1, monitor, editingDomain);
				fair.eResource().setModified(true);
				
			}};
	}
	
	private static void initializeTheFeaturesToMap(List<EStructuralFeature> list) {
		list.add(FairPackage.Literals.FAIR__NAME);
		list.add(FairPackage.Literals.PERSON__NAME);
		for (EStructuralFeature feature : FairPackage.Literals.PERSON.getEAllStructuralFeatures()) {
			if(!feature.isDerived()){
				list.add(feature);
			}
		}
		list.add(FairPackage.Literals.YOUNG_PERSON__PARENTS);
		list.add(FairPackage.Literals.YOUNG_PERSON__CLUB);
		
		
		list.add(FairPackage.Literals.DIVISION__NAME);
		list.add(FairPackage.Literals.DIVISION__DESCRIPTION);
		list.add(FairPackage.Literals.DIVISION__COMMENTS);
		
		list.add(FairPackage.Literals.DEPARTMENT__NAME);
		list.add(FairPackage.Literals.DEPARTMENT__DESCRIPTION);
		list.add(FairPackage.Literals.DEPARTMENT__COMMENTS);
		
		list.add(FairPackage.Literals.CLASS__NAME);
		list.add(FairPackage.Literals.CLASS__DESCRIPTION);
		list.add(FairPackage.Literals.CLASS__COMMENTS);
		
		list.add(FairPackage.Literals.LOT__NAME);
		list.add(FairPackage.Literals.LOT__DESCRIPTION);
		list.add(FairPackage.Literals.LOT__COMMENTS);
		
		for (EStructuralFeature feature : FairPackage.Literals.EXHIBIT.getEAllStructuralFeatures()) {
			if(!feature.isDerived()){
				list.add(feature);
			}
		}
		
		list.add(TrackerPackage.Literals.SWINE__LEFT_EAR_NOTCHING);
		list.add(TrackerPackage.Literals.SWINE__RIGHT_EAR_NOTCHING);
		list.add(TrackerPackage.Literals.OVINE__SCRAPIE_TAG);
		list.add(TrackerPackage.Literals.ANIMAL__VISUAL_ID);
		
	}
	
	private static File getSelectedResource(
			ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IResource resource = (IResource) ((IStructuredSelection) selection).getFirstElement();
			return new File(resource.getLocationURI());
		}
		return null;
	}
	
	private HSSFSheet getWorkSheet(File f){
        FileInputStream fin =null;
        HSSFSheet s = null;
        try {
        	// create a new file input stream with the input file specified
            // at the command line
            fin = new FileInputStream(f);
            HSSFWorkbook w = new HSSFWorkbook(fin);
            s = w.getSheetAt(0);
		} catch (Exception e) {
			logger.error("Failed to process the spreadsheet",e);
			// Create the required Status object
	        Status status = new Status(IStatus.ERROR, "com.verticon.tracker.fair.editor", 0,
	            "Failed to open the Fair Editor", e);
	        // Display the dialog
	        ErrorDialog.openError(workbench.getActiveWorkbenchWindow().getShell(),
	            "Fair Data Import Error", "Please insure the data file contains valid column names and row values.", status);
	        
		}finally{
			// once all the events are processed close our file input stream
		    if(fin!= null){
		    	try {
					fin.close();
				} catch (IOException e) {
					
				}
		    }
		}
		return s;
        
	}

	/**
	 * @param adaptable
	 * @return
	 */
	private static EditingDomain getEditingDomain(IAdaptable adaptable) {
		IEditingDomainProvider queryDataSetProvider = 
			(IEditingDomainProvider)adaptable.getAdapter(IEditingDomainProvider.class);
		if(queryDataSetProvider==null){
			return null;
		}
		EditingDomain editingDomain = queryDataSetProvider.getEditingDomain();
		return editingDomain;
	}
	
	/**
	 * Process the Worksheet and import data into the Fair.
	 * @param sheet
	 * @param fair
	 * @param firstDataRow
	 * @param monitor
	 * @param editingDomain
	 * @return
	 * @throws MissingCriticalDataException 
	 */
	private int processWorksheet(HSSFSheet sheet, Fair fair, int firstDataRow, IProgressMonitor monitor,
			EditingDomain editingDomain) {

		int count = 0;

		EObject root = null;
		if(importFairDataColumnMappingPage.mapContainsCompleteDeptClassLot()){
			root = fair;
			logger.debug("Importing {} rows of worksheet data specifying Division, Department, Class and Lot.", sheet.getLastRowNum());
		}else{
			root = exhibitPage.getSelectedLot();
			logger.debug("Importing {} rows of worksheet data to a selected Lot.", sheet.getLastRowNum());
		}
		for (int i = firstDataRow; i < sheet.getLastRowNum()+1; i++) {
			logger.debug("processing row {}",i);

			try {
				monitor.worked(1);
				procreator.prepare(fair, sheet.getRow(i), 
						importFairDataColumnMappingPage.getSpreadSheetColumnsToFeatureMap(),
						root, false, editingDomain);

				count++;

			} catch (RuntimeException e) {
				logger.error("Failed to process row "+i,e);
			} catch (MissingCriticalDataException e) {
				logger.error("Failed to process row "+i,e);
				return count;
			}
		}
		//Add all newly imported Divisions, Departments, Classes, and Lots
		logger.debug("Loading worksheet data.");
		procreator.execute();
		monitor.worked(10);
		return count;
	}

}
