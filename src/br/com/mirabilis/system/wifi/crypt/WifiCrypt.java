package br.com.mirabilis.system.wifi.crypt;

import br.com.mirabilis.system.wifi.exception.WifiException;

/**
	 * Criptográfia do wifi para acessar a conta. 
	 * @author Rodrigo Simões Rosa
	 *
	 */
	public enum WifiCrypt {

		WPA_PSK("WPA-PSK"),
		WPA2_PSK("WPA2-PSK"),
		WPA_EAP("WPA-EAP"),
		WPA2_EAP("WPA2_EAP"),
		
		CCMP("CCMP"),
		TKIP("TKIP"),
		preauth("preauth"),
		WPS("WPS"),
		WEP("WEP"), 
		
		/**
		 * Caso a rede não seja catálogada nesta enumeração.
		 */
		UNKNOWN("unknown");

		private String crypt;
		
		/**
		 * Construtor
		 * @param crypt
		 */
		private WifiCrypt(String crypt) {
			this.crypt = crypt;
		}
		
		@Override
		public String toString() {
			return this.crypt;
		}
		
		/**
		 * Retorna o cryptWifi de acordo com a string
		 * @param s
		 * @return
		 * @throws WifiException  
		 */
		public static WifiCrypt getCryptWifiByName(String s) {
			for(WifiCrypt c : values()){
				if(s.indexOf(c.toString()) != -1){
					return c;
				}
			}
			return WifiCrypt.UNKNOWN;
		}
	}