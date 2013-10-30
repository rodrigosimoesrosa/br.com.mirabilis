package br.com.mirabilis.util.request;

import android.os.AsyncTask;
import br.com.mirabilis.util.request.listener.DelegateListener;

/**
 * AysncTaskBase, tem como objetivo ser uma classe base das async criadas nas @link Activity e seus subtipos.
 * @author Rodrigo Simões Rosa.
 * @param <T> Tipo de parâmetro que o {@link #doInBackground(Object...)} irá receber.
 * @param <V> Tipo de parâmetro que o objeto @link ResponseData do método {@link #onPosExecute(ResponseData)} irá receber e que o {@link #doInBackground(Object...)} irá retornar.
 * @param <U> Tipo de parâmetro que o {@link #onProgressUpdate(Object...)} irá receber.
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
