package br.com.mirabilis.view.text;

import android.text.Editable;
import android.text.Selection;
import android.widget.EditText;

/**
 * Classe de controle de otimizações nos campos edição de texto.
 * @author Rodrigo Simões Rosa
 *
 */
public class FormatterEditText {
	
	/**
	 * Seta o posicionamento do cursor para o ultimo caracter do campo de texto.
	 * @param txtField
	 */
	public static void setFocusLastPosition(EditText txtField){
		int pos = txtField.getText().length();
		Editable editable = (Editable) txtField.getText();
		Selection.setSelection(editable, pos);
	}
}
