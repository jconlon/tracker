/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.verticon.tracker.edit.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITableItemLabelProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

import com.verticon.tracker.util.TrackerAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class TrackerItemProviderAdapterFactory extends TrackerAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright 2007 Verticon, Inc. All Rights Reserved.";

	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public TrackerItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);	
		supportedTypes.add(ITableItemLabelProvider.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.Tag} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TagItemProvider tagItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.Tag}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createTagAdapter() {
		if (tagItemProvider == null) {
			tagItemProvider = new TagItemProvider(this);
		}

		return tagItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.TagAllocated} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TagAllocatedItemProvider tagAllocatedItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.TagAllocated}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createTagAllocatedAdapter() {
		if (tagAllocatedItemProvider == null) {
			tagAllocatedItemProvider = new TagAllocatedItemProvider(this);
		}

		return tagAllocatedItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.Died} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DiedItemProvider diedItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.Died}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createDiedAdapter() {
		if (diedItemProvider == null) {
			diedItemProvider = new DiedItemProvider(this);
		}

		return diedItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.TagRetired} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TagRetiredItemProvider tagRetiredItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.TagRetired}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createTagRetiredAdapter() {
		if (tagRetiredItemProvider == null) {
			tagRetiredItemProvider = new TagRetiredItemProvider(this);
		}

		return tagRetiredItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.AnimalMissing} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AnimalMissingItemProvider animalMissingItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.AnimalMissing}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createAnimalMissingAdapter() {
		if (animalMissingItemProvider == null) {
			animalMissingItemProvider = new AnimalMissingItemProvider(this);
		}

		return animalMissingItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.ICVI} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ICVIItemProvider icviItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.ICVI}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createICVIAdapter() {
		if (icviItemProvider == null) {
			icviItemProvider = new ICVIItemProvider(this);
		}

		return icviItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.FairRegistration} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FairRegistrationItemProvider fairRegistrationItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.FairRegistration}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFairRegistrationAdapter() {
		if (fairRegistrationItemProvider == null) {
			fairRegistrationItemProvider = new FairRegistrationItemProvider(this);
		}

		return fairRegistrationItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.WeighIn} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected WeighInItemProvider weighInItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.WeighIn}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createWeighInAdapter() {
		if (weighInItemProvider == null) {
			weighInItemProvider = new WeighInItemProvider(this);
		}

		return weighInItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.Swine} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SwineItemProvider swineItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.Swine}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSwineAdapter() {
		if (swineItemProvider == null) {
			swineItemProvider = new SwineItemProvider(this);
		}

		return swineItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.Equine} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EquineItemProvider equineItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.Equine}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createEquineAdapter() {
		if (equineItemProvider == null) {
			equineItemProvider = new EquineItemProvider(this);
		}

		return equineItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.Caprine} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CaprineItemProvider caprineItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.Caprine}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCaprineAdapter() {
		if (caprineItemProvider == null) {
			caprineItemProvider = new CaprineItemProvider(this);
		}

		return caprineItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.Location} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LocationItemProvider locationItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.Location}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createLocationAdapter() {
		if (locationItemProvider == null) {
			locationItemProvider = new LocationItemProvider(this);
		}

		return locationItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.MedicalCondition} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MedicalConditionItemProvider medicalConditionItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.MedicalCondition}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMedicalConditionAdapter() {
		if (medicalConditionItemProvider == null) {
			medicalConditionItemProvider = new MedicalConditionItemProvider(this);
		}

		return medicalConditionItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.MedicalTreatment} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MedicalTreatmentItemProvider medicalTreatmentItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.MedicalTreatment}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMedicalTreatmentAdapter() {
		if (medicalTreatmentItemProvider == null) {
			medicalTreatmentItemProvider = new MedicalTreatmentItemProvider(this);
		}

		return medicalTreatmentItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.Birthing} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BirthingItemProvider birthingItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.Birthing}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createBirthingAdapter() {
		if (birthingItemProvider == null) {
			birthingItemProvider = new BirthingItemProvider(this);
		}

		return birthingItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.Calving} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CalvingItemProvider calvingItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.Calving}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCalvingAdapter() {
		if (calvingItemProvider == null) {
			calvingItemProvider = new CalvingItemProvider(this);
		}

		return calvingItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.BirthDefect} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BirthDefectItemProvider birthDefectItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.BirthDefect}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createBirthDefectAdapter() {
		if (birthDefectItemProvider == null) {
			birthDefectItemProvider = new BirthDefectItemProvider(this);
		}

		return birthDefectItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.Mastitis} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MastitisItemProvider mastitisItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.Mastitis}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMastitisAdapter() {
		if (mastitisItemProvider == null) {
			mastitisItemProvider = new MastitisItemProvider(this);
		}

		return mastitisItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.MilkTest} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MilkTestItemProvider milkTestItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.MilkTest}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMilkTestAdapter() {
		if (milkTestItemProvider == null) {
			milkTestItemProvider = new MilkTestItemProvider(this);
		}

		return milkTestItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.HerdTest} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected HerdTestItemProvider herdTestItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.HerdTest}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createHerdTestAdapter() {
		if (herdTestItemProvider == null) {
			herdTestItemProvider = new HerdTestItemProvider(this);
		}

		return herdTestItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.Premises} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PremisesItemProvider premisesItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.Premises}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPremisesAdapter() {
		if (premisesItemProvider == null) {
			premisesItemProvider = new PremisesItemProvider(this);
		}

		return premisesItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.BovineBeef} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BovineBeefItemProvider bovineBeefItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.BovineBeef}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createBovineBeefAdapter() {
		if (bovineBeefItemProvider == null) {
			bovineBeefItemProvider = new BovineBeefItemProvider(this);
		}

		return bovineBeefItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.Ovine} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OvineItemProvider ovineItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.Ovine}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createOvineAdapter() {
		if (ovineItemProvider == null) {
			ovineItemProvider = new OvineItemProvider(this);
		}

		return ovineItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.BovineBison} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BovineBisonItemProvider bovineBisonItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.BovineBison}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createBovineBisonAdapter() {
		if (bovineBisonItemProvider == null) {
			bovineBisonItemProvider = new BovineBisonItemProvider(this);
		}

		return bovineBisonItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.BovineDairy} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BovineDairyItemProvider bovineDairyItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.BovineDairy}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createBovineDairyAdapter() {
		if (bovineDairyItemProvider == null) {
			bovineDairyItemProvider = new BovineDairyItemProvider(this);
		}

		return bovineDairyItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.TagApplied} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TagAppliedItemProvider tagAppliedItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.TagApplied}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createTagAppliedAdapter() {
		if (tagAppliedItemProvider == null) {
			tagAppliedItemProvider = new TagAppliedItemProvider(this);
		}

		return tagAppliedItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.MovedIn} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MovedInItemProvider movedInItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.MovedIn}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMovedInAdapter() {
		if (movedInItemProvider == null) {
			movedInItemProvider = new MovedInItemProvider(this);
		}

		return movedInItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.MovedOut} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MovedOutItemProvider movedOutItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.MovedOut}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMovedOutAdapter() {
		if (movedOutItemProvider == null) {
			movedOutItemProvider = new MovedOutItemProvider(this);
		}

		return movedOutItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.LostTag} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LostTagItemProvider lostTagItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.LostTag}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createLostTagAdapter() {
		if (lostTagItemProvider == null) {
			lostTagItemProvider = new LostTagItemProvider(this);
		}

		return lostTagItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.ReplacedTag} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ReplacedTagItemProvider replacedTagItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.ReplacedTag}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createReplacedTagAdapter() {
		if (replacedTagItemProvider == null) {
			replacedTagItemProvider = new ReplacedTagItemProvider(this);
		}

		return replacedTagItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.Imported} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ImportedItemProvider importedItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.Imported}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createImportedAdapter() {
		if (importedItemProvider == null) {
			importedItemProvider = new ImportedItemProvider(this);
		}

		return importedItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.Exported} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExportedItemProvider exportedItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.Exported}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createExportedAdapter() {
		if (exportedItemProvider == null) {
			exportedItemProvider = new ExportedItemProvider(this);
		}

		return exportedItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.Sighting} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SightingItemProvider sightingItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.Sighting}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSightingAdapter() {
		if (sightingItemProvider == null) {
			sightingItemProvider = new SightingItemProvider(this);
		}

		return sightingItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.verticon.tracker.Slaughtered} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SlaughteredItemProvider slaughteredItemProvider;

	/**
	 * This creates an adapter for a {@link com.verticon.tracker.Slaughtered}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSlaughteredAdapter() {
		if (slaughteredItemProvider == null) {
			slaughteredItemProvider = new SlaughteredItemProvider(this);
		}

		return slaughteredItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class) || (((Class<?>)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory. 
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void dispose() {
		if (tagItemProvider != null) tagItemProvider.dispose();
		if (tagAllocatedItemProvider != null) tagAllocatedItemProvider.dispose();
		if (premisesItemProvider != null) premisesItemProvider.dispose();
		if (bovineBeefItemProvider != null) bovineBeefItemProvider.dispose();
		if (ovineItemProvider != null) ovineItemProvider.dispose();
		if (bovineBisonItemProvider != null) bovineBisonItemProvider.dispose();
		if (bovineDairyItemProvider != null) bovineDairyItemProvider.dispose();
		if (tagAppliedItemProvider != null) tagAppliedItemProvider.dispose();
		if (movedInItemProvider != null) movedInItemProvider.dispose();
		if (movedOutItemProvider != null) movedOutItemProvider.dispose();
		if (lostTagItemProvider != null) lostTagItemProvider.dispose();
		if (replacedTagItemProvider != null) replacedTagItemProvider.dispose();
		if (importedItemProvider != null) importedItemProvider.dispose();
		if (exportedItemProvider != null) exportedItemProvider.dispose();
		if (sightingItemProvider != null) sightingItemProvider.dispose();
		if (slaughteredItemProvider != null) slaughteredItemProvider.dispose();
		if (diedItemProvider != null) diedItemProvider.dispose();
		if (tagRetiredItemProvider != null) tagRetiredItemProvider.dispose();
		if (animalMissingItemProvider != null) animalMissingItemProvider.dispose();
		if (icviItemProvider != null) icviItemProvider.dispose();
		if (fairRegistrationItemProvider != null) fairRegistrationItemProvider.dispose();
		if (weighInItemProvider != null) weighInItemProvider.dispose();
		if (swineItemProvider != null) swineItemProvider.dispose();
		if (equineItemProvider != null) equineItemProvider.dispose();
		if (caprineItemProvider != null) caprineItemProvider.dispose();
		if (locationItemProvider != null) locationItemProvider.dispose();
		if (medicalConditionItemProvider != null) medicalConditionItemProvider.dispose();
		if (medicalTreatmentItemProvider != null) medicalTreatmentItemProvider.dispose();
		if (birthingItemProvider != null) birthingItemProvider.dispose();
		if (calvingItemProvider != null) calvingItemProvider.dispose();
		if (birthDefectItemProvider != null) birthDefectItemProvider.dispose();
		if (mastitisItemProvider != null) mastitisItemProvider.dispose();
		if (milkTestItemProvider != null) milkTestItemProvider.dispose();
		if (herdTestItemProvider != null) herdTestItemProvider.dispose();
	}

}
