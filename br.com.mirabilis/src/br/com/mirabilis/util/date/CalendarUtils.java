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
	 * Return {@link Calendar} object from {@link String} @param date.
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public static Calendar getCalendar(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(date));
		return calendar;
	}
	
	/**
	 * Return {@link String} object from {@link Calendar} @param calendar.
	 * @param calendar
	 * @return
	 */
	public static String getString(Calendar calendar) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(calendar.getTime());
	}
}
