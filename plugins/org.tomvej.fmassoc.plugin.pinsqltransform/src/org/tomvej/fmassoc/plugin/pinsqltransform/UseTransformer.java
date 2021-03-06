package org.tomvej.fmassoc.plugin.pinsqltransform;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.tomvej.fmassoc.core.communicate.ContextObjects;

/**
 * Pin a transformer part so that it provides transformed path for other
 * application parts.
 * 
 * @author Tomáš Vejpustek
 */
public class UseTransformer {
	private MHandledItem lastItem;

	/**
	 * Switch the pinned state.
	 */
	@Execute
	public void execute(MHandledItem currentItem, MPart part, MApplication app) {
		if (currentItem.isSelected()) {
			if (lastItem != null) {
				lastItem.setSelected(false);
			}
			lastItem = currentItem;
			app.getContext().set(ContextObjects.TRANSFORMATION_PART, part);
		} else {
			assert currentItem.equals(lastItem);
			lastItem = null;
			app.getContext().set(ContextObjects.TRANSFORMATION_PART, null);
		}
	}

}
