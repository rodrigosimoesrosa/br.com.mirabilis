package br.com.mirabilis.system.wifi.util;

import android.net.wifi.SupplicantState;

public class WifiState {
	
	private SupplicantState state;
	private boolean hasError;
	private int error;
	
	public WifiState(SupplicantState state, boolean hasError, int error) {
		this.state = state;
		this.hasError = hasError;
		this.error = error;
	}
	
	public boolean isHasError() {
		return hasError;
	}
	
	public SupplicantState getState() {
		return state;
	}
	
	public int getError() {
		return error;
	}
}
