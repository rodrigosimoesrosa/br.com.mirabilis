package br.com.mirabilis.view.menu.util;

import android.content.Context;
import android.widget.ImageButton;

/**
 * Classe dos bot�es a serem adicionados.
 * @author Rodrigo Sim�es Rosa.
 *
 */
public class MenuButton extends ImageButton {
	
	/**
	 * Construtor 
	 * @param context 
	 * @param OnClickListener listener que ser� chamado no click.
	 * @param image id do imagem do bot�o.
	 * @param style id do estilo do bot�o.
	 */
	public MenuButton(Context context, OnClickListener listener, int image, int style) {
		super(context);
		this.setBackgroundResource(style);
		this.setImageResource(image);
		this.setOnClickListener(listener);
	}
}