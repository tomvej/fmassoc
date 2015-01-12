package org.tomvej.fmassoc.parts.paths.filter;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;

public class FilterHandler {
	private MHandledItem handle;

	@Execute
	public void execute(MHandledItem handle) {
		this.handle = handle;
	}

	private void setDefaultTooltip() {
		if (handle != null) {
			handle.setTooltip(handle.getPersistedState().getOrDefault("tooltip", "Filter"));
		}
	}
}
