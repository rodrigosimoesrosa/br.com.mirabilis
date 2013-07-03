package br.com.mirabilis.view.gallery.horizontal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.R.bool;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Scroller;
import br.com.mirabilis.R;
import br.com.mirabilis.view.image.BitmapView;

/**
 * {@link HorizontalScrollGallery}, objeto que controla o fluxo e a exibição de itens do tipo {@link View}, através de um {@link Scroller}
 * @author Rodrigo Simões Rosa
 */
public class HorizontalScrollGallery extends ViewGroup {
	
	/**
     * Velocidade do efeito fling durante a transição de uma view para outra.
     */
    private final static int FLING_VELOCITY = 200;
    
	private final static int INVALID_SCREEN = -1;
    private final static int SPEC_UNDEFINED = -1;
    private final static int TOUCH_STATE_REST = 0;
    private final static int TOUCH_STATE_SCROLLING = 1;
	private static final int POSITION_TO_CHANGE = 2;
	private static final int DELAY = 500;
	
    private boolean firstLayout;
    private boolean allowLongPress;
    
    private Context context;
    private Scroller scroller;
    private VelocityTracker velocityTracker;
    
    /**
     * Tipo de redimensionamento de imagem.
     */
    private ScaleType scaleType;
    private Set<OnScrollListener> onScrollListener;
	private List<? extends View> views;
	
    private int currentPage;
    private int nextPage;
    private int touchSlop;
    private int maximumVelocity;
    private int delay;
    private int fling;
    private int touchState;
    private int widthView;
    private int heightView;
    private int pageWidthSpec, pageWidth, pageHeightSpec, pageHeight;
    
    private float lastMotionX;
    private float lastMotionY;
    
    /**
     * Bloco de inicialização.
     */
    {
    	this.onScrollListener = new HashSet<OnScrollListener>();
    	this.scaleType = ImageView.ScaleType.FIT_XY;
    	this.touchState = TOUCH_STATE_REST;
    	this.delay = DELAY;
    	this.fling = FLING_VELOCITY;
    	this.firstLayout = true;
    	this.nextPage = INVALID_SCREEN;
    }

    /**
     * Construtor que será chamado quando o objeto for inserido no XML.
     * @param context Context informado pela atividade.
     * @param attrs atributos inflados quando informados no xml.
     */
    public HorizontalScrollGallery(Context context, AttributeSet attrs) {
    	this(context, attrs, 0);
    }

    /**
     * Construtor que será chamado quando o objeto for inserido no XML.
     * @param context Context informado pela atividade.
     * @param attrs atributos inflados quando informados no xml.
     * @param defStyle não utilizado.
     */
    public HorizontalScrollGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HorizontalScrollGallery);
        this.pageWidthSpec = typedArray.getDimensionPixelSize(R.styleable.HorizontalScrollGallery_pageWidth, SPEC_UNDEFINED);
        this.pageHeightSpec = typedArray.getDimensionPixelSize(R.styleable.HorizontalScrollGallery_pageHeight, SPEC_UNDEFINED);
        typedArray.recycle();
    }
    
    /**
     * Inicializado ao ser adicionado na tela.
     */
    @Override
    protected void onAttachedToWindow() {
    	super.onAttachedToWindow();
    	init();
    }
    
    /**
     * Inicializa a galeria.
     */
    public void init() {
    	this.scroller = new Scroller(context);
    	this.currentPage = 0;
        ViewConfiguration config = ViewConfiguration.get(this.context);
        this.touchSlop = config.getScaledTouchSlop();
        this.maximumVelocity = config.getScaledMaximumFlingVelocity();
    }

    /**
     * Retorna uma lista de objetos que extends {@link View}
     * @return
     */
    public List<? extends View> getViews() {
		return views;
	}
    
    /**
     * Retorna o index da página em execução.
     * @return
     */
    public int getCurrentPage() {
        return this.currentPage;
    }
    
    /**
     * Seta a página atual. 
     * @param page
     */
    public void setCurrentPage(int page) {
        this.currentPage = Math.max(0, Math.min(page, this.getChildCount()));
        scrollTo(getScrollXForPage(this.currentPage), 0);
        this.invalidate();
    }

    /**
     * Remove all views.
     */
    @Override
    public void removeAllViews() {
    	this.views = new ArrayList<View>();
    	super.removeAllViews();
    }
    
    /**
     * Seta uma lista de objetos do tipo ou subtipo {@link View}
     * @param views
     */
    private <T extends View> void addViews(List<HorizontalScrollGalleryItem<T>> views){
    	for(HorizontalScrollGalleryItem<T> t : views){
    		this.addView(t);
    	}
    }
    
    /**
     * Seta uma lista {@link List} de objetos do tipo ou subtipo {@link View}, para a inicialização da {@link HorizontalScrollGallery}
     * @param views
     */
    public <T extends View> void setViews(List<T> views){
    	this.views = views;
    	addViews(getGalleryItens(views));
    }
    
    /**
     * Seta urls para downloads e criação de itens personalizados.
     * @param urls
     * @param useBuffer
     * @param defaultImage
     */
    public void setUrls(List<String> urls, boolean useBuffer, int defaultImage){
    	this.removeAllViews();
    	List<BitmapView> bmps = new ArrayList<BitmapView>();
    	for(String url : urls){
    		BitmapView b = new BitmapView(this.context, url, defaultImage, scaleType, useBuffer);
    		bmps.add(b);
    	}
    	setViews(bmps);
    }
    
    /**
     * Seta um  array de inteiros que correspondem as imagens a serem inseridas.
     * @param list
     */
    public void setResources(int[] images, boolean useBuffer){
    	this.removeAllViews();
    	List<BitmapView> bmps = new ArrayList<BitmapView>();
    	for(int i=0 ;i < images.length ; i++){
    		BitmapView b = new BitmapView(this.context,images[i],scaleType, useBuffer);
    		bmps.add(b);
    	}
    	setViews(bmps);
    }
    
    /**
     * Cria os objetos necessários para serem adicionados na galeria.
     * @param views
     * @return Retorna uma lista de {@link HorizontalScrollGalleryItem}
     */
    private <T extends View> List<HorizontalScrollGalleryItem<T>> getGalleryItens(List<T> views){
    	List<HorizontalScrollGalleryItem<T>> itens = new ArrayList<HorizontalScrollGalleryItem<T>>();
    	
    	for(T t : views){
    		HorizontalScrollGalleryItem<T> i = new HorizontalScrollGalleryItem<T>(context, t);
    		itens.add(i);
    	}
    	
    	return itens;
    }

    /**
     * Obtém o valor que {@link HorizontalScrollGallery#getScrollX()} deve retornar se a página especificada e é a página atual.
     * @param whichPage
     * @return
     */
    private int getScrollXForPage(int whichPage) {
        return (whichPage * pageWidth) - pageWidthPadding();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        } else if (nextPage != INVALID_SCREEN) {
            currentPage = nextPage;
            nextPage = INVALID_SCREEN;
            clearChildrenCache();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        long drawingTime = getDrawingTime();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            drawChild(canvas, getChildAt(i), drawingTime);
        }

        for (OnScrollListener listener : onScrollListener) {
            int adjustedScrollX = getScrollX() + pageWidthPadding();
            /**
             * dispará um listener para o onScroll.
             */
            if (adjustedScrollX % pageWidth == 0) {
                listener.onViewScrollFinished(adjustedScrollX / pageWidth);
            }
        }
    }

    /**
     * Retorna o valor padding da view.
     * @return
     */
    public int pageWidthPadding() {
        return ((getMeasuredWidth() - pageWidth) / 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        pageHeight = pageHeightSpec == SPEC_UNDEFINED ? getMeasuredHeight() : pageHeightSpec;
        pageHeight = Math.min(pageHeight, getMeasuredHeight());
        
        pageWidth = pageWidthSpec == SPEC_UNDEFINED ? getMeasuredWidth() : pageWidthSpec;
        pageWidth = Math.min(pageWidth, getMeasuredWidth());

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
        	getChildAt(i).measure(MeasureSpec.makeMeasureSpec(pageWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
        }

        if (firstLayout) {
            scrollTo(getScrollXForPage(currentPage), 0);
            firstLayout = false;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childLeft = 0;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                int childWidth = child.getMeasuredWidth();
                child.layout(childLeft, 0, childLeft + childWidth, child.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        int screen = indexOfChild(child);	
        
        if (screen != currentPage || !scroller.isFinished()) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int focusableScreen;
        if (nextPage != INVALID_SCREEN) {
            focusableScreen = nextPage;
        } else {
            focusableScreen = currentPage;
        }
        if(previouslyFocusedRect != null){
        	this.getChildAt(focusableScreen).requestFocus(direction, previouslyFocusedRect);	
        }
        return false;
    }

    @Override
    public boolean dispatchUnhandledMove(View focused, int direction) {
        if (direction == View.FOCUS_LEFT) {
            if (getCurrentPage() > 0) {
                snapToPage(getCurrentPage() - 1);
                return true;
            }
        } else if (direction == View.FOCUS_RIGHT) {
            if (getCurrentPage() < getChildCount() - 1) {
                snapToPage(getCurrentPage() + 1);
                return true;
            }
        }
        return super.dispatchUnhandledMove(focused, direction);
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction) {
        getChildAt(currentPage).addFocusables(views, direction);
        if (direction == View.FOCUS_LEFT) {
            if (currentPage > 0) {
                getChildAt(currentPage - 1).addFocusables(views, direction);
            }
        } else if (direction == View.FOCUS_RIGHT){
            if (currentPage < getChildCount() - 1) {
                getChildAt(currentPage + 1).addFocusables(views, direction);
            }
        }
    }

    /**
     * OnIterceptTouchEvent foi reescrito para interceptar o moviemento Touch e assim determinar uma ação customizada.
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
    	int action = motionEvent.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (touchState != TOUCH_STATE_REST)) {
            return true;
        }
        
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
            	
            	/**
            	 * Verificar se o usuário passou longe o suficiente de seu toque.
            	 */
                if (touchState == TOUCH_STATE_REST) {
                    checkStartScroll(x, y);
                }

                break;

            case MotionEvent.ACTION_DOWN:
                
            	/**
                 * Posicionamento do action_down. 
                 */
                lastMotionX = x;
                lastMotionY = y;
                allowLongPress = true;

                /**
                 * Estado do toque do usuário na tela.
                 */
                touchState = scroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                /**
                 * libera o touch.
                 */
                clearChildrenCache();
                touchState = TOUCH_STATE_REST;
                break;
        }

        /**
         * A única vez que queremos para interceptar eventos de movimento é se estamos no
         * O modo de arrastar.
         */
        return touchState != TOUCH_STATE_REST;
    }

    private void checkStartScroll(float x, float y) {
		
    	/**
		 * Calcula a diferença entre a ultima posição e a posição atual do touch.
		 */
        int diffX = (int) Math.abs(x - lastMotionX);
        int diffY = (int) Math.abs(y - lastMotionY);

        boolean xMoved = diffX > touchSlop;
        boolean yMoved = diffY > touchSlop;

        
        if (xMoved || yMoved) {

            if (xMoved) {
            	/**
            	* Scroll se o usuário moveu longe o suficiente ao longo do eixo X. 
            	*/
            	touchState = TOUCH_STATE_SCROLLING;
                enableChildrenCache();
            }
            
            /**
             * Cancela o longpress pendente. 
             */
            if (allowLongPress) {
                allowLongPress = false;
                View currentScreen = getChildAt(currentPage);
                currentScreen.cancelLongPress();
            }
        }
    }

    private void enableChildrenCache() {
        setChildrenDrawingCacheEnabled(true);
        setChildrenDrawnWithCacheEnabled(true);
    }

    private void clearChildrenCache() {
        setChildrenDrawnWithCacheEnabled(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        
        velocityTracker.addMovement(ev);

        int action = ev.getAction();
        float x = ev.getX();
        float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                lastMotionX = x;
                
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchState == TOUCH_STATE_REST) {
                    checkStartScroll(x, y);
                } else if (touchState == TOUCH_STATE_SCROLLING) {
                    int deltaX = (int) (lastMotionX - x);
                    lastMotionX = x;
                    if (getScrollX() < 0 || getScrollX() > getChildAt(getChildCount() - 1).getLeft()) {
                        deltaX /= 2;
                    }
                    scrollBy(deltaX, 0);
                }
                
                break;
            case MotionEvent.ACTION_UP:
                if (this.touchState == TOUCH_STATE_SCROLLING) {
                    VelocityTracker velocity = this.velocityTracker;
                    velocity.computeCurrentVelocity(delay, this.maximumVelocity);
                    int velocityX = (int) velocity.getXVelocity();

                    if (velocityX > fling && this.currentPage > 0) {
                        // Fling hard enough to move left
                    	snapToPage(this.currentPage - 1);
                    } else if (velocityX < -fling && this.currentPage < getChildCount() - 1) {
                        // Fling hard enough to move right
                    	snapToPage(this.currentPage + 1);
                    } else {
                    	snapToDestination();
                    }

                    if (velocityTracker != null) {
                        velocityTracker.recycle();
                        velocityTracker = null;
                    }
                }
                this.touchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_CANCEL:
                this.touchState = TOUCH_STATE_REST;
        }
        return true;
    }
    
    private void snapToDestination() {
    	int startX = getScrollXForPage(this.currentPage);
        int whichPage = this.currentPage;
        if (getScrollX() < startX - getWidth() / POSITION_TO_CHANGE) {
            whichPage = Math.max(0, whichPage-1);
        } else if (getScrollX() > startX + getWidth() / POSITION_TO_CHANGE) {
        	whichPage = Math.min(getChildCount()-1, whichPage+1);
        }
        snapToPage(whichPage);
    }

    private void snapToPage(int whichPage) {
    	//TODO checar como é mudado os itens e se é possível criar uma galeria circular.
        enableChildrenCache();

        boolean changingPages = whichPage != this.currentPage;

        this.nextPage = whichPage;
        
        View focusedChild = getFocusedChild();
        if (focusedChild != null && changingPages && focusedChild == getChildAt(this.currentPage)) {
            focusedChild.clearFocus();
        }

        int newX = getScrollXForPage(whichPage);
        int delta = newX - getScrollX();
        this.scroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);
        invalidate();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final SavedState state = new SavedState(super.onSaveInstanceState());
        state.currentScreen = this.currentPage;
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (savedState.currentScreen != INVALID_SCREEN) {
            this.currentPage = savedState.currentScreen;
        }
    }

    /**
     * ScrollLeft
     */
    public void scrollLeft() {
        if (this.nextPage == INVALID_SCREEN && this.currentPage > 0 && this.scroller.isFinished()) {
            snapToPage(this.currentPage - 1);
        }
    }

    /**
     * ScrollRight
     */
    public void scrollRight() {
    	if (this.nextPage == INVALID_SCREEN && this.currentPage < getChildCount() - 1 && this.scroller.isFinished()) {
            snapToPage(this.currentPage + 1);
        }
    }

    /**
     * Retorna a tela da view.
     * @param v
     * @return
     */
    public int getScreenForView(View v) {
        int result = -1;
        if (v != null) {
            ViewParent vp = v.getParent();
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                if (vp == getChildAt(i)) {
                    return i;
                }
            }
        }
        return result;
    }
    
    public ScaleType getScaleType() {
		return scaleType;
	}

	public void setScaleType(ScaleType scaleType) {
		this.scaleType = scaleType;
	}

	public int getWidthView() {
		return widthView;
	}

	public void setWidthView(int widthView) {
		this.widthView = widthView;
	}

	public int getHeightView() {
		return heightView;
	}

	public void setHeightView(int heightView) {
		this.heightView = heightView;
	}


    /**
     * @return {@link bool} is long presses are still allowed for the current touch
     */
    public boolean allowLongPress() {
        return allowLongPress;
    }

    public static class SavedState extends BaseSavedState {
        private int currentScreen = -1;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.currentScreen = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.currentScreen);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
            new Parcelable.Creator<SavedState>() {
                public SavedState createFromParcel(Parcel in) {
                    return new SavedState(in);
                }

                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
    }
    
    /**
     * Adiciona listener de scroll da galeria.
     * @param listener
     */
    public void setOnScrollListener(OnScrollListener listener) {
        onScrollListener.add(listener);
    }

    /**
     * Remove aquele especifico listener de scroll da galeria.
     * @param listener
     */
    public void removeOnScrollListener(OnScrollListener listener) {
        onScrollListener.remove(listener);
    }

    /**
     * Implement to receive events on scroll position and page snaps.
     */
    public static interface OnScrollListener {
    	
        /**
         * Receives the current scroll X value.  This value will be adjusted to assume the left edge of the first
         * page has a scroll position of 0.  Note that values less than 0 and greater than the right edge of the
         * last page are possible due to touch events scrolling beyond the edges.
         * @param scrollX Scroll X value
         */
        public void onScroll(int scrollX);

        /**
         * Invoked when scrolling is finished (settled on a page, centered).
         * @param currentPage The current page
         */
        public void onViewScrollFinished(int currentPage);
    }
}
