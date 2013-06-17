package br.com.mirabilis.view.text.exception;

/**
 * Classe de exceção da formatação de uma String para monetário.
 * @author Rodrigo Simões Rosa
 *
 */
public class MonetaryFormatException extends Exception {

	/**
	 * Serialização
	 */
	private static final long serialVersionUID = 1L;
	
	public MonetaryFormatException() {
		super("Ocorreu um erro na formatação");
	}
	
	public MonetaryFormatException(String value){
		super(value);
	}
	
	public MonetaryFormatException(Throwable e){
		super(e);
	}
}
