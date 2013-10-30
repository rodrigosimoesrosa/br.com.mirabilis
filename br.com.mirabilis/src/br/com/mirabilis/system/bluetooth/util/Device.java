package br.com.mirabilis.system.bluetooth.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import android.bluetooth.BluetoothDevice;
import br.com.mirabilis.system.bluetooth.exception.BluetoothException;
import br.com.mirabilis.system.bluetooth.listener.DeviceListener;
import br.com.mirabilis.system.bluetooth.service.BluetoothService;

/**
 * Classe que armazena as informações dos dispositivos encontrados.
 * @author Rodrigo
 *
 */
public class Device implements Serializable {

	/**
	 * Serialização. 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String macAddress;
	private String singleintension;
	private BluetoothService service;
	private int bonded;

	private DeviceListener listener;
	
	
	private static HashMap<String, Device> devices;
	
	/**
	 * Construtor
	 * @param macAddress
	 */
	private Device(String macAddress) {
		this.macAddress = macAddress;
	}

	/**
	 * Seta o nome do dispositivo.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Retorna o nome do dispositivo.
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Adiciona novo dispositivo.
	 * @param macAddress
	 * @return
	 */
	public static Device addDevice(String macAddress){
		if(devices == null){
			devices = new HashMap<String, Device>();
		}
		Device device = devices.get(macAddress);
		
		if(device == null){
			device = new Device(macAddress);
			devices.put(macAddress, device);
		}
		return device;
	}
	
	/**
	 * Retorna o macAddress.
	 * @return
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * Retorna o singleIntension.
	 * @return
	 */
	public String getSingleintension() {
		return singleintension;
	}

	/**
	 * Seta o singleIntension.
	 * @param singleintension
	 */
	public void setSingleintension(String singleintension) {
		this.singleintension = singleintension;
	}
	
	/**
	 * Retorna o estado do pareamento do dispositivo.
	 * {@link BluetoothDevice#BOND_BONDED}
	 * {@link BluetoothDevice#BOND_BONDING}
	 * {@link BluetoothDevice#BOND_NONE}
	 * @return
	 */
	public int getBonded() {
		return bonded;
	}

	/**
	 * Seta o estado do pareamento do dispositivo.
	 * {@link BluetoothDevice#BOND_BONDED}
	 * {@link BluetoothDevice#BOND_BONDING}
	 * {@link BluetoothDevice#BOND_NONE}
	 * @return
	 */
	public void setBonded(int bonded) {
		this.bonded = bonded;
	}
	
	/**
	 * Retorna o status do pareamento.
	 * @return
	 */
	public String getStatus() {
		switch (this.bonded) {
			case BluetoothDevice.BOND_BONDED:
				return "Pareado";
				
			case BluetoothDevice.BOND_BONDING:	
				return "Pareando ...";
				
			case BluetoothDevice.BOND_NONE:
			default:
				return "Não Pareado";
		}
	}
	
	/**
	 * Retorna o dispositivo com aquele macAddress.
	 * @param mac
	 * @return
	 * @throws BluetoothException
	 */
	public static Device getDeviceByKey(String mac) throws BluetoothException{
		if(devices == null){
			throw new BluetoothException("A lista de dispositivos está vazia. Desative e ative o bluetooth para resolver o problema.");
		}
		
		Device device = devices.get(mac);
		
		if(device == null){
			throw new BluetoothException("Não existe um dispostivo com esta chave. Desative e ative o bluetooth para resolver o problema.");
		}
		return device;
	}

	/**
	 * Retorna o hashMap com os dispositivos.
	 * @return
	 */
	public static HashMap<String, Device> getDevices() {
		return devices;
	}

	/**
	 * Retorna o service
	 * @return
	 */
	public BluetoothService getService() {
		return service;
	}

	/**
	 * Seta o service
	 * @param service
	 */
	public void setService(BluetoothService service) {
		this.service = service;
	}

	/**
	 * Limpa serviços dos dispositivos existentes.
	 */
	public static void clearService() {
		if(devices != null){
			for(Device d : devices.values()){
				if(d.service != null){
					try{
						d.service.getOutput().close();
						d.service.getInput().close();
						d.service.close();
						d.service = null;
					}catch(IOException e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void setOnBluetoothServiceListener(DeviceListener listener){
		this.listener = listener;
	}
	
	public void onConnected() {
		if(listener != null){
			this.listener.onConnected();
		}
	}

	public void onFailConnect() {
		if(listener != null){
			this.listener.onFailConnect();
		}
	}
}
