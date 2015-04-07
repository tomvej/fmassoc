package org.tomvej.fmassoc.parts.model;

/**
 * Exception signifying an error during model loading.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class ModelLoadingException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Specify message and cause.
	 */
	public ModelLoadingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Specify message.
	 */
	public ModelLoadingException(String message) {
		super(message);
	}

	/**
	 * Specify cause.
	 */
	public ModelLoadingException(Throwable cause) {
		super(cause);
	}

}
