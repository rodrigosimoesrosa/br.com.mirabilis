package br.com.mirabilis.view.base.popup;

import java.lang.reflect.Constructor;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.mirabilis.R;
import br.com.mirabilis.view.base.popup.exception.PopupException;
import br.com.mirabilis.view.base.popup.listener.PopupListener;
import br.com.mirabilis.view.base.popup.util.PopupManager;
import br.com.mirabilis.view.base.popup.util.PopupTransaction;
import br.com.mirabilis.view.utils.ViewUtil;

/**
 * Class abstract {@link PopupBase} that a subtype of {@link Fragment}
 * @author Rodrigo Simões Rosa
 */
public abstract class PopupBase extends Fragment {
	
	protected ViewGroup container;
	protected PopupTransaction transactionStyle;
	protected PopupListener listener;
	protected String content;
	protected String title;
	protected View view;
	protected FragmentActivity activity;
	protected TextView txtTitle;
	protected TextView txtContent;
	protected Button btnOk;
	protected int layout;
		
	/**
	 * Boot block, the layout based pop-up has the structure to implement / use the {@link PopupBase}. Anything other than that, can probably be launched Exception.
	 */
	{
		this.layout = R.layout.popup_base;
	}
	
	/**
	 * Construtor.
	 * @param activity
	 * @param container
	 * @param title
	 * @param content
	 * @param listener
	 * @param layout
	 */
	public PopupBase(FragmentActivity activity, ViewGroup container, String title, String content, PopupListener listener){
		this.activity = activity;
		this.container = container;
		this.title = title;
		this.content = content;
		this.listener = listener;
	}
	
	/**
	 * Set layout to PopupBase
	 * @param layout
	 */
	public void setLayout(int layout){
		this.layout = layout;
	}
	
	/**
	 * Set listener {@link PopupListener} that will be action from popup.
	 * @param listener
	 */
	public void setListener(PopupListener listener) {
		this.listener = listener;
	}

	/**
	 * Inflate itens in {@link PopupBase}
	 * @param attrs
	 * @param savedInstanceState
	 */
	public void onInflate(AttributeSet attrs, Bundle savedInstanceState) {
		super.onInflate(this.activity, attrs, savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		PopupManager.add(this);
		this.view = inflater.inflate(this.layout, container,false);
		setInit();
		setParams();
		setListeners();
		return view;
	}	
	
	/**
	 * Subscribe method onDestroy to remove instance {@link PopupBase} from {@link PopupManager}.
	 */
	@Override
	public void onDestroy() {
		PopupManager.remove(this);
		super.onDestroy();
	}
	
	/**
	 * Set the initialization of components from {@link PopupBase}
	 */
	public void setInit(){
		txtContent = (TextView) view.findViewById(R.id.txtContent);
		txtTitle = (TextView) view.findViewById(R.id.txtTitle);
		btnOk= (Button) view.findViewById(R.id.buttonOk);
		txtTitle.setText(title);
		txtContent.setText(content);
		
		btnOk.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if(listener != null){
					listener.onClose();
				}
				close();
			}
		});
	}
	
	/**
	 * Set params in popup.
	 */
	public abstract void setParams();
	
	/**
	 * Set listeners in popup.
	 */
	public abstract void setListeners();

	/**
	 * Close popup.
	 */
	public void close(){
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		if(this.transactionStyle == null){
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
		}else{
			transaction.setTransitionStyle(this.transactionStyle.getTransictionClose());
		}
		
		transaction.remove(this);
		transaction.commit();
		
		if(this.container != null){
			((ViewGroup)this.container.getParent()).removeView(this.container);
			this.container = null;
		}
		System.gc();
	}
	
	/**
	 * Remove popup.
	 * @param popup
	 * @return boolean
	 */
	public static <T extends PopupBase> boolean removePopup(T popup){
		return PopupManager.remove(popup);
	}
	
	/**
	 * Return a list active popup.
	 * @return
	 */
	public static List<PopupBase> getPopups() {
		return PopupManager.getPopups();
	}
	
	/**
	 * Set transaction motion style from popup.
	 * @param transactionSytle
	 */
	private void setTransactionStyle(PopupTransaction transactionSytle) {
		this.transactionStyle = transactionSytle;
	}
	
	/**
	 * Return title from popup.
	 * @return
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Method that will be create and show, new instance of {@link PopupBase} after all @param className be subtype of {@link PopupBase}
	 * @param className
	 * @param container
	 * @param activity
	 * @param title
	 * @param content
	 * @param listener
	 * @return
	 * @throws PopupException
	 */
	public static <S extends PopupBase, T extends PopupListener> S show(Class<? extends PopupBase> className, ViewGroup container, FragmentActivity activity, String title,String content, T listener) throws PopupException{
		return show(className, container, activity,0, title,content, listener, null);
	}
	

	/**
	 * Method that will be create and show, new instance of {@link PopupBase} after all @param className be subtype of {@link PopupBase} 
	 * @param className
	 * @param activity
	 * @param title
	 * @param content
	 * @param listener
	 * @return
	 * @throws PopupException
	 */
	public static <S extends PopupBase, T extends PopupListener> S show(Class<? extends PopupBase> className, FragmentActivity activity, String title,String content, T listener) throws PopupException{
		return show(className, activity,0, title,content, listener, null);
	}
	
	/**
	 * Method that will be create and show, new instance of {@link PopupBase} after all @param className be subtype of {@link PopupBase}
	 * @param className
	 * @param activity
	 * @param layout
	 * @param title
	 * @param content
	 * @param listener
	 * @return
	 * @throws PopupException
	 */
	public static <S extends PopupBase, T extends PopupListener> S show(Class<? extends PopupBase> className,FragmentActivity activity, int layout, String title,String content, T listener) throws PopupException{
		return show(className, activity,layout, title,content, listener, null);
	}
	
	/**
	 * Method that will be create and show, new instance of {@link PopupBase} after all @param className be subtype of {@link PopupBase}
	 * @param className
	 * @param activity
	 * @param layout
	 * @param title
	 * @param content
	 * @param listener
	 * @param transactionSytle
	 * @return
	 * @throws PopupException
	 */
	public static <S extends PopupBase, T extends PopupListener> S show(Class<? extends PopupBase> className, FragmentActivity activity,int layout, String title,String content, T listener,PopupTransaction transactionSytle) throws PopupException{
		LinearLayout container = PopupContainer.getPopupContainer(activity);
		activity.setContentView(container);
		return show(className, container, activity, layout, title,content, listener, transactionSytle);
	}
	
	/**
	 * Method that will be create and show, new instance of {@link PopupBase} after all @param className be subtype of {@link PopupBase}
	 * @param className
	 * @param container
	 * @param activity
	 * @param layout
	 * @param title
	 * @param content
	 * @param listener
	 * @param transactionSytle
	 * @return
	 * @throws PopupException
	 */
	@SuppressWarnings("unchecked")
	public static  <S extends PopupBase, T extends PopupListener> S show(Class<? extends PopupBase> className, ViewGroup container, FragmentActivity activity,int layout, String title,String content, T listener, PopupTransaction transactionSytle) throws PopupException{
		FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
		PopupBase popup = null;
		Class<? extends PopupBase> instance = null;
		Constructor<? extends PopupBase> constructor = null;
		
		try {
			instance = (Class<? extends PopupBase>) Class.forName(className.getName());
			constructor = instance.getConstructor(FragmentActivity.class,ViewGroup.class, String.class,String.class,PopupListener.class);
			popup = (PopupBase) constructor.newInstance(activity,container,title,content,listener);
			
			if(layout != 0){
				popup.setLayout(layout);
			}
			
		} catch (Exception e){
			e.printStackTrace();
			throw new PopupException("Error during creation of PopupBase :" + e.getMessage());
		}
		
		if(transactionSytle == null){
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);	
		}else{
			transaction.setTransitionStyle(transactionSytle.getTransictionOpen());
			popup.setTransactionStyle(transactionSytle);
		}
		transaction.add(container.getId(),popup);
		transaction.commit();
		
		return (S) popup;
	}
	
	/**
	 * Class that will be create container from add {@link PopupBase}
	 * @author Rodrigo Simões Rosa.
	 */
	private static class PopupContainer {
		
		private static LinearLayout getPopupContainer(Activity activity){
			LinearLayout linear = new LinearLayout(activity);
			linear.setId(ViewUtil.generateViewId());
			linear.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			linear.setGravity(Gravity.CENTER);
			linear.setBackgroundColor(activity.getResources().getColor(R.color.black_transparent));
			return linear;
		}
	}
}

