package br.com.mirabilis.util.request.http.exception;

/**
 * Classe de exceções do httpManager.
 * @author Rodrigo Simões Rosa
 *
 */
public class HttpRequestException extends Exception {

	/**
	 * Serialização
	 */
	private static final long serialVersionUID = 4985471658011888158L;

	public HttpRequestException(String message) {
		super(message);
	}
	
	public HttpRequestException(Throwable throwable) {
		super(throwable);
	}
	
	public HttpRequestException() {
		super("HttpManager error");
	}
}
