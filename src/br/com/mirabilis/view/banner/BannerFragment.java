package br.com.mirabilis.view.banner;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.mirabilis.R;
import br.com.mirabilis.view.gallery.horizontal.HorizontalScrollGallery;
import br.com.mirabilis.view.gallery.horizontal.HorizontalScrollGallery.OnScrollListener;

/**
 * {@link BannerFragment}, fragmento respons�vel pela exibi��o de imagens. Carregadas. Utiliza uma inst�ncia do objeto {@link HorizontalScrollGallery} para controlar o fluxo dos banners.
 * @author Rodrigo Sim�es Rosa
 */
public class BannerFragment extends Fragment {

	private Activity activity;
	private List<String> urls;
	private View view;
	private Timer timer;
	private HorizontalScrollGallery gallery;
	
	public static final int DELAY = 5000;
	
	private int layout;
	private int current;
	private int limit;
	private int delay;
	
	private boolean useBuffer;
	
	/**
	 * Bloco de inicializa��o.
	 */
	{
		this.delay = DELAY; 
		this.layout = R.layout.banner;
		this.useBuffer = false;
	}
	
	public BannerFragment(){}
	
	/**
	 * Construtor.
	 * @param activity
	 * @param layout
	 * @param urls
	 * @param useBuffer
	 */
	public BannerFragment(Activity activity, int layout, List<String> urls, boolean useBuffer){
		this(activity,urls,useBuffer);
		this.layout = layout;
	}
	
	/**
	 * Construtor.
	 * @param activity
	 * @param urls
	 * @param useBuffer
	 */
	public BannerFragment(Activity activity, List<String> urls, boolean useBuffer){
		this(activity, urls);
		this.useBuffer = useBuffer;
	}
	
	/**
	 * Construtor.
	 * @param activity
	 * @param urls
	 */
	public BannerFragment(Activity activity, List<String> urls) {
		this.urls = urls;
		this.activity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		this.view = inflater.inflate(this.layout, container,false);
		setParams();
		setListener();
		return this.view;
	}
	
	/**
	 * Seta os par�metros.
	 */
	private void setParams(){
		this.gallery = (HorizontalScrollGallery) this.view.findViewById(R.id.gallery);
	}
	
	/**
	 * Seta o listener.
	 */
	private void setListener(){
		this.gallery.setOnScrollListener(new OnScrollListener() {
			
			public void onViewScrollFinished(int currentPage) {
				Log.v("setOnScrollListener"," page : " + currentPage);
				current = currentPage;
			}
			
			public void onScroll(int scrollX) {}
		});
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		activity = getActivity();
		
		if(activity != null && urls != null){
			startUrls(this.urls, useBuffer);
		}
	}
	
	/**
	 * Seta as urls que ser�o inseridas no banner
	 * @param urls
	 * @param useBuffer
	 */
	public void startUrls(List<String> urls, boolean useBuffer){
		this.limit = urls.size();
		this.gallery.setUrls(urls, useBuffer);
		start();
	}
	
	/**
	 * Seta as views que ser�o inseridas no banner.
	 * @param views
	 */
	public void startViews(List<? extends View> views){
		this.limit = views.size();
		this.gallery.setViews(views);
		start();
	}
	
	/**
	 * Seta os resources que ser�o incluidos no banner.
	 * @param images
	 */
	public void startResources(int [] images){
		this.limit = images.length;
		this.gallery.setResources(images, useBuffer);
		start();
	}
	
	/**
	 * Tarefa de tempo que ser� inicializada atrav�s do {@link BannerFragment#timer}
	 */
	private TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
			activity.runOnUiThread(new Runnable() {
				
				public void run() {
					current ++;
					
					if(current == limit){
						current = 0;
					}
					gallery.setCurrentPage(current);		
				}
			});
		}
	};
	
	/**
	 * Inicializa o start utilizando {@link BannerFragment#task}
	 */
	private void start(){
		if(timer != null){
			timer.cancel();
		}
		timer = new Timer();
		timer.schedule(task, delay, delay);
	}
}
