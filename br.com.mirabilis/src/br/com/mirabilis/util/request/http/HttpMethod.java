package br.com.mirabilis.util.request.http;

import java.io.InputStream;

import org.json.JSONObject;

import br.com.mirabilis.util.request.ResponseData;
import br.com.mirabilis.util.request.http.listener.HttpRequestListener;

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
	public abstract ResponseData<InputStream> get(String url,
			int timeoutConnection, int timeoutSocket);

	/**
	 * Call http get.
	 * 
	 * @param url
	 * @return
	 */
	public abstract ResponseData<InputStream> get(String url);
	
	/**
	 * Call http get {@link JSONObject}
	 * @param url
	 * @return
	 */
	public ResponseData<JSONObject> getJson(String url);
	
	/**
	 * Responsible for sending a post called XML.
	 * @param url address of request
	 * @param xml content xml in string
	 * @param listener to response data.
	 */
	public void postXML(String url, String xml, HttpRequestListener<InputStream> listener);
	
	
	/**
	 * Responsible for sending a post called XML.
	 * @param url address of request.
	 * @param xml content xml in string.
	 * @param listener to response data.
	 * @param timeoutConnection
	 *            Timeout that will run until the connection established.
	 * @param timeoutSocket
	 *            Timeout will be the waiting time that the client waits.
	 */
	public void postXML(String url, String xml, HttpRequestListener<InputStream> listener, String cryptFormat, int timeoutConnection, int timeoutSocket);
	
	/**
	 * The caller sending a Json post.
	 * 
	 * @param url address of request.
	 * @param json content json to send.
	 * @param listener to response data.
	 * @param timeoutConnection
	 *            Timeout that will run until the connection established.
	 * @param timeoutSocket
	 *            Timeout will be the waiting time that the client waits.
	 */
	public void postJson(String url, JSONObject json, HttpRequestListener<InputStream> listener, String cryptFormat,int timeoutConnection, int timeoutSocket);
}
