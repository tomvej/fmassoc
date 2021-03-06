package org.tomvej.fmassoc.partmanager;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.menu.ItemType;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuItem;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

/**
 * Dynamic menu contribution which expands into all available parts. Allows part
 * closing and opening.
 * 
 * @author Tomáš Vejpustek
 */
public class Menu {
	@Inject
	private EModelService modelService;
	private MCommand openView, closeView;

	/**
	 * Initializes values.
	 */
	@PostConstruct
	public void initialize(MApplication app) {
		openView = modelService.findElements(app, "org.tomvej.fmassoc.partmanager.command.openview", MCommand.class, null)
				.get(0);
		closeView = modelService.findElements(app, "org.tomvej.fmassoc.partmanager.command.closeview", MCommand.class, null)
				.get(0);
	}


	/**
	 * Expands menu into available parts.
	 */
	@AboutToShow
	public void show(List<MMenuElement> items, MTrimmedWindow window) {
		modelService.getActivePerspective(window).getChildren().stream()
				.flatMap(e -> modelService.findElements(e, null, MPart.class, null).stream())
				.map(this::createMenuItem).forEach(i -> items.add(i));
	}

	private MMenuItem createMenuItem(MPart part) {
		MHandledMenuItem item = MMenuFactory.INSTANCE.createHandledMenuItem();
		item.setLabel(part.getLabel());
		item.setType(ItemType.CHECK);

		boolean open = part.isToBeRendered(); // empirically chosen
		item.setCommand(open ? closeView : openView);
		item.setSelected(open);

		MParameter parameter = MCommandsFactory.INSTANCE.createParameter();
		parameter.setName(open ?
				"org.tomvej.fmassoc.partmanager.command.closeview.partid" :
				"org.tomvej.fmassoc.partmanager.command.openview.partid");
		parameter.setValue(part.getElementId());
		item.getParameters().add(parameter);

		return item;
	}
}
