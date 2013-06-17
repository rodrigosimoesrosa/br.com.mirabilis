package br.com.mirabilis.view.base.popup.exception;

/**
 * PopupException, class exception from PopupBase.
 * @author Rodrigo Simões Rosa.
 *
 */
public class PopupException extends Exception {

	/**
	 * Serialization
	 */
	private static final long serialVersionUID = 5337340260648487027L;
	
	public PopupException() {
		super("Error in PopupBase");
	}
	
	public PopupException(Throwable t){
		super(t);
	}
	
	public PopupException(String s){
		super(s);
	}
}
