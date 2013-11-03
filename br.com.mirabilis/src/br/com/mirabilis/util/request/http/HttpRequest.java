package br.com.mirabilis.util.request.http;

import org.apache.http.HttpException;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import br.com.mirabilis.system.wifi.Wifi;
import br.com.mirabilis.util.request.http.exception.HttpRequestException;
import br.com.mirabilis.util.request.http.jakarta.HttpJakarta;

/**
 * Class abstract that make request's using http protocol.
 * 
 * @author Rodrigo Simões Rosa.
 */
public abstract class HttpRequest implements HttpMethod {

	protected Context context;
	protected String cryptFormat;
	protected int timeoutConnection;
	protected int timeoutSocket;

	/**
	 * Boot block.
	 */
	{
		cryptFormat = HTTP.UTF_8;
		timeoutConnection = 0;
		timeoutSocket = 0;
	}

	/**
	 * Type of lib.
	 * 
	 * @author Rodrigo Simões Rosa.
	 * 
	 */
	public enum HttpType {
		NORMAL, JAKARTA;
	}

	/**
	 * Return request type.
	 * 
	 * @param context
	 * @param type
	 * @return
	 * @throws HttpException
	 */
	public static HttpRequest create(Context context, HttpType type)
			throws HttpRequestException {

		if (type == null) {
			return new HttpJakarta(context);
		}

		switch (type) {
		case NORMAL:
			return null;

		case JAKARTA:
			return new HttpJakarta(context);

		default:
			throw new HttpRequestException("This type of request not exist");
		}
	}

	/**
	 * Return request type.
	 * 
	 * @param context
	 * @return
	 * @throws HttpRequestException
	 */
	public static HttpRequest create(Context context)
			throws HttpRequestException {
		return create(context, null);
	}

	/**
	 * Returns the parameters needed to perform the configuration.
	 * 
	 * @return
	 */
	protected HttpParams getHttpParams(int connection, int socket) {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, connection);
		HttpConnectionParams.setSoTimeout(params, socket);
		return params;
	}

	/**
	 * Checks if there is wifi connection and if there is a connected network.
	 * 
	 * @throws HttpRequestException
	 */
	protected void checkWifi() throws HttpRequestException {
		if (!Wifi.isWifiEnabled(this.context) || !Wifi.isWifiConnected(context)) {
			throw new HttpRequestException(
					"The Wifi is disabled or there is no antenna connected.");
		}
	}

	/**
	 * Enumeration responsible for storing the types of data to be sent via
	 * POST.
	 * 
	 * @author Rodrigo Simões Rosa.
	 */
	public enum ContentType {
		XML("text/xml"), JSON("application/json");

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
	 * Enumeration armazer responsible for header types and their properties.
	 * 
	 * @author Rodrigo Simões Rosa.
	 * 
	 */
	public enum HeaderType {
		XML("application/xml"), ACCEPT("Accept");

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
