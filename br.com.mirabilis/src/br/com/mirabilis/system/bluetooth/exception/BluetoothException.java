package br.com.mirabilis.system.bluetooth.exception;


/**
 * Exception correspondente aos objetos bluetooth e configura��es do mesmo.
 * @author Rodrigo Sim�es Rosa
 *
 */
public class BluetoothException extends Exception {
	
	/**
	 * Serializa��o
	 */
	private static final long serialVersionUID = 1L;

	public BluetoothException() {
		super("Erro na utiliza��o do bluetooth");
	}
	
	public BluetoothException(String s) {
		super(s);
	}
	
	public BluetoothException(Throwable t) {
		super(t);
	}
}
