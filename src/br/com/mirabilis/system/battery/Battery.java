package br.com.mirabilis.system.battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import br.com.mirabilis.system.battery.listener.BatteryListener;
import br.com.mirabilis.system.battery.receiver.BatteryReceiver;

public class Battery {
	
	private BatteryListener listener;
	private BatteryReceiver receiver;
	private Context context;
	private boolean usb;
	private boolean ac;
	private float levelBattery;
	
	public Battery(Context context) {
		this.context = context;
		start();
	}
	
	public void start() {
		this.receiver = new BatteryReceiver(this);
		IntentFilter filter = new IntentFilter();
		
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		filter.addAction(Intent.ACTION_BATTERY_LOW);
		filter.addAction(Intent.ACTION_BATTERY_OKAY);
		
		this.context.registerReceiver(this.receiver,filter);
	}

	public void setOnBatteryListener(BatteryListener listener){
		this.listener = listener;
	}

	public void onChangeLevel(float f) {
		this.levelBattery = f;
		this.listener.onBatteryChange(f);
	}

	public void onPluggedAC() {
		this.ac = true;
		this.usb = false;
		this.listener.onPluggedAC();
	}

	public void onPluggedUSB() {
		this.usb = true;
		this.ac = false;
		this.listener.onPluggedUSB();
	}
	
	public void destroy(){
		this.context.unregisterReceiver(this.receiver);
	}
	
	public boolean isAc() {
		return ac;
	}
	
	public boolean isUsb() {
		return usb;
	}
	
	public float getLevelBattery() {
		return levelBattery;
	}

	public void onBatteryLow() {
		this.listener.onBatteryLow();
	}

	public void onBatteryOkay() {
		this.listener.onBatteryOkay();
	}

	public void onTemperature(int temp) {
		this.listener.onTemperature(temp);
	}

	public void onVoltage(int voltage) {
		this.listener.onVoltage(voltage);
	}

	public void onTechnology(String technology) {
		this.listener.onTechnology(technology);
	}

	public void onStatus(int status) {
		this.listener.onStatus(status);
	}

	public void onPresent(boolean present) {
		this.listener.onPresent(present);
	}

	public void onHealth(int health) {
		this.listener.onHealth(health);
	}

	public void onBatteryIcon(int icon) {
		this.listener.onBatteryIcon(icon);
	}

	public void onPluggedOff() {
		// TODO Auto-generated method stub
		
	}
}
