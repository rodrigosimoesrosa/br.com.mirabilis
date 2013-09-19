package br.com.mirabilis.util.request.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import br.com.mirabilis.system.wifi.Wifi;
import br.com.mirabilis.util.request.ResponseData;
import br.com.mirabilis.util.request.http.exception.HttpManagerException;
import br.com.mirabilis.util.request.http.listener.HttpListener;

/**
 * Classe responsável pela realização de requisições http.
 * @author Rodrigo Simões Rosa.
 */
public class HttpManager {
	
	private Context context;
	private String cryptFormat;
	private int timeoutConnection;
	private int timeoutSocket;
	
	private static final String ERROR_HTTP = "Error http : ";
	private static final String ENTITY_NULL = "Objeto entity nulo";
	
	public HttpManager(Context context) {
		this.context = context;
	}
	
	/**
	 * Bloco de inicialização.
	 */
	{
		cryptFormat = HTTP.UTF_8;
		timeoutConnection = 0;
		timeoutSocket = 0;
	}
	
	/**
	 * Método responsável pela chamada get.
	 * @param url
	 * @param listener
	 */
	public ResponseData<InputStream> get(String url){
		return get(url, this.timeoutConnection, this.timeoutSocket);
	}
	
	public ResponseData<JSONObject> getJson(String url){
		String message = null;
		boolean successfully = false;
		JSONObject data = null;
		
		ResponseData<InputStream> response = get(url);
		if(response.isSuccessfully()){
			BufferedReader reader;
			try {
				reader = new BufferedReader(new InputStreamReader(response.getData(), "UTF-8"));
				StringBuilder stringBuilder = new StringBuilder();
				String temp;
				while ((temp = reader.readLine()) != null){
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
		}else{
			message = response.getMessage();
			successfully = false;
		}
		
		return new ResponseData<JSONObject>(successfully, message, data);
	}
	
	/**
	 * Método responsável pela chamada get.
	 * @param url
	 * @param listener
	 * @param timeoutConnection Timeout que será executado até a conexão estabelecer.
	 * @param timeoutSocket Timeout que será o tempo de espera que o client irá aguardar.
	 */
	@SuppressWarnings("finally")
	public ResponseData<InputStream> get(String url, int timeoutConnection, int timeoutSocket){
		
		HttpClient httpClient = new DefaultHttpClient(getHttpParams(timeoutConnection, timeoutSocket));
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
			if(status == HttpStatus.SC_OK){
				if(httpEntity != null){
					data = httpEntity.getContent();
					successfully = true;
				}else{
					message = ENTITY_NULL;
				}
			}else{
				message = ERROR_HTTP + status;
			}
		} catch (ClientProtocolException e) {
			message = e.getMessage();
		} catch (IOException e) {
			message = e.getMessage();
		} catch (HttpManagerException e) {
			message = e.getMessage();
		} finally {
			return new ResponseData<InputStream>(successfully, message, data);
		}
	}
	
	/**
	 * Método responsável pela chamada post enviando um XML.
	 * @param url
	 * @param xml
	 */
	public void postXML(String url, String xml, HttpListener<InputStream> listener){
		postXML(url, xml, listener, this.cryptFormat, this.timeoutConnection, this.timeoutSocket);
	}
	
	/**
	 * Método responsável pela chamada post enviando um XML.
	 * @param url
	 * @param listener
	 */
	public void postXML(String url, String xml, HttpListener<InputStream> listener, String cryptFormat, int timeoutConnection, int timeoutSocket){
		
		HttpClient httpClient = new DefaultHttpClient(getHttpParams(timeoutConnection,timeoutSocket));
		HttpPost httpPost = new HttpPost(url);
		HttpResponse httpResponse = null;
		HttpEntity httpEntity = null;
		
		boolean successfully = false;
		InputStream data = null;
		String message = null;
		try {
			checkWifi();
			StringEntity stringEntity = new StringEntity(xml, cryptFormat);
			stringEntity.setContentType(ContentType.XML.toString());
			
	        httpPost.setEntity(stringEntity);
	        
	        httpPost.addHeader(HeaderType.ACCEPT.toString(), HeaderType.XML.toString());
			httpPost.addHeader(HTTP.CONTENT_TYPE, HeaderType.XML.toString());
			
			httpResponse = httpClient.execute(httpPost);
			httpEntity = httpResponse.getEntity();
			int status = httpResponse.getStatusLine().getStatusCode();
			if(status == HttpStatus.SC_OK){
				if(httpEntity != null){
					data = httpResponse.getEntity().getContent();
					message = EntityUtils.toString(httpEntity);
					successfully = true;
				}else{
					message = ENTITY_NULL;
				}
			}else{
				message = ERROR_HTTP + status;
			}
		} catch (ClientProtocolException e) {
			message = e.getMessage();
		} catch (IOException e) {
			message = e.getMessage();
		} catch (HttpManagerException e) {
			message = e.getMessage();
		} finally {
			listener.onResponseData(new ResponseData<InputStream>(successfully, message, data));
		}
	}
	
	
	/**
	 * Método responsável pela chamada post enviando um Json.
	 * @param url
	 * @param listener
	 */
	public void postJson(String url, JSONObject json,HttpListener<InputStream> listener, String cryptFormat, int timeoutConnection, int timeoutSocket){
		
		HttpClient httpClient = new DefaultHttpClient(getHttpParams(timeoutConnection,timeoutSocket));
		HttpPost httpPost = new HttpPost(url);
		HttpResponse httpResponse = null;
		HttpEntity httpEntity = null;
		
		boolean successfully = false;
		InputStream data = null;
		String message = null;
		try {
			checkWifi();
			ByteArrayEntity baEntity = new ByteArrayEntity(json.toString().getBytes("UTF8"));
			baEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,ContentType.JSON.toString()));
			httpPost.setEntity(baEntity);
	        httpResponse = httpClient.execute(httpPost);
			httpEntity = httpResponse.getEntity();

			int status = httpResponse.getStatusLine().getStatusCode();
			if(status == HttpStatus.SC_OK){
				if(httpEntity != null){
					data = httpResponse.getEntity().getContent();
					message = EntityUtils.toString(httpEntity);
					successfully = true;
				}else{
					message = ENTITY_NULL;
				}
			}else{
				message = ERROR_HTTP + status;
			}
		} catch (ClientProtocolException e) {
			message = e.getMessage();
		} catch (IOException e) {
			message = e.getMessage();
		} catch (HttpManagerException e) {
			message = e.getMessage();
		} finally {
			listener.onResponseData(new ResponseData<InputStream>(successfully, message, data));
		}
	}

	/**
	 * Retorna os parâmetros necessários para realizar a configuração.
	 * @return
	 */
	private HttpParams getHttpParams(int connection, int socket) {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, connection);
		HttpConnectionParams.setSoTimeout(params, socket);
		return params;
	}

	/**
	 * Verifica se existe conexão com wifi e se existe uma rede conectada.
	 * @throws HttpManagerException
	 */
	private void checkWifi() throws HttpManagerException{
		if(!Wifi.isWifiEnabled(this.context) || !Wifi.isWifiConnected(context)){
			throw new HttpManagerException("O Wifi está desabilitado ou não existe nenhuma antena conectada.");
		}
	}
	
	/**
	 * Enumeração responsável por armazenar os tipos de dados a serem enviados via POST.
	 * @author Rodrigo Simões Rosa
	 */
	public enum ContentType{
		XML("text/xml"), 
		JSON("application/json");
		
		private String value;
		private ContentType(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return this.value;
		}
	}
	
	/**
	 * Enumeração responsável por armazer os tipos de header e suas propriedades.
	 * @author Rodrigo
	 *
	 */
	public enum HeaderType {
		XML("application/xml"),
		ACCEPT("Accept");
		
		private String value;
		private HeaderType(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return this.value;
		}
	}
}
