package br.com.mirabilis.util.service;

import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * Classe que contem métodos utilitários para serviços.
 * @author Rodrigo Simões Rosa.
 */
public class ServiceUtil {
	
	/**
	 * Realiza a checagem de um serviço.
	 * @param servicename
	 * @param context
	 * @return
	 */
	public static boolean isWorkingService(String servicename, Context context) {
		ActivityManager manager=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) manager.getRunningServices(30);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString().equals(servicename)) {
				return true;
			}
		}
		return false;
	}
}
