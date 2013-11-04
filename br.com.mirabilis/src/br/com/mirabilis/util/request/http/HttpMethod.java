package br.com.mirabilis.util.request.http;

import java.io.InputStream;
import java.util.Map;

import br.com.mirabilis.util.request.ResponseData;

/**
 * {@link HttpMethod} of {@link HttpRequest}
 * 
 * @author Rodrigo Simões Rosa.
 */
public interface HttpMethod {

	/**
	 * Call http get.
	 * 
	 * @param url
	 * @param timeoutConnection
	 *            Timeout that will run until the connection established.
	 * @param timeoutSocket
	 *            Timeout will be the waiting time that the client waits.
	 * @return
	 */
	public abstract ResponseData<InputStream> get(String url, int timeoutConnection, int timeoutSocket);

	/**
	 * Call http get.
	 * 
	 * @param url
	 * @return
	 */
	public abstract ResponseData<InputStream> get(String url);
	
	/**
	 * Call http post.
	 * @param url
	 * @param map
	 * @return
	 */
	public abstract ResponseData<InputStream> post(String url, Map<String, Object> map);
	
	/**
	 * Call http get.
	 * 
	 * @param url
	 * @param map 
	 * @param timeoutConnection
	 *            Timeout that will run until the connection established.
	 * @param timeoutSocket
	 *            Timeout will be the waiting time that the client waits.
	 * @return
	 */
	public abstract ResponseData<InputStream> post(String url, Map<String, Object> map, int timeoutConnection, int timeoutSocket);
	
}
