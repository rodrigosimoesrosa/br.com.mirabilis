package br.com.mirabilis.view.gallery.horizontal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import br.com.mirabilis.R;

/**
 * {@link HorizontalScrollGalleryItem}, objeto que ser� inserido em um {@link HorizontalScrollGallery}
 * Cont�m uma refer�ncia de inst�ncia do tipo T que extends View e que representa o item a ser inserido.
 * @author Rodrigo Sim�es Rosa
 */
public class HorizontalScrollGalleryItem <T extends View> extends RelativeLayout {

	
	private T view;
	
	/**
	 * Construtor que ser� chamado na inser��o atrav�s do XML.
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public HorizontalScrollGalleryItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Construtor que ser� chamado na inser��o atrav�s do XML.
	 * @param context
	 * @param attrs
	 */
	public HorizontalScrollGalleryItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * Construtor que ser� chamado na inser��o atrav�s do XML ou da cria��o de inst�ncia em runtime utilizando a linguagem Java.
	 * @param context
	 */
	public HorizontalScrollGalleryItem(Context context) {
		super(context);
	}
	
	/**
	 * Construtor que receber� a chamada atrav�s de cria��o de inst�ncia por meio de runtime.
	 * @param context
	 * @param view
	 */
	public HorizontalScrollGalleryItem(Context context, T view){
		this(context);
		this.view = view;
		
		LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.horizontal_gallery_item, this);
    }
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		this.addView(view);
	}
	
	/**
	 * Retorna a {@link View} utilizada no item.
	 * @return
	 */
	public T getView() {
		return view;
	}
}
