package br.com.mirabilis.task.delegate;

import br.com.mirabilis.task.AsyncTaskBase;
import br.com.mirabilis.task.util.ResponseData;

/**
 * Delegate, listener respons�vel pela execu��o de m�todos que ser�o
 * implementados na cria��o do objeto {@link AsyncTaskBase}
 * 
 * @author Rodrigo Sim�es Rosa
 * @param <T>
 *            Tipo de par�metro que o objeto @link ResponseData do m�todo
 *            {@link #onPosExecute(ResponseData)} ir� receber e que o
 *            {@link #doInBackground(Object...)} ir� retornar.
 */
public interface DelegateListener<T> {
	public void onPosExecute(ResponseData<T> response);
}
