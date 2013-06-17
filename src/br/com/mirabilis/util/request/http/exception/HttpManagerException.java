package br.com.mirabilis.util.request.http.exception;

/**
 * Classe de exceções do httpManager.
 * @author Rodrigo Simões Rosa
 *
 */
public class HttpManagerException extends Exception {

	/**
	 * Serialização
	 */
	private static final long serialVersionUID = 4985471658011888158L;

	public HttpManagerException(String message) {
		super(message);
	}
	
	public HttpManagerException(Throwable throwable) {
		super(throwable);
	}
	
	public HttpManagerException() {
		super("HttpManager error");
	}
}
