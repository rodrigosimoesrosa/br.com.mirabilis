/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mirabilis.util.callback.exception;
/**
 * Classe de Exception do Callback
 * @author Rodrigo SimÃµes Rosa
 */
public class CallbackException extends Exception {
    
    /**
	 * Serialização.
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Exception do Callback
     */
    public CallbackException() {
        this("CallbackException error");
    }
    
    /**
     * Exception do Callback
     * @param message Mensagem a ser disparada
     */
    public CallbackException(String message){
        super(message);
    }
    
    /**
     * Exception do Callback
     * @param throwable Throwable a ser disparada
     */
    public CallbackException(Throwable throwable){
        super(throwable);
    }
}
