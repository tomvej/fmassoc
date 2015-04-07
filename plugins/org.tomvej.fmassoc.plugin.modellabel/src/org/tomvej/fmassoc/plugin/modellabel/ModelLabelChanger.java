package org.tomvej.fmassoc.plugin.modellabel;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.tomvej.fmassoc.core.communicate.DataModelTopic;
import org.tomvej.fmassoc.model.db.DataModel;

/**
 * Changes main application window label according to the loaded model.
 * 
 * @author Tomáš Vejpustek
 */
public class ModelLabelChanger {
	@Inject
	private EModelService modelService;
	@Inject
	private MApplication application;

	private MTrimmedWindow getApplicationWindow() {
		List<MTrimmedWindow> windows = modelService.findElements(application, "org.tomvej.fmassoc.core.window.main",
				MTrimmedWindow.class, Collections.emptyList());
		if (windows.isEmpty()) {
			return null;
		} else {
			return windows.get(0);
		}
	}

	private static String trimParentheses(String target) {
		return target.replaceFirst(" ?\\([^)]*\\)$", "");
	}

	private String modelLabel;

	/** Clean label when model is loading. */
	@Inject
	@Optional
	public void modelLoading(@UIEventTopic(DataModelTopic.MODEL_LOADING) String modelLabel) {
		MTrimmedWindow window = getApplicationWindow();
		this.modelLabel = modelLabel;
		if (window != null) {
			window.setLabel(trimParentheses(window.getLabel()));
		}
	}

	/** Set label when model is loaded. */
	@Inject
	@Optional
	public void modelLoaded(@UIEventTopic(DataModelTopic.MODEL_CHANGED) DataModel model) {
		MTrimmedWindow window = getApplicationWindow();
		if (window != null && model != null && modelLabel != null) {
			window.setLabel(trimParentheses(window.getLabel()) + " (" + modelLabel + ")");
		}
	}
}
