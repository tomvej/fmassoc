package org.tomvej.fmassoc.parts.model;

public class ModelLoadingException extends Exception {

	public ModelLoadingException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModelLoadingException(String message) {
		super(message);
	}
}
