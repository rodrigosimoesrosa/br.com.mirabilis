package br.com.mirabilis.view.gallery.grid;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

/**
 * Define um gerador de views para uma {@link GridGalleryPage}
 */
public abstract class GridGalleryPageItemCreator<T> {

	protected LayoutParams itemParams;
	protected boolean enableClick = true;
	protected LayoutInflater inflater;

	public void setInflater(LayoutInflater inflater) {

		this.inflater = inflater;
	}

	public void setLayoutParams(LayoutParams layoutParams) {

		itemParams = layoutParams;
	}

	/**
	 * @param item
	 * @param listener
	 * @return View a ser inflada
	 */
	public abstract View getView(final T object, final GridGalleryClickListener<T> listener);

	public void enableClick(boolean enable) {

		enableClick = enable;
	}
}
