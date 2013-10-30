package br.com.mirabilis.system.wifi;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import br.com.mirabilis.system.wifi.crypt.WifiCrypt;
import br.com.mirabilis.system.wifi.exception.WifiException;
import br.com.mirabilis.system.wifi.listener.WifiListener;
import br.com.mirabilis.system.wifi.receiver.WifiReceiver;
import br.com.mirabilis.system.wifi.util.WifiState;

/**
 * Classe de controle do wifi do tablet.
 * @author Rodrigo Simões Rosa
 *
 */
public class Wifi {

	private WifiManager manager;
	private WifiListener listener;
	private boolean disableOthersNetwork;
	private static int priority;
	private static int updateTime;
	private Context context;
	
	private WifiReceiver receiver;
	private boolean receiverRegistered;
	private boolean scanning;
	private List<WifiConfiguration> connecteds;
	
	
	/**
	 * Bloco de inicialização 
	 */
	{
		/**
		 * Valor inicial da propriedade.
		 */
		disableOthersNetwork = false;
		priority = 40;
	}
	
	/**
	 * Construtor
	 * @param context
	 */
	public Wifi(Context context) {
		this(context,null);
	}
	
	/**
	 * Construtor.
	 * @param context
	 * @param actions
	 */
	public Wifi(Context context, String [] actions){
		this.manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		this.context = context;
		this.startReceiver(actions);
	}
	
	private void startReceiver(String [] actions){
		this.receiver = new WifiReceiver(this);
		this.receiverRegistered = true;
		IntentFilter filter = new IntentFilter();
		if(actions == null){
			filter.addAction(WifiManager.EXTRA_WIFI_STATE);
			filter.addAction(WifiManager.EXTRA_SUPPLICANT_ERROR);
			filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
			filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
			filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
			filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
			filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
			filter.addAction(WifiManager.EXTRA_NETWORK_INFO);
		}else{
			for(String action : actions){
				filter.addAction(action);
			}
		}
		this.context.registerReceiver(this.receiver,filter);
	}
	
	/**
	 * Objeto runnable que executa o update no wifi.
	 */
	public Runnable updateRunnable = new Runnable() {
		
		public void run() {
			updateWifi();
		}
	};
	
	
	/**
	 * Retorna as redes wifi disponiveis.
	 * @return
	 * @throws WifiException 
	 */
	public List<WifiConfiguration> getWifiConfigurations() throws WifiException {
		return this.manager.getConfiguredNetworks();
	}

	/**
	 * Seta o status do wifi.
	 * @param value
	 * @throws WifiException 
	 */
	public void setEnabled(boolean value) throws WifiException{
		if(!value){
			this.manager.saveConfiguration();
		}
		this.manager.setWifiEnabled(value);
		updateWifi();
	}
	
	public void handleSupplicantStateChanged(WifiState state) {
		this.listener.onHandleSupplicantStateChanged(state);
	}
	
	/**
	 * Retorna o estado do wifi
	 * @return
	 */
	public boolean getEnabled() {
		return this.manager.isWifiEnabled();
	}
	
	/**
	 * Inicializa o scaneamento.
	 * @return
	 */
	public boolean startScan() {
		if(!scanning){
			this.scanning = true;
			return this.manager.startScan();	
		}
		return false;
	}
	
	/**
	 * Ouvinte de todas as mudanças ocorridas na rede wifi.
	 * @param message
	 */
	public void onWifiNetworkChanged(WifiListener listener) {
		this.listener = listener;
	}

	/**
	 * Adiciona rede wifi para conexão.
	 * @param wc
	 * @throws WifiException 
	 */
	public void addNetwork(WifiConfiguration wc) throws WifiException {
		this.enableNetwork(this.manager.addNetwork(wc));
	}
	
	/**
	 * Habilita rede através do id.
	 * @param id
	 * @throws WifiException
	 */
	public void enableNetwork(int id) throws WifiException {
		this.disableAllNetworks();
		this.getNetworkById(id).status = WifiConfiguration.Status.ENABLED;
		this.manager.enableNetwork(id, disableOthersNetwork);
		this.manager.getConfiguredNetworks();
		this.manager.saveConfiguration();
		//this.onScanResultsAvailableAction();
	}
	
	/**
	 * Remove uma especifica rede wifi configurada.
	 * @param wc
	 * @throws WifiException 
	 */
	public void removeNetwork(WifiConfiguration wc) throws WifiException{
		wc.status = WifiConfiguration.Status.DISABLED;
		this.manager.removeNetwork(wc.networkId);
		this.manager.getConfiguredNetworks();
		this.manager.saveConfiguration();
		this.onScanResultsAvailableAction();
		//this.onNetworkStateChangeAction();
	}
	
	/**
	 * Desabilita todas as redes wifi's conectadas.
	 * @throws WifiException
	 */
	private void disableAllNetworks() throws WifiException{
		List<WifiConfiguration> list = this.getWifiConfigurations();
		for(WifiConfiguration w : list){
			w.status = WifiConfiguration.Status.DISABLED;
			this.manager.disableNetwork(w.networkId);
		}
		
	}
	
	/**
	 * Verifica a existência de uma rede com aquele especifico nome.
	 * @param ssid Nome da rede
	 * @return
	 * @throws WifiException
	 */
	public WifiConfiguration getIfExistNetwork(String ssid) {
		return getIfExistNetwork(this.manager, ssid);
	}
	
	public static WifiConfiguration getIfExistNetwork(WifiManager manager, String ssid){
		List<WifiConfiguration> list = manager.getConfiguredNetworks();
		for(WifiConfiguration w : list){
			if(w.SSID.equals("\"".concat(ssid).concat("\""))){
				return w;
			}
		}
		return null;
	}
	
	/**
	 * Retorna a rede ativa no momento.
	 * @return
	 */
	public WifiConfiguration getCurrentNetwork(){
		List<WifiConfiguration> list = this.manager.getConfiguredNetworks();
		for(WifiConfiguration w : list){
			if(w.status == WifiConfiguration.Status.CURRENT){
				return w;
			}
		}
		return null;
	}
	
	/**
	 * Retorna a rede de acordo com o id.
	 * @param networkId
	 * @return
	 * @throws WifiException
	 */
	public WifiConfiguration getNetworkById(int networkId) throws WifiException {
		List<WifiConfiguration> list = this.manager.getConfiguredNetworks();
		for(WifiConfiguration w : list){
			if(w.networkId == networkId){
				return w;
			}
		}
		return null;
	}
	
	/**
	 * Retorna uma lista das criptografias disponíveis para wifi. 
	 * @param capabilities
	 * @return
	 * @throws WifiException 
	 */
	public static List<WifiCrypt> getWifiCrypts(String capabilities) {
		
		List<WifiCrypt> list = new ArrayList<WifiCrypt>();
		
		capabilities = capabilities.replace("][",",");
		capabilities = capabilities.replace("[", "");
		capabilities = capabilities.replace("]", "");
		//capabilities = capabilities.replace("-", ",");
		capabilities = capabilities.replace("+", ",");
		String [] temp = capabilities.split(",");
		for(String s : temp){
			if(list.indexOf(WifiCrypt.getCryptWifiByName(s)) == -1){
				list.add(WifiCrypt.getCryptWifiByName(s));	
			}
		}
		return list;
	}
	
	/**
	 * Seta a prioridade do para abertura de novas conexões de rede. 
	 * @param v
	 */
	public static void setPriority(int v){
		priority = v;
	}
	
	/**
	 * Seta o tempo de atualização.
	 * @param v
	 */
	public static void setUpdateTime(int v){
		updateTime = v;
	}
	
	/**
	 * Retorna a prioridade informada.
	 */
	public static int getPriority(){
		return priority;
	}
	
	/**
	 * Retorna o tempo de atualização.
	 * @return
	 */
	public static int getUpdateTime(){
		return updateTime;
	}
	
	/**
	 * Desabilita outras redes wifi uma vez que das redes seja habilitada;
	 */
	public void setDisableOtherNetwork(boolean disable){
		this.disableOthersNetwork = disable;
	}

	/**
	 * Atualiza os wifiConfiguration, que são as redes cadastradas e configuradas.
	 */
	public void updateWifiConfiguration() {
		List<WifiConfiguration> list = this.manager.getConfiguredNetworks();
        if(list != null && list.size() == 0){
        	this.listener.onWifiNetwork(list);
        }
	}

	/**
	 * Realiza a atualização das informações da rede ativa.
	 */
	public void onNetworkStateChangeAction() {
		WifiInfo info = this.manager.getConnectionInfo();
		this.listener.onNetworkStateChangeAction(info);
	}
	
	public WifiConfiguration getWifiConfigurationByBSSID(String bssid){
		if(bssid == null){
			return null;
		}
		List<WifiConfiguration> list = manager.getConfiguredNetworks();
		for(WifiConfiguration wc : list){
			if(wc.BSSID.equalsIgnoreCase(bssid)){
				return wc;
			}
		}
		return null;
	}
	
	public WifiConfiguration getWifiConfigurationBySSID(String ssid){
		if(ssid == null){
			return null;
		}
		List<WifiConfiguration> list = manager.getConfiguredNetworks();
		for(WifiConfiguration wc : list){
			if(wc.SSID.equalsIgnoreCase(ssid)){
				return wc;
			}
		}
		return null;
	}
	
	/**
	 * Retorna as informações da ultima rede conectada.
	 * @return
	 */
	public WifiInfo recoveryWifiInfo(){
		return this.manager.getConnectionInfo();
	}
	

	/**
	 * Retorna o resultado da busca por redes e a qualidade do sinal.
	 * @return
	 * @throws WifiException 
	 */
	public void onScanResultsAvailableAction() {
		scanning = false;
		listener.onScanResultsAvailableAction(this.manager.getScanResults());
	}
	
	/**
	 * Atualiza todos os serviços wifi
	 */
	public void updateWifi(){
		//onNetworkStateChangeAction();
		onScanResultsAvailableAction();
		updateWifiConfiguration();
	}
	
	/**
	 * Retorna o status.
	 * @param wc
	 * @return
	 */
	public String getStatusByWifiConfiguration(WifiConfiguration wc){
		return WifiConfiguration.Status.strings[wc.status].equals(WifiConfiguration.Status.DISABLED) ? "Desativada" : "Ativa";
	}
	
	/*public class WifiHandler extends Handler {
	    
		private List<br.com.phoenix.util.callback.Callback> list;
		private br.com.phoenix.util.callback.Callback callback;
		
		public WifiHandler(List<br.com.phoenix.util.callback.Callback> list) {
			this.list = list;
		}
		
		public WifiHandler(br.com.phoenix.util.callback.Callback call) {
			this.callback = call;
		}
		
		private int time;
		
		
		{
			time = 6000;
		}
		
		@SuppressLint("HandlerLeak")
		public void handleMessage(android.os.Message msg){
	    
	    switch (msg.what){
			default:
			break;
			
			case 0:
				super.handleMessage(msg);
				if(callback != null){
					try{
						callback.call();
					}catch(CallbackException e){
						e.printStackTrace();
					}
				}else{
				
					for(br.com.phoenix.util.callback.Callback c : list){
						try {
							c.call();
						} catch (CallbackException e) {
							e.printStackTrace();
						}
					}
				}
				break;
		    }
	    }
    }*/

	/**
	 * Remove o receiver.
	 */
	public void unregister() {
		this.receiverRegistered = false;
		this.context.unregisterReceiver(this.receiver);
	}
	
	public boolean isReceiverRegistered() {
		return receiverRegistered;
	}
	
	/**
	 * Retorna o macAddress do dispositivo.
	 * @param context
	 * @return
	 */
	public static String getMacAddress(Context context){
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}
	
	/**
	 * Inicializa o wifi
	 * @param context
	 * @param value
	 */
	public static boolean enable(Context context,boolean value){
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return wifi.setWifiEnabled(value);
	}

	public void onSupplicantStateChangedAction() {
		if(this.manager.isWifiEnabled()){
			startScan();
		}
		this.listener.onSupplicantStateChangedAction(this.manager.isWifiEnabled());
	}

	public void onExtraNetworkInfo() {
		this.listener.onExtraNetworkInfo();
	}
	
	public void setScanning(boolean value){
		this.scanning = value;
	}
	
	public boolean isScanning() {
		return scanning;
	}
	
	/**
	 * Verifica se o wifiinfo está conectado com sucesso.
	 * @param info
	 * @return
	 */
	public static boolean isWifiConnected(Context context){
		WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		
		if(info != null){
			switch (info.getSupplicantState()) {
				case COMPLETED:
					return true;
		
				default:
					return false;
			}
		}
		return false;
	}
	
	/**
	 * Retorna o estado atual do wifi.
	 * @param context
	 * @return
	 */
	public static boolean isWifiEnabled(Context context){
		WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return  manager.isWifiEnabled();
	}

	public void onWifiStateChangedAction(boolean value) {
		this.listener.onWifiStateChangedAction(value);
	}

	public void onNetworkInfo(NetworkInfo netInfo, String bssid) {
		this.listener.onNetworkInfo(netInfo,bssid);
	}

	public void addConnected(String ssid) {
		if(this.connecteds == null){
			this.connecteds = new ArrayList<WifiConfiguration>();
			
		}
		this.connecteds.add(getWifiConfigurationBySSID(ssid));
	}
	
	public List<WifiConfiguration> getConnecteds(){
		return this.connecteds;
	}
	
	public boolean isHasConnection(WifiConfiguration wfg){
		if(connecteds == null || wfg == null || wfg.SSID == null){
			return false;
		}
		
		
		for(WifiConfiguration w : connecteds){
			if(wfg.SSID.equalsIgnoreCase(w.SSID)){
				return true; 
			}
		}
		return false;
	}
}




