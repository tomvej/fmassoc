package org.tomvej.fmassoc.plugin.mobilemodelloader.xml;

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
import org.tomvej.fmassoc.parts.model.ModelLoadingException;

@XmlRootElement(name = "data_model")
public class DataModelNode {
	static final String IMPL_NAME_XML_PATH = "imp_details/@oracle_sql_name";

	@XmlElement(name = "type")
	private List<TypeNode> types;


	public DataModelBuilder transform() throws ModelLoadingException {
		TableCache<String> byName = new TableCache<>(t -> t.getName());
		Map<TypeNode, TableImpl> tables = new HashMap<>();

		DataModelBuilder result = new DataModelBuilder(byName);
		for (TypeNode type : types) {
			TableImpl table = result.addTable(new TableBuilder().setName(type.getName()).setImplName(type.getImplName())
					.setNumber(type.getNumber()).setIdImplName(type.getOIDColumn()));
			if (table == null) {
				throw new ModelLoadingException("Duplicate type names: " + type.getName());
			}
			tables.put(type, table);
			type.getProperties().forEach(p -> result.addProperty(new PropertyBuilder(p.getName(), p.getImplName()), table));
		}

		for (TypeNode type : types) {
			for (AssociationNode assoc : type.getAssociations()) {
				AssociationBuilder builder = new AssociationBuilder().setName(assoc.getName())
						.setImplName(assoc.getImplName()).setReverseName(assoc.getReverseName())
						.setMandatory(assoc.isMandatory()).setMultiplicity(assoc.getMultiplicity());
				TableImpl destination = byName.get(assoc.getTargetName());
				if (destination == null) {
					throw new ModelLoadingException("Unknown table name: " + assoc.getTargetName());
				}
				result.addAssociation(builder, tables.get(type), destination);
			}
		}

		return result;
	}
}
