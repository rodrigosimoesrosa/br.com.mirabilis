package br.com.mirabilis.util.request.http.exception;

/**
 * Classe de exce��es do httpManager.
 * @author Rodrigo Sim�es Rosa
 *
 */
public class HttpManagerException extends Exception {

	/**
	 * Serializa��o
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
