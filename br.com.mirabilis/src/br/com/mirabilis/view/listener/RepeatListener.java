package br.com.mirabilis.view.listener;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

/**
 * Classe de repeti��o de OnClickListener, utilizada at� a vers�o 14 do Android, aonde tem como objetivo disparar o onClickListener enquanto o View estiver sendo pressionado.
 * @author Rodrigo Sim�es Rosa & Anderson Lasak.
 */
public class RepeatListener implements OnTouchListener {
	
	private MotionEvent currentMotionEvent;
	private int initialInterval;
	private final int normalInterval;
	private final OnClickListener clickListener;
	private View currenView;
	private Handler handler = new Handler();
	
	/**
	 * Runnable respons�vel pela itera��o das repeti��es.
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
	 * Listener para repeti��o de chamadas onClickListener.
	 * @param initialInterval Tempo para iniciar a repeti��o.
	 * @param normalInterval Tempo em que a repeti��o ocorre.
	 * @param clickListener OnClickListener que ser� executado nas repeti��es.
	 */
	public RepeatListener(int initialInterval, int normalInterval, OnClickListener clickListener) {
		if (clickListener == null) throw new IllegalArgumentException("null runnable");

		if (initialInterval < 0 || normalInterval < 0){
			throw new IllegalArgumentException("Adicione intervalos v�lidos");
	    }
		
		this.initialInterval = initialInterval;
	    this.normalInterval = normalInterval;
	    this.clickListener = clickListener;
	}

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

