package org.tomvej.fmassoc.parts.sql.tree;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.tomvej.fmassoc.model.db.Property;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.parts.sql.tree.model.AssociationColumns;
import org.tomvej.fmassoc.parts.sql.tree.model.ObjectIdColumn;
import org.tomvej.fmassoc.parts.sql.tree.model.PathContentProvider;
import org.tomvej.fmassoc.parts.sql.tree.model.PropertyColumns;
import org.tomvej.fmassoc.parts.sql.tree.model.TreeNode;
import org.tomvej.fmassoc.parts.sql.tree.model.VersionColumns;
import org.tomvej.fmassoc.swt.wrappers.SelectionWrapper;

/**
 * Handles checked state for path tree.
 * 
 * @author Tomáš Vejpustek
 */
public class PathTreeCheckModel {
	private CheckboxTreeViewer tree;
	private PathContentProvider provider;
	private Map<Class<? extends TreeNode>, Button> buttons = new HashMap<>();
	private Map<Button, Supplier<Collection<? extends TreeNode>>> suppliers = new HashMap<>();

	private final SelectionListener selectionListener = new SelectionWrapper(this::buttonSelected);

	/**
	 * Specify tree and content provider. Content provider must be the same as
	 * the one used by the tree.
	 */
	public PathTreeCheckModel(CheckboxTreeViewer treeViewer, PathContentProvider contentProvider) {
		Validate.isTrue(contentProvider.equals(treeViewer.getContentProvider()));
		tree = treeViewer;
		provider = contentProvider;
		tree.addCheckStateListener(this::checkStateChanged);
	}

	/**
	 * Specify button for ID_OBJECTs.
	 */
	public void setOidButton(Button btn) {
		addButton(btn, ObjectIdColumn.class, provider::getOidColumns);
	}

	/**
	 * Specify button for associations.
	 */
	public void setAssociationButton(Button btn) {
		addButton(btn, AssociationColumns.class, provider::getAssociationProxies);
	}

	/**
	 * Specify buttons for properties (except version properties).
	 */
	public void setPropertyButton(Button btn) {
		addButton(btn, PropertyColumns.class, provider::getPropertyProxies);
	}

	/**
	 * Specify button for version properties.
	 */
	public void setVersionButton(Button btn) {
		addButton(btn, VersionColumns.class, provider::getVersionProxies);
	}

	private void addButton(Button btn, Class<? extends TreeNode> clazz, Supplier<Collection<? extends TreeNode>> supplier) {
		Validate.isTrue((btn.getStyle() & SWT.CHECK) == SWT.CHECK, "Button is not check button.");
		Button old = buttons.put(clazz, btn);
		if (old != null) {
			old.removeSelectionListener(selectionListener);
			suppliers.remove(old);
		}
		suppliers.put(btn, supplier);
		btn.addSelectionListener(selectionListener);
	}


	/**
	 * Listens to checked state changes on the treeviewer.
	 */
	private void checkStateChanged(CheckStateChangedEvent event) {
		assert event.getCheckable().equals(tree); // event from the right tree
		assert tree.getContentProvider().equals(provider); // correct provider

		changeCheckState(event.getElement(), event.getChecked());

		if (event.getElement() instanceof Table) {
			buttons.values().forEach(this::updateButtonState);
		} else if (event.getElement() instanceof TreeNode) {
			updateButtonState(getButton(event.getElement()));
		} else if (event.getElement() instanceof Property) {
			updateButtonState(getButton(getParent(event.getElement())));
		} else {
			throw new IllegalArgumentException("Unsupported element class: " + event.getElement().getClass());
		}
	}

	/**
	 * For given element state change, makes its children and parents checked
	 * accordingly.
	 */
	private void changeCheckState(Object element, boolean checked) {
		tree.setSubtreeChecked(element, checked);
		tree.setGrayed(element, false);
		processParents(element);
	}

	/**
	 * Makes parents checked accordingly from children status.
	 */
	private void processParents(Object element) {
		Object parent = getParent(element);
		while (parent != null) {
			Object[] children = provider.getChildren(parent);
			int number = children.length;
			long checked = Arrays.stream(children).filter(this::isElementChecked).count();
			tree.setChecked(parent, checked > 0);
			tree.setGrayed(parent, checked > 0 && number != checked);

			parent = getParent(parent);
		}
	}

	/**
	 * Updates button checked state from its children.
	 */
	private void updateButtonState(Button btn) {
		Collection<? extends TreeNode> children = getChildren(btn);
		int number = children.size();
		long checked = children.stream().filter(this::isElementChecked).count();
		btn.setSelection(checked > 0);
		btn.setGrayed(checked > 0 && number != checked);
	}


	/**
	 * Returns button for given element.
	 */
	private Button getButton(Object element) {
		return Validate.notNull(buttons.get(element.getClass()),
				"Button for " + element.getClass() + " not specified.");
	}

	/**
	 * Listener to checkbox selection events.
	 */
	private void buttonSelected(SelectionEvent event) {
		assert event.widget instanceof Button;
		assert tree.getContentProvider().equals(provider);
		Button btn = (Button) event.widget;

		btn.setGrayed(false);
		if (tree.getInput() != null) {
			getChildren(btn).forEach(o -> changeCheckState(o, btn.getSelection()));
		}
	}

	/**
	 * Returns parent in tree for given element.
	 */
	private Object getParent(Object element) {
		return provider.getParent(element);
	}

	/**
	 * Returns children elements for given button.
	 */
	private Collection<? extends TreeNode> getChildren(Button btn) {
		return suppliers.get(btn).get();
	}

	/**
	 * Refreshes checked state from buttons.
	 */
	public void refresh() {
		if (tree.getInput() != null) {
			Object[] elements = provider.getElements(tree.getInput());
			Collection<Button> btns = buttons.values();
			if (btns.stream().allMatch(PathTreeCheckModel::isButtonChecked)) {
				Arrays.stream(elements).forEach(e -> tree.setSubtreeChecked(e, true));
			} else if (btns.stream().anyMatch(PathTreeCheckModel::isButtonChecked)) {
				Arrays.stream(elements).forEach(e -> tree.setGrayChecked(e, true));
				btns.stream().filter(PathTreeCheckModel::isButtonChecked)
						.flatMap(b -> getChildren(b).stream())
						.forEach(e -> tree.setSubtreeChecked(e, true));
			}
		}
	}

	private static boolean isButtonChecked(Button btn) {
		if (btn.getGrayed()) {
			btn.setSelection(false);
		}
		return btn.getSelection();
	}

	private boolean isElementChecked(Object element) {
		return tree.getChecked(element) && !tree.getGrayed(element);
	}
}
