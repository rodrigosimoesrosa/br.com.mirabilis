package br.com.mirabilis.system.wifi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import br.com.mirabilis.system.wifi.Wifi;
import br.com.mirabilis.system.wifi.util.WifiState;

/**
 * Classe de broadcast.
 * @author Rodrigo Simões Rosa
 *
 */
public class WifiReceiver extends BroadcastReceiver{

	private Wifi wifi;
	
	/**
	 * Construtor.
	 * @param wifi
	 */
	public WifiReceiver(Wifi wifi) {
		Log.v("WifiReceiver","Created");
		this.wifi = wifi;
	}
	
	public WifiReceiver(Wifi wifi, String[] actions){
		
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
			this.wifi.onScanResultsAvailableAction();
		}else if(intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			this.wifi.onWifiStateChangedAction(wifiManager.isWifiEnabled());
		}else if(intent.getAction().equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)){
			this.wifi.onSupplicantStateChangedAction();
		}else if(intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)){
			WifiState state = new WifiState((SupplicantState) intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE),
		             intent.hasExtra(WifiManager.EXTRA_SUPPLICANT_ERROR),
		             intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 0));
			
			if(state.getState().equals(SupplicantState.COMPLETED)){
				WifiInfo info = wifi.recoveryWifiInfo();
				this.wifi.addConnected(info.getSSID());
			}
			if(this.wifi.getEnabled()){
				this.wifi.handleSupplicantStateChanged(state);
			}
		}else if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
			NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			String bssid = intent.getStringExtra(WifiManager.EXTRA_BSSID);
			this.wifi.onNetworkInfo(netInfo,bssid);
			this.wifi.onNetworkStateChangeAction();
		}else if(intent.getAction().equals(WifiManager.EXTRA_NETWORK_INFO)){
			this.wifi.onExtraNetworkInfo();
		}else if(intent.getAction().equals(WifiManager.EXTRA_WIFI_STATE)){
			
		}else if(intent.getAction().equals(WifiManager.EXTRA_SUPPLICANT_ERROR)){
			
		}
	}
}
