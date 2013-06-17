package br.com.mirabilis.view.base.popup.util;
/**
 * PopupTransaction, set id styles for make transaction popup open/close.
 * @author Rodrigo Simões Rosa
 *
 */
public class PopupTransaction {
	
	private int transictionOpen;
	private int transictionClose;
	
	public PopupTransaction(int transictionOpen,int transictionClose) {
		this.transictionOpen = transictionOpen;
		this.transictionClose = transictionClose;
	}
	
	public int getTransictionClose() {
		return transictionClose;
	}
	
	public int getTransictionOpen() {
		return transictionOpen;
	}
}