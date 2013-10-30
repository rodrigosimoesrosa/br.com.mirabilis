package br.com.mirabilis.view.text;

import android.text.Editable;
import android.text.Selection;
import android.widget.EditText;

/**
 * {@link EditText} utils
 * 
 * @author Rodrigo Simões Rosa.
 * 
 */
public final class EditTextUtils {

	/**
	 * Arrow positioning the cursor to the last character of the text field.
	 * 
	 * @param txtField
	 */
	public final static void setFocusToLastPosition(EditText txtField) {
		int pos = txtField.getText().length();
		Editable editable = (Editable) txtField.getText();
		Selection.setSelection(editable, pos);
	}
}
