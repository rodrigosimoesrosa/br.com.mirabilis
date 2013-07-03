package br.com.mirabilis.view.gallery.grid;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Responsável por construir cada página com itens do grid
 */
class GridGalleryPage<T> extends LinearLayout {

	private GridGalleryPageItemCreator<T> pageItemCreator;

	private int numRows;
	private int numColumns;

	private List<View> itens;

	private GridGalleryClickListener<T> listener;

	public GridGalleryPage(Context context,
			GridGalleryPageItemCreator<T> creator, int itemWidth, int itemHeight) {

		super(context);
		setParams();
		itens = new ArrayList<View>();
		this.pageItemCreator = creator;
		pageItemCreator.setInflater(LayoutInflater.from(context));
		pageItemCreator.setLayoutParams(GridGalleryUtils.getLayoutParams(
				itemWidth, itemHeight));
	}

	private void setParams() {

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		super.setLayoutParams(params);
		super.setOrientation(VERTICAL);
	}

	public void setPage(int rows, int columns) {

		numRows = rows;
		numColumns = columns;
	}

	public void addItem(T item) {

		itens.add(pageItemCreator.getView(item, listener));
	}

	public void create() {

		int viewPos = 0;

		for (int row = 0; row < numRows; row++) {

			LinearLayout container = getLayoutContainer();

			for (int column = 0; column < numColumns; column++) {

				if (viewPos >= itens.size() || itens.get(viewPos) == null)
					break;

				container.addView(itens.get(viewPos++));
			}

			super.addView(container);
		}
	}

	private LinearLayout getLayoutContainer() {

		LinearLayout container = new LinearLayout(getContext());

		container.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		return container;
	}

	public void setListener(GridGalleryClickListener<T> listener) {

		this.listener = listener;
	}

	public void enable(boolean enabled) {

		pageItemCreator.enableClick(enabled);
	}
}
