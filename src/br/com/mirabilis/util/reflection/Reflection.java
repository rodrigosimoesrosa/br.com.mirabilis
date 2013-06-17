package br.com.mirabilis.util.reflection;

import java.lang.reflect.Method;

import br.com.mirabilis.util.reflection.exception.ReflectionException;

/**
 * Classe que tem como objetivo utilizar do recurso da reflexão para acessar métodos e outras propriedade de um objeto.
 * @author Rodrigo Simões Rosa.
 */
public class Reflection {

	/**
	 * Método que tem como objetivo acessar um método especifico do objeto @param classReflection, utilizando o parametro @param methodReflection.
	 * @param classReflection
	 * @param methodReflection
	 * @param objReturn
	 * @param typeReturn
	 * @throws ReflectionException 
	 */
	@SuppressWarnings("rawtypes")
	public static void methodWithOneReturn(Object classReflection, String methodReflection, Object objReturn, Object typeReturn) throws ReflectionException{
		Class<?> c = null;
		try{
		    c = Class.forName(classReflection.getClass().getName());
			Method method = null;
			if(objReturn == null){
				method = c.getDeclaredMethod(methodReflection);
	 			Object temp = classReflection;
	 			method.invoke(temp);
			}else{
				Class[] argTypes = new Class[] { (Class) typeReturn };
				method = c.getDeclaredMethod(methodReflection,argTypes);
				Object temp = classReflection;
				method.invoke(temp,new Object[]{objReturn});
			}
		}catch(Throwable e){
			e.printStackTrace();
			throw new ReflectionException(e.getMessage());
		}finally{
			System.gc();
		}
	}
	
	/**
	 * @see Reflection#method(Object, String, Object, Object)
	 * @param classReflection
	 * @param methodReflection
	 */
	public static void method(Object classReflection, String methodReflection) throws ReflectionException{
		methodWithOneReturn(classReflection, methodReflection, null, null);
	}

	/**
	 * @see Reflection#method(Object, String, Object, Object)
	 * @param classReflection
	 * @param methodReflection
	 * @param objReturn
	 * @param typeReturn
	 */
	public static void methodWithManyReturns(Object classReflection, String methodReflection, Object [] objReturn, Object typeReturn) throws ReflectionException{
		methodWithOneReturn(classReflection, methodReflection, objReturn, typeReturn);
	}
}
