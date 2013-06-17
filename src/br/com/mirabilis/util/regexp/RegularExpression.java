package br.com.mirabilis.util.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Expressão Regular
 * Realiza a validação de String's por meio de expressões regulares.
 * 
 * @author Rodrigo Simões Rosa.
 */
public class RegularExpression {
    
    enum Expressions{
    	
    	MAIL("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$"),
    	ONLY_NUMBERS_CHARACTERS("[a-zA-Z0-9]+"),
    	DATE("^([1-9]|0[1-9]|[1,2][0-9]|3[0,1])/([1-9]|1[0,1,2])/\\d{4}$"),
    	DECIMAL("^\\d*[0-9](\\.\\d*[0-9])?$"),
    	IP("^((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})$"),
    	DATE_FORM_DB("^\\d{4}-(0[1-9]|1[0,1,2])-(0[1-9]|[1,2][0-9]|3[0,1])$"),
    	TEL_BR("^\\(?\\d{2}\\)?[\\s-]?\\d{4}-?\\d{4}$"),
    	TIME("^([0-1][0-9]|[2][0-3])(:([0-5][0-9]))(:([0-5][0-9])){1,2}$"),
    	SIMPLE_TIME("^([0-1][0-9]|[2][0-3])(:([0-5][0-9])){1,2}$"),
    	URL("^(http[s]?://|ftp://)?(www\\.)?[a-zA-Z0-9-\\.]+\\.(com|org|net|mil|edu|ca|co.uk|com.au|gov|br)$"),
    	USER_NAME("^[a-zA-Z0-9\\.\\_]+$"),
    	PASSWORD("^[a-zA-Z0-9\\!\\@\\#\\$\\%\\&\\*\\?]+$"),
    	MAC_ADDRESS("([0-9a-fA-F]{2}[:-]){5}[0-9a-fA-F]{2}$");
     
    	private String value;
        
        private Expressions(String value){
        	this.value = value;
        }
        
        @Override
        public String toString() {
        	return value;
        }
    }    
    
    private static Matcher getMatcher(String str, Expressions exp){
    	Pattern p = Pattern.compile(exp.toString());  
	    Matcher m = p.matcher(str);
	    return m;
    }
    
    /**
     * Verifica se email informado é valido.<br/>
     * Ex.: email@dom.com 
     * @param mailAddress - endereco de email.
     * @return retorna valor boleano.
     */
    private static boolean isValidMail(String mailAddress){
    	return getMatcher(mailAddress, Expressions.MAIL).matches();    
    }
        
    /**
     * Verifica se conjunto de emails são validos.<br/>
     * Ex.: email@dom.com 
     * @param mailAddresses - lista ou varios emails.
     * @return retorna valor boleano.
     */
    public static boolean isValidMail(String... mailAddresses){
    	for(String mail : mailAddresses){
    		if(!isValidMail(mail.trim())){
    			return false;
    		}
    	}
    	return true;
    }
     
    /**
     * Verifica se texto informado não pussui caractere especial, ou seja,
     * apenas será aceito numeros (0-9) e caracteres de (a-z, A-Z).<br/>
     * Ex.: teste1234
     * @param str - texto a ser validado.
     * @return retorna valor boleano.
     */
    public static boolean isTextNoEspecialChar(String str){
    	return getMatcher(str, Expressions.ONLY_NUMBERS_CHARACTERS).matches();
    }
    
    /**
     * Verifica se a data é válida.<br/>
     * Ex.: 21/12/2012
     * @param str - texto a ser validado.
     * @return retorna valor boleano.
     */
    public static boolean isValidData(String str){
        return getMatcher(str, Expressions.DATE).matches();
    }
    
    /**
     * Verifica se a data é válida para banco de dados.<br/>
     * Ex.: 2012-12-21
     * @param str - texto a ser validado.
     * @return retorna valor boleano.
     */
    public static boolean isValidDataForDataBase(String str){
        return getMatcher(str, Expressions.DATE_FORM_DB).matches();
    }
    
    /**
     * Verifica se a hora é válida.<br/>
     * Ex.: 14:20:12
     * @param str - texto a ser validado.
     * @return retorna valor boleano.
     */
    public static boolean isValidTime(String str){
        return getMatcher(str, Expressions.TIME).matches();
    }
    
    /**
     * Verifica se a hora é válida.<br/>
     * Ex.: 14:20
     * @param str - texto a ser validado.
     * @return retorna valor boleano.
     */
    public static boolean isValidSimpleTime(String str){
        return getMatcher(str, Expressions.SIMPLE_TIME).matches();
    }
    
    /**
     * Verifica se a o texto decimal é válida.<br/>
     * Ex.: 1234567890
     * @param str - texto a ser validado.
     * @return retorna valor boleano.
     */
    public static boolean isValidDecimal(String str){
        return getMatcher(str, Expressions.DECIMAL).matches();
    }
    
    /**
     * Verifica se a o texto é um IP válido.<br/>
     * Ex.: 10.0.0.2
     * @param str - texto a ser validado.
     * @return retorna valor boleano.
     */
    public static boolean isValidIP(String str){
        return getMatcher(str, Expressions.IP).matches();
    }
    
    /**
     * Verifica se a o texto é um Telefone válido.<br/>
     * Ex.: (11) 1234-1234
     * @param str - texto a ser validado.
     * @return retorna valor boleano.
     */
    public static boolean isValidTel(String str){
        return getMatcher(str, Expressions.TEL_BR).matches();
    }
    
    /**
     * Verifica se a o texto é uma URL válido.<br/>
     * Ex.: http://www.google.com.br
     * @param str - texto a ser validado.
     * @return retorna valor boleano.
     */
    public static boolean isValidURL(String str){
        return getMatcher(str, Expressions.URL).matches();
    }
    
    /**
     * Verifica se a o texto é um usuario válido.<br/>
     * Ex.: teste.teste_1234567890
     * @param str - texto a ser validado.
     * @return retorna valor boleano.
     */
    public static boolean isValidUserName(String str){
        return getMatcher(str, Expressions.USER_NAME).matches();
    }
    
    /**
     * Verifica se a o texto é uma senha válida.<br/>
     * Ex.: qwerty1234567890!@#$%&*
     * @param str - texto a ser validado.
     * @return retorna valor boleano.
     */
    public static boolean isValidPassword(String str){
        return getMatcher(str, Expressions.PASSWORD).matches();
    }
    
    /**
     * Verifica se o texto é um macAddress válido.
     * Ex.: 1A:00:2D:4F:0E
     * @param str - texto a ser validado.
     * @return retorna valor boleano.
     */
    public static boolean isValidMacAddress(String str){
    	return getMatcher(str, Expressions.MAC_ADDRESS).matches();
    }
}