package br.com.mirabilis.util.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
	
	@SuppressWarnings("resource")
	public static byte[] getBytesFromFile(File file) throws IOException {
	    InputStream is = new FileInputStream(file);

	    long length = file.length();

	    /*if (length > Integer.MAX_VALUE) {}*/

	    byte[] bytes = new byte[(int)length];

	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }

	    if (offset < bytes.length) {
	        throw new IOException("Não foi possivel realizar a leitura da imagem "+file.getName());
	    }

	    is.close();
	    return bytes;
	}
}
