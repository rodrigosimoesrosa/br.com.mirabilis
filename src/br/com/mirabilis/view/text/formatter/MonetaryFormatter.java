package br.com.mirabilis.view.text.formatter;

import br.com.mirabilis.view.text.exception.MonetaryFormatException;


/**
 * Classe formatadora de dados do tipo String. Tem como objetivo formatar uma String para o formato monetario.
 * @author Rodrigo Simões Rosa
 */
public class MonetaryFormatter {

	public static final char COMMA = ',';
	public static final String ZEROS = "00";
	public static final char ZERO = '0';
	public static final int DEFAULT_LIMIT = 2;
	public static final char POINTER = '.';
	
	private int limit;
	
	/**
	 * Construtor.
	 * @param limit limite permitido por aquela String de acordo com o padrão monetário.
	 */
	public MonetaryFormatter(int limit) {
		this.limit = limit;
	}
	
	/**
	 * Método que retorna o valor monetário.
	 * @param value
	 * @return
	 * @throws MonetaryFormatException
	 */
	public String returnMonetaryValue(String value) throws MonetaryFormatException{
		return returnMonetaryValue(value, limit);
	}
	
	/**
	 * Método estático que retorna valor formatado do tipo dinheiro.
	 * @param value Valor
	 * @param limit
	 * @return
	 * @throws MonetaryFormatException 
	 */
	public static String returnMonetaryValue(String value,int limit) throws MonetaryFormatException{
		try{
			char key = value.charAt(value.length() -1);
			StringBuilder beforeCommon;
			StringBuilder afterCommon;
			
			value = value.substring(0, (value.length() -1)).replaceAll("\\".concat(String.valueOf(POINTER)),"").trim();
			
			String [] temp = value.split(String.valueOf(COMMA));
			String before = temp[0];
			
			if(temp.length > 1){
				String after = temp[1];
				if(after.length() == ZEROS.length()){
					if(before.length() == limit){
						temp = value.split(String.valueOf(COMMA));
						beforeCommon = new StringBuilder(temp[0]);
						afterCommon = new StringBuilder(temp[1]);
						insertPointerValue(beforeCommon);
						return beforeCommon.append(COMMA).append(afterCommon).toString();
					}	
				}
			}
			
			beforeCommon = new StringBuilder();
			afterCommon = new StringBuilder();
			boolean comma = false;
			
			for(int i = value.length(); i > 0; i--){
				try{
					char keyTemp = value.charAt(i -1);	
					if(keyTemp != COMMA){
						
						if(comma){
							if(beforeCommon.length() < limit){
								beforeCommon.append(keyTemp);	
							}
						}else {
							if(afterCommon.length() < 1){
								afterCommon.append(keyTemp);
							}else{
								comma = true;
								if(beforeCommon.length() < limit){
									beforeCommon.append(keyTemp);
								}
							}
						}
					}
				}catch(StringIndexOutOfBoundsException e){
					throw new MonetaryFormatException("Ocorreu um erro na formatação!");
				}
			}
			afterCommon.reverse();
			beforeCommon.reverse();
			
			if(beforeCommon.toString().equals("")){
				beforeCommon.append(ZERO);
			}
			
			beforeCommon.replace(0, beforeCommon.length(), Integer.valueOf(beforeCommon.toString()).toString());
			
			insertPointerValue(beforeCommon);
			afterCommon.append(key);
			return beforeCommon.append(COMMA).append(afterCommon).toString();
		}catch(StringIndexOutOfBoundsException e){
			throw new MonetaryFormatException("Não foi possível recupera o digito de entrada");
		}
	}
	
	/**
	 * Insere os pontos de acordo com os milhares.
	 * @param value
	 */
	public static void insertPointerValue(StringBuilder value){
		int count = 0;
		value.reverse();
		for(int j = 0; j < value.length(); j++){
			count++;
			if(count == 4){
				count = 0;
				value.insert(j, POINTER);
			}
		}
		value.reverse();
	}
}
