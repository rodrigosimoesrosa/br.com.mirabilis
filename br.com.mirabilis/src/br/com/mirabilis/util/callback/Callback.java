package br.com.mirabilis.util.callback;

import java.lang.reflect.Method;
import java.util.ArrayList;

import br.com.mirabilis.util.callback.exception.CallbackException;

/**
 * Classe callback.
 * @author Rodrigo Sim�es Rosa
 */
public class Callback {
	
	private Object classReflection;
	private String callback;
	
	/**
    * Criar inst�ncia do Callback aonde ser� chamado o m�todo call no fim do processo.
    * @param classReflection inst�ncia da classe aonde ser� chamado o m�todo.
    * @param callback Nome do m�todo que ser� chamado.
    */
	public Callback(Object classReflection, String callback) {
		this.classReflection = classReflection;
        this.callback = callback;
		new CallbackData(this,callback);
	}
	
	/**
    * Tem como objetivo chamar o m�todo passado como refer�ncia.
    * @param objReturn Objeto de retorno para o m�todo.
    * @throws CallbackException Dispara exceptions referente a chamada do m�todo referenciado.
    */
    @SuppressWarnings("rawtypes")
	public void call(Object objReturn , Object typeReturn) throws CallbackException{
		Class<?> c = null;
		try{
		    c = Class.forName(classReflection.getClass().getName());
			Method method = null;
			Class[] argTypes = new Class[] { (Class) typeReturn };
			method = c.getDeclaredMethod(callback,argTypes);
			Object temp = classReflection;
			method.invoke(temp,new Object[]{objReturn});
		}catch(Throwable e){
			e.printStackTrace();
			throw new CallbackException("CallbackException erro na chamada do m�todo");
		}finally{
			CallbackData.destroyCallback(callback);
		}
	}
    
    /**
     * Tem como objetivo chamar o m�todo passado como refer�ncia.
     * @param typeReturn Tipo de retorno.
     * @param objReturn Objeto de retorno para o m�todo.
     * @throws CallbackException Dispara exceptions referente a chamada do m�todo referenciado.
     */
    @SuppressWarnings("rawtypes")
	public void call(Object [] objReturn , Object typeReturn) throws CallbackException{
 		Class<?> c = null;
 		try{
 		    c = Class.forName(classReflection.getClass().getName());
 			Method method = null;
 			Class[] argTypes = new Class[] { (Class) typeReturn };
 			method = c.getDeclaredMethod(callback,argTypes);
 			Object temp = classReflection;
 			method.invoke(temp, new Object[]{objReturn});
 		}catch(Throwable e){
 			throw new CallbackException("CallbackException erro na chamada do m�todo :".concat(e.getMessage()));
 		}finally{
 			CallbackData.destroyCallback(callback);
 		}
 	}
    
    /**
     * Tem como objetivo chamar o m�todo passado como refer�ncia.
     * @throws CallbackException Dispara exceptions referente a chamada do m�todo referenciado.
     */
     public void call() throws CallbackException{
 		Class<?> c = null;
 		try{
 		    c = Class.forName(classReflection.getClass().getName());
 			Method method = null;
 			method = c.getDeclaredMethod(callback);
 			Object temp = classReflection;
 			method.invoke(temp);
 		}catch(Throwable e){
 			throw new CallbackException("CallbackException erro na chamada do m�todo");
 		}finally{
 			CallbackData.destroyCallback(callback);
 		}
 	}
        
	/**
	* ArrayList dos callbacks em mem�ria e seus respectivos m�todos de referencia.
    */
	private static ArrayList<CallbackData> callbacksData = new ArrayList<CallbackData>();
	
	/**
    * Classe de controle dos callbacks
    */
	public static class CallbackData {
		
		@SuppressWarnings("unused")
		private Callback callback;
		private String call;
		
		/**
        * Construtor da classe de controle dos callbacks.
        * @param callback inst�ncia do callback.
        * @param call refer�ncia do m�todo do callback.
        */
		public CallbackData(Callback callback,String call){
			this.callback = callback;
			this.call = call;
			callbacksData.add(this);
		}
                
		/**
        * Retorna o callbackData por meio de uma string que � refer�ncia do m�todo.
        * @param call refer�ncia do m�todo.
        * @return Retorna o CallbackData encontrado ou null caso n�o encontre.
        */
		public static CallbackData getCallbackByCall(String call){
			for(CallbackData callbackData : callbacksData){
				if(callbackData.call.equals(call)){
					return callbackData;
				}
			}
			return null;
		}
		
		/**
        * Destr�i a instancia do callback da mem�ria.
        * @param callback .
        */
        public static void destroyCallback(String callback){
	        callbacksData.remove(getCallbackByCall(callback));
	        System.gc();
        }
	}
}