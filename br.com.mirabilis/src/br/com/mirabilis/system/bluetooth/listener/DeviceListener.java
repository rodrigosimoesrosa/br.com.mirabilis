package br.com.mirabilis.system.bluetooth.listener;

/**
 * Listener que verifica o termino de conexão com o dispositivo.
 * @author Rodrigo Simões Rosa
 *
 */
public interface DeviceListener {
	
	/**
	 * Assim que completar a conexão com o dispositivo. 
	 * @param input
	 * @param output
	 */
	void onConnected();
	
	/**
	 * Dispara um listener ao ocorrer uma falha na comunicação.
	 */
	void onFailConnect();
}
