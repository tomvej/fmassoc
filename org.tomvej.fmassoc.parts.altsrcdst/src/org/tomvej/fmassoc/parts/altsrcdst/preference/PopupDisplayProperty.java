package org.tomvej.fmassoc.parts.altsrcdst.preference;

import java.util.function.Function;

import org.tomvej.fmassoc.model.db.Table;

public enum PopupDisplayProperty implements Function<Table, String> {
	NAME("Table name", t -> t.getName()),
	IMPL_NAME("Table implementation name", t -> t.getImplName());

	private final String caption;
	private final Function<Table, String> labelProvider;

	private PopupDisplayProperty(String caption, Function<Table, String> labelProvider) {
		this.caption = caption;
		this.labelProvider = labelProvider;
	}

	@Override
	public String apply(Table arg0) {
		return labelProvider.apply(arg0);
	}

	@Override
	public String toString() {
		return caption;
	}
}
