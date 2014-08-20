package org.tomvej.fmassoc.model.builder.simple;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.Named;

class NamedImpl implements Named {
	private final String name, implName;

	NamedImpl(String name, String implName) {
		this.name = Validate.notBlank(name);
		this.implName = Validate.notBlank(implName);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getImplName() {
		return implName;
	}

}
