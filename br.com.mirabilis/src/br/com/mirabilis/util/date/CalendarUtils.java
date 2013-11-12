package br.com.mirabilis.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Class that manipulate {@link String} to {@link Calendar} and {@link Calendar} to {@link String}
 * @author Rodrigo Simões Rosa
 *
 */
public class CalendarUtils {

	/**
	 * Pattern default.
	 */
	private static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * Return {@link Calendar} object from {@link String} @param date.
	 * Use default pattern {@link #PATTERN_DEFAULT}
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public static Calendar getCalendar(String date) throws ParseException {
		return getCalendar(PATTERN_DEFAULT, date);
	}
	
	/**
	 * Return {@link Calendar} object from {@link String} @param date.
	 * @param date
	 * @param pattern Pattern to use in conversion.
	 * @return
	 * @throws ParseException 
	 */
	public static Calendar getCalendar(String pattern, String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(date));
		return calendar;
	}
	
	/**
	 * Return {@link String} object from {@link Calendar} @param calendar.
	 * Use default pattern {@link #PATTERN_DEFAULT}
	 * @param calendar
	 * @return
	 */
	public static String getString(Calendar calendar) {
		return getString(PATTERN_DEFAULT, calendar);
	}
	
	/**
	 * Return {@link String} object from {@link Calendar} @param calendar.
	 * @param pattern Pattern to use in conversion.
	 * @param calendar
	 * @return
	 */
	public static String getString(String pattern, Calendar calendar) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(calendar.getTime());
	}
}
