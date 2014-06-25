package br.com.mirabilis.task.util;

import java.io.Serializable;

/**
 * Response object containing generic exceptions.
 * @author Rodrigo, Anderson
 *
 * @param <T>
 */
public final class ResponseData<T> implements Serializable {

	private T data;
	private boolean successfully;
	private Exception exception;
	private String message;
	
	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = -1396907768012910102L;
	
	/**
	 * Constructor.
	 * @param successfully
	 * @param exception
	 * @param data
	 */
	public ResponseData(boolean successfully, T data) {
		this(successfully, null, data);
	}
	
	/**
	 * Constructor.
	 * @param successfully
	 * @param message
	 * @param data
	 */
	public ResponseData(boolean successfully, Exception exception, T data) {
		this.successfully = successfully;
		this.data = data;
		this.exception = exception;
	}
	
	/**
	 * Set message.
	 * @param message
	 */
	public void setMessage(String message){
		this.message = message;
	}
	
	/**
	 * Return message.
	 * @return
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Return exception generic.
	 * @return
	 */
	public Exception getException() {
		return exception;
	}
	
	/**
	 * Return boolean is successfully.
	 * @return
	 * @throws Exception
	 */
	public boolean isSuccessfully() throws Exception {
		if(exception != null){
			throw exception;
		}
		return successfully;
	}
	
	/**
	 * Return data T.
	 * @return
	 */
	public T getData() {
		return data;
	}
}
