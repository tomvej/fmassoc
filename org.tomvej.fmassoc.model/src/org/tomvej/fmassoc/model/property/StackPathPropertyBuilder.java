package org.tomvej.fmassoc.model.property;

import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.AssociationInfo;

/**
 * Property builder which uses stack to save auxiliary values needed for
 * property value computation.
 * Its function is defined by {@link StackPathProperty}.
 * 
 * @author Tomáš Vejpustek
 *
 * @param <T>
 *            Type of property values.
 * @param <S>
 *            Type of values stored on stack.
 * 
 * @see StackPathProperty
 */
public class StackPathPropertyBuilder<T, S> implements PathPropertyBuilder<T> {
	private final Deque<S> stack = new ArrayDeque<>();
	private final StackPathProperty<T, S> property;

	/**
	 * Specify algorithms for computing stack values and retrieving property
	 * values from stack values and value at the bottom of the stack.
	 * 
	 * @param bottomValue
	 *            Value at the bottom of the stack.
	 */
	public StackPathPropertyBuilder(StackPathProperty<T, S> property, S bottomValue) {
		this.property = Validate.notNull(property);
		stack.push(bottomValue);
	}

	@Override
	public void push(AssociationInfo association) {
		stack.push(property.getNewValue(stack.peek(), association));
	}

	@Override
	public void pop(AssociationInfo target) {
		Validate.validState(stack.size() > 1, "Cannot remove associations from an empty path.");
		stack.pop();
	}

	@Override
	public T getValue() {
		return property.getValue(stack.peek());
	}
}
