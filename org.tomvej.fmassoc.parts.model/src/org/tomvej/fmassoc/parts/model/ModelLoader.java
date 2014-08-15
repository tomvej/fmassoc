package org.tomvej.fmassoc.parts.model;

import org.tomvej.fmassoc.model.db.DataModel;

public interface ModelLoader {
	DataModel loadModel(String id) throws ModelLoadingException;
}
