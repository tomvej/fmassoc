package org.tomvej.fmassoc.model.property;

import org.tomvej.fmassoc.model.db.AssociationInfo;

/**
 * Algorithm used for {@link StackPathPropertyBuilder}. See the methods
 * 
 * @author Tomáš Vejpustek
 * 
 * @param <T>
 *            Type of property values.
 * @param <S>
 *            Type of values stored on stack.
 * 
 * @see StackPathPropertyBuilder
 */
public interface StackPathProperty<T, S> {

	/**
	 * This method is called each time new association is added to the path and
	 * computes value which is added to the top of the stack.
	 * 
	 * @param previousValue
	 *            Value previously on the top of the stack.
	 * @param target
	 *            Added association
	 * @return Value which should be added to the top of the stack.
	 */
	S getNewValue(S previousValue, AssociationInfo target);

	/**
	 * This method is used to convert values on stack to property values.
	 * It is called only on the top of the stack.
	 * 
	 * @param value
	 *            Value on the top of the stack.
	 * @return Property value corresponding to the stack value.
	 */
	T getValue(S value);
}
