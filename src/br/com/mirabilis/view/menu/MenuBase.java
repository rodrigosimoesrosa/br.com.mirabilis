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
 * Menu Customizado.
 * @author Rodrigo Simões Rosa.
 *
 */
public class MenuBase extends RelativeLayout {

	private LinearLayout content;
	private List<MenuButton> list;
	private int layout;
	
	/**
	 * Bloco de inicialização.
	 */
	{
		this.layout = R.layout.menu_base;
	}
	
	/**
	 * Construtor.
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MenuBase(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 * Construtor.
	 * @param context
	 * @param attrs
	 */
	public MenuBase(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * Construtor.
	 * @param context
	 */
	public MenuBase(Context context) {
		super(context);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layout, this);
        this.initComponents();
    }
	
	/**
	 * Inicializa componentes.
	 */
	private void initComponents() {
		this.content = (LinearLayout) this.findViewById(R.id.menuContent);
	}
	
	/**
	 * Adiciona botão no menu.
	 * @param btn
	 */
	public void addMenuButton(MenuButton btn){
		this.list = this.list != null? this.list : new ArrayList<MenuButton>();
		this.list.add(btn);
		this.generate();
	}
	
	/**
	 * Adiociona lista de botões.
	 * @param list
	 */
	public void addList(List<MenuButton> list){
		this.list = list;
		this.generate();
	}
	
	/**
	 * Limpa a o menu.
	 */
	private void clear(){
		this.content.removeAllViews();
	}
	
	/**
	 * Gera novos botões de acordo com a lista.
	 */
	private void generate(){
		this.clear();
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		params.weight = 1;
		
		for(final MenuButton btn : this.list){
			this.content.addView(btn);
			btn.setLayoutParams(params);
		}
	}
}
