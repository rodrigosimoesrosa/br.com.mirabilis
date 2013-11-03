package br.com.mirabilis.view.listener;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

/**
 * Class repetition OnClickListener, used until version 14 Android, where the trigger aims onClickListener while the View is being pressed.
 * @author Rodrigo Simões Rosa & Anderson Lasak.
 */
public class RepeatListener implements OnTouchListener {
	
	private MotionEvent currentMotionEvent;
	private int initialInterval;
	private final int normalInterval;
	private final OnClickListener clickListener;
	private View currenView;
	private Handler handler = new Handler();
	
	/**
	 * Runnable responsible iteration of repetitions.
	 */
	private Runnable runnable = new Runnable() {
		public void run() {
			if(currentMotionEvent.getAction() == MotionEvent.ACTION_DOWN || currentMotionEvent.getAction() == MotionEvent.ACTION_MOVE){
				handler.postDelayed(this, normalInterval);
				clickListener.onClick(currenView);
			}
		}
	};

	/**
	 * Listener to repeat calls onClickListener.
	 * @param initialInterval Time to start repeating.
	 * @param normalInterval Time repetition occurs.
	 * @param clickListener OnClickListener that will run in repeats.
	 */
	public RepeatListener(int initialInterval, int normalInterval, OnClickListener clickListener) {
		if (clickListener == null) throw new IllegalArgumentException("null runnable");

		if (initialInterval < 0 || normalInterval < 0){
			throw new IllegalArgumentException("Adicione intervalos válidos");
	    }
		
		this.initialInterval = initialInterval;
	    this.normalInterval = normalInterval;
	    this.clickListener = clickListener;
	}

	/**
	 * OnTouch
	 */
	public boolean onTouch(View view, MotionEvent motionEvent) {
		this.currentMotionEvent = motionEvent;	
		switch (motionEvent.getAction()) {
	    	case MotionEvent.ACTION_DOWN:
	    		handler.removeCallbacks(runnable);
		        handler.postDelayed(runnable, initialInterval);
		        currenView = view;
		        clickListener.onClick(view);
		        break;
		        
	    	case MotionEvent.ACTION_UP:
	    		handler.removeCallbacks(runnable);
	    		currenView = null;
	            break;
	    }
	    return false;
	}
}

