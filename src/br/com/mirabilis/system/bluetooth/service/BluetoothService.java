package br.com.mirabilis.system.bluetooth.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import br.com.mirabilis.system.bluetooth.BluetoothManager;
import br.com.mirabilis.system.bluetooth.exception.BluetoothException;
import br.com.mirabilis.system.bluetooth.util.Device;


public class BluetoothService extends Service {
	
	public static final String BLUETOOTH_SERVICE = "br.com.mirabilis.system.bluetooth.service.BluetoothService";
	
	public Vector<Byte> packdata = new Vector<Byte>(2048);
	public byte[] buffer;
	private BluetoothAdapter bluetooth;
	private BluetoothSocket socket;
	private OutputStream output;
	private boolean receive;
	private InputStream input;
	private boolean connected;
	private Device device; 
	
	public static final int STATE_NONE = 0;       // we're doing nothing
	public static final int STATE_LISTEN = 1;     // now listening for incoming connections
	public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
	public static final int STATE_CONNECTED = 3;  // now connected to a remote device
	public static final int STATE_CONNECTED_FAILED = 4;  
	public static final int STATE_CONNECTED_ANALDATA = 5;  
	public static final int STATE_CONNECTED_DOSUCCESS = 6; 
	public static final int STATE_CONNECTED_SERVER = 7; 
	public static final int STATE_CANCEL_POS = 8; 
	public static final int STATE_SHOW_CARDINFO = 9;
	public static final int STATE_SHOW_PENDING = 10;
	public static final int STATE_SHOW_PWD = 11;
	
	private static BluetoothService instance;
	
	/**
	 * Cria o serviço de bluetooth
	 * @param savedInstanceState
	 */
	public void onCreate(Bundle savedInstanceState) {
		
	}
	
	/**
	 * Inicia a thread de escuta do recebimento de dados daquele dispositivo.
	 */
	public void startReceiveThread() {
		receive = true;
	}
	
	/**
	 * Interrompe a thread de escuta do recebimento de dados daquele dispositivo.
	 */
	public void stopReceiveThread() {
		receive = false;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * Inicializa o serviço.
	 */
	public void onStart(Intent intent, int startId) {
		Log.w("BluetoothService", "Criado e inicializado");
		instance = this;
		bluetooth = BluetoothAdapter.getDefaultAdapter();
		Device.clearService();
		
		if(bluetooth != null){
			try {
				device = Device.getDeviceByKey(intent.getStringExtra("macAddress"));
				doConnection();
			} catch (BluetoothException e) {
				e.printStackTrace();
			}
			
			boolean stop = intent.getBooleanExtra("status", false);
			if(stop){
				stopReceiveThread();
				if(socket != null){
					try{
						if(bluetooth != null)
							bluetooth.cancelDiscovery();
						socket.close();
						connected = false;
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
		super.onStart(intent, startId);
	}
	
	/**
	 * Realiza a conexão com o dispositivo.
	 */
	public void doConnection(){
		if(device != null){
			if(connect()){
				startReceiveThread();
			}
		}
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}
	
	public class LocalBinder extends Binder {
		BluetoothService getService() {
			return BluetoothService.this;
		}
	}
	
	private final IBinder mBinder = new LocalBinder();
	
	/**
	 * Serviço de enviar dados para o dispositivo bluetooth. 
	 * @param data Array de bytes.
	 * @param handler
	 * @throws BluetoothException
	 */
	public void writeByteSocket(final byte[] data) throws BluetoothException{
		if(socket == null){
			throw new BluetoothException("Realize a abertura de conexão primeiro com o dispositivo.");
		}
		try{
			writeByte(data);
		}catch(Exception e){
			Log.v("BluetoothService", e.getMessage());
			throw new BluetoothException(e);
		}
	}
	
	private void writeByte(final byte[] data) throws IOException{
		output.write(data);
		output.flush();
	}
	
	/**
	 * Serviço de enviar dados para o dispositivo bluetooth. 
	 * @param data String
	 * @param handler
	 * @throws BluetoothException
	 */
	public void writeByteSocket(final String data) throws BluetoothException {
		writeByteSocket(data.getBytes());
	}
	
	/**
	 * Serviço de abertura de conexão com o dispositivo bluetooth.
	 * @param device
	 * @return
	 */
	private boolean connect() {
		try {
			BluetoothDevice bluetoothDevice = bluetooth.getRemoteDevice(device.getMacAddress());
			socket = bluetoothDevice.createRfcommSocketToServiceRecord(BluetoothManager.getUUID());
			socket.connect();
			if (socket != null) {
				connected = true;
				input = socket.getInputStream();
				output= socket.getOutputStream();
				device.onConnected();
				return true;
			}else{
				device.onFailConnect();
				return false;
			}
		} catch (IOException e) {
			try{
				if(bluetooth != null)
					bluetooth.cancelDiscovery();
				if(socket != null){
					socket.close();
					socket = null;
					connected = false;
				}
				device.onFailConnect();
			}catch(Exception ee){
				ee.printStackTrace();
			}
			return false;
		} 
	}
	
	public void setReceive(boolean b) {
		receive = b;
	}
	
	public boolean getReceive() {
		return receive;
	}

	/**
	 * Retorna caso esteja conectado
	 * @return
	 */
	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public void close() {
		try {
			if(output != null){
				this.output.close();
			}
			
			if(input != null){
				this.input.close();
			}	
			
			if(socket != null){
				this.socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static BluetoothService getActiveService() {
		return instance;
	}
	
	public Device getDevice() {
		return device;
	}
	
	public BluetoothSocket getSocket() {
		return socket;
	}
	
	public InputStream getInput() {
		return input;
	}
	
	public OutputStream getOutput() {
		return output;
	}
	
	public void setDevice(Device device) {
		this.device = device;
	}
	
	public void setSocket(BluetoothSocket socket) {
		this.socket = socket;
	}
	
	public void setInput(InputStream input) {
		this.input = input;
	}
	
	public void setOutput(OutputStream output) {
		this.output = output;
	}

	public static void clear() {
		if(instance != null){
			if(instance.socket != null){
				if(instance.output != null){
					try {
						instance.output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(instance.input != null){
					try {
						instance.input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}	
				}
			}
		}
		instance = null;
	}
}
