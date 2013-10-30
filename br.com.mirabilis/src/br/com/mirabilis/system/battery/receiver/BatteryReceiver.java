package br.com.mirabilis.system.battery.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import br.com.mirabilis.system.battery.Battery;

public class BatteryReceiver extends BroadcastReceiver {

	private Battery battery;
	
	public BatteryReceiver(Battery instance) {
		this.battery = instance;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
			
			int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			battery.onChangeLevel(level);

			int plug = intent.getIntExtra(android.os.BatteryManager.EXTRA_PLUGGED,0);
			if(plug == android.os.BatteryManager.BATTERY_PLUGGED_AC){
				battery.onPluggedAC();
			}else if(plug == android.os.BatteryManager.BATTERY_PLUGGED_USB){
				battery.onPluggedUSB();
			}else if(plug == 0){
				battery.onPluggedOff();
			}
			
			int temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
			battery.onTemperature(temp);
			int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
			battery.onVoltage(voltage);
			
			String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
			battery.onTechnology(technology);
			
			int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
			battery.onStatus(status);
			
			boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
			battery.onPresent(present);
			
			int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_UNKNOWN);
			battery.onHealth(health);
			
			int icon= intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL,0);
			battery.onBatteryIcon(icon);
			
		}else if(intent.getAction().equals(Intent.ACTION_BATTERY_LOW)){
			battery.onBatteryLow();
		}else if(intent.getAction().equals(Intent.ACTION_BATTERY_OKAY)){
			battery.onBatteryOkay();
		}
	}
}
