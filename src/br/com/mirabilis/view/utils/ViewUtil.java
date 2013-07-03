package br.com.mirabilis.view.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class ViewUtil {
	
	/**
	 * Generate a value suitable for use in {@link #setId(int)}.
	 * This value will not collide with ID values generated at build time by aapt for R.id.
	 *
	 * @return a generated ID value
	 */
	public static int generateViewId() {
		final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
	    for (;;) {
	        final int result = sNextGeneratedId.get();
	        // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
	        int newValue = result + 1;
	        
	        if (newValue > 0x00FFFFFF){
	        	newValue = 1; // Roll over to 1, not 0.
	        }
	        if (sNextGeneratedId.compareAndSet(result, newValue)) {
	            return result;
	        }
	    }
	}
}
