package org.tomvej.fmassoc.parts.sql.independent;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.tomvej.fmassoc.core.communicate.PathTransformerTopic;

/**
 * Used to "pin" this transformer part so that it provides transformed path for
 * other application parts.
 * 
 * @author Tomáš Vejpustek
 */
public class UseTransformer {
	private MHandledItem lastSelected;

	private boolean isSelected() {
		return lastSelected != null;
	}

	/**
	 * Switch the pinned state.
	 */
	@Execute
	public void execute(IEventBroker broker, MPart part, MHandledItem item) {
		if (item.isSelected()) {
			assert !isSelected();
			lastSelected = item;
			broker.post(PathTransformerTopic.SELECT, part);
		} else {
			assert isSelected() && item.equals(lastSelected);
			lastSelected = null;
		}

	}

	/**
	 * Listens to other parts being pinned and removes pinning.
	 */
	@Inject
	@Optional
	public void otherSelected(@UIEventTopic(PathTransformerTopic.SELECT) MPart other, MPart thisPart) {
		if (!thisPart.equals(other) && isSelected()) {
			lastSelected.setSelected(false);
			lastSelected = null;
		}
	}

}