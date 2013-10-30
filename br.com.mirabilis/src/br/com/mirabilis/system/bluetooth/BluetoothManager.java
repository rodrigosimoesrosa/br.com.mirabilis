package br.com.mirabilis.system.bluetooth;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import br.com.mirabilis.system.bluetooth.exception.BluetoothException;
import br.com.mirabilis.system.bluetooth.listener.BluetoothListener;
import br.com.mirabilis.system.bluetooth.receiver.BluetoothReceiver;
import br.com.mirabilis.system.bluetooth.service.BluetoothService;
import br.com.mirabilis.system.bluetooth.util.Device;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;


/**
 * Classe gerenciadora de bluetooth.
 * @author Rodrigo Simões Rosa
 */
public class BluetoothManager {

	private Context context;
	private BluetoothAdapter bluetooth;
	private BluetoothReceiver receiver;
	private BluetoothListener listener;
	private boolean scanning;
	private boolean registered;
	private BluetoothManager instance;
	
	/**
	 * UUID necessário para identifcação de aplicação unica.
	 */
	private static UUID uuid;
	
	/**
	 * Bloco de inicialização
	 */
	static {
		uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	}
	
	/**
	 * Construtor
	 * @param context
	 * @throws BluetoothException
	 */
	public BluetoothManager(Context context) {
		instance = this;
		
		this.context = context;
		this.bluetooth = BluetoothAdapter.getDefaultAdapter();
		startReceiver(context);
	}
	
	/**
	 * Inicializa o ouvinte das ações de bluetooth.
	 */
	public void startReceiver(Context context){
		this.receiver = new BluetoothReceiver(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		filter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
		filter.addAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		filter.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		filter.addAction(BluetoothAdapter.EXTRA_STATE);
		filter.addAction(BluetoothAdapter.EXTRA_PREVIOUS_STATE);
		filter.addAction(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE);
		filter.addAction(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION);
		filter.addAction(BluetoothAdapter.EXTRA_LOCAL_NAME);
		filter.addAction(BluetoothAdapter.EXTRA_SCAN_MODE);
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothDevice.EXTRA_DEVICE);
		filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		filter.addAction("android.bleutooth.device.action.UUID");
		filter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
		
		context.registerReceiver(this.receiver,filter);
		this.registered = true;
	}
	
	/**
	 * Seta o dispositivo bluetooth como ligado/desligado
	 * @param value
	 */
	public void setEnable(boolean value){
		if(value){
			bluetooth.enable();
		} else {
			bluetooth.disable();
			BluetoothService.clear();
		}
	}
	
	public boolean isRegistered(){
		return registered;
	}
	
	/**
	 * Retorna o estado do dispositivo bluetooth.
	 * @return
	 */
	public boolean isEnable() {
		return bluetooth.isEnabled();
	}
	
	/**
	 * Retorna o estado do escaneamento de dispositivos bluetooth
	 * @return
	 */
	public boolean isScanning() {
		return this.scanning;
	}

	/**
	 * Seta o estado do escaneamento de dispositivos bluetooth
	 * @param value
	 */
	public void setScanning(boolean value){
		this.scanning = value;
	}

	/**
	 * Inicializa a descoberta de dispositivos bluetooth
	 */
	public void startDiscovery() {
		this.bluetooth.cancelDiscovery();
		this.bluetooth.startDiscovery();
	}
	
	/**
	 * Retorna os dispositivos bluetooth pareados contidos na memória do dispositivo.
	 * @return
	 */
	public Set<BluetoothDevice> getBondedDevices() {
		return bluetooth.getBondedDevices();
	}

	/**
	 * Retorna o dispositivo remoto de acordo com o macAddress.
	 * @param macAddress
	 * @return
	 */
	public BluetoothDevice getRemoteDevice(String macAddress) {
		return bluetooth.getRemoteDevice(macAddress);
	}
	
	/**
	 * Retorna o estado de descoberta de novos dispositivos
	 * @return
	 */
	public boolean isDiscovery(){
		return bluetooth.isDiscovering();
	}

	/**
	 * Cancela a descoberta de novos dispositivos.
	 */
	public void cancelDiscovery() {
		bluetooth.cancelDiscovery();
	}
	
	/**
	 * Seta o ouvinte do bluetooth.
	 * @param listener
	 */
	public void setOnBluetoothListener(BluetoothListener listener) {
		this.listener = listener;
	}
	
	/**
	 * É disparado para o ouvinte caso o estado seja alterado, e também caso seja verdadeiro é inicializado a chamada do método startDiscovery().
	 * @param value
	 */
	public void onChangeState(boolean value){
		if(value){
			startDiscovery();
		}
		this.listener.onChangeState(value);
	}
	
	/**
	 * É disparado para o ouvinte caso o nome do dispositivo seja alterado e por sua vez retorna o novo nome.
	 */
	public void onChangeName() {
		this.listener.onChangeName(this.bluetooth.getName());
	}

	/**
	 * É disparado para o ouvinte caso a descoberta de dispositivos seja inicializada.
	 */
	public void onDiscoveryStarted() {
		this.listener.onDiscoveryStarted();
	}

	/**
	 * É disparado para o ouvinte caso a descoberta de dispositivos seja finalizada.
	 */
	public void onDiscoveryFinished() {
		this.listener.onDiscoveryFinished(new ArrayList<Device>(Device.getDevices().values()));
	}
	
	/**
	 * É disparado para o ouvinta caso um novo pareamento seja realizado. Retornando o novo dispositivo.
	 * @param device
	 */
	public void onPairing(Device device) {
		this.listener.onPairing(device);
	}
	
	/**
	 * Destrói a intancia do BluetoothManager e o ouvinte das ações.
	 */
	public void destroy(){
		unregisterReceiver();
		//instance = null;
	}
	
	public void unregisterReceiver(){
		Log.v("BluetoothManager","unregisterReceiver");
		context.unregisterReceiver(receiver);
		registered = false;
	}
	
	/**
	 * Seta o nome do bluetooth, que será utilizado para a visualização dos outros dispositivos.
	 * @param value
	 */
	public void setName(String value){
		this.bluetooth.setName(value);
	}
	
	/**
	 * Seta um novo uuid de acordo com a string informada.
	 * @param value
	 */
	public static void setUUID(String value) {
		uuid = UUID.fromString(value);
	}
	
	/**
	 * Retorna uuid.
	 * @return
	 */
	public static UUID getUUID() {
		return uuid;
	}
	
	public BluetoothManager getInstance() {
		return instance;
	}
	
	public static void pairDevice(Device device) throws BluetoothException {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		BluetoothDevice bluetoothDevice = adapter.getRemoteDevice(device.getMacAddress());
		
		if(bluetoothDevice == null){
			throw new BluetoothException("Dispositivo nulo. É impossivel completar a ação.");
		}
		
		try {
		    Method m = bluetoothDevice.getClass().getMethod("createBond", (Class[]) null);
		    m.invoke(bluetoothDevice, (Object[]) null);
		} catch (Exception e) {
			throw new BluetoothException("Ocorreu um erro no pareamento do dispositivo" + bluetoothDevice.getName());
		}
	}

	public static void unpairDevice(Device device) throws BluetoothException {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		BluetoothDevice bluetoothDevice = adapter.getRemoteDevice(device.getMacAddress());
		
		if(bluetoothDevice == null){
			throw new BluetoothException("Dispositivo nulo. É impossivel completar a ação.");
		}
		
		try {
		    Method m = bluetoothDevice.getClass().getMethod("removeBond", (Class[]) null);
		    m.invoke(bluetoothDevice, (Object[]) null);
		} catch (Exception e) {
			throw new BluetoothException("Ocorreu um erro no despareamento do dispositivo" + bluetoothDevice.getName());
		}
	}
}
