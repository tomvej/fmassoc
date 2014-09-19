package org.tomvej.fmassoc.plugin.mobilemodelloader.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data_model")
public class DataModelNode {
	public static final String IMPL_NAME_XML_PATH = "imp_details/@oracle_sql_name";

	@XmlElement(name = "type")
	private List<TypeNode> tables;

}
