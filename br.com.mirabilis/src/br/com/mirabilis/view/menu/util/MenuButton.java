package br.com.mirabilis.view.menu.util;

import android.content.Context;
import android.widget.ImageButton;

/**
 * Classe dos botões a serem adicionados.
 * @author Rodrigo Simões Rosa.
 *
 */
public class MenuButton extends ImageButton {
	
	/**
	 * Construtor 
	 * @param context 
	 * @param OnClickListener listener que será chamado no click.
	 * @param image id do imagem do botão.
	 * @param style id do estilo do botão.
	 */
	public MenuButton(Context context, OnClickListener listener, int image, int style) {
		super(context);
		this.setBackgroundResource(style);
		this.setImageResource(image);
		this.setOnClickListener(listener);
	}
}