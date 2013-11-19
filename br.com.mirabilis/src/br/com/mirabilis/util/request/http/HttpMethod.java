package br.com.mirabilis.util.request.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import br.com.mirabilis.util.request.http.exception.HttpRequestException;

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
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws HttpRequestException 
	 */
	public abstract InputStream get(String url, int timeoutConnection, int timeoutSocket) throws HttpRequestException, ClientProtocolException, IOException;

	/**
	 * Call http get.
	 * 
	 * @param url
	 * @return
	 * @throws IOException 
	 * @throws HttpRequestException 
	 * @throws ClientProtocolException 
	 */
	public abstract InputStream get(String url) throws ClientProtocolException, HttpRequestException, IOException;
	
	/**
	 * Call http post.
	 * @param url
	 * @param map
	 * @return
	 * @throws IOException 
	 * @throws HttpRequestException 
	 * @throws ClientProtocolException 
	 */
	public abstract InputStream post(String url, Map<String, Object> map) throws ClientProtocolException, HttpRequestException, IOException;
	
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
	 * @throws UnsupportedEncodingException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws HttpRequestException 
	 */
	public abstract InputStream post(String url, Map<String, Object> map, int timeoutConnection, int timeoutSocket) throws UnsupportedEncodingException, HttpRequestException, ClientProtocolException, IOException;
	
}
