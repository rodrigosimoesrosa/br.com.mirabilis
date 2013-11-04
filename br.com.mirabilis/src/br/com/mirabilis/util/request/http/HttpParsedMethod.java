package br.com.mirabilis.util.request.http;

import org.json.JSONObject;
import br.com.mirabilis.util.request.ResponseData;
import br.com.mirabilis.util.request.http.listener.HttpRequestListener;

/**
 * Interface that contains methods for parser objects acquired in http requests.
 * 
 * @author Rodrigo Simões Rosa.
 * 
 */
public interface HttpParsedMethod {

	/**
	 * Do download file.
	 * 
	 * @param url
	 *            to download file.
	 * @return {@link ResponseData} with array of bytes from image.
	 */
	public ResponseData<String> downloadFile(String url);

	/**
	 * Do download image.
	 * 
	 * @param url
	 *            to download image.
	 * @return {@link ResponseData} with array of bytes from image.
	 */
	public ResponseData<byte[]> downloadImage(String url);

	/**
	 * Call http get {@link JSONObject}
	 * 
	 * @param url
	 * @return
	 */
	public ResponseData<JSONObject> getJson(String url);

	/**
	 * Responsible for sending a post called XML.
	 * 
	 * @param url
	 *            address of request
	 * @param xml
	 *            content xml in string
	 * @param listener
	 *            to response data.
	 */
	public void postXML(String url, String xml,
			HttpRequestListener<String> listener);

	/**
	 * Responsible for sending a post called XML.
	 * 
	 * @param url
	 *            address of request.
	 * @param xml
	 *            content xml in string.
	 * @param listener
	 *            to response data.
	 * @param timeoutConnection
	 *            Timeout that will run until the connection established.
	 * @param timeoutSocket
	 *            Timeout will be the waiting time that the client waits.
	 */
	public void postXML(String url, String xml,
			HttpRequestListener<String> listener, String cryptFormat,
			int timeoutConnection, int timeoutSocket);

	/**
	 * The caller sending a Json post.
	 * 
	 * @param url
	 *            address of request.
	 * @param json
	 *            content json to send.
	 * @param listener
	 *            to response data.
	 * @param timeoutConnection
	 *            Timeout that will run until the connection established.
	 * @param timeoutSocket
	 *            Timeout will be the waiting time that the client waits.
	 */
	public void postJson(String url, JSONObject json,
			HttpRequestListener<JSONObject> listener, String cryptFormat,
			int timeoutConnection, int timeoutSocket);

}
