/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.verticon.tracker.fair.validation;


/**
 * A sample validator interface for {@link com.verticon.tracker.fair.Person}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface PersonValidator {
	boolean validate();

	boolean validateFirstName(String value);
	boolean validateLastName(String value);
	boolean validatePhone(String value);
	boolean validateStreet(String value);
	boolean validateCity(String value);
	boolean validateState(String value);
	boolean validateZipCode(String value);
	boolean validateName(String value);

	boolean validateComments(String value);

	boolean validatePin(String value);
}
