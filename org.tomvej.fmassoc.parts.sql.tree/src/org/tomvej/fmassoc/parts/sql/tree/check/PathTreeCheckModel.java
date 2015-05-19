package org.tomvej.fmassoc.parts.sql.tree.check;

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

public class PathTreeCheckModel {
	private CheckboxTreeViewer tree;
	private PathContentProvider provider;
	private Map<Class<? extends TreeNode>, Button> buttons = new HashMap<>();
	private Map<Button, Supplier<Collection<? extends TreeNode>>> suppliers = new HashMap<>();

	private final SelectionListener selectionListener = new SelectionWrapper(this::buttonSelected);

	public PathTreeCheckModel(CheckboxTreeViewer treeViewer, PathContentProvider contentProvider) {
		Validate.isTrue(contentProvider.equals(treeViewer.getContentProvider()));
		tree = treeViewer;
		provider = contentProvider;
		tree.addCheckStateListener(this::checkStateChanged);
	}

	public void setOidButton(Button btn) {
		addButton(btn, ObjectIdColumn.class, provider::getOidColumns);
	}

	public void setAssociationButton(Button btn) {
		addButton(btn, AssociationColumns.class, provider::getAssociationProxies);
	}

	public void setPropertyButton(Button btn) {
		addButton(btn, PropertyColumns.class, provider::getPropertyProxies);
	}

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


	private void checkStateChanged(CheckStateChangedEvent event) {
		assert event.getCheckable().equals(tree); // event from the right tree
		assert tree.getContentProvider().equals(provider); // correct provider

		changeCheckState(event.getElement(), event.getChecked());

		if (event.getElement() instanceof Table) {
			buttons.values().forEach(this::checkButtonState);
		} else if (event.getElement() instanceof TreeNode) {
			checkButtonFor(event.getElement());
		} else if (event.getElement() instanceof Property) {
			checkButtonFor(getParent(event.getElement()));
		} else {
			throw new IllegalArgumentException("Unsupported element class: " + event.getElement().getClass());
		}
	}

	private void changeCheckState(Object element, boolean checked) {
		tree.setSubtreeChecked(element, checked);
		tree.setGrayed(element, false);
		processParents(element);
	}

	private void processParents(Object element) {
		Object parent = getParent(element);
		while (parent != null) {
			Object[] children = provider.getChildren(parent);
			int number = children.length;
			long checked = Arrays.stream(children).filter(tree::getChecked).count();
			tree.setChecked(parent, checked > 0);
			tree.setGrayed(parent, checked > 0 && number != checked);

			parent = getParent(parent);
		}
	}

	private void checkButtonState(Button btn) {
		Collection<? extends TreeNode> children = getChildren(btn);
		int number = children.size();
		long checked = children.stream().filter(tree::getChecked).count();
		btn.setSelection(checked > 0);
		btn.setGrayed(checked > 0 && number != checked);
	}

	private void checkButtonFor(Object element) {
		checkButtonState(Validate.notNull(buttons.get(element.getClass()),
				"Button for " + element.getClass() + " not specified."));
	}

	private void buttonSelected(SelectionEvent event) {
		assert event.widget instanceof Button;
		assert tree.getContentProvider().equals(provider);
		Button btn = (Button) event.widget;

		btn.setGrayed(false);
		getChildren(btn).forEach(o -> changeCheckState(o, btn.getSelection()));
	}

	private Object getParent(Object element) {
		return provider.getParent(element);
	}

	private Collection<? extends TreeNode> getChildren(Button btn) {
		return suppliers.get(btn).get();
	}

}
