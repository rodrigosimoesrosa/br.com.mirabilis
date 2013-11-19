package br.com.mirabilis.util.request.http.listener;

import br.com.mirabilis.util.request.http.HttpRequest;

/**
 * Interface of {@link HttpRequest}
 * 
 * @author Rodrigo Simões Rosa.
 * 
 * @param <T>
 */
public interface HttpRequestListener<T> {

	/**
	 * Method that result of request {@link HttpRequest}
	 * 
	 * @param data
	 */
	public void onResponseHttp(T data);
}
