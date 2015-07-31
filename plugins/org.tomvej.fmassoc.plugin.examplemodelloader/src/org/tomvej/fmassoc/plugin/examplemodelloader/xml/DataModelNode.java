package org.tomvej.fmassoc.plugin.examplemodelloader.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.tomvej.fmassoc.model.builder.simple.AssociationBuilder;
import org.tomvej.fmassoc.model.builder.simple.DataModelBuilder;
import org.tomvej.fmassoc.model.builder.simple.PropertyBuilder;
import org.tomvej.fmassoc.model.builder.simple.TableBuilder;
import org.tomvej.fmassoc.model.builder.simple.TableCache;
import org.tomvej.fmassoc.model.builder.simple.TableImpl;
import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.model.db.Multiplicity;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;

/**
 * XML element containing the whole data model
 * 
 * @author Tomáš Vejpustek
 */
@XmlRootElement(name = "datamodel")
public class DataModelNode {
	@XmlElement(name = "table")
	private List<TableNode> tables;

	/**
	 * Transform this XML element into actual java data model.
	 */
	public DataModel transform() throws ModelLoadingException {
		if (tables == null) {
			throw new ModelLoadingException("Data model contains no tables.");
		}

		TableCache<String> byName = new TableCache<String>(t -> t.getName());
		DataModelBuilder dataModel = new DataModelBuilder(byName);
		Map<TableNode, TableImpl> mapping = new HashMap<>();

		int number = 0;
		for (TableNode table : tables) {
			table.validate();
			for (PropertyNode property : table.getProperties()) {
				property.validate();
			}

			TableImpl result = dataModel.addTable(new TableBuilder().setNumber(number++).setName(table.getName())
					.setImplName(table.getName()).setIdImplName("ID"));
			if (result == null) {
				throw new ModelLoadingException("Duplicate table name: " + table.getName());
			}
			if (table.isForbidden()) {
				dataModel.addForbidden(result);
			}

			table.getProperties().forEach(p -> dataModel.addProperty(new PropertyBuilder(p.getName()), result));
			mapping.put(table, result);
		}

		for (TableNode table : tables) {
			for (AssociationNode assoc : table.getAssociation()) {
				assoc.validate();
				AssociationBuilder result = new AssociationBuilder()
						.setName(assoc.getName())
						.setReverseName(assoc.getReverse())
						.setImplName("ID_" + assoc.getTable())
						.setMandatory(assoc.isMandatory())
						.setMultiplicity(assoc.isUnique() ? Multiplicity.ONE_TO_ONE : Multiplicity.MANY_TO_ONE);
				TableImpl dst = byName.get(assoc.getTable());
				if (dst == null) {
					throw new ModelLoadingException("Unknown destination table for `" + assoc.getName() + "': "
							+ assoc.getTable());
				}
				dataModel.addAssociation(result, mapping.get(table), dst);
			}
		}

		return dataModel.create();
	}
}
