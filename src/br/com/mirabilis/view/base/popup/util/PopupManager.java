package br.com.mirabilis.view.base.popup.util;

import java.util.ArrayList;
import java.util.List;

import br.com.mirabilis.view.base.popup.PopupBase;


/**
 * Class that will be manager PopupBase's on Project.
 * @author Rodrigo Simões Rosa.
 */
public class PopupManager {
	
	private static List<PopupBase> popups;
	
	/**
	 * Create list of PopupBase's.
	 */
	public static void create(){
		popups = new ArrayList<PopupBase>();
	}
	
	/**
	 * Return list of PopupBase's.
	 * @return
	 */
	public static List<PopupBase> getPopups() {
		return popups;
	}

	/**
	 * Add popup in list of PopupBase's.
	 * @param popupBase
	 */
	public static <T extends PopupBase> void add(T popupBase) {
		if(popups == null){
			PopupManager.create();
		}
		popups.add(popupBase);
	}

	/**
	 * Remove popup from list of PopupBase's and return boolean value from result.
	 * @param popup
	 * @return
	 */
	public static <T extends PopupBase> boolean remove(T popup) {
		return popups.remove(popup);
	}
}
