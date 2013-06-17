package br.com.mirabilis.system.wifi.util;

import java.util.List;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.util.Log;
import br.com.mirabilis.system.wifi.Wifi;
import br.com.mirabilis.system.wifi.crypt.WifiCrypt;

/**
 * Classe de scaneamento de redes wifi.
 * @author Rodrigo Simões Rosa
 *
 */
public class WifiItem {

	public ScanResult scan;
	public List<WifiCrypt> wifiCrypts;
	private WifiConfiguration wfg;
	private boolean connected;
	
	/**
	 * Construtor
	 * @param scan objeto rede scaneada.
	 * @param crypt criptografia utilizada pela rede.
	 */
	public WifiItem(ScanResult scan, List<WifiCrypt> crypts) {
		this.wifiCrypts = crypts;
		this.scan = scan;
		this.wfg = new WifiConfiguration();
		this.wfg.SSID = "\"".concat(scan.SSID).concat("\"");
		this.wfg.priority = Wifi.getPriority();
		this.wfg.hiddenSSID = true;
		this.wfg.wepTxKeyIndex = 0;
	}
	
	/**
	 * Retorna o tipo ativo de configuração de wifi.
	 * @return
	 */
	public WifiCrypt getType() {
		if(this.wifiCrypts.indexOf(WifiCrypt.WEP) != -1){
			return WifiCrypt.WEP;
		}else if(this.wifiCrypts.size() == 1 && this.wifiCrypts.indexOf(WifiCrypt.WPS) != -1){
			return WifiCrypt.WPS;
		}else {	
			return WifiCrypt.WPA_PSK;
		}
	}
	
	/**
	 * Retorna uma string contendo todas as criptografias.
	 * @return
	 */
	public String getCrypt(){
		String c = "";
		for(WifiCrypt w : wifiCrypts){
			c += " ".concat(w.toString());
		}
		return c;
	}
	
	/**
	 * Gera as informações necessárias.
	 */
	private void generate(){
		generate(null);
	}
	
	/**
	 * Gera as informações necessárias.
	 * @param password
	 */
	private void generate(String password){
		Log.v("GENERATE",this.getType().toString());
		switch (this.getType()) {
			case WPS:
				wfg.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
				wfg.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
				wfg.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
				wfg.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
				wfg.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
				wfg.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
				wfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
				wfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
				wfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
				wfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
				
				wfg.allowedAuthAlgorithms.clear();
				break;
			
			case WEP:
				wfg.hiddenSSID = true;
		        wfg.status = WifiConfiguration.Status.DISABLED;  
		    	wfg.priority = 40;
				wfg.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		    	wfg.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		    	wfg.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		    	wfg.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
		    	wfg.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
		    	wfg.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		    	wfg.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		    	wfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		    	wfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
				if (isHexPassword(password)){
					Log.v(WifiItem.class.getName(),"senha hexa : "+ password);
					wfg.wepKeys[0] = password;
				}else {
					String pass = "\"" + password + "\"";
					Log.v(WifiItem.class.getName(),"senha :" + pass);
					wfg.wepKeys[0] = pass;
				}
				wfg.wepTxKeyIndex = 0;
				break;
				
			case WPA_PSK:
				wfg.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
				wfg.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
				wfg.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
				wfg.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
				wfg.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
				wfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
				wfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
				wfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
				wfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
				wfg.preSharedKey = "\"".concat(password).concat("\"");
				break;
				
			default:
				break;
		}
	}
	
	/**
	 * Retorna verdadeiro caso a senha seja hexa.
	 * @param p
	 * @return
	 */
	private boolean isHexPassword(String password){
		if (password == null) {
            return false;
        }
     
        int len = password.length();
        if (len != 10 && len != 26 && len != 58) {
            return false;
        }
     
        for (int i = 0; i < len; ++i) {
            char c = password.charAt(i);
            if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
                continue;
            }
            return false;
        }
        return true;
	}
	
	/**
	 * Retorna a configuração de wifi gerada.
	 * @param password
	 * @return
	 */
	public WifiConfiguration getWifiConfiguration(String password) {
		this.generate(password);
		return this.wfg;
	}
	
	/**
	 * Retorna a configuração de wifi gerada.
	 * @return
	 */
	public WifiConfiguration getWifiConfiguration(){
		this.generate();
		return this.wfg;
	}
	
	public void setConnected(boolean value){
		this.connected = value;
	}
	
	public boolean getConnected(){
		return this.connected;
	}
	
}
