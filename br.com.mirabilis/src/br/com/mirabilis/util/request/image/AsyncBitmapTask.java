package br.com.mirabilis.util.request.image;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import br.com.mirabilis.util.image.BufferImage;
import br.com.mirabilis.util.request.AsyncTaskBase;
import br.com.mirabilis.util.request.ResponseData;
import br.com.mirabilis.util.request.http.HttpManager;
import br.com.mirabilis.util.request.listener.DelegateListener;

/**
 * AsyncBitmapTask, tarefa responsável pelo carregamento de uma imagem, utilizando a url como referência e o objeto Delegate.
 * @author Rodrigo Simões Rosa.
 */
public class AsyncBitmapTask extends AsyncTaskBase<Void, Void, Bitmap> {
	
	private String url;
	private Context context;
	private Bitmap bmp;
	private boolean useBuffer;
	
	/**
	 * Construtor.
	 * @param context Necessário para realizar a busca de imagem.
	 * @param url da imagem que será realizada o download.
	 * @param delegate objeto que tratará a resposta do download.
	 */
	public AsyncBitmapTask(Context context, String url, DelegateListener<Bitmap> delegate){
		this(context, url, delegate, true);
	}
	 
	/**
	 * Construtor.
	 * @param context Necessário para realizar a busca de imagem.
	 * @param url da imagem que será realizada o download.
	 * @param delegate objeto que tratará a resposta do download.
	 * @param useBuffer booleano que determinará se aquela imagem será armazenada no buffer.
	 */
	public AsyncBitmapTask(Context context, String url, DelegateListener<Bitmap> delegate, boolean useBuffer) {
		super(delegate);
		this.url = url;
		this.context = context;
		this.useBuffer = useBuffer;
	}

	@Override
	protected ResponseData<Bitmap> doInBackground(Void... params) {
		
		ResponseData<InputStream> response = null;
		HttpManager request = null;
		
		if(useBuffer){
			bmp = BufferImage.getBitmap(url);
			if(bmp == null){
				request = new HttpManager(context);
				response = request.get(url);
				if(response.isSuccessfully()){
					bmp = BitmapFactory.decodeStream(response.getData());
					BufferImage.addBitmap(url, bmp);
					return new ResponseData<Bitmap>(true, response.getMessage(), bmp);
				}else{
					return new ResponseData<Bitmap>(false, response.getMessage(), null);
				}
			}else{
				return new ResponseData<Bitmap>(true, null, bmp);
			}
		}else{
			request = new HttpManager(context);
			response = request.get(url);
			if(response.isSuccessfully()){
				bmp = BitmapFactory.decodeStream(response.getData());
				return new ResponseData<Bitmap>(true, response.getMessage(), bmp);
			}else{
				return new ResponseData<Bitmap>(false, response.getMessage(), null);
			}
		}
	}
}
