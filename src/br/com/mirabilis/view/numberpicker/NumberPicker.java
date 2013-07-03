package br.com.mirabilis.view.numberpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.mirabilis.R;
import br.com.mirabilis.view.listener.RepeatListener;

public class NumberPicker extends LinearLayout {

	private static final int DEFAULT_MIN_VALUE = 0;
	private static final int DEFAULT_MAX_VALUE = 100;
	private static final int DEFAULT_ID_LAYOUT = R.layout.def_number_picker;

	private int idLayout = DEFAULT_ID_LAYOUT;
	private int minValue = DEFAULT_MIN_VALUE;
	private int maxValue = DEFAULT_MAX_VALUE;

	private int currentValue;

	private TextView txtTarget;
	private View btnIncrement;
	private View btnDecrement;
	
	private Animation anim;

	public NumberPicker(Context context, AttributeSet attrs) {

		super(context, attrs);
		loadAttributeSet(attrs);
	}

	public NumberPicker(Context context) {

		super(context);
		inflate(context, idLayout, this);
	}

	private void loadAttributeSet(AttributeSet attrs) {

		TypedArray array = getContext().obtainStyledAttributes(attrs,
				R.styleable.NumberPickerAttribute);

		this.idLayout = array.getResourceId(
				R.styleable.NumberPickerAttribute_idLayout, DEFAULT_ID_LAYOUT);
		this.minValue = array.getInteger(
				R.styleable.NumberPickerAttribute_minValue, DEFAULT_MIN_VALUE);
		this.maxValue = array.getInteger(
				R.styleable.NumberPickerAttribute_maxValue, DEFAULT_MAX_VALUE);
		this.currentValue = array.getInteger(
				R.styleable.NumberPickerAttribute_currentValue, minValue);

		array.recycle();
	}

	@Override
	protected void onFinishInflate() {

		super.onFinishInflate();

		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		inflater.inflate(idLayout, this);
	}

	private void findComponents() {

		txtTarget = (TextView) findViewById(R.id.txtTarget);

		if (txtTarget instanceof EditText) {

			txtTarget.setInputType(InputType.TYPE_CLASS_NUMBER);
		}

		btnIncrement = findViewById(R.id.btnIncrement);
		btnDecrement = findViewById(R.id.btnDecrement);
	}

	@Override
	protected void onAttachedToWindow() {

		super.onAttachedToWindow();

		findComponents();

		if (!isValidValue(currentValue))
			currentValue = minValue;

		updateTextValue();
		setListeners();
	}

	private void setListeners() {

		RepeatListener repeatListener = new RepeatListener(400, 100,
				new OnClickListener() {

					public void onClick(View v) {

						if(anim != null)
							v.startAnimation(anim);
						
						if (v == btnIncrement) {

							increment();
						} else if (v == btnDecrement) {

							decrement();
						}
					}
				});

		btnIncrement.setOnTouchListener(repeatListener);
		btnDecrement.setOnTouchListener(repeatListener);

		if (txtTarget instanceof EditText) {

			txtTarget.addTextChangedListener(new TextWatcher() {

				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

					String text = s.toString();

					if (text.length() > 0) {

						if (!text.equals(Integer.toString(currentValue))) {

							int newValue = Integer.parseInt(text);

							if (isValidValue(newValue))
								currentValue = newValue;

							updateTextValue();
						}
					} else {
						currentValue = minValue;
						updateTextValue();
					}

					Selection.setSelection(txtTarget.getEditableText(),
							txtTarget.getText().length());
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				public void afterTextChanged(Editable s) {
				}
			});
		}

		txtTarget.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus())
					Selection.setSelection(txtTarget.getEditableText(),
							txtTarget.getText().length());
			}
		});
	}

	private void decrement() {

		if (currentValue > minValue) {

			currentValue--;
			updateTextValue();
		}
	}

	private void increment() {

		if (currentValue < maxValue) {

			currentValue++;
			updateTextValue();
		}
	}

	private boolean isValidValue(int value) {

		return value >= minValue && value <= maxValue;
	}

	private void updateTextValue() {

		if (txtTarget != null)
			txtTarget.setText(Integer.toString(currentValue));
	}

	public void setMinValue(int value) {

		if (value <= maxValue) {

			minValue = value;

			if (currentValue < minValue) {

				currentValue = minValue;
				updateTextValue();
			}
		}
	}

	public void setMaxValue(int value) {

		if (value >= minValue) {

			maxValue = value;

			if (currentValue > maxValue) {

				currentValue = maxValue;
				updateTextValue();
			}
		}
	}

	public void setCurrentValue(int value) {

		if (isValidValue(value)) {

			currentValue = value;
			updateTextValue();
		}
	}

	/**
	 * Retorna o valor atual do NumberPicker.
	 * 
	 * @return
	 */
	public int getCurrentValue() {
		return this.currentValue;
	}

	@Override
	public void setEnabled(boolean enabled) {

		txtTarget.setEnabled(enabled);
		btnDecrement.setEnabled(enabled);
		btnIncrement.setEnabled(enabled);
	}

	@Override
	public void setClickable(boolean clickable) {

		txtTarget.setClickable(clickable);
		btnDecrement.setClickable(clickable);
		btnIncrement.setClickable(clickable);
	}
	
	public void setButtonsAnimation(Animation anim){
		
		this.anim = anim;
	}
}
