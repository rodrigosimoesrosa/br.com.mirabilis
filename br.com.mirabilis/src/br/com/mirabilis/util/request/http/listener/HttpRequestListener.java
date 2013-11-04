package br.com.mirabilis.util.request.http.listener;

import br.com.mirabilis.util.request.ResponseData;
import br.com.mirabilis.util.request.http.HttpRequest;

/**
 * Interface of {@link HttpRequest}
 * @author Rodrigo Simões Rosa.
 *
 * @param <InputStrem>
 */
public interface HttpRequestListener<InputStrem> {
	
	/**
	 * Method that result of request {@link HttpRequest} 
	 * @param data
	 */
	public void onResponseData(ResponseData<InputStrem> data);
}
