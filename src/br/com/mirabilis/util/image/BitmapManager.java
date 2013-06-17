package br.com.mirabilis.util.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Classe para o tratamento de bitmap's do projeto.
 * @author Rodrigo Simões Rosa
 *
 */
public class BitmapManager {
	
	/**
	 * Retorna a imagem para ser inserida na atividade.
	 * @param image
	 * @return
	 */
	public static Bitmap getBitmap(byte[] image){
		if(image == null)
			return null;
		
		return BitmapFactory.decodeByteArray(image, 0, image.length);
	}
}
