package br.com.mirabilis.util.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import br.com.mirabilis.system.info.SystemInfo;

/**
 * Classe de controle de buffer de imagem.
 * @author Rodrigo Simões Rosa
 *
 */
public final class BufferImage {

	private LruCache<String, Bitmap> cache; 
	private static BufferImage singleton;
	
	/**
	 * Construtor privado.
	 * @param percent "1f == 100%",".2f == 20%", ".5f == 50%" e assim por diante. 
	 */
	private BufferImage(Context context, float percent){
		singleton = this; 
		cache = new LruCache<String,Bitmap>(SystemInfo.getMBMemoryAvaliableByPercent(context, percent));	
	}
	
	/**
	 * Inicialização do buffer. Sendo obrigatória sua inicialização.
	 * @param percent
	 */
	public static void init(Context context,float percent){
		if(singleton == null){
			new BufferImage(context, percent);	
		}
	}
	
	/**
	 * Retorna um bitmap salvo no cache. Caso não exista retorna nulo.
	 * @param url
	 * @return Bitmap.
	 */
	public static Bitmap getBitmap(String url) {
		checkBuffer();
		return singleton.cache.get(url);
	}
	
	private static void checkBuffer(){
		if(singleton == null){
			throw new NullPointerException("O buffer deve ser inicializado");
		}
	}
	
	/**
	 * Adiciona bitmap no cache.
	 * @param url
	 * @param bmp
	 * @return Bitmap adicionado
	 */
	public static Bitmap addBitmap(String url, Bitmap bmp){
		checkBuffer();
		return singleton.cache.put(url, bmp);
	}
	
}
