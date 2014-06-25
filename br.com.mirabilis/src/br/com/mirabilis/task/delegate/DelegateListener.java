package br.com.mirabilis.task.delegate;

import br.com.mirabilis.task.AsyncTaskBase;
import br.com.mirabilis.task.util.ResponseData;

/**
 * Delegate, listener responsável pela execução de métodos que serão
 * implementados na criação do objeto {@link AsyncTaskBase}
 * 
 * @author Rodrigo Simões Rosa
 * @param <T>
 *            Tipo de parâmetro que o objeto @link ResponseData do método
 *            {@link #onPosExecute(ResponseData)} irá receber e que o
 *            {@link #doInBackground(Object...)} irá retornar.
 */
public interface DelegateListener<T> {
	public void onPosExecute(ResponseData<T> response);
}
