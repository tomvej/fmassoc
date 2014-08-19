package org.tomvej.fmassoc.parts.model.manager;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

public class ShowModelManager {
	@Execute
	public void execute(Shell parent) {
		new ModelManagerDialog(parent).open();
	}

}