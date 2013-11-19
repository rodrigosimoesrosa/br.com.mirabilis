package br.com.mirabilis.util.request.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.mirabilis.util.request.ResponseData;
import br.com.mirabilis.util.request.http.exception.HttpRequestException;
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
	 * @throws IOException 
	 * @throws HttpRequestException 
	 * @throws ClientProtocolException 
	 */
	public String downloadFile(String url) throws ClientProtocolException, HttpRequestException, IOException;

	/**
	 * Do download image.
	 * 
	 * @param url
	 *            to download image.
	 * @return {@link ResponseData} with array of bytes from image.
	 * @throws IOException 
	 * @throws HttpRequestException 
	 */
	public byte[] downloadImage(String url) throws IOException, HttpRequestException;

	/**
	 * Call http get {@link JSONObject}
	 * 
	 * @param url
	 * @return
	 * @throws IOException 
	 * @throws HttpRequestException 
	 * @throws ClientProtocolException 
	 * @throws JSONException 
	 */
	public JSONObject getJson(String url) throws ClientProtocolException, HttpRequestException, IOException, JSONException;

	/**
	 * Responsible for sending a post called XML.
	 * 
	 * @param url
	 *            address of request
	 * @param xml
	 *            content xml in string
	 * @param listener
	 *            to response data.
	 * @throws IOException 
	 * @throws HttpRequestException 
	 * @throws ClientProtocolException 
	 */
	public void postXML(String url, String xml,
			HttpRequestListener<String> listener) throws ClientProtocolException, HttpRequestException, IOException;

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
	 * @throws HttpRequestException 
	 * @throws UnsupportedEncodingException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public void postXML(String url, String xml,
			HttpRequestListener<String> listener, String cryptFormat,
			int timeoutConnection, int timeoutSocket) throws HttpRequestException, UnsupportedEncodingException, ClientProtocolException, IOException;

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
	 * @throws UnsupportedEncodingException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws HttpRequestException 
	 * @throws JSONException 
	 */
	public void postJson(String url, JSONObject json,
			HttpRequestListener<JSONObject> listener, String cryptFormat,
			int timeoutConnection, int timeoutSocket) throws UnsupportedEncodingException, ClientProtocolException, IOException, HttpRequestException, JSONException;

}
