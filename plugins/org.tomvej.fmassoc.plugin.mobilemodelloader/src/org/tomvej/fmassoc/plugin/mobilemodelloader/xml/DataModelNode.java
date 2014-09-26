package org.tomvej.fmassoc.plugin.mobilemodelloader.xml;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.tuple.Pair;
import org.tomvej.fmassoc.model.builder.simple.AssociationBuilder;
import org.tomvej.fmassoc.model.builder.simple.DataModelBuilder;
import org.tomvej.fmassoc.model.builder.simple.PropertyBuilder;
import org.tomvej.fmassoc.model.builder.simple.TableBuilder;
import org.tomvej.fmassoc.model.builder.simple.TableCache;
import org.tomvej.fmassoc.model.builder.simple.TableImpl;
import org.tomvej.fmassoc.model.db.Multiplicity;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;

/**
 * XML node corresponding to the data model.
 * 
 * @author Tomáš Vejpustek
 */
@XmlRootElement(name = "data_model")
public class DataModelNode {
	static final String IMPL_NAME_XML_PATH = "imp_details/@oracle_sql_name";
	private static final List<String> VERSION_PROPERTIES = Collections.unmodifiableList(Arrays.asList("ID_VERSION",
			"ID_BRANCH", "ID_PREV_VERSION1", "ID_PREV_VERSION2", "FG_OBJ_DELETED", "ID_USER", "TS_USER", "TS_SERVER"));
	private static final List<String> ROOT_PROPERTIES = Collections.unmodifiableList(Arrays.asList("ID_MSG_RCVD",
			"IND_SYNC", "IND_COMMS_PRIORITY"));

	@XmlElement(name = "type")
	private List<TypeNode> types;

	/**
	 * Transform this data model into a {@link DataModelBuilder}.
	 * 
	 * @return Newly created builder corresponding to this model and cache of
	 *         tables by their name.
	 * @throws ModelLoadingException
	 *             When transformation fails.
	 */
	public Pair<DataModelBuilder, TableCache<String>> transform() throws ModelLoadingException {
		if (types == null) {
			throw new ModelLoadingException("Data model contains no tables.");
		}

		TableCache<String> byName = new TableCache<>(t -> t.getName());
		Map<TypeNode, TableImpl> tables = new HashMap<>();

		DataModelBuilder result = new DataModelBuilder(byName);
		TableImpl blob = createBlob(result);

		for (TypeNode type : types) {
			type.validate();
			for (PropertyNode p : type.getProperties()) {
				p.validate();
			}

			TableImpl table = result.addTable(new TableBuilder().setName(type.getName()).setImplName(type.getImplName())
					.setNumber(type.getNumber()).setIdImplName(type.getOIDColumn()));
			if (table == null) {
				throw new ModelLoadingException("Duplicate type names: " + type.getName());
			}
			tables.put(type, table);

			/* Add properties */
			for (PropertyNode p : type.getProperties()) {
				if (p.isBlob()) {
					result.addAssociation(
							new AssociationBuilder().setName(p.getName()).setReverseName("blob_for_" + p.getName())
									.setImplName(p.getImplName()).setMultiplicity(Multiplicity.ONE_TO_ONE)
									.setMandatory(p.isMandatory()), table, blob);
				} else {
					result.addProperty(new PropertyBuilder(p.getName(), p.getImplName()), table);
				}
			}
			/* Add default properties */
			VERSION_PROPERTIES.forEach(s -> result.addProperty(new PropertyBuilder(s), table));
			if (type.getHierarchy().isRoot()) {
				ROOT_PROPERTIES.forEach(s -> result.addProperty(new PropertyBuilder(s), table));
			}
		}

		for (TypeNode type : types) {
			for (AssociationNode assoc : type.getAssociations()) {
				assoc.validate();
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

		return Pair.of(result, byName);
	}

	private static TableImpl createBlob(DataModelBuilder builder) {
		TableImpl blob = builder.addTable(new TableBuilder().setName("BLOB").setImplName("TFCBLOB407").setNumber(0)
				.setIdImplName("ID_BLOB"));
		builder.addProperty(new PropertyBuilder("NO_ROW"), blob);
		builder.addProperty(new PropertyBuilder("TXT_BLOB"), blob);
		builder.addProperty(new PropertyBuilder("LN_SIZE_UNCOMP"), blob);
		builder.addProperty(new PropertyBuilder("TP_COMPRESSION"), blob);
		builder.addProperty(new PropertyBuilder("DT_REAP_CHECK"), blob);
		return blob;
	}

}
