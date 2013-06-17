package br.com.mirabilis.system.bluetooth;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import br.com.mirabilis.system.bluetooth.exception.BluetoothException;
import br.com.mirabilis.system.bluetooth.service.BluetoothService;
import br.com.mirabilis.system.bluetooth.util.Device;

public class Bluetooth {
	
	public Device device;
	public Context context;
	private BluetoothService bluetoothService;
	
	public Bluetooth(Device device, Context context){
		this.device = device;
		this.context = context;
	}
	
	/**
	 * Método que inicializa a comunicação com o dispositivo.
	 * @param device Dispositivo bluetooth encontrado.
	 * @throws BluetoothException
	 */
	public void start() throws BluetoothException{
		if(device == null){
			throw new BluetoothException("Este dispositivo encontra-se nulo");
		}
		connect();
	}
	
	/**
	 * Conecta com o dispositivo informado.
	 * @param device
	 * @return
	 * @throws BluetoothException
	 * @throws IOException 
	 */
	public void connect() throws BluetoothException {
		bluetoothService = BluetoothService.getActiveService();
		if(bluetoothService == null){
			Bundle extras = new Bundle();
			extras.putSerializable("macAddress", device.getMacAddress());
			Intent service = new Intent(BluetoothService.BLUETOOTH_SERVICE);
			service.putExtras(extras);
			context.startService(service);
		}else{
			bluetoothService.setDevice(device);
			bluetoothService.doConnection();
		}
	}
	
	/**
	 * Escreve e envia por socket vinculado com dispositivo.
	 * @param device
	 * @param data
	 * @param handler
	 * @throws BluetoothException
	 */
	public void writeByteSocket(byte [] data) throws BluetoothException {
		bluetoothService = BluetoothService.getActiveService();
		if(bluetoothService == null){
			throw new BluetoothException("O serviço deste dispositivo ainda não foi inicializado.");
		}
		bluetoothService.writeByteSocket(data);
	}
	
	/**
	 * Escreve e envia por socket vinculado com dispositivo.
	 * @param device
	 * @param data
	 * @param handler
	 * @throws BluetoothException
	 */
	public void writeByteSocket(String data) throws BluetoothException {
		this.writeByteSocket(data.getBytes());
	}
	
	public void readByte(String key, final Handler handler) throws BluetoothException {
		/*Device device = Device.getDeviceByKey(key);
		if(device == null){
			throw new BluetoothException("Dispositivo inexistente");
		}
		
		if(device.getBonded() != BluetoothDevice.BOND_BONDED){
			throw new BluetoothException("É necessário realizar o pareamento de dispositivo");
		}
		
		try{
			final InputStream input = device.getBluetoothSocket().getInputStream();
			Thread threadWrite = new Thread(){
				public void run() {
					byte[] buffer = new byte[Byte.MAX_VALUE]; 
					while (true) {
						try {
							Message msg = Message.obtain();
							msg.what = BluetoothMessage.MESSAGE_READ;
							Bundle b = new Bundle();
							input.read(buffer);
							b.putByteArray("response", buffer);
							msg.setData(b);
							handler.sendMessage(msg);
						}catch (IOException e) { 
							break; 
						}
					}
				};
			};
			threadWrite.start();
			
		}catch(IOException e){
			throw new BluetoothException(e);
		}*/
	}
	
	/**
	 * Retorna o status de conexão do dispositivo.
	 * @return
	 * @throws BluetoothException
	 */
	public boolean isConnected() throws BluetoothException{
		bluetoothService = BluetoothService.getActiveService();
		if(bluetoothService != null){
			return bluetoothService.isConnected();
		}
		throw new BluetoothException("O serviço deste dispositivo ainda não foi inicializado.");
	}
	
	/**
	 * Retorna dispositivo bluetooth.
	 * @return
	 */
	public Device getDevice() {
		return device;
	}
	
	public BluetoothService getBluetoothService() {
		bluetoothService = BluetoothService.getActiveService();
		return bluetoothService;
	}
}