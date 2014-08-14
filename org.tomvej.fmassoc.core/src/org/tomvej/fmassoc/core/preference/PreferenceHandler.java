package org.tomvej.fmassoc.core.preference;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.swt.widgets.Shell;

public class PreferenceHandler {

	@Execute
	public void execute(Shell shell, IExtensionRegistry registry, Logger logger,
			@Optional @Named("org.eclipse.ui.window.preferences.pageid") String pageId) {
		IConfigurationElement[] config = registry.getConfigurationElementsFor("org.tomvej.fmassoc.core.preferencePage");
		Map<String, Pair<PreferenceNode, String>> nodes = new HashMap<>();
		for (IConfigurationElement elem : config) {
			String id = elem.getAttribute("id");
			try {
				PreferenceNode node = new PreferenceNode(id, (IPreferencePage) elem.createExecutableExtension("class"));
				nodes.put(id, Pair.of(node, elem.getAttribute("parent")));
			} catch (CoreException ce) {
				logger.error(ce, "Cannot create preference page " + id + ".");
			}
		}

		PreferenceManager manager = new PreferenceManager();
		for (Map.Entry<String, Pair<PreferenceNode, String>> element : nodes.entrySet()) {
			PreferenceNode node = element.getValue().getLeft();
			String parent = element.getValue().getRight();

			if (parent == null) {
				manager.addToRoot(node);
			} else {
				Pair<PreferenceNode, String> parentNode = nodes.get(parent);
				if (parentNode != null) {
					parentNode.getLeft().add(node);
				} else {
					logger.warn("Cannot find parent node " + parent + " for node " + element.getKey()
							+ ". Appending to root.");
					manager.addToRoot(node);
				}
			}
		}

		PreferenceDialog dialog = new PreferenceDialog(shell, manager);
		if (pageId != null) {
			dialog.setSelectedNode(pageId);
		}
		dialog.open();
	}
}
