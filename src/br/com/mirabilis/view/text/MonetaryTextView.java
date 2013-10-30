package br.com.mirabilis.view.text;

import java.math.BigDecimal;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.TextView;
import br.com.mirabilis.view.text.exception.MonetaryFormatterException;
import br.com.mirabilis.view.text.formatter.MonetaryFormatter;
import br.com.mirabilis.view.text.formatter.MonetaryFormatter.MonetaryCharacters;

/**
 * Class properties monetary specifies for text fields (TextView).
 * 
 * @author Rodrigo Simões Rosa
 */
public final class MonetaryTextView {

	private TextView field;
	private String result;
	private MonetaryKeyFormatterListener monetaryKeyFormatterListener;
	private MonetaryFormatter formatter;

	/**
	 * boot block
	 */
	{
		this.result = "";
	}

	/**
	 * Constructor.
	 * 
	 * @param field
	 *            Field that receive properties monetary.
	 */
	public MonetaryTextView(TextView field) {
		this(field, MonetaryFormatter.DEFAULT_LIMIT);
	}

	/**
	 * Constructor
	 * 
	 * @param field
	 *            Field that receive properties monetary.
	 * @param limit
	 *            Limit from field.
	 */
	public MonetaryTextView(TextView field, int limit) {
		this.field = field;
		this.formatter = new MonetaryFormatter(limit);
		setText(String.valueOf(MonetaryCharacters.ZERO.toString())
				+ String.valueOf(MonetaryCharacters.COMMA.toString())
				+ MonetaryCharacters.ZEROS.toString());
		Selection.setSelection(field.getEditableText(), getContent().length());
		setListeners();
	}

	/**
	 * Set listeners.
	 */
	private void setListeners() {

		this.field.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_ENTER:
						if (monetaryKeyFormatterListener != null) {
							monetaryKeyFormatterListener.onKeyEnter(getValue());
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

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {

				String value = s.toString();

				if (result.equals(value)) {
					return;
				}

				try {
					result = formatter.getMonetary(value);
				} catch (MonetaryFormatterException e) {
					e.printStackTrace();
				}

				field.setText(result);
				Selection.setSelection((Editable) field.getText(),
						result.length());
			}
		});
	}

	/**
	 * Return {@link BigDecimal} from {@link #field}.
	 * 
	 * @return
	 */
	public BigDecimal getValue() {
		String value = getContent().replaceAll(
				"\\".concat(String.valueOf(MonetaryCharacters.POINTER
						.toString())), "").replace(
				String.valueOf(MonetaryCharacters.COMMA.toString()),
				String.valueOf(MonetaryCharacters.POINTER.toString()));

		value = value.trim();
		return new BigDecimal(value);
	}

	/**
	 * Set {@link BigDecimal} inside {@link #field}.
	 * 
	 * @param value
	 */
	public void setValue(BigDecimal value) {
		String content = value.toString().replace(
				MonetaryCharacters.POINTER.toString(),
				MonetaryCharacters.COMMA.toString());
		this.field.setText(content);
	}

	/**
	 * Set content inside {@link #field}.
	 * 
	 * @param value
	 */
	private void setText(String value) {
		this.field.setText(value);
	}

	/**
	 * Return content field.
	 * 
	 * @return
	 */
	public String getContent() {
		return this.field.getText().toString().trim();
	}

	/**
	 * Set listener.
	 * 
	 * @param listener
	 */
	public void setMonetaryKeyFormatterListener(
			MonetaryKeyFormatterListener listener) {
		this.monetaryKeyFormatterListener = listener;
	}

	/**
	 * Listener of {@link MonetaryTextView}.
	 * 
	 * @author Rodrigo Simões Rosa.
	 * 
	 */
	public interface MonetaryKeyFormatterListener {

		/**
		 * Receive value {@link BigDecimal}.
		 * 
		 * @param value
		 */
		public void onKeyEnter(BigDecimal value);
	}
}
