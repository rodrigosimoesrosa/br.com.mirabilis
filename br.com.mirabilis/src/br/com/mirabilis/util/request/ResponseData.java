package br.com.mirabilis.util.request;

import java.io.Serializable;

/**
 * Objeto de resposta gen�rico para AsyncTask e subtipos.
 * @author Rodrigo, Anderson
 *
 * @param <T>
 */
public class ResponseData<T> implements Serializable {

	private T data;
	private String message;
	private boolean successfully;
	private Exception exception;
	
	/**
	 * Serializa��o
	 */
	private static final long serialVersionUID = -1396907768012910102L;
	
	public ResponseData(boolean successfully, String message, T data) {
		this.successfully = successfully;
		this.message = message;
		this.data = data;
	}
	
	public void setException(Exception exception) {
		this.exception = exception;
	}
	
	public Exception getException() {
		return exception;
	}
	
	public boolean isSuccessfully() {
		return successfully;
	}
	
	public String getMessage() {
		return message;
	}
	
	public T getData() {
		return data;
	}
}
