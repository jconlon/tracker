/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.verticon.tracker.fair.validation;

import com.verticon.tracker.fair.Department;

import org.eclipse.emf.common.util.EList;

/**
 * A sample validator interface for {@link com.verticon.tracker.fair.Division}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface DivisionValidator {
	boolean validate();

	boolean validateName(String value);
	boolean validateDepartment(EList<Department> value);

	boolean validateDepartments(EList<Department> value);

	boolean validateComments(String value);
}
