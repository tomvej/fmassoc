package org.tomvej.fmassoc.plugin.mobilemodelloader.wizards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.tomvej.fmassoc.core.widgets.tablechooser.TableChooser;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.swt.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.swt.wrappers.TextLabelProvider;

/**
 * Specifies default forbidden tables for a data model.
 * 
 * @author Tomáš Vejpustek
 */
public class ForbiddenPage extends WizardPage {

	private TableChooser chooser;
	private Button addBtn, rmBtn;
	private ListViewer forbiddenList;
	private IObservableList forbidden;

	/**
	 * Create the wizard page.
	 */
	public ForbiddenPage() {
		super("Forbidden Tables");
		setTitle("Forbidden Tables");
		setDescription("Specify default forbidden tables (search does not cross them, unless they are source or destination).");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(3, false));

		GridDataFactory listLayout = GridDataFactory.fillDefaults().grab(true, true);
		GridDataFactory btnLayout = GridDataFactory.fillDefaults();

		forbiddenList = new ListViewer(container, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		forbiddenList.getControl().setLayoutData(listLayout.create());

		Composite btnPanel = new Composite(container, SWT.NONE);
		btnPanel.setLayoutData(GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).create());
		btnPanel.setLayout(new GridLayout(1, false));

		chooser = new TableChooser(container);
		chooser.setLayoutData(listLayout.create());

		addBtn = new Button(btnPanel, SWT.PUSH);
		addBtn.setText("Add");
		addBtn.setLayoutData(btnLayout.create());
		rmBtn = new Button(btnPanel, SWT.PUSH);
		rmBtn.setText("Remove");
		rmBtn.setLayoutData(btnLayout.create());

		setControl(container);


		forbiddenList.setLabelProvider(new TextLabelProvider<Table>(t -> t.getName() + " (" + t.getImplName() + ")"));
		forbiddenList.setContentProvider(new ObservableListContentProvider());
		forbidden = Properties.selfList(Table.class).observe(new ArrayList<>());
		forbiddenList.setInput(forbidden);

		forbidden.addListChangeListener(e -> refresh());
		forbiddenList.addSelectionChangedListener(e -> refresh());
		chooser.setTableListener(t -> refresh());

		addBtn.addSelectionListener(new SelectionWrapper(e -> forbidden.add(chooser.getSelection())));
		rmBtn.addSelectionListener(new SelectionWrapper(e ->
				forbidden.removeAll(((IStructuredSelection) forbiddenList.getSelection()).toList())));

		refresh();
	}

	@SuppressWarnings("unchecked")
	private void refresh() {
		addBtn.setEnabled(chooser.getSelection() != null);
		rmBtn.setEnabled(!forbiddenList.getSelection().isEmpty());
		chooser.setFilter(forbidden);
	}

	/**
	 * Specify tables to choose from.
	 */
	public void setTables(Collection<Table> tables) {
		chooser.setTables(tables);
		forbidden.clear();
	}

	@Override
	public IWizardPage getNextPage() {
		return null;
	}

	/**
	 * Return selected forbidden tables.
	 */
	@SuppressWarnings("unchecked")
	public Collection<Table> getForbidden() {
		return Collections.unmodifiableList(forbidden);
	}

	/**
	 * Specify forbidden tables.
	 */
	public void setForbidden(Collection<Table> forbidden) {
		this.forbidden.clear();
		this.forbidden.addAll(forbidden);
	}
}
