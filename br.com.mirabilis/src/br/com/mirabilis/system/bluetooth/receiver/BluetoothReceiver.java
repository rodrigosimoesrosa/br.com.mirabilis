package br.com.mirabilis.system.bluetooth.receiver;

import java.util.Iterator;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import br.com.mirabilis.system.bluetooth.BluetoothManager;
import br.com.mirabilis.system.bluetooth.exception.BluetoothException;
import br.com.mirabilis.system.bluetooth.util.Device;

public class BluetoothReceiver extends BroadcastReceiver {

	private BluetoothManager manager;
	
	/**
	 * Construtor.
	 * @param Bluetooth bluetooth
	 */
	public BluetoothReceiver(BluetoothManager manager) {
		this.manager = manager;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
			Iterator<BluetoothDevice> bd = manager.getBondedDevices().iterator();
			
			while(bd.hasNext()){
				BluetoothDevice b = bd.next();
				Device d = Device.addDevice(b.getAddress().trim());
				d.setName(b.getName());
				d.setBonded(BluetoothDevice.BOND_BONDED);
			}
			this.manager.onDiscoveryFinished();
			
		}else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)){
			
			this.manager.onDiscoveryStarted();
			
		}else if(action.equals(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED)){
			
			this.manager.onChangeName();
			
		}else if(action.equals(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)){
			
			Log.v("Bluetooth", "ACTION_REQUEST_DISCOVERABLE");
			
		}else if(action.equals(BluetoothAdapter.ACTION_REQUEST_ENABLE)){
			
			Log.v("Bluetooth", "ACTION_REQUEST_ENABLE");
			
		}else if(action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)){
			
			if(this.manager.isScanning()){
				this.manager.setScanning(false);
				this.manager.onChangeState(manager.isEnable());
			}
			
		}else if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
			
			this.manager.setScanning(true);
			
		}else if(action.equals(BluetoothDevice.ACTION_FOUND)){
			
			BluetoothDevice bd = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
			
			Device d = Device.addDevice(bd.getAddress().trim());
			d.setName(bd.getName());
			d.setBonded(bd.getBondState());
			d.setSingleintension(rssi + "");
			
		}else if(action.equals(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION)){
			
			Log.w("Bluetooth", "EXTRA_DISCOVERABLE_DURATION");
			
		}else if(action.equals(BluetoothAdapter.EXTRA_LOCAL_NAME)){
			
			Log.w("Bluetooth", "EXTRA_LOCAL_NAME");
			
		}else if(action.equals(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE)){
			
			Log.w("Bluetooth", "EXTRA_PREVIOUS_SCAN_MODE");
			
		}else if(action.equals(BluetoothAdapter.EXTRA_PREVIOUS_STATE)){
			
			Log.w("Bluetooth", "EXTRA_PREVIOUS_STATE");
			
		}else if(action.equals(BluetoothAdapter.EXTRA_SCAN_MODE)){
			
			Log.w("Bluetooth", "EXTRA_SCAN_MODE");
			
		}else if(action.equals(BluetoothAdapter.EXTRA_STATE)){
			
			Log.w("Bluetooth", "EXTRA_STATE");
			
		}else if(action.equals("android.bluetooth.device.action.UUID")){
	        
			Parcelable [] uuidExtra = intent.getParcelableArrayExtra("android.bluetooth.device.extra.UUID");
	        for(Parcelable p : uuidExtra){
	        	Log.v("UUID", p.toString());
	        }
	        
		}else if(action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)){
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			Log.v(BluetoothDevice.class.getName(),device.getName());
		}else if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
			BluetoothDevice bd = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			if(bd.getBondState() == BluetoothDevice.BOND_BONDED){
				Device device = null;
				try {
					device = Device.getDeviceByKey(bd.getAddress().trim());
					Log.v("ENCONTROU", "DEVICE");
				} catch (BluetoothException e) {
					Log.v("NÂO ENCONTROU", "DEVICE");
				}
				manager.onPairing(device);
			}else if(bd.getBondState() == BluetoothDevice.BOND_NONE){
				manager.onPairing(null);
			}
		}
	}

}
