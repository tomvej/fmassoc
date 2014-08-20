package org.tomvej.fmassoc.plugin.constantmodelloader;

import org.eclipse.jface.wizard.IWizard;
import org.tomvej.fmassoc.model.builder.simple.AssociationBuilder;
import org.tomvej.fmassoc.model.builder.simple.DataModelBuilder;
import org.tomvej.fmassoc.model.builder.simple.TableBuilder;
import org.tomvej.fmassoc.model.builder.simple.TableImpl;
import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.parts.model.ModelLoader;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;

/**
 * Creates only one model (fragment of FM data model) independently on model id.
 * Used for testing purposes.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class ConstantModelLoader implements ModelLoader {

	private static TableBuilder setupTable(TableBuilder target, String name, String implName, int number) {
		target.setName(name);
		target.setImplName("TFT" + number + implName);
		target.setNumber(number);
		return target;
	}

	private static TableBuilder setupTable(TableBuilder target, String name, int number) {
		target.setName(name);
		target.setImplName("TFT" + number + name);
		target.setNumber(number);
		return target;
	}

	private static void addAssociation(DataModelBuilder builder, TableImpl src, TableImpl dst, boolean many, boolean opt,
			String name, String reverseName, String implName) {
		builder.addAssociation(new AssociationBuilder(name, implName, null, !opt, reverseName), src, dst);
	}

	@Override
	public DataModel loadModel(String id) throws ModelLoadingException {
		DataModelBuilder model = new DataModelBuilder();
		TableBuilder tables = new TableBuilder().setIdImplName("ID_OBJECT");

		TableImpl wr359 = model.addTable(setupTable(tables, "WORK_REQUEST", "WORKREQUEST", 359));
		TableImpl work170 = model.addTable(setupTable(tables, "WORK", 170));
		TableImpl mwr360 = model.addTable(setupTable(tables, "MOB_WORK_REQ", "MOBWORKREQ", 360));
		TableImpl mwork166 = model.addTable(setupTable(tables, "MOBILE_WORK", "MOBILEWORK", 166));
		TableImpl wsch164 = model.addTable(setupTable(tables, "WORK_SCHEDULE", "WORKSCHEDULE", 164));
		TableImpl task123 = model.addTable(setupTable(tables, "TASK", 123));
		TableImpl timeb160 = model.addTable(setupTable(tables, "TIME_BOOKING", "TIMEBOOKING", 160));
		TableImpl rcr182 = model.addTable(setupTable(tables, "RESOURCE_CREW_RECORD", "RESOURCECREW", 182));

		tables.setIdImplName("ID_O_PARTIALINFO");
		TableImpl winfo165 = model.addTable(setupTable(tables, "WORK_INFO", "WORKINFO", 165));
		TableImpl taskinfo136 = model.addTable(setupTable(tables, "TASK_INFO", "TASKINFO", 136));
		TableImpl tbinfo163 = model.addTable(setupTable(tables, "TIME_BOOKING_INFO", "TIMEBOOKINGINFO", 163));

		addAssociation(model, work170, winfo165, true, true, "follows_from", "has_follow_on", "ID_O_WORKINFO_F");
		addAssociation(model, work170, wr359, true, false, "is_on_work_request", "contains", "id_o_workrequest");
		addAssociation(model, wsch164, winfo165, true, false, "defined_by", "defines", "ID_O_WORKINFO");
		addAssociation(model, mwork166, mwr360, true, false, "is_component_of", "contains", "ID_O_MOB_WORK_REQ");
		addAssociation(model, mwork166, wsch164, false, false, "is_for", "has", "ID_O_WORKSCHEDULE");
		addAssociation(model, mwr360, wr359, true, false, "is_defined_by", "has_wr_specific", "ID_O_WORK_REQUEST");
		addAssociation(model, task123, taskinfo136, false, false, "has_partial", "is_partial_for", "ID_O_TASKINFO");
		addAssociation(model, task123, mwork166, true, true, "is_described_by", "describes", "ID_O_MOBILEWORK");
		addAssociation(model, taskinfo136, winfo165, true, true, "describes", "is_described_by", "ID_O_WORKINFO");
		addAssociation(model, timeb160, mwork166, true, false, "has_mobilework", "for_mobilework", "ID_O_MOBILEWORK");
		addAssociation(model, timeb160, tbinfo163, false, true, "has_partial", "is_partial_for", "ID_O_TIMEBOOKINGINFO");
		addAssociation(model, tbinfo163, rcr182, false, true, "is_in_progress_with", "current", "ID_O_RESOURCECREW_C");
		addAssociation(model, tbinfo163, taskinfo136, true, true, "records", "is_recorder_for", "ID_O_TASKINFO");
		addAssociation(model, tbinfo163, rcr182, false, false, "was_made_by", "previous", "ID_O_RESOURCECREW_P");
		addAssociation(model, rcr182, taskinfo136, true, true, "is_performing_a", "is_performed_by", "ID_O_TASKINFO");
		addAssociation(model, rcr182, mwork166, true, true, "is_working_on", "is_performed_by", "ID_O_MOBILEWORK");
		return model.create();
	}

	@Override
	public IWizard createNewWizard(String id) {
		return new OnePageWizard(new NewWizardPage());
	}

	@Override
	public IWizard createEditWizard(String id) {
		return new OnePageWizard(new EditWizardPage());
	}


}
