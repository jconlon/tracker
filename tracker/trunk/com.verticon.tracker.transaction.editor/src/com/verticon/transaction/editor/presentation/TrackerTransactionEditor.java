/**
 * <copyright>
 * </copyright>
 *
 * $Id: EXTLibraryEditor.java,v 1.4 2006/05/18 21:19:26 cdamus Exp $
 */
package com.verticon.transaction.editor.presentation;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.action.EditingDomainActionBarContributor;
import org.eclipse.emf.edit.ui.celleditor.AdapterFactoryTreeEditor;
import org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.emf.edit.ui.dnd.ViewerDragAdapter;
import org.eclipse.emf.edit.ui.provider.NotifyChangedToViewerRefresh;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.ui.provider.TransactionalAdapterFactoryContentProvider;
import org.eclipse.emf.transaction.ui.provider.TransactionalAdapterFactoryLabelProvider;
import org.eclipse.emf.transaction.ui.view.ExtendedPropertySheetPage;
import org.eclipse.emf.workspace.EMFCommandOperation;
import org.eclipse.emf.workspace.IWorkspaceCommandStack;
import org.eclipse.emf.workspace.ResourceUndoContext;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;

import com.verticon.tracker.Animal;
import com.verticon.tracker.Event;
import com.verticon.tracker.Premises;
import com.verticon.tracker.TrackerPackage;
import com.verticon.tracker.edit.provider.TrackerItemProviderAdapterFactory;
import com.verticon.tracker.editor.presentation.IAnimalSelectionProvider;
import com.verticon.tracker.editor.presentation.ICustomActionBarContributor;
import com.verticon.tracker.editor.presentation.IEventSelectionProvider;
import com.verticon.tracker.editor.presentation.ISelectionViewerProvider;
import com.verticon.tracker.editor.presentation.SelectionViewerFilter;
import com.verticon.tracker.editor.presentation.TrackerTableEditorUtils;
import com.verticon.tracker.editor.util.ConsoleUtil;
import com.verticon.tracker.transaction.editor.TransactionEditorPlugin;


/**
 * Tracker transaction aware model editor.
 * <!-- begin-user-doc -->
 * This particular implementation is customized from the default editor generated
 * by EMF.  This editor differs from the default EMF implementation in the following
 * ways:
 * <ul>
 *   <li>all instances operate in a single, shared
 *       {@link TransactionalEditingDomain transactional editing domain}</li>
 *   <li>a {@link ResourceSetListener} is statically registered on this editing
 *       domain that automatically creates editors for any resource loaded (e.g.,
 *       by proxy resolution)</li>
 *   <li>the editing domain delegates command-stack functionality to the worbench
 *       {@link IOperationHistory}.  It provides its {@link IUndoContext} to
 *       specialized implementations of the undo/redo actions that operate on the
 *       operation history.  Execution of commands is also delegated to the
 *       operation history</li>
 *   <li>refreshing of the tree content and property sheet is performed within
 *       read-only transactions on the editing domain, using the
 *       {@link TransactionalEditingDomain#runExclusive(Runnable)} API</li>
 *   <li>only the 'selection' tree view is provided (it is not a multi-page editor)</li>
 *   <li>synchronization of the workspace resource with the loaded EMF resource
 *       uses the {@link WorkspaceSynchronizer} utility API</li>
 * </ul>
 * 
 * Customized similarly generated Tracker Editor by implementing these interfaces
 * IEventSelectionProvider, IAnimalSelectionProvider, ISelectionViewerProvider 
 * TODO always un NOT this class if Editor changes are made, but be sure to add the above interfaces and NOT it back.
 * <!-- end-user-doc -->
 * @generated NOT
 */
public class TrackerTransactionEditor
	extends MultiPageEditorPart
	implements IEventSelectionProvider, IAnimalSelectionProvider, ISelectionViewerProvider, IEditingDomainProvider, ISelectionProvider, IMenuListener, IViewerProvider, IGotoMarker {
	
	private static final String CONSOLE = TrackerTransactionEditor.class.getSimpleName();
	
	/**
	 * This keeps track of the editing domain that is used to track all changes to the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AdapterFactoryEditingDomain editingDomain;

	/**
	 * Animals Table Viewer
	 * @generated NOT
	 */
	protected TableViewer animalsTableViewer;
	
	/**
	 * Events Table Viewer
	 * @generated NOT
	 */
	protected TableViewer eventsTableViewer;
	
	protected IUndoContext undoContext;
	protected Resource resource;  // the resource that we are editing
	
	/**
	 * This keeps track of the active viewer pane, in the book.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ViewerPane currentViewerPane;
	
	/**
	 * This is the one adapter factory used for providing views of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComposedAdapterFactory adapterFactory;

	/**
	 * This is the content outline page.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IContentOutlinePage contentOutlinePage;

	/**
	 * This is a kludge...
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IStatusLineManager contentOutlineStatusLineManager;

	/**
	 * This is the content outline page's viewer.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TreeViewer contentOutlineViewer;

	/**
	 * This is the property sheet page.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PropertySheetPage propertySheetPage;

	/**
	 * This is the viewer that shadows the selection in the content outline.
	 * The parent relation must be correctly defined for this to work.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TreeViewer selectionViewer;

	/**
	 * This keeps track of the active content viewer, which may be either one of the viewers in the pages or the content outline viewer.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Viewer currentViewer;

	/**
	 * This listens to which ever viewer is active.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ISelectionChangedListener selectionChangedListener;

	/**
	 * This keeps track of all the {@link org.eclipse.jface.viewers.ISelectionChangedListener}s that are listening to this editor.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Collection<ISelectionChangedListener> selectionChangedListeners = new ArrayList<ISelectionChangedListener>();

	/**
	 * This keeps track of the selection of the editor as a whole.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ISelection editorSelection = StructuredSelection.EMPTY;

	/**
	 * This listens for when the outline becomes active
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IPartListener partListener =
		new IPartListener() {
			public void partActivated(IWorkbenchPart p) {
				if (p instanceof ContentOutline) {
					if (((ContentOutline)p).getCurrentPage() == contentOutlinePage) {
						getActionBarContributor().setActiveEditor(TrackerTransactionEditor.this);

						setCurrentViewer(contentOutlineViewer);
					}
				}
				else if (p instanceof PropertySheet) {
					if (((PropertySheet)p).getCurrentPage() == propertySheetPage) {
						getActionBarContributor().setActiveEditor(TrackerTransactionEditor.this);
						handleActivate();
					}
				}
				else if (p == TrackerTransactionEditor.this) {
					handleActivate();
				}
			}
			public void partBroughtToTop(IWorkbenchPart p) {
			}
			public void partClosed(IWorkbenchPart p) {
			}
			public void partDeactivated(IWorkbenchPart p) {
			}
			public void partOpened(IWorkbenchPart p) {
			}
		};

	/**
	 * Resources that have been removed since last activation.
	 * @generated
	 */
	Collection<Resource> removedResources = new ArrayList<Resource>();

	/**
	 * Resources that have been changed since last activation.
	 * @generated
	 */
	Collection<Resource> changedResources = new ArrayList<Resource>();

	/**
	 * Resources that have been moved since last activation.
	 * Maps {@link Resource resource} to {@link URI new URI}
	 */
	Map<Resource, URI> movedResources = new HashMap<Resource, URI>();

	/**
	 * Resources that have been saved.
	 * @generated
	 */
	Collection<Resource> savedResources = new ArrayList<Resource>();
	
	private boolean dirty;

	private IOperationHistoryListener historyListener = new IOperationHistoryListener() {
		public void historyNotification(final OperationHistoryEvent event) {
			if (event.getEventType() == OperationHistoryEvent.DONE) {
				Set<?> affectedResources = ResourceUndoContext.getAffectedResources(
						event.getOperation());
				
				if (affectedResources.contains(getResource())) {
					final IUndoableOperation operation = event.getOperation();
					
					// remove the default undo context so that we can have
					//     independent undo/redo of independent resource changes
					operation.removeContext(((IWorkspaceCommandStack)
							getEditingDomain().getCommandStack()).getDefaultUndoContext());
					
					// add our undo context to populate our undo menu
					operation.addContext(getUndoContext());
					
					getSite().getShell().getDisplay().asyncExec(new Runnable() {
						public void run() {
							dirty = true;
							firePropertyChange(IEditorPart.PROP_DIRTY);

							// Try to select the affected objects.
							//
							if (operation instanceof EMFCommandOperation) {
								Command command = ((EMFCommandOperation) operation).getCommand();
								
								if (command != null) {
									setSelectionToViewer(command
											.getAffectedObjects());
								}
							}
							
							if (propertySheetPage != null) {
								propertySheetPage.refresh();
							}
						}
					});
				}
			}
		}};
	
	/**
	 * Synchronizes workspace changes with the editing domain.
	 */
	protected WorkspaceSynchronizer workspaceSynchronizer;
	
	/**
	 * Handles activation of the editor or it's associated views.
	 */
	protected void handleActivate() {
		
		// Recompute the read only state.
		//
		if (editingDomain.getResourceToReadOnlyMap() != null) {
		  editingDomain.getResourceToReadOnlyMap().clear();

		  // Refresh any actions that may become enabled or disabled.
		  //
		  setSelection(getSelection());
		}

		try {
			final Resource res = getResource();
			
			if (removedResources.contains(res)) {
				if (handleDirtyConflict()) {
					getSite().getPage().closeEditor(TrackerTransactionEditor.this, false);
					TrackerTransactionEditor.this.dispose();
				}
			} else if (movedResources.containsKey(res)) {
				if (savedResources.contains(res)) {
					getOperationHistory().dispose(undoContext, true, true, true);
					
					// change saved resource's URI and remove from map
					res.setURI((URI) movedResources.remove(res));
						
					// must change my editor input
					IEditorInput newInput = new FileEditorInput(
							WorkspaceSynchronizer.getFile(res));
					setInputWithNotify(newInput);
					setPartName(newInput.getName());
				} else {
					handleMovedResource();
				}
			} else if (changedResources.contains(res)) {
				changedResources.removeAll(savedResources);
				handleChangedResource();
			}
		} finally {
			removedResources.clear();
			changedResources.clear();
			movedResources.clear();
			savedResources.clear();
		}
	}

	private WorkspaceSynchronizer.Delegate createSynchronizationDelegate() {
		return new WorkspaceSynchronizer.Delegate() {
			public boolean handleResourceDeleted(Resource resource) {
				if ((resource == getResource()) && !isDirty()) {
					// just close now without prompt
					getSite().getShell().getDisplay().asyncExec
						(new Runnable() {
							 public void run() {
								 getSite().getPage().closeEditor(TrackerTransactionEditor.this, false);
								 TrackerTransactionEditor.this.dispose();
							 }
						 });
				} else {
					removedResources.add(resource);
				}
				
				return true;
			}
			
			public boolean handleResourceChanged(Resource resource) {
				changedResources.add(resource);
				
				return true;
			}
			
			public boolean handleResourceMoved(Resource resource, URI newURI) {
				movedResources.put(resource, newURI);
				
				return true;
			}
			
			public void dispose() {
				removedResources.clear();
				changedResources.clear();
				movedResources.clear();
			}};
	}
	
	/**
	 * Handles what to do with changed resource on activation.
	 */
	protected void handleChangedResource() {
		Resource res = getResource();
		
		if (changedResources.contains(res) && (!isDirty() || handleDirtyConflict())) {
			changedResources.remove(res);
			
			getOperationHistory().dispose(undoContext, true, true, true);
			dirty = false;
			firePropertyChange(IEditorPart.PROP_DIRTY);

			ResourceLoadedListener listener = ResourceLoadedListener.getDefault();
			listener.ignore(res);
			
			try {
				if (res.isLoaded()) {
					res.unload();
					try {
						res.load(Collections.EMPTY_MAP);
					} catch (IOException exception) {
						TransactionEditorPlugin.INSTANCE.log(exception);
					}
				}
	
				selectionViewer.setInput(getResource());
			} finally {
				listener.watch(res);
			}
		}
	}
	
	/**
	 * Handles what to do with moved resource on activation.
	 */
	protected void handleMovedResource() {
		if (!isDirty() || handleDirtyConflict()) {
			Resource res = getResource();
			URI newURI = (URI) movedResources.get(res);
			
			if (newURI != null) {
				if (res.isLoaded()) {
					// unload
					res.unload();
				}

				// load the new URI in another editor
				res.getResourceSet().getResource(newURI, true);
			}
		}
	}

	/**
	 * Shows a dialog that asks if conflicting changes should be discarded.
	 * 
	 * @generated
	 */
	protected boolean handleDirtyConflict() {
		return
			MessageDialog.openQuestion
				(getSite().getShell(),
				 getString("_UI_FileConflict_label"), //$NON-NLS-1$
				 getString("_WARN_FileConflict")); //$NON-NLS-1$
	}

	/**
	 * This creates a model editor.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public TrackerTransactionEditor() {
		super();

		// Create an adapter factory that yields item providers.
		//
		List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
		factories.add(new ResourceItemProviderAdapterFactory());
		factories.add(new TrackerItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());

		adapterFactory = new ComposedAdapterFactory(factories);

		// Get the registered workbench editing domain.
		//
		editingDomain = (AdapterFactoryEditingDomain) TransactionalEditingDomain.Registry.INSTANCE.getEditingDomain(
				"com.verticon.transaction.editor.TrackerEditingDomain"); //$NON-NLS-1$
		editingDomain.getResourceSet().getAdapterFactories().add(adapterFactory);
		undoContext = new ObjectUndoContext(
				this,
				TransactionEditorPlugin.getPlugin().getString("_UI_EXTLibraryEditor_label")); //$NON-NLS-1$
		getOperationHistory().addOperationHistoryListener(historyListener);
	}

	/**
	 * This is here for the listener to be able to call it. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void firePropertyChange(int action) {
		super.firePropertyChange(action);
	}

	/**
	 * This sets the selection into whichever viewer is active. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSelectionToViewer(Collection<?>  collection) {
		final Collection<?>  theSelection = collection;
		// Make sure it's okay.
		//
		if (theSelection != null && !theSelection.isEmpty()) {
			// I don't know if this should be run this deferred
			// because we might have to give the editor a chance to process the viewer update events
			// and hence to update the views first.
			//
			//
			Runnable runnable =
				new Runnable() {
					public void run() {
						// Try to select the items in the current content viewer of the editor.
						//
						if (currentViewer != null) {
							currentViewer.setSelection(new StructuredSelection(theSelection.toArray()), true);
						}
					}
				};
			runnable.run();
		}
	}

	/**
	 * This returns the editing domain as required by the {@link IEditingDomainProvider} interface.
	 * This is important for implementing the static methods of {@link AdapterFactoryEditingDomain}
	 * and for supporting {@link org.eclipse.emf.edit.ui.action.CommandAction}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public class ReverseAdapterFactoryContentProvider extends TransactionalAdapterFactoryContentProvider {
		public ReverseAdapterFactoryContentProvider(AdapterFactory adapterFactory) {
			super((TransactionalEditingDomain) getEditingDomain(), adapterFactory);
		}

		public Object [] getElements(Object object) {
			Object parent = super.getParent(object);
			return (parent == null ? Collections.EMPTY_SET : Collections.singleton(parent)).toArray();
		}

		public Object [] getChildren(Object object) {
			Object parent = super.getParent(object);
			return (parent == null ? Collections.EMPTY_SET : Collections.singleton(parent)).toArray();
		}

		public boolean hasChildren(Object object) {
			Object parent = super.getParent(object);
			return parent != null;
		}

		public Object getParent(Object object) {
			return null;
		}
	}

	/**
	 * This makes sure that one content viewer, either for the current page or the outline view, if it has focus,
	 * is the current one.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCurrentViewer(Viewer viewer) {
		// If it is changing...
		//
		if (currentViewer != viewer) {
			if (selectionChangedListener == null) {
				// Create the listener on demand.
				//
				selectionChangedListener =
					new ISelectionChangedListener() {
						// This just notifies those things that are affected by the section.
						//
						public void selectionChanged(SelectionChangedEvent selectionChangedEvent) {
							setSelection(selectionChangedEvent.getSelection());
						}
					};
			}

			// Stop listening to the old one.
			//
			if (currentViewer != null) {
				currentViewer.removeSelectionChangedListener(selectionChangedListener);
			}

			// Start listening to the new one.
			//
			if (viewer != null) {
				viewer.addSelectionChangedListener(selectionChangedListener);
			}

			// Remember it.
			//
			currentViewer = viewer;

			// Set the editors selection based on the current viewer's selection.
			//
			setSelection(currentViewer == null ? StructuredSelection.EMPTY : currentViewer.getSelection());
		}
	}

	/**
	 * This returns the viewer as required by the {@link IViewerProvider} interface.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Viewer getViewer() {
		return currentViewer;
	}

	/**
	 * This creates a context menu for the viewer and adds a listener as well registering the menu for extension.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createContextMenuFor(StructuredViewer viewer) {
		MenuManager contextMenu = new MenuManager("#PopUp"); //$NON-NLS-1$
		contextMenu.add(new Separator("additions")); //$NON-NLS-1$
		contextMenu.setRemoveAllWhenShown(true);
		contextMenu.addMenuListener(this);
		Menu menu= contextMenu.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(contextMenu, viewer);

		int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
		Transfer[] transfers = new Transfer[] { LocalTransfer.getInstance() };
		viewer.addDragSupport(dndOperations, transfers, new ViewerDragAdapter(viewer));
		viewer.addDropSupport(dndOperations, transfers, new EditingDomainViewerDropAdapter(editingDomain, viewer));
	}

	/**
	 * This is the method called to load a resource into the editing domain's resource set based on the editor's input.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createModel() {
		// I assume that the input is a file object.
		//
		IFileEditorInput modelFile = (IFileEditorInput)getEditorInput();

		try {
			// Load the resource through the editing domain.
			//
			resource = editingDomain.loadResource(URI.createPlatformResourceURI(
					modelFile.getFile().getFullPath().toString(), true).toString());
		}
		catch (Exception exception) {
			TransactionEditorPlugin.INSTANCE.log(exception);
		}
	}
	
	protected Resource getResource() {
		return resource;
	}
	
	public IUndoContext getUndoContext() {
		return undoContext;
	}

	
	@Override
	protected void createPages() {
		createModel();

		createSelectionTreeViewer(getString("_UI_SelectionPage_label"));
		createEventsTableViewer(getString("_UI_EventsTablePage_label"));
		createAnimalsTableViewer("Animals");
		
		IEditorActionBarContributor abc = getActionBarContributor();
		if(abc != null && abc instanceof TrackerActionBarContributor){
			TrackerActionBarContributor trackerActionBarContributor =(TrackerActionBarContributor)abc;
			SelectionViewerFilter svf = trackerActionBarContributor.customActionBarContributor.getSelectionViewerFilter();
			trackerActionBarContributor.customActionBarContributor.getSelectionViewerFilter().setMainViewer(selectionViewer);
			svf.addViewer(eventsTableViewer);
			svf.addViewer(animalsTableViewer);
		}
		setActivePage(0);
		
		// Ensures that this editor will only display the page's tab
		// area if there are more than one page
		//
		getContainer().addControlListener
			(new ControlAdapter() {
				boolean guard = false;
				public void controlResized(ControlEvent event) {
					if (!guard) {
						guard = true;
						hideTabs();
						guard = false;
					}
				}
			 });

//		updateProblemIndication();
		
	}
	
	/**
	 * This is the method used by the framework to install your own controls.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
//	public void createPartControl(Composite parent) {
	protected void createSelectionTreeViewer(String pageName){

		ViewerPane viewerPane =
			new ViewerPane(getSite().getPage(), TrackerTransactionEditor.this) {
				public Viewer createViewer(Composite composite) {
					Tree tree = new Tree(composite, SWT.MULTI);
					TreeViewer newTreeViewer = new TreeViewer(tree);
					return newTreeViewer;
				}
				public void requestActivation() {
					super.requestActivation();
					setCurrentViewerPane(this);
				}
			};
		viewerPane.createControl(getContainer());

		selectionViewer = (TreeViewer)viewerPane.getViewer();

		selectionViewer.setContentProvider(
				new TransactionalAdapterFactoryContentProvider(
						(TransactionalEditingDomain) getEditingDomain(), 
						adapterFactory
				)
		);

		selectionViewer.setLabelProvider(
				new TransactionalAdapterFactoryLabelProvider(
						(TransactionalEditingDomain) getEditingDomain(), adapterFactory));
		
		// unlike other EMF editors, I edit only a single resource, not a resource set
		selectionViewer.setInput(getResource());

		new AdapterFactoryTreeEditor(selectionViewer.getTree(), adapterFactory);

		createContextMenuFor(selectionViewer);
		
		int pageIndex = addPage(viewerPane.getControl());
		setPageText(pageIndex,pageName );
	}
	
	
	/**
	 * Events Table
	 */
	private void createEventsTableViewer(String tableName) {
		ViewerPane viewerPane =
			new ViewerPane(getSite().getPage(), TrackerTransactionEditor.this) {
			public Viewer createViewer(Composite composite) {
				return new TableViewer(composite);
			}
			public void requestActivation() {
				super.requestActivation();
				setCurrentViewerPane(this);
				this.getViewer().refresh();
			}
		};
		viewerPane.createControl(getContainer());
		eventsTableViewer = (TableViewer)viewerPane.getViewer();
		
		TrackerTableEditorUtils.createEventsTableViewer(
				viewerPane, 
				eventsTableViewer, 
				adapterFactory);

		
		Object rootObject = getRoot();
		if (rootObject instanceof Premises){
			Premises premises = (Premises)rootObject;
			eventsTableViewer.setInput(premises);
			viewerPane.setTitle(premises);
			//FIXME Kludge to refresh the EventsTable and animals table. 
			premises.eAdapters().add(new MyContentAdapter());
		}
		createContextMenuFor(eventsTableViewer);
		int pageIndex = addPage(viewerPane.getControl());
		setPageText(pageIndex, tableName);
		
	}
	
	
	/**
	 * Animals Table
	 * References fields animalsTableViewer, 
	 * Note: Implemented and committed task for Ticket 139 under Ticket 134 by mistake.
	 * @param tableName 
	 */
	private void createAnimalsTableViewer(String tableName) {
		ViewerPane viewerPane =
			new ViewerPane(getSite().getPage(), TrackerTransactionEditor.this) {
			public Viewer createViewer(Composite composite) {
				return new TableViewer(composite);
			}
			public void requestActivation() {
				super.requestActivation();
				setCurrentViewerPane(this);
				this.getViewer().refresh();
			}
		};
		viewerPane.createControl(getContainer());
		animalsTableViewer = (TableViewer)viewerPane.getViewer();
		
		TrackerTableEditorUtils.createAnimalsTableViewer(
				viewerPane, 
				animalsTableViewer, 
				adapterFactory);
		
		
		Object rootObject = getRoot();
		if (rootObject instanceof Premises){
			Premises premises = (Premises)rootObject;
			animalsTableViewer.setInput(premises);
			viewerPane.setTitle(premises);
		}
		createContextMenuFor(animalsTableViewer);
		int pageIndex = addPage(viewerPane.getControl());
		setPageText(pageIndex, tableName);
		  
	}
	
	/**
	 * Kludge to refresh the EventsTable and AnimalsTable.  
	 * @author jconlon
	 *
	 */
	class MyContentAdapter extends EContentAdapter{
		public MyContentAdapter() {
			super();
		}
		
		@Override
		public void notifyChanged(Notification notification){
			super.notifyChanged(notification);//needed to walk the entire model

			if (notification.getFeature() == TrackerPackage.eINSTANCE.getTag_Events() && 
					TrackerTransactionEditor.this.eventsTableViewer != null && 
					TrackerTransactionEditor.this.eventsTableViewer.getControl() != null && 
					!TrackerTransactionEditor.this.eventsTableViewer.getControl().isDisposed())
		    {
		    	  printToConsole("Calling NotifiedChangedToViewerRefresh: "+notification.getFeature());
		        NotifyChangedToViewerRefresh.handleNotifyChanged(
		          TrackerTransactionEditor.this.eventsTableViewer,
		          notification.getNotifier(),
		          notification.getEventType(),
		          notification.getFeature(),
		          notification.getOldValue(),
		          notification.getNewValue(),
		          notification.getPosition());
		      }else if (notification.getFeature() == TrackerPackage.eINSTANCE.getPremises_Animals() && 
						TrackerTransactionEditor.this.animalsTableViewer != null && 
						TrackerTransactionEditor.this.animalsTableViewer.getControl() != null && 
						!TrackerTransactionEditor.this.animalsTableViewer.getControl().isDisposed())
			    {
			    	  printToConsole("Calling NotifiedChangedToViewerRefresh: "+notification.getFeature());
			        NotifyChangedToViewerRefresh.handleNotifyChanged(
			          TrackerTransactionEditor.this.animalsTableViewer,
			          notification.getNotifier(),
			          notification.getEventType(),
			          notification.getFeature(),
			          notification.getOldValue(),
			          notification.getNewValue(),
			          notification.getPosition());
		    	  
		      }
		    }
	}
	
	
	/**
	 * @return
	 */
	private Object getRoot() {
		Resource resource = getResource();
		  Object rootObject = resource.getContents().get(0);
		return rootObject;
	}
	/**
	 * If there is just one page in the multi-page editor part,
	 * this hides the single tab at the bottom.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void hideTabs() {
		if (getPageCount() <= 1) {
			setPageText(0, "");
			if (getContainer() instanceof CTabFolder) {
				((CTabFolder)getContainer()).setTabHeight(1);
				Point point = getContainer().getSize();
				getContainer().setSize(point.x, point.y + 6);
			}
		}
	}

	/**
	 * If there is more than one page in the multi-page editor part,
	 * this shows the tabs at the bottom.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void showTabs() {
		if (getPageCount() > 1) {
			setPageText(0, getString("_UI_SelectionPage_label"));
			if (getContainer() instanceof CTabFolder) {
				((CTabFolder)getContainer()).setTabHeight(SWT.DEFAULT);
				Point point = getContainer().getSize();
				getContainer().setSize(point.x, point.y - 6);
			}
		}
	}

	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCurrentViewerPane(ViewerPane viewerPane) {
		if (currentViewerPane != viewerPane) {
			if (currentViewerPane != null) {
				currentViewerPane.showFocus(false);
			}
			currentViewerPane = viewerPane;
		}
		setCurrentViewer(currentViewerPane.getViewer());
	}

	
	/**
	 * This is how the framework determines which interfaces we implement.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class key) {
		if (key.equals(IContentOutlinePage.class)) {
			return getContentOutlinePage();
		}
		else if (key.equals(IPropertySheetPage.class)) {
			return getPropertySheetPage();
		}
		else if (key.equals(IGotoMarker.class)) {
			return this;
		}
		else if (key.equals(IUndoContext.class)) {
			// used by undo/redo actions to get their undo context
			return undoContext;
		}
		else {
			return super.getAdapter(key);
		}
	}

	/**
	 * This accesses a cached version of the content outliner.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public IContentOutlinePage getContentOutlinePage() {
		if (contentOutlinePage == null) {
			// The content outline is just a tree.
			//
			class MyContentOutlinePage extends ContentOutlinePage {
				public void createControl(Composite parent) {
					super.createControl(parent);
					contentOutlineViewer = getTreeViewer();
					contentOutlineViewer.addSelectionChangedListener(this);

					// Set up the tree viewer.
					//
					contentOutlineViewer.setContentProvider(
						new TransactionalAdapterFactoryContentProvider(
							(TransactionalEditingDomain) getEditingDomain(), adapterFactory));
					contentOutlineViewer.setLabelProvider(
						new TransactionalAdapterFactoryLabelProvider(
							(TransactionalEditingDomain) getEditingDomain(), adapterFactory));
					
					// unlike other EMF editors, I edit only a single resource, not a resource set
					contentOutlineViewer.setInput(getResource());

					// Make sure our popups work.
					//
					createContextMenuFor(contentOutlineViewer);

					if (!editingDomain.getResourceSet().getResources().isEmpty()) {
					  // Select the root object in the view.
					  //
					  ArrayList<Resource> selection = new ArrayList<Resource>();
					  selection.add(getResource());
					  contentOutlineViewer.setSelection(new StructuredSelection(selection), true);
					}
				}

				public void makeContributions(IMenuManager menuManager, IToolBarManager toolBarManager, IStatusLineManager statusLineManager) {
					super.makeContributions(menuManager, toolBarManager, statusLineManager);
					contentOutlineStatusLineManager = statusLineManager;
				}

				public void setActionBars(IActionBars actionBars) {
					super.setActionBars(actionBars);
					getActionBarContributor().shareGlobalActions(this, actionBars);
				}
			}

			contentOutlinePage = new MyContentOutlinePage();

			// Listen to selection so that we can handle it is a special way.
			//
			contentOutlinePage.addSelectionChangedListener
				(new ISelectionChangedListener() {
					 // This ensures that we handle selections correctly.
					 //
					 public void selectionChanged(SelectionChangedEvent event) {
						 handleContentOutlineSelection(event.getSelection());
					 }
				 });
		}

		return contentOutlinePage;
	}

	/**
	 * This accesses a cached version of the property sheet.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public IPropertySheetPage getPropertySheetPage() {
		if (propertySheetPage == null) {
			propertySheetPage =
				new ExtendedPropertySheetPage(editingDomain) {
					public void setSelectionToViewer(List<?> selection) {
						TrackerTransactionEditor.this.setSelectionToViewer(selection);
						TrackerTransactionEditor.this.setFocus();
					}

					public void setActionBars(IActionBars actionBars) {
						super.setActionBars(actionBars);
						getActionBarContributor().shareGlobalActions(this, actionBars);
					}
				};
			propertySheetPage.setPropertySourceProvider(new TransactionalAdapterFactoryContentProvider((TransactionalEditingDomain) getEditingDomain(), adapterFactory));
		}

		return propertySheetPage;
	}

	/**
	 * This deals with how we want selection in the outliner to affect the other views.
	 * <!-- begin-user-doc -->
	 * Ties in the eventsTableViewer to the selection in the outliner.
	 * <!-- end-user-doc -->
	 */
	public void handleContentOutlineSelection(ISelection selection) {
		if (currentViewerPane != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
			Iterator<?> selectedElements = ((IStructuredSelection)selection).iterator();
			if (selectedElements.hasNext()) {
				// Get the first selected element.
				//
				Object selectedElement = selectedElements.next();

				// If it's the selection viewer, then we want it to select the same selection as this selection.
				//
				if (currentViewerPane.getViewer() == selectionViewer ) {
					ArrayList<Object> selectionList = new ArrayList<Object>();
					selectionList.add(selectedElement);
					while (selectedElements.hasNext()) {
						selectionList.add(selectedElements.next());
					}

					// Set the selection to the widget.
					//
					selectionViewer.setSelection(new StructuredSelection(selectionList));
				// Handle animalsTableViewer
				} else if (currentViewerPane.getViewer() == eventsTableViewer){
					if(selectedElement instanceof Event){

						ArrayList<Object> selectionList = new ArrayList<Object>();
						selectionList.add(selectedElement);
						while (selectedElements.hasNext()) {
							Object o = selectedElements.next();
							if(o instanceof Event){
								selectionList.add(o);
							}

						}

						// Set the selection to the widget.
						//
						eventsTableViewer.setSelection(new StructuredSelection(selectionList));
					}
					
				
				}
				else if (currentViewerPane.getViewer() == animalsTableViewer){
					if(selectedElement instanceof Animal){

						ArrayList<Object> selectionList = new ArrayList<Object>();
						selectionList.add(selectedElement);
						while (selectedElements.hasNext()) {
							Object o = selectedElements.next();
							if(o instanceof Animal){
								selectionList.add(o);
							}

						}

						// Set the selection to the widget.
						//
						animalsTableViewer.setSelection(new StructuredSelection(selectionList));
					}
					
				
				}
				else {
					// Set the input to the widget.
					//
					if (currentViewerPane.getViewer().getInput() != selectedElement) {
						currentViewerPane.getViewer().setInput(selectedElement);
						currentViewerPane.setTitle(selectedElement);
					}
				}
			}
		}
	}

	/**
	 * This is for implementing {@link IEditorPart} and simply tests the command stack.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * This is for implementing {@link IEditorPart} and simply saves the model file.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void doSave(IProgressMonitor progressMonitor) {
		// Do the work within an operation because this is a long running activity
		// that modifies the workbench.  Moreover, we must do this in a read-only
		// transaction in the editing domain, to ensure exclusive read access
		//
		WorkspaceModifyOperation operation =
			new WorkspaceModifyOperation() {
				// This is the method that gets invoked when the operation runs.
				//
				public void execute(IProgressMonitor monitor) {
					try {
						((TransactionalEditingDomain) getEditingDomain()).runExclusive(new Runnable() {
							public void run() {
								try {
									// Save the resource to the file system.
									//
									Resource savedResource = getResource();
									savedResources.add(savedResource);
									savedResource.save(Collections.EMPTY_MAP);
								}
								catch (Exception exception) {
									TransactionEditorPlugin.INSTANCE.log(exception);
								}
							}});
					}
					catch (Exception exception) {
						TransactionEditorPlugin.INSTANCE.log(exception);
					}
				}
			};

		try {
			// This runs the options, and shows progress.
			//
			new ProgressMonitorDialog(getSite().getShell()).run(true, false, operation);

			// Refresh the necessary state.
			//
			dirty = false;
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
		catch (Exception exception) {
			// Something went wrong that shouldn't.
			TransactionEditorPlugin.INSTANCE.log(exception);
		}
	}

	/**
	 * This always returns true because it is not currently supported.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * This also changes the editor's input.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void doSaveAs() {
		SaveAsDialog saveAsDialog= new SaveAsDialog(getSite().getShell());
		saveAsDialog.open();
		IPath path= saveAsDialog.getResult();
		if (path != null) {
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if (file != null) {
				doSaveAs(URI.createPlatformResourceURI(file.getFullPath().toString(),true), new FileEditorInput(file));
			}
		}
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected void doSaveAs(final URI uri, final IEditorInput editorInput) {
		// changing the URI is, conceptually, a write operation.  However, it does
		//    not affect the abstract state of the model, so we only need exclusive
		//    (read) access
		try {
			((TransactionalEditingDomain) getEditingDomain()).runExclusive(new Runnable() {
				public void run() {
					getResource().setURI(uri);
					setInputWithNotify(editorInput);
					setPartName(editorInput.getName());
				}});
		} catch (InterruptedException e) {
			TransactionEditorPlugin.INSTANCE.log(e);
			
			// don't follow through with the save because we were interrupted while
			//    trying to start the transaction, so our URI is not actually changed
			return;
		}
		
		IProgressMonitor progressMonitor =
			getActionBars().getStatusLineManager() != null ?
				getActionBars().getStatusLineManager().getProgressMonitor() :
				new NullProgressMonitor();
		doSave(progressMonitor);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void gotoMarker(IMarker marker) {
		try {
			if (marker.getType().equals(EValidator.MARKER)) {
				final String uriAttribute = marker.getAttribute(EValidator.URI_ATTRIBUTE, null);
				if (uriAttribute != null) {
					try {
						((TransactionalEditingDomain) getEditingDomain()).runExclusive(new Runnable() {
							public void run() {
								URI uri = URI.createURI(uriAttribute);
								EObject eObject = editingDomain.getResourceSet().getEObject(uri, true);
								if (eObject != null) {
								  setSelectionToViewer(Collections.singleton(editingDomain.getWrapper(eObject)));
								}
							}});
					} catch (InterruptedException e) {
						TransactionEditorPlugin.INSTANCE.log(e);
					}
				}
			}
		}
		catch (CoreException exception) {
			TransactionEditorPlugin.INSTANCE.log(exception);
		}
	}

	/**
	 * This is called during startup.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void init(IEditorSite site, IEditorInput editorInput) {
		setSite(site);
		setInputWithNotify(editorInput);
		setPartName(editorInput.getName());
		site.setSelectionProvider(this);
		site.getPage().addPartListener(partListener);
		
		workspaceSynchronizer = new WorkspaceSynchronizer(
				(TransactionalEditingDomain) editingDomain,
				createSynchronizationDelegate());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void setFocus() {
		selectionViewer.getControl().setFocus();
	}

	/**
	 * This implements {@link org.eclipse.jface.viewers.ISelectionProvider}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.add(listener);
	}

	/**
	 * This implements {@link org.eclipse.jface.viewers.ISelectionProvider}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.remove(listener);
	}

	/**
	 * This implements {@link org.eclipse.jface.viewers.ISelectionProvider} to return this editor's overall selection.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ISelection getSelection() {
		return editorSelection;
	}

	/**
	 * This implements {@link org.eclipse.jface.viewers.ISelectionProvider} to set this editor's overall selection.
	 * Calling this result will notify the listeners.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSelection(ISelection selection) {
		editorSelection = selection;

		for (Iterator<?> listeners = selectionChangedListeners.iterator(); listeners.hasNext(); ) {
			ISelectionChangedListener listener = (ISelectionChangedListener)listeners.next();
			listener.selectionChanged(new SelectionChangedEvent(this, selection));
		}
		setStatusLineManager(selection);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStatusLineManager(ISelection selection) {
		IStatusLineManager statusLineManager = currentViewer != null && currentViewer == contentOutlineViewer ?
			contentOutlineStatusLineManager : getActionBars().getStatusLineManager();
	
		if (statusLineManager != null) {
			if (selection instanceof IStructuredSelection) {
				Collection<?> collection = ((IStructuredSelection)selection).toList();
				switch (collection.size()) {
					case 0: {
						statusLineManager.setMessage(getString("_UI_NoObjectSelected")); //$NON-NLS-1$
						break;
					}
					case 1: {
						String text = new AdapterFactoryItemDelegator(adapterFactory).getText(collection.iterator().next());
						statusLineManager.setMessage(getString("_UI_SingleObjectSelected", text)); //$NON-NLS-1$
						break;
					}
					default: {
						statusLineManager.setMessage(getString("_UI_MultiObjectSelected", Integer.toString(collection.size()))); //$NON-NLS-1$
						break;
					}
				}
			}
			else {
				statusLineManager.setMessage(""); //$NON-NLS-1$
			}
		}
	}

	/**
	 * This looks up a string in the plugin's plugin.properties file.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static String getString(String key) {
		return TransactionEditorPlugin.INSTANCE.getString(key);
	}

	/**
	 * This looks up a string in plugin.properties, making a substitution.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static String getString(String key, Object s1) {
		return TransactionEditorPlugin.INSTANCE.getString(key, new Object [] { s1 });
	}

	/**
	 * This implements {@link org.eclipse.jface.action.IMenuListener} to help fill the context menus with contributions from the Edit menu.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void menuAboutToShow(IMenuManager menuManager) {
		((IMenuListener)getEditorSite().getActionBarContributor()).menuAboutToShow(menuManager);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EditingDomainActionBarContributor getActionBarContributor() {
		return (EditingDomainActionBarContributor)getEditorSite().getActionBarContributor();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IActionBars getActionBars() {
		return getActionBarContributor().getActionBars();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	private IOperationHistory getOperationHistory() {
		return ((IWorkspaceCommandStack) editingDomain.getCommandStack()).getOperationHistory();
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void dispose() {
		workspaceSynchronizer.dispose();
		getOperationHistory().removeOperationHistoryListener(historyListener);
		getOperationHistory().dispose(getUndoContext(), true, true, true);
		
		getResource().unload();
		editingDomain.getResourceSet().getResources().remove(getResource());
		
		getSite().getPage().removePartListener(partListener);

		adapterFactory.dispose();

		if (getActionBarContributor().getActiveEditor() == this) {
			getActionBarContributor().setActiveEditor(null);
		}

		if (propertySheetPage != null) {
			propertySheetPage.dispose();
		}

		if (contentOutlinePage != null) {
			contentOutlinePage.dispose();
		}

		super.dispose();
	}

	private static void printToConsole(String msg) {
		ConsoleUtil.println(CONSOLE, new Date()
				+ "\t" + msg);
	}

	/**
	 * This is used to track the active viewer.
	 * <!-- begin-user-doc -->
	 * Also used to enable and disable buttons
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	protected void pageChange(int pageIndex) {
		super.pageChange(pageIndex);
		IEditorActionBarContributor abc = getActionBarContributor();
		if(abc != null && abc instanceof TrackerActionBarContributor){
			TrackerActionBarContributor tabc = (TrackerActionBarContributor)abc;
			resetActionBarContributor(tabc.customActionBarContributor);
		}
		
		if (contentOutlinePage != null) {
			handleContentOutlineSelection(contentOutlinePage.getSelection());
		}
	}

	/* (non-Javadoc)
	 * @see com.verticon.tracker.editor.presentation.CustomActionBarContributor#setActivePage(com.verticon.tracker.editor.presentation.TrackerEditor, int)
	 */
	private void resetActionBarContributor(ICustomActionBarContributor icabc){
		if (currentViewerPane != null){
			
			if (currentViewerPane.getViewer() instanceof TreeViewer ) {
				icabc.enableTreeViewerActions();
			} else {
				icabc.disableTreeViewerActions();
			}
			if (currentViewerPane.getViewer() == selectionViewer ){
				icabc.enableSelectionViewerActions();
			}else{
				icabc.disableSelectionViewerActions();
			}
		}
	}
	
	public ISelection getEventSelection() {
		return eventsTableViewer.getSelection();
	}

	public void setEventSelection(ISelection selection) {
		eventsTableViewer.setSelection(selection);
		
	}

	public ISelection getAnimalSelection() {
		return animalsTableViewer.getSelection();
	}

	public void setAnimalSelection(ISelection selection) {
		animalsTableViewer.setSelection(selection);
	}

	public ISelection getSelectionViewerSelection() {
		return selectionViewer.getSelection();
	}

	public void setSelectionViewerSelection(ISelection selection) {
		selectionViewer.setSelection(selection);
	}
}
