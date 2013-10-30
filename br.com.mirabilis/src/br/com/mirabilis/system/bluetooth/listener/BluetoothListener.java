package br.com.mirabilis.system.bluetooth.listener;

import java.util.List;

import br.com.mirabilis.system.bluetooth.util.Device;


/**
 * Listener Bluetooth
 * @author Rodrigo Simões Rosa
 *
 */
public interface BluetoothListener {
	void onChangeState(boolean state);
	void onChangeName(String name);
	void onDiscoveryStarted();
	void onDiscoveryFinished(List<Device> values);
	void onPairing(Device device);
}
