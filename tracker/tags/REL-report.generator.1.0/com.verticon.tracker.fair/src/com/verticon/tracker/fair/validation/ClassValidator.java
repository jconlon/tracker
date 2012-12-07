/*******************************************************************************
 * Copyright (c) 2010 Verticon, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Verticon, Inc. - initial API and implementation
 *******************************************************************************/
package com.verticon.tracker.fair.validation;

import org.eclipse.emf.common.util.EList;

import com.verticon.tracker.fair.Department;
import com.verticon.tracker.fair.Lot;
import com.verticon.tracker.fair.Person;

/**
 * A sample validator interface for {@link com.verticon.tracker.fair.Class}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface ClassValidator {
	boolean validate();

	boolean validateName(String value);
	boolean validateLot(EList<Lot> value);

	boolean validateLots(EList<Lot> value);

	boolean validateJudges(EList<Person> value);

	boolean validateDepartment(Department value);

	boolean validateComments(String value);

	boolean validateDescription(String value);

	boolean validateJudge(EList<Person> value);
}
