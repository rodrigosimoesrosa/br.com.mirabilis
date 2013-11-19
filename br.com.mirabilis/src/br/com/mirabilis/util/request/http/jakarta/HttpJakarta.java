package br.com.mirabilis.util.request.http.jakarta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import br.com.mirabilis.util.request.http.HttpRequest;
import br.com.mirabilis.util.request.http.exception.HttpRequestException;
import br.com.mirabilis.util.request.http.listener.HttpRequestListener;

/**
 * Implementation of {@link HttpRequest} using Jakarta lib's for request http.
 * 
 * @author Rodrigo Simões Rosa
 * 
 */
public final class HttpJakarta extends HttpRequest {

	private static final String ERROR_HTTP = "Error http : ";
	private static final String ENTITY_NULL = "Objeto entity nulo";

	public HttpJakarta(Context context) {
		this.context = context;
	}

	public InputStream get(String url) throws ClientProtocolException,
			HttpRequestException, IOException {
		return get(url, this.timeoutConnection, this.timeoutSocket);
	}

	public InputStream get(String url, int timeoutConnection, int timeoutSocket)
			throws HttpRequestException, ClientProtocolException, IOException {

		HttpClient httpClient = new DefaultHttpClient(getHttpParams(
				timeoutConnection, timeoutSocket));
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = null;
		HttpEntity httpEntity = null;

		InputStream data = null;

		httpResponse = httpClient.execute(httpGet);
		httpEntity = httpResponse.getEntity();

		int status = httpResponse.getStatusLine().getStatusCode();
		if (status == HttpStatus.SC_OK) {
			if (httpEntity != null) {
				data = httpEntity.getContent();
			} else {
				throw new HttpRequestException(ENTITY_NULL);
			}
		} else {
			throw new HttpRequestException(ERROR_HTTP + status);
		}
		return data;
	}

	public InputStream post(String url, Map<String, Object> map)
			throws ClientProtocolException, HttpRequestException, IOException {
		return post(url, map, this.timeoutConnection, this.timeoutSocket);
	}

	public InputStream post(String url, Map<String, Object> map,
			int timeoutConnection, int timeoutSocket)
			throws HttpRequestException, ClientProtocolException, IOException {

		InputStream data = null;

		HttpClient httpClient = new DefaultHttpClient(getHttpParams(
				timeoutConnection, timeoutSocket));
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> params = getParams(map);
		httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity entity = response.getEntity();
		if (entity == null) {
			throw new HttpRequestException(
					"HttpRequest.post response is equals null!");
		} else {
			data = entity.getContent();
		}
		return data;
	}

	public JSONObject getJson(String url) throws ClientProtocolException,
			HttpRequestException, IOException, JSONException {
		JSONObject data = null;

		InputStream response = get(url);
		if (response != null) {
			BufferedReader reader;
			reader = new BufferedReader(
					new InputStreamReader(response, "UTF-8"));
			StringBuilder stringBuilder = new StringBuilder();
			String temp;
			while ((temp = reader.readLine()) != null) {
				stringBuilder.append(temp);
			}
			data = new JSONObject(stringBuilder.toString());
		}
		return data;
	}

	public void postXML(String url, String xml,
			HttpRequestListener<String> listener)
			throws ClientProtocolException, HttpRequestException, IOException {
		postXML(url, xml, listener, this.cryptFormat, this.timeoutConnection,
				this.timeoutSocket);
	}

	public void postXML(String url, String xml,
			HttpRequestListener<String> listener, String cryptFormat,
			int timeoutConnection, int timeoutSocket)
			throws HttpRequestException, ClientProtocolException, IOException {

		HttpClient httpClient = new DefaultHttpClient(getHttpParams(
				timeoutConnection, timeoutSocket));
		HttpPost httpPost = new HttpPost(url);
		HttpResponse httpResponse = null;
		HttpEntity httpEntity = null;

		String data = null;

		StringEntity stringEntity = new StringEntity(xml, cryptFormat);
		stringEntity.setContentType(ContentType.XML.toString());

		httpPost.setEntity(stringEntity);

		httpPost.addHeader(HeaderType.ACCEPT.toString(),
				HeaderType.XML.toString());
		httpPost.addHeader(HTTP.CONTENT_TYPE, HeaderType.XML.toString());

		httpResponse = httpClient.execute(httpPost);
		httpEntity = httpResponse.getEntity();
		int status = httpResponse.getStatusLine().getStatusCode();
		if (status == HttpStatus.SC_OK) {
			if (httpEntity != null) {
				data = readString(httpEntity.getContent());
				EntityUtils.toString(httpEntity);
			} else {
				throw new HttpRequestException(ENTITY_NULL);
			}
		} else {

		}
		listener.onResponseHttp(data);
	}

	public void postJson(String url, JSONObject json,
			HttpRequestListener<JSONObject> listener, String cryptFormat,
			int timeoutConnection, int timeoutSocket)
			throws ClientProtocolException, IOException, HttpRequestException,
			JSONException {

		HttpClient httpClient = new DefaultHttpClient(getHttpParams(
				timeoutConnection, timeoutSocket));
		HttpPost httpPost = new HttpPost(url);
		HttpResponse httpResponse = null;
		HttpEntity httpEntity = null;

		JSONObject data = null;
		ByteArrayEntity baEntity = new ByteArrayEntity(json.toString()
				.getBytes("UTF8"));
		baEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				ContentType.JSON.toString()));
		httpPost.setEntity(baEntity);
		httpResponse = httpClient.execute(httpPost);
		httpEntity = httpResponse.getEntity();

		int status = httpResponse.getStatusLine().getStatusCode();
		if (status == HttpStatus.SC_OK) {
			if (httpEntity != null) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(httpEntity.getContent(), "UTF-8"));
				StringBuilder stringBuilder = new StringBuilder();
				String temp;
				while ((temp = reader.readLine()) != null) {
					stringBuilder.append(temp);
				}
				data = new JSONObject(stringBuilder.toString());
				EntityUtils.toString(httpEntity);
			} else {
				throw new HttpRequestException(ENTITY_NULL);
			}
		} else {
			throw new HttpRequestException(ERROR_HTTP + status);
		}
		listener.onResponseHttp(data);
	}

	@Override
	public byte[] downloadImage(String url) throws ClientProtocolException,
			HttpRequestException, IOException {
		InputStream response = get(url);
		byte[] data = null;
		if (response != null) {
			data = readBytes(response);
		}
		return data;
	}

	@Override
	public String downloadFile(String url) throws ClientProtocolException,
			HttpRequestException, IOException {
		InputStream response = get(url);
		String data = null;
		if (response != null) {
			data = readString(response);
		}
		return data;
	}
}
