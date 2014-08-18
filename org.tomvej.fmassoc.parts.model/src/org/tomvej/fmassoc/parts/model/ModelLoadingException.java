package org.tomvej.fmassoc.parts.model;

public class ModelLoadingException extends Exception {

	private static final long serialVersionUID = 1459120304486382595L;

	public ModelLoadingException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModelLoadingException(String message) {
		super(message);
	}


}
