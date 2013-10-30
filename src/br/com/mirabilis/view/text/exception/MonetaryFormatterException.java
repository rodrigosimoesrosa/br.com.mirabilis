package br.com.mirabilis.view.text.exception;

import br.com.mirabilis.view.text.formatter.MonetaryFormatter;

/**
 * Exception class of {@link MonetaryFormatter} a String for money.
 * 
 * @author Rodrigo Simões Rosa
 * 
 */
public class MonetaryFormatterException extends Exception {

	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = 1948718393905497401L;

	/**
	 * Constructor.
	 */
	public MonetaryFormatterException() {
		super("MonetaryFormatter Error");
	}

	/**
	 * Constructor.
	 * 
	 * @param value
	 */
	public MonetaryFormatterException(String value) {
		super(value);
	}

	/**
	 * Constructor.
	 * 
	 * @param e
	 */
	public MonetaryFormatterException(Throwable e) {
		super(e);
	}
}
