package org.tomvej.fmassoc.parts.model.core;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.tomvej.fmassoc.core.wrappers.ListChangeListenerWrapper;

/**
 * List of models. Permits only one add operation:
 * {@link #add(String, ModelLoaderEntry)}. Does not permit setting or moving.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class ModelList extends WritableList {
	private final PreferenceModelManager manager;

	/**
	 * Specify preference manager and initial list of models (does not load
	 * models from preferences).
	 */
	public ModelList(PreferenceModelManager preferenceManager, List<ModelEntry> initialModels) {
		super(initialModels, ModelEntry.class);
		manager = Validate.notNull(preferenceManager);
		addListChangeListener(new ListChangeListenerWrapper(this::handleRemove));
	}

	private void handleRemove(ListDiffEntry diff) {
		if (!diff.isAddition()) {
			manager.remove((ModelEntry) diff.getElement());
		}
	}

	/**
	 * Add a new model. Specify its label and used model loader.
	 * 
	 * @return The newly created model or {@code null} when it was not
	 *         successful.
	 */
	public ModelEntry add(String label, ModelLoaderEntry loader) {
		ModelEntry model = manager.add(label, loader);
		if (model != null) {
			super.add(model);
		}
		return model;
	}

	@Override
	public void add(int index, Object element) {
		throw new UnsupportedOperationException("Cannot add models.");
	}

	@Override
	public boolean add(Object element) {
		throw new UnsupportedOperationException("Cannot add models.");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean addAll(Collection c) {
		throw new UnsupportedOperationException("Cannot add models.");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean addAll(int index, Collection c) {
		throw new UnsupportedOperationException("Cannot add models.");
	}

	@Override
	public Object set(int index, Object element) {
		throw new UnsupportedOperationException("Cannot add models.");
	}

	@Override
	public Object move(int oldIndex, int newIndex) {
		throw new UnsupportedOperationException("Move operation not supported.");
	}
}
