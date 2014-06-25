package br.com.mirabilis.util.request.image;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import br.com.mirabilis.http.HttpBuilder;
import br.com.mirabilis.http.HttpRequest;
import br.com.mirabilis.http.exception.HttpRequestException;
import br.com.mirabilis.http.info.HttpType;
import br.com.mirabilis.util.image.BufferImage;
import br.com.mirabilis.util.request.AsyncTaskBase;
import br.com.mirabilis.util.request.ResponseData;
import br.com.mirabilis.util.request.listener.DelegateListener;

/**
 * AsyncBitmapTask, tarefa responsável pelo carregamento de uma imagem,
 * utilizando a url como referência e o objeto Delegate.
 * 
 * @author Rodrigo Simões Rosa.
 */
public class AsyncBitmapTask extends AsyncTaskBase<Void, Void, Bitmap> {

	private String url;
	private Context context;
	private Bitmap bmp;
	private boolean useBuffer;

	/**
	 * Construtor.
	 * 
	 * @param context
	 *            Necessário para realizar a busca de imagem.
	 * @param url
	 *            da imagem que será realizada o download.
	 * @param delegate
	 *            objeto que tratará a resposta do download.
	 */
	public AsyncBitmapTask(Context context, String url,
			DelegateListener<Bitmap> delegate) {
		this(context, url, delegate, true);
	}

	/**
	 * Construtor.
	 * 
	 * @param context
	 *            Necessário para realizar a busca de imagem.
	 * @param url
	 *            da imagem que será realizada o download.
	 * @param delegate
	 *            objeto que tratará a resposta do download.
	 * @param useBuffer
	 *            booleano que determinará se aquela imagem será armazenada no
	 *            buffer.
	 */
	public AsyncBitmapTask(Context context, String url,
			DelegateListener<Bitmap> delegate, boolean useBuffer) {
		super(delegate);
		this.url = url;
		this.context = context;
		this.useBuffer = useBuffer;
	}

	@Override
	protected ResponseData<Bitmap> doInBackground(Void... params) {

		ResponseData<Bitmap> data = null;
		HttpRequest request = null;
		InputStream inputStream = null;
		if (useBuffer) {
			bmp = BufferImage.getBitmap(url);
			if (bmp == null) {
				try {
					request = HttpBuilder.build(HttpType.JAKARTA);
					inputStream = request.get(url);
					if (inputStream != null) {
						bmp = BitmapFactory.decodeStream(inputStream);
						BufferImage.addBitmap(url, bmp);
						data = new ResponseData<Bitmap>(true, bmp);
					}
				} catch (HttpRequestException e) {
					data = new ResponseData<Bitmap>(false, e, null);
				} catch (Exception e) {
					data = new ResponseData<Bitmap>(false, e, null);
				}
			} else {
				data = new ResponseData<Bitmap>(true, bmp);
			}
		} else {
			try {
				request = HttpBuilder.build(HttpType.JAKARTA);
				inputStream = request.get(url);
				if (inputStream != null) {
					bmp = BitmapFactory.decodeStream(inputStream);
					data = new ResponseData<Bitmap>(true, bmp);
				}
			} catch (HttpRequestException e) {
				data = new ResponseData<Bitmap>(false, e, null);
			} catch (Exception e) {
				data = new ResponseData<Bitmap>(false, e, null);
			}
		}
		return data;
	}
}
