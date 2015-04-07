package org.tomvej.fmassoc.swt.dnd;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * Support for dragging elements between viewers.
 * 
 * @author Tomáš Vejpustek
 * @param <T>
 *            Type of draggable elements
 */
public class ViewerDnDSupport<T> {
	private static final Transfer[] TRANSFERS = new Transfer[] { LocalSelectionTransfer.getTransfer() };
	private T selection;
	// whether the item was actually dropped (to avoid losing items)
	private boolean dropped;
	private StructuredViewer source, destination;

	private BiConsumer<T, Pair<StructuredViewer, StructuredViewer>> listener;

	/**
	 * Attach drag and drop support to the viewer.
	 * 
	 * @param target
	 *            Target viewer.
	 * @param baseList
	 *            List of elements in viewer.
	 * @param getSelected
	 *            Method for getting selected element of the viewer.
	 */
	public void pluginViewer(StructuredViewer target, List<T> baseList, Supplier<T> getSelected) {
		ViewerEntry entry = new ViewerEntry(target, baseList, getSelected);
		target.addDragSupport(DND.DROP_MOVE, TRANSFERS, entry.dragListener);
		target.addDropSupport(DND.DROP_MOVE, TRANSFERS, entry.dropListener);
	}

	/**
	 * Registers a listener which is notified of finished DnD events. Listeners
	 * accepts dragged element, source viewer, target viewer.
	 */
	public void setChangeListener(BiConsumer<T, Pair<StructuredViewer, StructuredViewer>> listener) {
		this.listener = listener;
	}

	private void fireChanges() {
		if (listener != null) {
			listener.accept(selection, Pair.of(source, destination));
		}
	}

	private class ViewerEntry {
		private final StructuredViewer viewer;
		private final List<T> elements;
		private final Supplier<T> getSelection;
		private final DragSourceListener dragListener = new DragSourceListener() {

			@Override
			public void dragStart(DragSourceEvent event) {
				if (getSelection.get() == null) {
					event.doit = false;
				} else {
					dropped = false; // must be set here
				}
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				selection = getSelection.get();
				source = viewer;
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				if (dropped) {
					elements.remove(selection);
					fireChanges();
				}
				selection = null;
				source = null;
			}
		};

		private class DropListener extends ViewerDropAdapter {

			public DropListener(Viewer viewer) {
				super(viewer);
			}

			@Override
			public boolean validateDrop(Object target, int operation, TransferData transferType) {
				return true;
			}

			@Override
			public boolean performDrop(Object data) {
				destination = viewer;

				int srcIndex = elements.indexOf(selection);
				if (viewer.equals(source)) {
					if (selection.equals(getCurrentTarget())) {
						return true; // no change
					}
					elements.remove(selection);
				} else {
					dropped = true;
				}

				int index = elements.indexOf(getCurrentTarget());
				switch (getCurrentLocation()) {
					case LOCATION_ON:
					case LOCATION_AFTER:
						index++;
						break;
				}
				if (index < 0) {
					elements.add(selection);
				} else {
					elements.add(index, selection);
				}

				if (!dropped && srcIndex != elements.indexOf(selection)) {
					fireChanges();
				}
				return true;
			}
		}

		private final DropTargetListener dropListener;


		public ViewerEntry(StructuredViewer viewer, List<T> elements, Supplier<T> getSelection) {
			this.viewer = viewer;
			this.elements = Validate.notNull(elements);
			this.getSelection = Validate.notNull(getSelection);
			dropListener = new DropListener(viewer);
		}
	}

}
