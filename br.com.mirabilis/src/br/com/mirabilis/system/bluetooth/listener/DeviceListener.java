package br.com.mirabilis.system.bluetooth.listener;

/**
 * Listener que verifica o termino de conex�o com o dispositivo.
 * @author Rodrigo Sim�es Rosa
 *
 */
public interface DeviceListener {
	
	/**
	 * Assim que completar a conex�o com o dispositivo. 
	 * @param input
	 * @param output
	 */
	void onConnected();
	
	/**
	 * Dispara um listener ao ocorrer uma falha na comunica��o.
	 */
	void onFailConnect();
}
