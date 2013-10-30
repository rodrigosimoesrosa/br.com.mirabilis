package br.com.mirabilis.view.text.formatter;

import br.com.mirabilis.view.text.exception.MonetaryFormatterException;

/**
 * Formatter class data type String. Aims to format a String to format monetary.
 * 
 * @author Rodrigo Simões Rosa
 */
public final class MonetaryFormatter {

	public static final int DEFAULT_LIMIT = 2;
	private int limit;

	/**
	 * Enumeration that has {@link MonetaryCharacters}.
	 * 
	 * @author Rodrigo Simões Rosa.
	 * 
	 */
	public enum MonetaryCharacters {
		COMMA(","), ZEROS("00"), ZERO("0"), POINTER(".");

		private String value;

		private MonetaryCharacters(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}

		public char toChar() {
			return value.charAt(0);
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param limit
	 *            Limit for that string according to the monetary standard.
	 */
	public MonetaryFormatter(int limit) {
		this.limit = limit;
	}

	/**
	 * Returns the monetary value.
	 * 
	 * @param value
	 * @return
	 * @throws MonetaryFormatterException
	 */
	public String getMonetary(String value) throws MonetaryFormatterException {
		return getMonetary(value, limit);
	}

	/**
	 * Returns the formatted value type money.
	 * 
	 * @param value
	 *            Valor
	 * @param limit
	 * @return
	 * @throws MonetaryFormatterException
	 */
	public static String getMonetary(String value, int limit)
			throws MonetaryFormatterException {
		try {
			char key = value.charAt(value.length() - 1);
			StringBuilder beforeCommon;
			StringBuilder afterCommon;

			value = value
					.substring(0, (value.length() - 1))
					.replaceAll(
							"\\".concat(String
									.valueOf(MonetaryCharacters.POINTER
											.toString())), "").trim();

			String[] temp = value.split(String.valueOf(MonetaryCharacters.COMMA
					.toString()));
			String before = temp[0];

			if (temp.length > 1) {
				String after = temp[1];
				if (after.length() == MonetaryCharacters.ZEROS.toString()
						.length()) {
					if (before.length() == limit) {
						temp = value.split(String
								.valueOf(MonetaryCharacters.COMMA.toString()));
						beforeCommon = new StringBuilder(temp[0]);
						afterCommon = new StringBuilder(temp[1]);
						insertPointerValue(beforeCommon);
						return beforeCommon
								.append(MonetaryCharacters.COMMA.toString())
								.append(afterCommon).toString();
					}
				}
			}

			beforeCommon = new StringBuilder();
			afterCommon = new StringBuilder();
			boolean comma = false;

			for (int i = value.length(); i > 0; i--) {
				try {
					char keyTemp = value.charAt(i - 1);
					if (keyTemp != MonetaryCharacters.COMMA.toChar()) {

						if (comma) {
							if (beforeCommon.length() < limit) {
								beforeCommon.append(keyTemp);
							}
						} else {
							if (afterCommon.length() < 1) {
								afterCommon.append(keyTemp);
							} else {
								comma = true;
								if (beforeCommon.length() < limit) {
									beforeCommon.append(keyTemp);
								}
							}
						}
					}
				} catch (StringIndexOutOfBoundsException e) {
					throw new MonetaryFormatterException(
							"An error has occurred in formatting!");
				}
			}
			afterCommon.reverse();
			beforeCommon.reverse();

			if (beforeCommon.toString().equals("")) {
				beforeCommon.append(MonetaryCharacters.ZERO.toString());
			}

			beforeCommon.replace(0, beforeCommon.length(),
					Integer.valueOf(beforeCommon.toString()).toString());

			insertPointerValue(beforeCommon);
			afterCommon.append(key);
			return beforeCommon.append(MonetaryCharacters.COMMA.toString())
					.append(afterCommon).toString();
		} catch (StringIndexOutOfBoundsException e) {
			throw new MonetaryFormatterException(
					"Unable to retrieve the digit input");
		}
	}

	/**
	 * Inserts the points according to the thousands.
	 * 
	 * @param value
	 */
	public static void insertPointerValue(StringBuilder value) {
		int count = 0;
		value.reverse();
		for (int j = 0; j < value.length(); j++) {
			count++;
			if (count == 4) {
				count = 0;
				value.insert(j, MonetaryCharacters.POINTER.toString());
			}
		}
		value.reverse();
	}
}
