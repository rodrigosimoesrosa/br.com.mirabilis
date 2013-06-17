package br.com.mirabilis.system.wifi.exception;


/**
 * Exception correspondente aos objetos wifi e configurações de rede.
 * @author Rodrigo Simões Rosa
 *
 */
public class WifiException extends Exception {
	
	/**
	 * Serialização
	 */
	private static final long serialVersionUID = 1L;

	public WifiException() {
		super("Erro na utilização do wifi");
	}
	
	public WifiException(String s) {
		super(s);
	}
	
	public WifiException(Throwable t) {
		super(t);
	}
}
