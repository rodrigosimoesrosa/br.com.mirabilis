package br.com.mirabilis.system.battery.listener;

import android.os.BatteryManager;

public interface BatteryListener {

	public void onBatteryChange(float f);

	public void onPluggedAC();
	
	public void onPluggedUSB();

	public void onBatteryLow();

	public void onBatteryOkay();

	public void onTemperature(int temp);

	public void onVoltage(int voltage);

	public void onTechnology(String technology);

	/**
	 * Retorna o status correspondente de acordo com as constantes.
	 * {@link BatteryManager#BATTERY_STATUS_CHARGING}<br>
	 * {@link BatteryManager#BATTERY_STATUS_DISCHARGING}<br>
	 * {@link BatteryManager#BATTERY_STATUS_FULL}<br>
	 * {@link BatteryManager#BATTERY_STATUS_NOT_CHARGING}<br>
	 * {@link BatteryManager#BATTERY_STATUS_UNKNOWN}<br>
	 * @param status
	 */
	public void onStatus(int status);

	public void onPresent(boolean present);

	/**
	 * Retorna o health correspondente de acordo com as constantes.
	 * @param health
	 */
	public void onHealth(int health);

	/**
	 * Retorna o icone que demonstra o status da bateria.
	 * @param icon
	 */
	public void onBatteryIcon(int icon);
}
