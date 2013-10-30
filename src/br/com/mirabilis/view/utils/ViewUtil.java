package br.com.mirabilis.view.utils;

import java.util.concurrent.atomic.AtomicInteger;

import android.view.View;

/**
 * Class utils for {@link View}.
 * 
 * @author Rodrigo Simões Rosa
 */
public final class ViewUtil {

	/**
	 * Generate a value suitable for use in {@link #setId(int)}. This value will
	 * not collide with ID values generated at build time by aapt for R.id.
	 * 
	 * @return a generated ID value
	 */
	public final static int generateViewId() {
		AtomicInteger nextGeneratedID = new AtomicInteger(1);

		while (true) {
			int result = nextGeneratedID.get();

			int value = result + 1;

			if (value > 0x00FFFFFF) {
				value = 1;
			}

			if (nextGeneratedID.compareAndSet(result, value)) {
				return result;
			}
		}
	}
}
