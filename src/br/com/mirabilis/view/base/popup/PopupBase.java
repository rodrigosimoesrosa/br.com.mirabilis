package br.com.mirabilis.view.base.popup;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import br.com.mirabilis.view.base.popup.listener.PopupListener;

/**
 * Class {@link PopupBase} that objective show custom popup.
 * @author Rodrigo Simões Rosa.
 * @param <T extends {@link PopupListener}> 
 */
public abstract class PopupBase<T extends PopupListener> extends Dialog {
	
	protected T listener;
	protected int layout;
	protected Context context;

	/**
	 * Constructor.
	 * @param context 
	 * @param layout
	 * @param listener
	 */
	public PopupBase(Context context, int layout, T listener){
		super(context);
		initComponent(context, layout, listener);
		setAttributesForComponent();
	}

	/**
	 * Constructor.
	 * @param context
	 * @param layout
	 * @param theme
	 * @param listener
	 */
	public PopupBase(Context context, int layout, int theme, T listener) {
		super(context, theme);
		initComponent(context, layout, listener);
		setAttributesForComponent();
	}
	
	/**
	 * Init value for component.
	 * @param context
	 * @param layout
	 * @param listener
	 */
	private void initComponent(Context context, int layout, T listener){
		this.context = context;
		this.layout = layout;
		this.listener = listener;
	}
	
	/**
	 * Set attributes to init and construct component.
	 */
	private void setAttributesForComponent() {
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(this.layout);
		super.setCancelable(false);
		setParams();
		setListeners();
	}
	
	@Override
	public void dismiss() {
		listener.onClose();
		super.dismiss();
	}
	
	/**
	 * Initialize information of view's inside layout informated {@link PopupBase#layout}
	 */
	protected abstract void setParams();
	
	/**
	 * Initialize listener method's. 
	 */
	protected abstract void setListeners();
}
