package org.tomvej.fmassoc.parts.paths;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.tomvej.fmassoc.core.communicate.ContextObjects;
import org.tomvej.fmassoc.core.communicate.PathSearchTopic;
import org.tomvej.fmassoc.core.wrappers.TextColumnLabelProvider;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.path.Path;

/**
 * Part with found paths table.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class Part {
	private TableViewer pathTable;
	private TableViewerColumn pathColumn;

	/**
	 * Create components comprising the found table part.
	 */
	@PostConstruct
	public void createComponents(Composite parent, ESelectionService selectionService,
			@Named(ContextObjects.FOUND_PATHS) List<Path> foundPaths, PathPreferenceManager preference) {
		pathTable = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		pathTable.getTable().setHeaderVisible(true);
		pathTable.getTable().setLinesVisible(true);
		pathTable.getTable().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		pathColumn = new TableViewerColumn(pathTable, SWT.LEFT);
		pathColumn.getColumn().setText("Path");
		pathColumn.getColumn().setWidth(100);

		pathColumn.setLabelProvider(preference.getLabelProvider());

		pathTable.setContentProvider(ArrayContentProvider.getInstance());
		pathTable.setInput(foundPaths);
		pathTable.addSelectionChangedListener(e -> selectionService.setSelection(
				((IStructuredSelection) pathTable.getSelection()).getFirstElement()));
	}

	/**
	 * Add path to the table.
	 */
	@Inject
	@Optional
	public void receivePath(@UIEventTopic(PathSearchTopic.PUBLISH) Path target) {
		pathTable.refresh();
	}

	/**
	 * Change path column label provider. If null changes to default.
	 */
	@Inject
	@Optional
	public void labelProviderChanged(@UIEventTopic(PathTablePreferenceTopic.PROVIDER_CHANGE) ColumnLabelProvider provider) {
		pathColumn.setLabelProvider(provider != null ? provider :
				new TextColumnLabelProvider<Path>(Part::getDefaultLabel));
	}

	private static String getDefaultLabel(Path target) {
		StringBuilder result = new StringBuilder(target.getSource().getName());
		for (AssociationProperty assoc : target.getAssociations()) {
			result.append(" ").append(assoc.getName()).append(" ").append(assoc.getDestination().getName());
		}
		return result.toString();
	}

}
