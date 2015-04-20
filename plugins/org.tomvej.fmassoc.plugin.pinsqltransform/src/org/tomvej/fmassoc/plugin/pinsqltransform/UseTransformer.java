package org.tomvej.fmassoc.plugin.pinsqltransform;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.tomvej.fmassoc.core.communicate.PathTransformerTopic;

public class UseTransformer {
	private MHandledItem lastItem;

	@Execute
	public void execute(MHandledItem currentItem, MPart part, IEventBroker broker) {
		if (currentItem.isSelected()) {
			if (lastItem != null) {
				lastItem.setSelected(false);
			}
			lastItem = currentItem;
			broker.post(PathTransformerTopic.SELECT, part);
		} else {
			assert currentItem.equals(lastItem);
			lastItem = null;
			broker.post(PathTransformerTopic.SELECT, null);
		}
	}

}
