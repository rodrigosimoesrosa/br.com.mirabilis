package br.com.mirabilis.system.bluetooth.exception;


/**
 * Exception correspondente aos objetos bluetooth e configurações do mesmo.
 * @author Rodrigo Simões Rosa
 *
 */
public class BluetoothException extends Exception {
	
	/**
	 * Serialização
	 */
	private static final long serialVersionUID = 1L;

	public BluetoothException() {
		super("Erro na utilização do bluetooth");
	}
	
	public BluetoothException(String s) {
		super(s);
	}
	
	public BluetoothException(Throwable t) {
		super(t);
	}
}
