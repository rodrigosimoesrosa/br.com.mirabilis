package br.com.mirabilis.view.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import br.com.mirabilis.R;
import br.com.mirabilis.util.request.ResponseData;
import br.com.mirabilis.util.request.image.AsyncBitmapTask;
import br.com.mirabilis.util.request.listener.DelegateListener;

/**
 * {@link BitmapView} you may receive an image url or resource to an image embedded in the project.
 * With that she starts the loading, displaying and hiding the object {@link ProgressBar}
 * @author Rodrigo Simões Rosa
 */
public class BitmapView extends RelativeLayout {

	private Context context;
	private ProgressBar preloader;
	private ImageView imageView;
	private String url;
	private int imageResource;
	private int defaultImage;
	private ScaleType scaleType;
	private boolean useBuffer;
	private AsyncBitmapTask task;
	private ImageType type;
	
	/**
	 * Image types.
	 * @author Rodrigo Simões Rosa.
	 */
	public enum ImageType {
		RESOURCE, URL;
	}
	
	/**
	 * Boot block.
	 */
	{
		this.useBuffer = false;
	}
	
	/**
	 * Constructor.
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public BitmapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Constructor.
	 * @param context
	 * @param attrs
	 */
	public BitmapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * Constructor.
	 * @param context
	 */
	public BitmapView(Context context) {
		super(context);
	}
	
	/**
	 * Constructor that takes a url to represent an image to be downloaded.
	 * @param context
	 * @param url
	 * @param type
	 * @param useBuffer
	 */
	public BitmapView(Context context, String url, int defaultImage, ScaleType type, boolean useBuffer) {
		this(context,type, useBuffer);
		this.url = url;
		this.type = ImageType.URL;
		this.defaultImage = defaultImage;
	}
	
	/**
	 * Constructor that takes an id resource that represents an image to be inserted.
	 * @param context
	 * @param resource
	 * @param type
	 * @param useBuffer
	 */
	public BitmapView(Context context, int resource, ScaleType type, boolean useBuffer){
		this(context,type,useBuffer);
		this.type = ImageType.RESOURCE;
		this.imageResource = resource;
	}
	
	/**
	 * Constructor.
	 * @param context
	 * @param type
	 * @param useBuffer
	 */
	private BitmapView(Context context, ScaleType type, boolean useBuffer){
		this(context);
		this.scaleType = type;
		this.context = context;
		this.useBuffer = useBuffer;
		
		LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bitmap_view, this);
        this.initComponents();
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if(type.equals(ImageType.URL)){
			download();
		}else{
			delegate.onPosExecute(new ResponseData<Bitmap>(true, null, null));
		}
	}
	
	/**
	 * Performs start the download, using an object of type {@link AsyncBitmapTask},
	 * it makes part loading and delegates to the {@link BitmapView} inserting a {@link Bitmap} in object {@link BitmapView}
	 */
	private void download(){
		this.task = new AsyncBitmapTask(context, url, delegate, useBuffer);
		this.task.execute();
	}
	
	/**
	* Listener that will be responsible for receiving the response of the task and thus enter a {@link Bitmap} in {@link BitmapView#imageView}.
	*/ 
	private DelegateListener<Bitmap> delegate = new DelegateListener<Bitmap>() {
		
		public void onPosExecute(ResponseData<Bitmap> response) {
			preloader.setVisibility(View.GONE);
			if(response.isSuccessfully()){
				if(type.equals(ImageType.RESOURCE)){
					imageView.setImageResource(imageResource);	
				}else{
					imageView.setImageBitmap(response.getData());
				}
			}else{
				if(defaultImage == 0){
					imageView.setImageDrawable(getResources().getDrawable(R.drawable.unknow));
				}else{
					imageView.setImageDrawable(getResources().getDrawable(defaultImage));	
				}
			}
			imageView.setScaleType(scaleType);
		}
	};
	
	/**
	 * Initialization of components.
	 */
	private void initComponents(){
		this.preloader = (ProgressBar) this.findViewById(R.id.preloader);
		this.imageView = (ImageView) this.findViewById(R.id.imageView);
	}
}
