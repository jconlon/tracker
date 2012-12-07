/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.verticon.google.kmlgx.tests;

import junit.textui.TestRunner;

import com.verticon.google.kmlgx.KmlgxFactory;
import com.verticon.google.kmlgx.TourControl;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Tour Control</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class TourControlTest extends TourPrimitiveTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(TourControlTest.class);
	}

	/**
	 * Constructs a new Tour Control test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TourControlTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Tour Control test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected TourControl getFixture() {
		return (TourControl)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(KmlgxFactory.eINSTANCE.createTourControl());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

} //TourControlTest
