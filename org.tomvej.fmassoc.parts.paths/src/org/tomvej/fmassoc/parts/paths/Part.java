package org.tomvej.fmassoc.parts.paths;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;
import org.tomvej.fmassoc.core.communicate.ContextObjects;
import org.tomvej.fmassoc.core.communicate.PathSearchTopic;
import org.tomvej.fmassoc.core.properties.PathPropertyEntry;
import org.tomvej.fmassoc.core.search.SearchInput;
import org.tomvej.fmassoc.core.tables.ColumnSortSupport;
import org.tomvej.fmassoc.core.tables.SortEntry;
import org.tomvej.fmassoc.core.wrappers.KeyReleasedWrapper;
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
	@Inject
	private IEclipseContext context;
	@Inject
	private IEventBroker broker;

	private TableViewer pathTable;
	private TableViewerColumn pathColumn;
	private final Map<PathPropertyEntry<?>, TableColumn> propertyColumns = new HashMap<>();
	private ColumnSortSupport sortSupport;
	private Clipboard clipboard;

	/**
	 * Create components comprising the found table part.
	 */
	@PostConstruct
	public void createComponents(Composite parent, ESelectionService selectionService, Display display,
			@Named(ContextObjects.FOUND_PATHS) List<Path> foundPaths, PathPreferenceManager preference) {
		clipboard = new Clipboard(display);

		pathTable = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		pathTable.getTable().setHeaderVisible(true);
		pathTable.getTable().setLinesVisible(true);
		pathTable.getTable().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		sortSupport = new ColumnSortSupport(pathTable);

		pathColumn = new TableViewerColumn(pathTable, SWT.LEFT);
		pathColumn.getColumn().setText("Path");
		pathColumn.getColumn().setWidth(100);

		preference.getColumns().forEach(this::addColumn);

		labelProviderChanged(preference.getLabelProvider());

		pathTable.setContentProvider(ArrayContentProvider.getInstance());
		pathTable.setInput(foundPaths);
		pathTable.addSelectionChangedListener(e -> selectionService.setSelection(
				((IStructuredSelection) pathTable.getSelection()).getFirstElement()));
		pathTable.addDragSupport(DND.DROP_COPY, new Transfer[] { TextTransfer.getInstance() }, new DragSourceAdapter() {

			public void dragStart(DragSourceEvent event) {
				if (getTransformedPath() == null) {
					event.doit = false;
				}
			};

			public void dragSetData(DragSourceEvent event) {
				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					event.data = getTransformedPath();
				}
			};
		});
		pathTable.getTable().addKeyListener(new KeyReleasedWrapper('c', SWT.CTRL, e -> copyTransformedPath()));
	}

	private String getTransformedPath() {
		return (String) context.get(ContextObjects.TRANSFORMED_PATH);
	}

	private void copyTransformedPath() {
		String trans = getTransformedPath();
		if (trans != null) {
			clipboard.setContents(new Object[] { trans }, new Transfer[] { TextTransfer.getInstance() });
		}
	}

	/**
	 * Add path to the table.
	 */
	@Inject
	@Optional
	public void receivePath(@UIEventTopic(PathSearchTopic.PUBLISH) Path target) {
		pathTable.refresh();
		propertyColumns.values().forEach(c -> c.pack());
		pathColumn.getColumn().pack();
	}

	/**
	 * Clear table on search start.
	 */
	@Inject
	@Optional
	public void searchStarted(@UIEventTopic(PathSearchTopic.START) SearchInput target) {
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
		pathTable.refresh();
	}

	private static String getDefaultLabel(Path target) {
		StringBuilder result = new StringBuilder(target.getSource().getName());
		for (AssociationProperty assoc : target.getAssociations()) {
			result.append(" ").append(assoc.getName()).append(" ").append(assoc.getDestination().getName());
		}
		return result.toString();
	}

	/**
	 * Add path property column.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Inject
	@Optional
	public void addColumn(@UIEventTopic(PathTablePreferenceTopic.COLUMN_ADDED) PathPropertyEntry<?> columnEntry) {
		if (propertyColumns.containsKey(columnEntry)) {
			return;
		}
		TableViewerColumn viewerColumn = new TableViewerColumn(pathTable, SWT.LEFT,
				pathTable.getTable().getColumnCount() - 1);
		TableColumn column = viewerColumn.getColumn();
		column.setText(columnEntry.getName());
		column.setToolTipText(columnEntry.getDescription());
		column.setWidth(100);

		viewerColumn.setLabelProvider(new TextColumnLabelProvider<Path>(
				p -> columnEntry.getProperty().getValue(p).toString()));

		sortSupport.addColumn(column);
		sortSupport.setComparator(column,
				new PathPropertyComparator(columnEntry.getProperty(), columnEntry.getComparator()));

		propertyColumns.put(columnEntry, column);
		broker.post(MultisortTopic.COLUMNS, propertyColumns.values());
		pathTable.refresh();
	}

	/**
	 * Remove path property column.
	 */
	@Inject
	@Optional
	public void removeColumn(@UIEventTopic(PathTablePreferenceTopic.COLUMN_REMOVED) PathPropertyEntry<?> columnEntry) {
		TableColumn column = propertyColumns.remove(columnEntry);
		if (column != null) {
			column.dispose();
			sortSupport.setComparator(column, null);
		}
	}

	/**
	 * Apply multisort.
	 */
	@Inject
	@Optional
	public void multisort(@UIEventTopic(MultisortTopic.SORT) List<SortEntry> sort) {
		if (sort != null) {
			sortSupport.multisort(sort);
		}
	}

	/**
	 * Free system resources.
	 */
	@PreDestroy
	public void dispose() {
		clipboard.dispose();
	}
}
