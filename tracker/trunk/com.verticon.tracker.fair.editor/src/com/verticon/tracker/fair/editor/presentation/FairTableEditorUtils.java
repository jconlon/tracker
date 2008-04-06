/**
 * 
 */
package com.verticon.tracker.fair.editor.presentation;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.verticon.tracker.Premises;
import com.verticon.tracker.TrackerPackage;
import com.verticon.tracker.editor.presentation.AnimalSorter;
import com.verticon.tracker.editor.presentation.TrackerReportEditorPlugin;
import com.verticon.tracker.editor.presentation.TrackerTableEditorUtils;
import com.verticon.tracker.fair.Fair;

/**
 * @author jconlon
 * 
 */
public class FairTableEditorUtils {

	/**
	 * Animals Table
	 */
	public static void createAnimalsTableViewer(ViewerPane viewerPane,
			final TableViewer tableViewer, AdapterFactory adapterFactory) {

		TrackerTableEditorUtils.createAnimalsTableViewer(viewerPane,
				tableViewer, adapterFactory);
		// The utility does not set the contentAdapter to find the Premises do
		// it again here
		tableViewer.setContentProvider(new AdapterFactoryContentProvider(
				adapterFactory) 
				{
					@Override
					public Object[] getElements(Object object) {
						if (object instanceof Fair) {
							Premises premises = ((Fair) object).getPremises();
							if (premises != null) {
								return premises.getAnimals().toArray();
							}
						} else if (object instanceof Premises) {
							Premises premises = (Premises) object;
							if (premises != null) {
								return premises.getAnimals().toArray();
							}
						}
						return null;
					}

				});

	}

	/**
	 * Events Table
	 */
	public static void createEventsTableViewer(ViewerPane viewerPane,
			final TableViewer tableViewer, AdapterFactory adapterFactory) {
		TrackerTableEditorUtils.createEventsTableViewer(viewerPane,
				tableViewer, adapterFactory);
		// The utility does not set the contentAdapter to find the Premises do
		// it again here
		tableViewer.setContentProvider(new AdapterFactoryContentProvider(
				adapterFactory) 
				{
					public Object[] getElements(Object object) {
						if (object instanceof Fair) {
							Premises premises = ((Fair) object).getPremises();
							if (premises != null) {
								return premises.eventHistory().toArray();
							}
						} else if (object instanceof Premises) {
							Premises premises = (Premises) object;
							if (premises != null) {
								return premises.eventHistory().toArray();
							}
						}
						return null;
					}

					public void notifyChanged(Notification notification) {

						switch (notification.getEventType()) {
						case Notification.ADD:
						case Notification.ADD_MANY:
							if (notification.getFeature() != TrackerPackage.eINSTANCE
									.getTag_Events()) {
								return;
							}
						}
						super.notifyChanged(notification);

					}
				});
	}

	/**
	 * People Table
	 */
	public static void createPeopleTableViewer(ViewerPane viewerPane,
			final TableViewer tableViewer, AdapterFactory adapterFactory) {

		final Table table = tableViewer.getTable();
		TableLayout layout = new TableLayout();
		table.setLayout(layout);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Animal
		final TableColumn animalColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(3, 220, true));
		animalColumn.setText(getString("_UI_AnimalColumn_label"));

		// Ain
		final TableColumn ainColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(3, 150, true));
		ainColumn.setText(getString("_UI_AinColumn_label"));

		// Animal
		final TableColumn animalTypeColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 50, true));
		animalTypeColumn.setText(getString("_UI_AnimalColumn_label"));

		// Species
		final TableColumn speciesColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 40, true));
		speciesColumn.setText(getString("_UI_SpeciesColumn_label"));

		// Sex
		final TableColumn sexColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 50, true));
		sexColumn.setText(getString("_UI_SexColumn_label"));

		// Breed
		final TableColumn breedColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 70, true));
		breedColumn.setText(getString("_UI_BreedColumn_label"));

		// BirthDate
		final TableColumn dDateColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 100, true));
		dDateColumn.setText(getString("_UI_BirthDateColumn_label"));

		// Age
		final TableColumn ageColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 80, true));
		ageColumn.setText(getString("_UI_AgeColumn_label"));

		// LastEventDateTime
		final TableColumn lastEventDateTimeColumn = new TableColumn(table,
				SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 200, true));
		lastEventDateTimeColumn.setText(getString("_UI_LastEventColumn_label"));

		// Weight
		final TableColumn weightColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 80, true));
		weightColumn.setText("Weight");

		// WeightGainPerDay
		final TableColumn weightGainColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 80, true));
		weightGainColumn.setText("WeightGainPerDay");

		// Comments
		final TableColumn commentsColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 200, true));
		commentsColumn.setText(getString("_UI_CommentsColumn_label"));

		Listener sortListener = new Listener() {

			public void handleEvent(org.eclipse.swt.widgets.Event e) {
				// determine new sort column and direction
				TableColumn sortColumn = table.getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = table.getSortDirection();
				if (sortColumn == currentColumn) {
					dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
				} else {
					table.setSortColumn(currentColumn);
					dir = SWT.UP;
				}

				// sort the data based on column and direction

				int sortIdentifier = 0;

				if (currentColumn == animalColumn) {
					sortIdentifier = AnimalSorter.ANIMAL_TEXT;
				} else if (currentColumn == ainColumn) {
					sortIdentifier = AnimalSorter.AIN;
				} else if (currentColumn == animalTypeColumn) {
					sortIdentifier = AnimalSorter.ANIMAL;
				} else if (currentColumn == dDateColumn) {
					sortIdentifier = AnimalSorter.BIRTHDATE;
				} else if (currentColumn == speciesColumn) {
					sortIdentifier = AnimalSorter.SPECIES;
				} else if (currentColumn == sexColumn) {
					sortIdentifier = AnimalSorter.SEX;
				} else if (currentColumn == breedColumn) {
					sortIdentifier = AnimalSorter.BREED;
				} else if (currentColumn == ageColumn) {
					sortIdentifier = AnimalSorter.AGE;

				} else if (currentColumn == weightColumn) {
					sortIdentifier = AnimalSorter.WEIGHT;
				} else if (currentColumn == weightGainColumn) {
					sortIdentifier = AnimalSorter.WEIGHT_GAIN;

				} else if (currentColumn == commentsColumn) {
					sortIdentifier = AnimalSorter.COMMENTS;
				} else if (currentColumn == lastEventDateTimeColumn) {
					sortIdentifier = AnimalSorter.LAST_EVENT_DATE;
				}

				table.setSortDirection(dir);
				tableViewer.setSorter(new AnimalSorter(sortIdentifier, dir));
			}

		};

		breedColumn.addListener(SWT.Selection, sortListener);
		animalColumn.addListener(SWT.Selection, sortListener);
		ainColumn.addListener(SWT.Selection, sortListener);
		lastEventDateTimeColumn.addListener(SWT.Selection, sortListener);
		animalTypeColumn.addListener(SWT.Selection, sortListener);
		speciesColumn.addListener(SWT.Selection, sortListener);
		sexColumn.addListener(SWT.Selection, sortListener);
		dDateColumn.addListener(SWT.Selection, sortListener);
		ageColumn.addListener(SWT.Selection, sortListener);

		weightColumn.addListener(SWT.Selection, sortListener);
		weightGainColumn.addListener(SWT.Selection, sortListener);

		commentsColumn.addListener(SWT.Selection, sortListener);

		tableViewer.setColumnProperties(new String[] { "a", "b", "c", "d", "e",
				"f", "g", "h", "i", "j", "k", "l" });

		/**
		 * The default ItemProvider returned via the adapterFactory for Premises
		 * should be able to handle all notifications of animals being added or
		 * removed.
		 * 
		 * To get Animal Elements override the getElements method
		 */
		tableViewer.setContentProvider(new AdapterFactoryContentProvider(
				adapterFactory) // 14.2.2
				{
					@Override
					public Object[] getElements(Object object) {
						return ((Premises) object).getAnimals().toArray();
					}

				});
		tableViewer.setLabelProvider(new AdapterFactoryLabelProvider(
				adapterFactory));

	}
	
	/**
	 * People Table
	 */
	public static void createExhibitsTableViewer(ViewerPane viewerPane,
			final TableViewer tableViewer, AdapterFactory adapterFactory) {

		final Table table = tableViewer.getTable();
		TableLayout layout = new TableLayout();
		table.setLayout(layout);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Animal
		final TableColumn animalColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(3, 220, true));
		animalColumn.setText(getString("_UI_AnimalColumn_label"));

		// Ain
		final TableColumn ainColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(3, 150, true));
		ainColumn.setText(getString("_UI_AinColumn_label"));

		// Animal
		final TableColumn animalTypeColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 50, true));
		animalTypeColumn.setText(getString("_UI_AnimalColumn_label"));

		// Species
		final TableColumn speciesColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 40, true));
		speciesColumn.setText(getString("_UI_SpeciesColumn_label"));

		// Sex
		final TableColumn sexColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 50, true));
		sexColumn.setText(getString("_UI_SexColumn_label"));

		// Breed
		final TableColumn breedColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 70, true));
		breedColumn.setText(getString("_UI_BreedColumn_label"));

		// BirthDate
		final TableColumn dDateColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 100, true));
		dDateColumn.setText(getString("_UI_BirthDateColumn_label"));

		// Age
		final TableColumn ageColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 80, true));
		ageColumn.setText(getString("_UI_AgeColumn_label"));

		// LastEventDateTime
		final TableColumn lastEventDateTimeColumn = new TableColumn(table,
				SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 200, true));
		lastEventDateTimeColumn.setText(getString("_UI_LastEventColumn_label"));

		// Weight
		final TableColumn weightColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 80, true));
		weightColumn.setText("Weight");

		// WeightGainPerDay
		final TableColumn weightGainColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 80, true));
		weightGainColumn.setText("WeightGainPerDay");

		// Comments
		final TableColumn commentsColumn = new TableColumn(table, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(2, 200, true));
		commentsColumn.setText(getString("_UI_CommentsColumn_label"));

		Listener sortListener = new Listener() {

			public void handleEvent(org.eclipse.swt.widgets.Event e) {
				// determine new sort column and direction
				TableColumn sortColumn = table.getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = table.getSortDirection();
				if (sortColumn == currentColumn) {
					dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
				} else {
					table.setSortColumn(currentColumn);
					dir = SWT.UP;
				}

				// sort the data based on column and direction

				int sortIdentifier = 0;

				if (currentColumn == animalColumn) {
					sortIdentifier = AnimalSorter.ANIMAL_TEXT;
				} else if (currentColumn == ainColumn) {
					sortIdentifier = AnimalSorter.AIN;
				} else if (currentColumn == animalTypeColumn) {
					sortIdentifier = AnimalSorter.ANIMAL;
				} else if (currentColumn == dDateColumn) {
					sortIdentifier = AnimalSorter.BIRTHDATE;
				} else if (currentColumn == speciesColumn) {
					sortIdentifier = AnimalSorter.SPECIES;
				} else if (currentColumn == sexColumn) {
					sortIdentifier = AnimalSorter.SEX;
				} else if (currentColumn == breedColumn) {
					sortIdentifier = AnimalSorter.BREED;
				} else if (currentColumn == ageColumn) {
					sortIdentifier = AnimalSorter.AGE;

				} else if (currentColumn == weightColumn) {
					sortIdentifier = AnimalSorter.WEIGHT;
				} else if (currentColumn == weightGainColumn) {
					sortIdentifier = AnimalSorter.WEIGHT_GAIN;

				} else if (currentColumn == commentsColumn) {
					sortIdentifier = AnimalSorter.COMMENTS;
				} else if (currentColumn == lastEventDateTimeColumn) {
					sortIdentifier = AnimalSorter.LAST_EVENT_DATE;
				}

				table.setSortDirection(dir);
				tableViewer.setSorter(new AnimalSorter(sortIdentifier, dir));
			}

		};

		breedColumn.addListener(SWT.Selection, sortListener);
		animalColumn.addListener(SWT.Selection, sortListener);
		ainColumn.addListener(SWT.Selection, sortListener);
		lastEventDateTimeColumn.addListener(SWT.Selection, sortListener);
		animalTypeColumn.addListener(SWT.Selection, sortListener);
		speciesColumn.addListener(SWT.Selection, sortListener);
		sexColumn.addListener(SWT.Selection, sortListener);
		dDateColumn.addListener(SWT.Selection, sortListener);
		ageColumn.addListener(SWT.Selection, sortListener);

		weightColumn.addListener(SWT.Selection, sortListener);
		weightGainColumn.addListener(SWT.Selection, sortListener);

		commentsColumn.addListener(SWT.Selection, sortListener);

		tableViewer.setColumnProperties(new String[] { "a", "b", "c", "d", "e",
				"f", "g", "h", "i", "j", "k", "l" });

		/**
		 * The default ItemProvider returned via the adapterFactory for Premises
		 * should be able to handle all notifications of animals being added or
		 * removed.
		 * 
		 * To get Animal Elements override the getElements method
		 */
		tableViewer.setContentProvider(new AdapterFactoryContentProvider(
				adapterFactory) // 14.2.2
				{
					@Override
					public Object[] getElements(Object object) {
						return ((Premises) object).getAnimals().toArray();
					}

				});
		tableViewer.setLabelProvider(new AdapterFactoryLabelProvider(
				adapterFactory));

	}

	/**
	 * This looks up a string in the plugin's plugin.properties file. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static String getString(String key) {
		return FairEditorPlugin.INSTANCE.getString(key);
	}

}
