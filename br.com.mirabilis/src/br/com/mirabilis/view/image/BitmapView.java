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
 * {@link BitmapView} que pode receber uma url de imagem ou um resource de uma imagem inserida no projeto. 
 * Com isso ela inicializa o carregamento, exibição, e ocultação do objeto {@link ProgressBar}
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
	 * Tipos de imagem.
	 * @author Rodrigo Simões Rosa.
	 */
	public enum ImageType {
		RESOURCE, URL;
	}
	
	/**
	 * Bloco de inicialização.
	 */
	{
		this.useBuffer = false;
	}
	
	/**
	 * Construtor.
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public BitmapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Construtor.
	 * @param context
	 * @param attrs
	 */
	public BitmapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * Construtor.
	 * @param context
	 */
	public BitmapView(Context context) {
		super(context);
	}
	
	/**
	 * Construtor que recebe uma url que representará uma imagem a ser baixada.
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
	 * Construtor que recebe um id resource que representará uma imagem a ser inserida.
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
	 * Construtor.
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
	 * Realiza o inicio do download, utilizando um objeto do tipo {@link AsyncBitmapTask},
	 * que faz a parte do carregamento e delega para o {@link BitmapView} a inserção de um {@link Bitmap} no objeto {@link BitmapView}
	 */
	private void download(){
		this.task = new AsyncBitmapTask(context, url, delegate, useBuffer);
		this.task.execute();
	}
	
	/**
	 * Listener que será responsável por receber a resposta da tarefa e assim inserir um {@link Bitmap} no {@link BitmapView#imageView}.
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
	 * Inicialização de componentes.
	 */
	private void initComponents(){
		this.preloader = (ProgressBar) this.findViewById(R.id.preloader);
		this.imageView = (ImageView) this.findViewById(R.id.imageView);
	}
}
