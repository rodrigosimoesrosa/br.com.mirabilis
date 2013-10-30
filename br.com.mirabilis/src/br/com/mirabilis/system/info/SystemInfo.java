package br.com.mirabilis.system.info;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Classe que fornece as informa��es do sistema operacional android. 
 * @author Rodrigo Sim�es Rosa
 *
 */
public class SystemInfo {

	/**
	 * Retorna a quantidade de mem�ria disponivel em bytes.
	 * @return
	 */
	public static long getMemoryAvaliable(Context context){
		ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		actManager.getMemoryInfo(info);
		return info.availMem;
	}
	
	/**
	 * Retorna a quantidade mem�ria em MB de acordo com o percentual "1f == 100%",".2f == 20%", ".5f == 50%" e assim por diante. 
	 * @param percent
	 * @return
	 */
	public static int getMBMemoryAvaliableByPercent(Context context,float percent){
		long value = (long) (getMemoryAvaliable(context) * percent);
		return (int) (value / 1024);
	}
}
