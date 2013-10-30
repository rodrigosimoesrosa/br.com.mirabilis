package br.com.mirabilis.util.reflection.exception;

import java.io.Serializable;

public class ReflectionException extends Exception implements Serializable{
	
	/**
	* Serialização.
	*/
	private static final long serialVersionUID = -6293263676311788206L;

	/**
	* Exception do ReflectionException
	*/
	public ReflectionException() {
		this("CallbackException error");
	}
	    
	 /**
	 * Exception do Reflection
	 * @param message Mensagem a ser disparada
	 */
	 public ReflectionException(String message){
		 super(message);
	 }
	 
	 /**
	 * Exception do Reflection
	 * @param throwable Throwable a ser disparada
	 */
	 public ReflectionException(Throwable throwable){
	    super(throwable);
	 }
}
