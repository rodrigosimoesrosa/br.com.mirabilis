package br.com.mirabilis.util.request;

import android.os.AsyncTask;
import br.com.mirabilis.util.request.listener.DelegateListener;

/**
 * AysncTaskBase, tem como objetivo ser uma classe base das async criadas nas @link Activity e seus subtipos.
 * @author Rodrigo Sim�es Rosa.
 * @param <T> Tipo de par�metro que o {@link #doInBackground(Object...)} ir� receber.
 * @param <V> Tipo de par�metro que o objeto @link ResponseData do m�todo {@link #onPosExecute(ResponseData)} ir� receber e que o {@link #doInBackground(Object...)} ir� retornar.
 * @param <U> Tipo de par�metro que o {@link #onProgressUpdate(Object...)} ir� receber.
 */
public abstract class AsyncTaskBase<T,U,V> extends AsyncTask<T,U,ResponseData<V>>{

	private DelegateListener<V> delegate;
	
	public AsyncTaskBase(DelegateListener<V> delegate) {
		this.delegate = delegate;
	}
	
	@Override
	protected void onPostExecute(ResponseData<V> response) {
		if(delegate != null){
			this.delegate.onPosExecute(response);
		}
	}
}
