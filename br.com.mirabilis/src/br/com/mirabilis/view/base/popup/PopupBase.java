package br.com.mirabilis.view.base.popup;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.Window;
import br.com.mirabilis.R;
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
	
	private boolean blockedTouchEvent;

	/**
	 * Constructor.
	 * @param context 
	 * @param layout
	 * @param listener
	 */
	public PopupBase(Context context, int layout, T listener){
		super(context, R.style.DialogTheme);
		initComponent(context, layout, listener);
		setAttributesForComponent();
	}
	
	/**
	 * Constructor.
	 * @param context
	 * @param layout
	 */
	public PopupBase(Context context, int layout){
		super(context, R.style.DialogTheme);
		initComponent(context, layout);
		setAttributesForComponent();
	}

	private void initComponent(Context context, int layout) {
		initComponent(context, layout, null);
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
	
	/**
	 * Listener dispatcher from touch.
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
	    if (!this.blockedTouchEvent) {
	        return super.dispatchTouchEvent(event);
	    }
	    return this.blockedTouchEvent;
	}
	
	/**
	 * set block in touchEvent
	 * @param value
	 */
	public void setBlockTouchEvent(boolean value){
		this.blockedTouchEvent = value;
	}
	
	/**
	 * return block state from touch
	 * @return
	 */
	public boolean isBlockedTouchEvent() {
		return blockedTouchEvent;
	}
	
	@Override
	public void dismiss() {
		if(listener != null){
			listener.onClose();	
		}
		super.dismiss();
	}
	
	/**
	 * Return listener that set in popup.
	 * @return
	 */
	public T getListener() {
		return listener;
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
