package br.com.mirabilis.system.bluetooth.util;

import android.bluetooth.BluetoothDevice;

/**
 * Armazena os resultados scaneados de bluetooth.
 * @author Rodrigo Simões Rosa
 *
 */
public class BluetoothScan {

	private BluetoothDevice bluetoothDevice;
	private short rssi;
	
	public BluetoothScan(BluetoothDevice bd,short rssi) {
		this.bluetoothDevice = bd;
		this.rssi = rssi;
	}
	
	public BluetoothDevice getBluetoothDevice() {
		return bluetoothDevice;
	}
	
	public short getRssi() {
		return rssi;
	}
}
