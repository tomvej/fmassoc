package org.tomvej.fmassoc.plugin.filters.basic;

import java.util.function.Consumer;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

@FunctionalInterface
public interface ValueControlProvider<T> {

	Control createControl(Composite parent, Consumer<T> listener, T initial);
}
