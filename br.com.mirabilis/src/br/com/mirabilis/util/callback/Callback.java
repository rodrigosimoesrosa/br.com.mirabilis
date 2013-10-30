package br.com.mirabilis.util.callback;

import java.lang.reflect.Method;
import java.util.ArrayList;

import br.com.mirabilis.util.callback.exception.CallbackException;

/**
 * Classe callback.
 * @author Rodrigo Simões Rosa
 */
public class Callback {
	
	private Object classReflection;
	private String callback;
	
	/**
    * Criar instância do Callback aonde será chamado o método call no fim do processo.
    * @param classReflection instância da classe aonde será chamado o método.
    * @param callback Nome do método que será chamado.
    */
	public Callback(Object classReflection, String callback) {
		this.classReflection = classReflection;
        this.callback = callback;
		new CallbackData(this,callback);
	}
	
	/**
    * Tem como objetivo chamar o método passado como referência.
    * @param objReturn Objeto de retorno para o método.
    * @throws CallbackException Dispara exceptions referente a chamada do método referenciado.
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
			throw new CallbackException("CallbackException erro na chamada do método");
		}finally{
			CallbackData.destroyCallback(callback);
		}
	}
    
    /**
     * Tem como objetivo chamar o método passado como referência.
     * @param typeReturn Tipo de retorno.
     * @param objReturn Objeto de retorno para o método.
     * @throws CallbackException Dispara exceptions referente a chamada do método referenciado.
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
 			throw new CallbackException("CallbackException erro na chamada do método :".concat(e.getMessage()));
 		}finally{
 			CallbackData.destroyCallback(callback);
 		}
 	}
    
    /**
     * Tem como objetivo chamar o método passado como referência.
     * @throws CallbackException Dispara exceptions referente a chamada do método referenciado.
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
 			throw new CallbackException("CallbackException erro na chamada do método");
 		}finally{
 			CallbackData.destroyCallback(callback);
 		}
 	}
        
	/**
	* ArrayList dos callbacks em memória e seus respectivos métodos de referencia.
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
        * @param callback instância do callback.
        * @param call referência do método do callback.
        */
		public CallbackData(Callback callback,String call){
			this.callback = callback;
			this.call = call;
			callbacksData.add(this);
		}
                
		/**
        * Retorna o callbackData por meio de uma string que é referência do método.
        * @param call referência do método.
        * @return Retorna o CallbackData encontrado ou null caso não encontre.
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
        * Destrói a instancia do callback da memória.
        * @param callback .
        */
        public static void destroyCallback(String callback){
	        callbacksData.remove(getCallbackByCall(callback));
	        System.gc();
        }
	}
}