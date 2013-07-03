package br.com.mirabilis.view.utils;

import android.app.Activity;
import android.view.ViewParent;


public class ActivityUtils {
	
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
