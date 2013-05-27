package br.com.mirabilis.view.text;

import java.math.BigDecimal;

import br.com.mirabilis.view.text.exception.MonetaryFormatException;
import br.com.mirabilis.view.text.formatter.MonetaryFormatter;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.TextView;

/**
 * Classe de propriedades monetárias especifica para campos de texto (TextView).
 * @author Rodrigo Simões Rosa
 */
public class MonetaryTextView {

	private TextView field;
	private String result;
	private OnKeyFormatter onKeyFormatter;
	private MonetaryFormatter formatter;
	
	/**
	 * Bloco de inicialização de instância.
	 */
	{
		this.result = "";
	}
	
	/**
	 * Construtor 
	 * @param field Campo de texto a ser adicionado as propriedades monetárias.
	 */
	public MonetaryTextView(TextView field){
		this(field, MonetaryFormatter.DEFAULT_LIMIT);
	}
	
	/**
	 * Construtor
	 * @param field Campo de texto a ser adicionado as propriedades monetárias.
	 * @param limit quantidade de digitos permitidos.
	 */
	public MonetaryTextView(TextView field, int limit) {
		this.field = field;
		this.formatter = new MonetaryFormatter(limit);
		setText(String.valueOf(MonetaryFormatter.ZERO) + String.valueOf(MonetaryFormatter.COMMA) + MonetaryFormatter.ZEROS);
		Selection.setSelection(field.getEditableText(), getContent().length());
		setListeners();
	}
	
	/**
	 * Seta os listeners
	 */
	private void setListeners(){
		
		this.field.setOnKeyListener(new OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getAction() == KeyEvent.ACTION_DOWN){
					switch (keyCode) {
						case KeyEvent.KEYCODE_ENTER:
							if(onKeyFormatter != null){
								onKeyFormatter.onKeyEnter(getValue());
							}
							break;
							
						case KeyEvent.KEYCODE_DEL:
							break;
						default:
							break;
					}
				}
				return false;
			}
		});
		
		this.field.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			public void afterTextChanged(Editable s) {
				
				String value = s.toString();
				
				if(result.equals(value)){
					return;
				}
				
				try {
					result = formatter.returnMonetaryValue(value);
				} catch (MonetaryFormatException e) {
					e.printStackTrace();
				}
				
				field.setText(result);
				Selection.setSelection((Editable)field.getText(), result.length());
			}
		});
	}
	
	/**
	 * Retorna um valor BigDecimal de acordo com o valor no campo.
	 * @return
	 */
	public BigDecimal getValue() {
		String value = getContent().replaceAll("\\".concat(String.valueOf(MonetaryFormatter.POINTER)),"")
				.replace(String.valueOf(MonetaryFormatter.COMMA),String.valueOf(MonetaryFormatter.POINTER));
		
		value = value.trim();
		return new BigDecimal(value);
	}
	
	/**
	 * Inseri um valor de bigDecimal no campo.
	 * @param value
	 */
	public void setValue(BigDecimal value){
		String content = value.toString().replace(MonetaryFormatter.POINTER, MonetaryFormatter.COMMA); 
		this.field.setText(content);
	}
	
	/**
	 * Inseri uma string no campo de texto.
	 * @param value
	 */
	private void setText(String value){
		this.field.setText(value);
	}
	
	/**
	 * Retorna o conteudo do campo.
	 * @return
	 */
	public String getContent(){
		return this.field.getText().toString().trim();
	}
	
	/**
	 * Listener
	 * @param onKeyFormatter
	 */
	public void setOnKeyFormatter(OnKeyFormatter onKeyFormatter) {
		this.onKeyFormatter = onKeyFormatter;
	}
	
	/**
	 * Interface do componente MonetaryFormmater
	 * @author Rodrigo Simões Rosa.
	 *
	 */
	public interface OnKeyFormatter {
		public void onKeyEnter(BigDecimal value);
	}
}
