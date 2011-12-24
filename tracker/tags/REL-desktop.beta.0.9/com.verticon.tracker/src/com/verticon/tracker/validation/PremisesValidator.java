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
package com.verticon.tracker.validation;

import org.eclipse.emf.common.util.EList;

import com.verticon.tracker.Animal;
import com.verticon.tracker.Location;
import com.verticon.tracker.Tag;

/**
 * A sample validator interface for {@link com.verticon.tracker.Premises}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface PremisesValidator {
	boolean validate();

	boolean validatePremisesId(String value);
	boolean validateEmailContact(String value);
	boolean validateAnimals(EList<Animal> value);
	boolean validateUnAppliedTags(EList<Tag> value);

	boolean validateLocations(EList<Location> value);

	boolean validateUri(String value);

	
	boolean validateName(String value);

}
