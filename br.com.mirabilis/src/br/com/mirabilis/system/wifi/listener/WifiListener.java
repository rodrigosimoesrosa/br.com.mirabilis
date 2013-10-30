package br.com.mirabilis.system.wifi.listener;

import java.util.List;

import br.com.mirabilis.system.wifi.util.WifiState;

import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;

/**
 * Interface do wifi que representa a escuta de eventos de conexão wifi.
 * @author Rodrigo Simões Rosa
 *
 */
public interface WifiListener {
	
	void onNewNetworkFound(boolean found);
	void onScanResultsAvailableAction(List<ScanResult> list);
	void onWifiNetwork(List<WifiConfiguration> list);
	void onNetworkStateChangeAction(WifiInfo networkInfo);
	void onSupplicantStateChangedAction(boolean value);
	void onExtraNetworkInfo();
	void onWifiStateChangedAction(boolean value);
	void onNetworkInfo(NetworkInfo netInfo, String bssid);
	void onHandleSupplicantStateChanged(WifiState state);
}
