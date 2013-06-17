package br.com.mirabilis.view.gallery.horizontal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import br.com.mirabilis.R;

/**
 * {@link HorizontalScrollGalleryItem}, objeto que será inserido em um {@link HorizontalScrollGallery}
 * Contém uma referência de instância do tipo T que extends View e que representa o item a ser inserido.
 * @author Rodrigo Simões Rosa
 */
public class HorizontalScrollGalleryItem <T extends View> extends RelativeLayout {

	
	private T view;
	private int layout;
	
	/**
	 * bloco de inicialização.
	 */
	{
		this.layout = R.layout.horizontal_gallery_item;
	}
	
	/**
	 * Construtor que será chamado na inserção através do XML.
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public HorizontalScrollGalleryItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Construtor que será chamado na inserção através do XML.
	 * @param context
	 * @param attrs
	 */
	public HorizontalScrollGalleryItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * Construtor que será chamado na inserção através do XML ou da criação de instância em runtime utilizando a linguagem Java.
	 * @param context
	 */
	public HorizontalScrollGalleryItem(Context context) {
		super(context);
	}
	
	/**
	 * Construtor que receberá a chamada através de criação de instância por meio de runtime.
	 * @param context
	 * @param view
	 */
	public HorizontalScrollGalleryItem(Context context, T view){
		this(context);
		this.view = view;
		
		LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(this.layout, this);
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
