package br.com.mirabilis.view.utils;

import android.app.Activity;
import android.view.ViewParent;

/**
 * Class utils for {@link Activity}.
 * 
 * @author Rodrigo Simões Rosa.
 */
public final class ActivityUtils {

	/**
	 * Return the {@link Activity} of {@link ViewParent}.
	 * 
	 * @param parent
	 * @return
	 */
	public final static Activity getActivity(ViewParent parent) {
		if (parent instanceof Activity) {
			return (Activity) parent;
		}
		if (parent.getParent() != null) {
			return getActivity(parent.getParent());
		}
		return null;
	}
}
