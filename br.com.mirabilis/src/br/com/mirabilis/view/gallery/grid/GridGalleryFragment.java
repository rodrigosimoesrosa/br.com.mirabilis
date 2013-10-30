package br.com.mirabilis.view.gallery.grid;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import br.com.mirabilis.R;

/**
 * Fragment que constrói um grid de acordo com os itens, o número de linhas e
 * colunas parametrizados, manipula a exibição de cada página do grid e
 * possibilita controle dos cliques em cada item da grid
 * 
 * @author Anderson
 */
public class GridGalleryFragment extends Fragment {

	private boolean enableToCreate;
	private int containerWidth;
	private int containerHeight;

	private int numRows;
	private int numColumns;
	private int itemWidth;
	private int itemHeight;

	private GridGalleryPageItemCreator<?> creator;
	private List<?> itens;
	private LinearLayout gridContainer;
	private GridGalleryClickListener<?> listener;
	private List<GridGalleryPage<?>> pages;
	private int currentPage;

	private View next;
	private View previows;
	private boolean blockUserAction;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.grid_gallery, container);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		getView().getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {

						int[] loc = new int[2];

						View v = getView().findViewById(R.id.container);
						v.getLocationOnScreen(loc);

						containerWidth = v.getWidth();
						containerHeight = v.getHeight();

						if (enableToCreate) {

							getView().getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
							create();
						}
					}
				});

		next = getView().findViewById(R.id.next);
		previows = getView().findViewById(R.id.previows);
	}

	/**
	 * Constrói o grid com o modo de construção especificado dos itens.
	 * 
	 * @param type
	 *            {@link Type} tipo de construtor de itens que será usado
	 * @param itens
	 *            informações que serão exibidas
	 * @param listener
	 *            callback para os cliques em cada item do grid
	 * @param rows
	 *            número de linhas do grid
	 * @param columns
	 *            número de colunas do grid
	 */
	public <T> void create(List<T> itens,
			GridGalleryPageItemCreator<T> creator,
			GridGalleryClickListener<T> listener, int rows, int columns) {

		this.itens = itens;
		this.creator = creator;
		this.listener = listener;
		this.numRows = rows;
		this.numColumns = columns;

		enableToCreate = true;
	}

	private void create() {

		gridContainer = (LinearLayout) getView().findViewById(R.id.container);

		calculateItensSize();
		buildPages(itens);
		addPagesToContainer();
		setCurrentPage(currentPage);
		setActions();
	}

	private void calculateItensSize() {

		itemWidth = containerWidth / numColumns;
		itemHeight = containerHeight / numRows;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void buildPages(List<?> itens) {

		pages = new ArrayList<GridGalleryPage<?>>();

		int numPages = getNumPages();
		int itensPerPage = numRows * numColumns;
		int itemIndex = 0;

		for (int pageIndex = 0; pageIndex < numPages; pageIndex++) {

			GridGalleryPage page = new GridGalleryPage(getActivity(), creator,
					itemWidth, itemHeight);
			page.setPage(numRows, numColumns);
			page.setListener(listener);

			for (int i = 0; i < itensPerPage; i++) {

				if (itemIndex >= itens.size() || itens.get(itemIndex) == null)
					break;

				page.addItem(itens.get(itemIndex++));
			}

			pages.add(page);
			page.create();
		}
	}

	private int getNumPages() {

		int itensPerPage = numRows * numColumns;
		int pageRest = itens.size() % itensPerPage > 0 ? 1 : 0;

		return itens.size() / itensPerPage + pageRest;
	}

	private void setCurrentPage(int page) {

		showButtons();

		gridContainer.setVisibility(View.INVISIBLE);

		if (page == pages.size() - 1)
			next.setVisibility(View.INVISIBLE);

		if (page == 0)
			previows.setVisibility(View.INVISIBLE);

		for (int i = 0; i < pages.size(); i++) {

			if (i == page)
				pages.get(i).setVisibility(View.VISIBLE);
			else
				pages.get(i).setVisibility(View.GONE);
		}

		gridContainer.setVisibility(View.VISIBLE);
		currentPage = page;

		blockUserAction = false;
	}

	private void showButtons() {

		next.setVisibility(View.VISIBLE);
		previows.setVisibility(View.VISIBLE);
	}

	@SuppressWarnings("rawtypes")
	private void addPagesToContainer() {

		for (GridGalleryPage page : pages)
			gridContainer.addView(page);
	}

	private void setActions() {

		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!blockUserAction) {

					blockUserAction = true;
					setCurrentPage(++currentPage);
				}
			}
		});
		previows.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!blockUserAction) {

					blockUserAction = true;
					setCurrentPage(--currentPage);
				}
			}
		});
	}

	/**
	 * Habilita ou desabilita a função de clique nos itens do grid e nos
	 * alternadores de página
	 * 
	 * @param enabled
	 */
	@SuppressWarnings("rawtypes")
	public void enableClick(boolean enabled) {

		next.setEnabled(enabled);
		next.setClickable(enabled);

		previows.setEnabled(enabled);
		previows.setClickable(enabled);

		for (GridGalleryPage page : pages) {

			page.enable(enabled);
		}
	}

	/**
	 * Tipos de contrutores de itens do grid
	 */
	public enum Type {
		DEFAULT, DETAILED, CATEGORY;
	}
}
