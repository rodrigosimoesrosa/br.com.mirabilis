package br.com.mirabilis.util.request.http.jakarta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import br.com.mirabilis.util.request.ResponseData;
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

	public ResponseData<InputStream> get(String url) {
		return get(url, this.timeoutConnection, this.timeoutSocket);
	}

	@SuppressWarnings("finally")
	public ResponseData<InputStream> get(String url, int timeoutConnection,
			int timeoutSocket) {

		HttpClient httpClient = new DefaultHttpClient(getHttpParams(
				timeoutConnection, timeoutSocket));
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = null;
		HttpEntity httpEntity = null;

		String message = null;
		InputStream data = null;
		boolean successfully = false;

		try {
			checkWifi();
			httpResponse = httpClient.execute(httpGet);
			httpEntity = httpResponse.getEntity();

			int status = httpResponse.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				if (httpEntity != null) {
					data = httpEntity.getContent();
					successfully = true;
				} else {
					message = ENTITY_NULL;
				}
			} else {
				message = ERROR_HTTP + status;
			}
		} catch (ClientProtocolException e) {
			message = e.getMessage();
		} catch (IOException e) {
			message = e.getMessage();
		} catch (HttpRequestException e) {
			message = e.getMessage();
		} finally {
			return new ResponseData<InputStream>(successfully, message, data);
		}
	}

	public ResponseData<InputStream> post(String url, Map<String, Object> map) {
		return post(url, map, this.timeoutConnection, this.timeoutSocket);
	}

	@SuppressWarnings("finally")
	public ResponseData<InputStream> post(String url, Map<String, Object> map,
			int timeoutConnection, int timeoutSocket) {

		String message = null;
		InputStream data = null;
		boolean successfully = false;
		try {
			HttpClient httpClient = new DefaultHttpClient(getHttpParams(
					timeoutConnection, timeoutSocket));
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> params = getParams(map);
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				successfully = false;
				message = "HttpRequest.post response is equals null!";
			} else {
				data = entity.getContent();
				successfully = true;
			}
		} catch (UnsupportedEncodingException e) {
			message = e.getMessage();
		} catch (ClientProtocolException e) {
			message = e.getMessage();
		} catch (IOException e) {
			message = e.getMessage();
		} finally {
			return new ResponseData<InputStream>(successfully, message, data);
		}
	}

	public ResponseData<JSONObject> getJson(String url) {
		String message = null;
		boolean successfully = false;
		JSONObject data = null;

		ResponseData<InputStream> response = get(url);
		if (response.isSuccessfully()) {
			BufferedReader reader;
			try {
				reader = new BufferedReader(new InputStreamReader(
						response.getData(), "UTF-8"));
				StringBuilder stringBuilder = new StringBuilder();
				String temp;
				while ((temp = reader.readLine()) != null) {
					stringBuilder.append(temp);
				}
				data = new JSONObject(stringBuilder.toString());
				successfully = true;
			} catch (UnsupportedEncodingException e) {
				message = e.getMessage();
			} catch (IOException e) {
				message = e.getMessage();
			} catch (JSONException e) {
				message = e.getMessage();
			}
		} else {
			message = response.getMessage();
			successfully = false;
		}
		return new ResponseData<JSONObject>(successfully, message, data);
	}

	public void postXML(String url, String xml,
			HttpRequestListener<String> listener) {
		postXML(url, xml, listener, this.cryptFormat, this.timeoutConnection,
				this.timeoutSocket);
	}

	public void postXML(String url, String xml,
			HttpRequestListener<String> listener, String cryptFormat,
			int timeoutConnection, int timeoutSocket) {

		HttpClient httpClient = new DefaultHttpClient(getHttpParams(
				timeoutConnection, timeoutSocket));
		HttpPost httpPost = new HttpPost(url);
		HttpResponse httpResponse = null;
		HttpEntity httpEntity = null;

		boolean successfully = false;
		String data = null;
		String message = null;
		try {
			checkWifi();
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
					message = EntityUtils.toString(httpEntity);
					successfully = true;
				} else {
					message = ENTITY_NULL;
				}
			} else {
				message = ERROR_HTTP + status;
			}
		} catch (ClientProtocolException e) {
			message = e.getMessage();
		} catch (IOException e) {
			message = e.getMessage();
		} catch (HttpRequestException e) {
			message = e.getMessage();
		} finally {
			listener.onResponseData(new ResponseData<String>(successfully,
					message, data));
		}
	}

	public void postJson(String url, JSONObject json,
			HttpRequestListener<JSONObject> listener, String cryptFormat,
			int timeoutConnection, int timeoutSocket) {

		HttpClient httpClient = new DefaultHttpClient(getHttpParams(
				timeoutConnection, timeoutSocket));
		HttpPost httpPost = new HttpPost(url);
		HttpResponse httpResponse = null;
		HttpEntity httpEntity = null;

		boolean successfully = false;
		JSONObject data = null;
		String message = null;
		try {
			checkWifi();
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
							new InputStreamReader(httpEntity.getContent(),
									"UTF-8"));
					StringBuilder stringBuilder = new StringBuilder();
					String temp;
					while ((temp = reader.readLine()) != null) {
						stringBuilder.append(temp);
					}
					data = new JSONObject(stringBuilder.toString());
					message = EntityUtils.toString(httpEntity);
					successfully = true;
				} else {
					message = ENTITY_NULL;
				}
			} else {
				message = ERROR_HTTP + status;
			}
		} catch (ClientProtocolException e) {
			message = e.getMessage();
		} catch (IOException e) {
			message = e.getMessage();
		} catch (HttpRequestException e) {
			message = e.getMessage();
		} catch (JSONException e) {
			message = e.getMessage();
		} finally {
			listener.onResponseData(new ResponseData<JSONObject>(successfully,
					message, data));
		}
	}

	@Override
	public ResponseData<byte[]> downloadImage(String url) {
		ResponseData<InputStream> response = get(url);
		String message = null;
		byte[] data = null;
		boolean successfully = false;
		if (response.isSuccessfully()) {
			try {
				data = readBytes(response.getData());
				successfully = true;
			} catch (IOException e) {
				message = response.getMessage();
			}
		} else {
			message = response.getMessage();
		}
		return new ResponseData<byte[]>(successfully, message, data);
	}

	@Override
	public ResponseData<String> downloadFile(String url) {
		ResponseData<InputStream> response = get(url);
		String message = null;
		String data = null;
		boolean successfully = false;
		if (response.isSuccessfully()) {
			try {
				data = readString(response.getData());
				successfully = true;
			} catch (IOException e) {
				message = response.getMessage();
			}
		} else {
			message = response.getMessage();
		}
		return new ResponseData<String>(successfully, message, data);
	}
}
