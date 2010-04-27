/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.verticon.tracker.validation;

import com.verticon.tracker.GoatBreed;

/**
 * A sample validator interface for {@link com.verticon.tracker.Caprine}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface CaprineValidator {
	boolean validate();

	boolean validateGoatBreed(GoatBreed value);

	boolean validateGoatBread(GoatBreed value);
}