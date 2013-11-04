package br.com.mirabilis.view.menu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import br.com.mirabilis.R;
import br.com.mirabilis.view.menu.util.MenuButton;

/**
 * Customized Menu
 * 
 * @author Rodrigo Simões Rosa.
 * 
 */
public class MenuBase extends RelativeLayout {

	private LinearLayout content;
	private List<MenuButton> list;
	private int layout;

	/**
	 * Boot block
	 */
	{
		this.layout = R.layout.menu_base;
	}

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MenuBase(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param attrs
	 */
	public MenuBase(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Constructor.
	 * 
	 * @param context
	 */
	public MenuBase(Context context) {
		super(context);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater inflater = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(layout, this);
		this.initComponents();
	}

	/**
	 * Init components.
	 */
	private void initComponents() {
		this.content = (LinearLayout) this.findViewById(R.id.menuContent);
	}

	/**
	 * Add menu button.
	 * 
	 * @param btn
	 */
	public void addMenuButton(MenuButton btn) {
		this.list = this.list != null ? this.list : new ArrayList<MenuButton>();
		this.list.add(btn);
		this.generate();
	}

	/**
	 * Add list of menu button's.
	 * 
	 * @param list
	 */
	public void addList(List<MenuButton> list) {
		this.list = list;
		this.generate();
	}

	/**
	 * Clear menu.
	 */
	private void clear() {
		this.content.removeAllViews();
	}

	/**
	 * Generate menu.
	 */
	private void generate() {
		this.clear();

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		params.weight = 1;

		for (final MenuButton btn : this.list) {
			this.content.addView(btn);
			btn.setLayoutParams(params);
		}
	}
}
