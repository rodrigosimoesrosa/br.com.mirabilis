package br.com.mirabilis.system.wifi.exception;


/**
 * Exception correspondente aos objetos wifi e configura��es de rede.
 * @author Rodrigo Sim�es Rosa
 *
 */
public class WifiException extends Exception {
	
	/**
	 * Serializa��o
	 */
	private static final long serialVersionUID = 1L;

	public WifiException() {
		super("Erro na utiliza��o do wifi");
	}
	
	public WifiException(String s) {
		super(s);
	}
	
	public WifiException(Throwable t) {
		super(t);
	}
}
