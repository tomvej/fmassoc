package org.tomvej.fmassoc.parts.paths;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.tomvej.fmassoc.core.communicate.PathSearchTopic;
import org.tomvej.fmassoc.core.wrappers.TextColumnLabelProvider;
import org.tomvej.fmassoc.model.path.Path;

/**
 * Part with found paths table.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class Part {
	private TableViewer pathTable;

	/**
	 * Create components comprising the found table part.
	 */
	@PostConstruct
	public void createComponents(Composite parent, ESelectionService selectionService) {
		pathTable = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		pathTable.getTable().setHeaderVisible(true);
		pathTable.getTable().setLinesVisible(true);
		pathTable.getTable().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		TableViewerColumn pathColumn = new TableViewerColumn(pathTable, SWT.LEFT);
		pathColumn.getColumn().setText("Path");
		pathColumn.getColumn().setWidth(100);

		// FIXME create different action provider
		pathColumn.setLabelProvider(new TextColumnLabelProvider<Path>(path -> path.getAssociations().toString()));

		pathTable.addSelectionChangedListener(e -> selectionService.setSelection(
				((IStructuredSelection) pathTable.getSelection()).getFirstElement()));
	}

	/**
	 * Add path to the table.
	 */
	@Inject
	@Optional
	public void receivePath(@UIEventTopic(PathSearchTopic.PUBLISH) Path target) {
		pathTable.add(target);
	}

}
