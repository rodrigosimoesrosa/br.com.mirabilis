package br.com.mirabilis.view.utils;

import android.app.Activity;
import android.view.ViewParent;

/**
 * Class of control methods utils for activity's.
 * @author Rodrigo Simões Rosa.
 */
public class ActivityUtils {
	
	/**
	 * Return first Activity of View.
	 * @param parent
	 * @return
	 */
	public static Activity getActivity(ViewParent parent){
		if(parent instanceof Activity){
			return (Activity) parent;
		}
		if(parent.getParent() != null){
			return getActivity(parent.getParent());
		}
		return null;
	}
}
