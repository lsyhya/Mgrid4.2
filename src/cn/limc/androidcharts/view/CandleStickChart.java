package cn.limc.androidcharts.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import cn.limc.androidcharts.entity.OHLCEntity;


public class CandleStickChart extends GridChart {
	
	////////////榛樿鍊�///////////////
	/** 鏄剧ず绾嚎鏁� */
	public static final int DEFAULT_LATITUDE_NUM = 4;
	
	/** 鏄剧ず绾嚎鏁� */
	public static final int DEFAULT_LONGTITUDE_NUM = 3;
	
	/** 闃崇嚎杈癸拷?锟斤拷鑹� */
	public static final int DEFAULT_POSITIVE_STICK_BORDER_COLOR = Color.RED;
	
	/** 闃崇嚎濉拷?锟斤拷鑹� */
	public static final int DEFAULT_POSITIVE_STICK_FILL_COLOR = Color.RED;
	
	/** 闃寸嚎杈癸拷?锟斤拷鑹� */
	public static final int DEFAULT_NEGATIVE_STICK_BORDER_COLOR = Color.GREEN;
	
	/** 闃寸嚎濉拷?锟斤拷鑹� */
	public static final int DEFAULT_NEGATIVE_STICK_FILL_COLOR = Color.GREEN;
	
	/** 鍗佸瓧绾块鑹� */
	public static final int DEFAULT_CROSS_STICK_COLOR = DEFAULT_POSITIVE_STICK_BORDER_COLOR;

	////////////灞烇拷?鍒楄〃/////////////////

	/** 闃崇嚎棰滆壊 */
	private int positiveStickBorderColor = DEFAULT_POSITIVE_STICK_BORDER_COLOR ;

	/** 闃崇嚎濉拷?锟斤拷鑹� */
	private int positiveStickFillColor = DEFAULT_POSITIVE_STICK_FILL_COLOR;

	/** 闃寸嚎棰滆壊 */
	private int negativeStickBorderColor = DEFAULT_NEGATIVE_STICK_BORDER_COLOR;

	/** 闃寸嚎濉拷?锟斤拷鑹� */
	private int negativeStickFillColor = DEFAULT_NEGATIVE_STICK_FILL_COLOR;

	/** 鍗佸瓧绾块鑹� */
	private int crossStickColor = DEFAULT_CROSS_STICK_COLOR;
	
	/** 鏄剧ず绾嚎鏁� */
	private int latitudeNum = DEFAULT_LATITUDE_NUM;
	
	/** 鏄剧ず缁忕嚎鏁� */
	private int longtitudeNum = DEFAULT_LONGTITUDE_NUM;
	
	/** K绾挎暟鎹� */
	private List<OHLCEntity> OHLCData;
	
	/** 鍥捐〃涓拷?锟斤拷铚＄儧绾� */
	private int maxCandleSticksNum;

	/** K绾挎樉绀猴拷?锟斤拷浠锋牸 */
	private float maxPrice = 0;

	/** K绾挎樉绀猴拷?锟斤拷浠锋牸 */
	private float minPrice = 0;	
	
	/////////////////锟�??鍑芥暟///////////////

	public CandleStickChart(Context context) {
		super(context);
	}

	public CandleStickChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CandleStickChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	///////////////鍑芥暟鏂癸拷?////////////////

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		initAxisY();
		initAxisX();
		super.onDraw(canvas);

		drawCandleSticks(canvas);
	}

	/**
	 * 鑾峰彇X杞村埢搴︿綅缃�,锟�?锟�??锟芥渶澶�1
	 * @param value
	 * @return
	 */
	@Override
	public String getAxisXGraduate(Object value){
		float graduate = Float.valueOf(super.getAxisXGraduate(value));
		int index = (int) Math.floor(graduate*maxCandleSticksNum);
		
		if(index >= maxCandleSticksNum){
			index = maxCandleSticksNum -1;
		}else if(index < 0){
			index = 0;
		}
		
		//杩斿洖X杞村��
		return String.valueOf(OHLCData.get(index).getDate());
	}
	
	public int getSelectedIndex() {
		if(null == super.getTouchPoint()){
			return 0;
		}
		float graduate = Float.valueOf(super.getAxisXGraduate(super.getTouchPoint().x));
		int index = (int) Math.floor(graduate*maxCandleSticksNum);
		
		if(index >= maxCandleSticksNum){
			index = maxCandleSticksNum -1;
		}else if(index < 0){
			index = 0;
		}
		
		return index;
	}
	
	/**
	 * 鑾峰彇Y杞村埢搴︿綅缃�,锟�?锟�??锟芥渶澶�1
	 * @param value
	 * @return
	 */
	@Override
	public String getAxisYGraduate(Object value){
		float graduate = Float.valueOf(super.getAxisYGraduate(value));
		return  String.valueOf((int)Math.floor(graduate * (maxPrice - minPrice) + minPrice));
	}
	
	/**
	 * 澶氱偣瑙︽帶浜嬩欢
	 */
	protected void drawWithFingerMove() {
	}
	
	/**
	 * 鍒濆鍖朮杞�
	 */
	protected void initAxisX() {
		List<String> TitleX = new ArrayList<String>();
		if(null != OHLCData){
			float average = maxCandleSticksNum / longtitudeNum;
			//锟�?锟斤拷鍒诲害
			for (int i = 0; i < longtitudeNum; i++) {
				int index = (int) Math.floor(i * average);
				if(index > maxCandleSticksNum-1){
					index = maxCandleSticksNum-1;
				}
				//杩斤拷??锟�?
				TitleX.add(String.valueOf(OHLCData.get(index).getDate()).substring(4));
			}
			TitleX.add(String.valueOf(OHLCData.get(maxCandleSticksNum-1).getDate()).substring(4));
		}
		super.setAxisXTitles(TitleX);
	}
	
	/**
	 * 鍒濆鍖朰杞�
	 */
	protected void initAxisY() {
		List<String> TitleY = new ArrayList<String>();
		float average = (int)((maxPrice - minPrice) / latitudeNum)/10 * 10;
		//锟�?锟斤拷鍒诲害
		for (int i = 0; i < latitudeNum; i++) {
			String value = String.valueOf((int)Math.floor(minPrice + i * average));
			if(value.length() < super.getAxisYMaxTitleLength()){
				while(value.length() < super.getAxisYMaxTitleLength()){
					value = new String(" ") + value;
				}
			}
			TitleY.add(value);
		}
		//锟�?锟斤拷锟�?锟斤拷鍊�
		String value = String.valueOf((int)Math.floor(((int)maxPrice) / 10 * 10));
		if(value.length() < super.getAxisYMaxTitleLength()){
			while(value.length() < super.getAxisYMaxTitleLength()){
				value = new String(" ") + value;
			}
		}
		TitleY.add(value);

		super.setAxisYTitles(TitleY);
	}

	/**
	 * 缁樺埗铚＄儧绾�
	 * @param canvas
	 */
	protected void drawCandleSticks(Canvas canvas) {
		// 铚＄儧妫掑搴�
		float stickWidth = ((super.getWidth() - super.getAxisMarginLeft()-super.getAxisMarginRight()) / maxCandleSticksNum) - 1;
		// 铚＄儧妫掕捣濮嬬粯鍒朵綅缃�
		float stickX = super.getAxisMarginLeft() + 1;

		Paint mPaintPositive = new Paint();
		mPaintPositive.setColor(positiveStickFillColor);

		Paint mPaintNegative = new Paint();
		mPaintNegative.setColor(negativeStickFillColor);
		
		Paint mPaintCross = new Paint();
		mPaintCross.setColor(crossStickColor);

		if(null !=  OHLCData){
			for (int i = 0; i < OHLCData.size(); i++) {
				OHLCEntity ohlc = OHLCData.get(i);
				float openY = (float) ((1f - (ohlc.getOpen() - minPrice)
						/ (maxPrice - minPrice)) * (super.getHeight() - super
						.getAxisMarginBottom()) - super.getAxisMarginTop());
				float highY = (float) ((1f - (ohlc.getHigh() - minPrice)
						/ (maxPrice - minPrice)) * (super.getHeight() - super
						.getAxisMarginBottom()) - super.getAxisMarginTop());
				float lowY = (float) ((1f - (ohlc.getLow() - minPrice)
						/ (maxPrice - minPrice)) * (super.getHeight() - super
						.getAxisMarginBottom()) - super.getAxisMarginTop());
				float closeY = (float) ((1f - (ohlc.getClose() - minPrice)
						/ (maxPrice - minPrice)) * (super.getHeight() - super
						.getAxisMarginBottom()) - super.getAxisMarginTop());
	
				// 锟�?锟斤拷鍜岀敓浜绾夸腑锟�?锟斤拷绾垮拰闃崇嚎
				if (ohlc.getOpen() < ohlc.getClose()) {
				//闃崇嚎
					//鏍规嵁瀹藉害鍒ゆ柇鏄惁缁樺埗绔嬫煴
					if(stickWidth >= 2f){
						canvas.drawRect(stickX, closeY, stickX + stickWidth, openY,
								mPaintPositive);
					}
					canvas.drawLine(stickX + stickWidth / 2f, highY, stickX
							+ stickWidth / 2f, lowY, mPaintPositive);
				} else if (ohlc.getOpen() > ohlc.getClose()) {
				//闃寸嚎
					//鏍规嵁瀹藉害鍒ゆ柇鏄惁缁樺埗绔嬫煴
					if(stickWidth >= 2f){
						canvas.drawRect(stickX, openY, stickX + stickWidth, closeY,
								mPaintNegative);
					}
					canvas.drawLine(stickX + stickWidth / 2f, highY, stickX
							+ stickWidth / 2f, lowY, mPaintNegative);
				} else {
				//鍗佸瓧绾�
					//鏍规嵁瀹藉害鍒ゆ柇鏄惁缁樺埗妯嚎
					if(stickWidth >= 2f){
						canvas.drawLine(stickX, closeY, stickX + stickWidth, openY,
								mPaintCross);
					}
					canvas.drawLine(stickX + stickWidth / 2f, highY, stickX
							+ stickWidth / 2f, lowY, mPaintCross);
				}
	
				//X浣嶇Щ
				stickX = stickX + 1 + stickWidth;
			}
		}
	}
	
	//Push鏁版嵁缁樺埗K绾垮浘
	public void pushData(OHLCEntity entity){
		if(null != entity){
			//杩斤拷?锟斤拷鎹埌鏁版嵁鍒楄〃
			addData(entity);
			//寮哄埗閲嶏拷?
			super.postInvalidate();
		}
	}
	
	public void addData(OHLCEntity entity){
		if(null != entity){
			//杩斤拷?锟斤拷鎹�
			if(null == OHLCData || 0==OHLCData.size()){
				OHLCData = new ArrayList<OHLCEntity>();
				this.minPrice = ((int)entity.getLow()) / 10 * 10;
				this.maxPrice = ((int)entity.getHigh()) / 10 * 10;
			}
			
			this.OHLCData.add(entity);
			
			if (this.minPrice > entity.getLow()){
				this.minPrice = ((int)entity.getLow()) / 10 * 10;
			}
			
			if (this.maxPrice < entity.getHigh()){
				this.maxPrice = 10 + ((int)entity.getHigh()) / 10 * 10;
			}
			
			if(OHLCData.size() > maxCandleSticksNum){
				maxCandleSticksNum = maxCandleSticksNum +1;
			}
		}
	}
	
	
	
	//////////////灞烇拷?GetterSetter/////////////////
	
	public List<OHLCEntity> getOHLCData() {
		return OHLCData;
	}

	public void setOHLCData(List<OHLCEntity> data) {
		//锟�?锟斤拷宸叉湁鏁版嵁
		if(null != OHLCData){
			OHLCData.clear();
		}
		for(OHLCEntity e :data){
			addData(e);
		}
	}
	
	public int getPositiveStickBorderColor() {
		return positiveStickBorderColor;
	}

	public void setPositiveStickBorderColor(int positiveStickBorderColor) {
		this.positiveStickBorderColor = positiveStickBorderColor;
	}

	public int getPositiveStickFillColor() {
		return positiveStickFillColor;
	}

	public void setPositiveStickFillColor(int positiveStickFillColor) {
		this.positiveStickFillColor = positiveStickFillColor;
	}

	public int getNegativeStickBorderColor() {
		return negativeStickBorderColor;
	}

	public void setNegativeStickBorderColor(int negativeStickBorderColor) {
		this.negativeStickBorderColor = negativeStickBorderColor;
	}

	public int getNegativeStickFillColor() {
		return negativeStickFillColor;
	}

	public void setNegativeStickFillColor(int negativeStickFillColor) {
		this.negativeStickFillColor = negativeStickFillColor;
	}

	public int getCrossStickColor() {
		return crossStickColor;
	}

	public void setCrossStickColor(int crossStickColor) {
		this.crossStickColor = crossStickColor;
	}

	public int getLatitudeNum() {
		return latitudeNum;
	}

	public void setLatitudeNum(int latitudeNum) {
		this.latitudeNum = latitudeNum;
	}

	public int getMaxCandleSticksNum() {
		return maxCandleSticksNum;
	}

	public void setMaxCandleSticksNum(int maxCandleSticksNum) {
		this.maxCandleSticksNum = maxCandleSticksNum;
	}

	public float getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(float maxPrice) {
		this.maxPrice = maxPrice;
	}

	public float getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(float minPrice) {
		this.minPrice = minPrice;
	}

	public int getLongtitudeNum() {
		return longtitudeNum;
	}

	public void setLongtitudeNum(int longtitudeNum) {
		this.longtitudeNum = longtitudeNum;
	}

	
	private final int NONE = 0;
	private final int ZOOM = 1;
	private final int DOWN = 2;
	
	private float olddistance = 0f;
	private float newdistance = 0f;
	
	
	private int TOUCH_MODE;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		final float MIN_LENGTH = (super.getWidth()/40)<5?5:(super.getWidth()/50);
		
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 璁剧疆鎷栨媺妯★拷?
		case MotionEvent.ACTION_DOWN:
			TOUCH_MODE = DOWN;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			TOUCH_MODE = NONE;
			return super.onTouchEvent(event);
		// 璁剧疆澶氱偣瑙︽懜妯★拷?
		case MotionEvent.ACTION_POINTER_DOWN:
			olddistance = spacing(event);
			if (olddistance > MIN_LENGTH) {
				TOUCH_MODE = ZOOM;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if(TOUCH_MODE == ZOOM){
				newdistance = spacing(event);
				if (newdistance > MIN_LENGTH && Math.abs(newdistance - olddistance) > MIN_LENGTH) {
					
					if(newdistance > olddistance){
						zoomIn();
					}else{
						zoomOut();
					}
					//閲嶇疆璺濈
					olddistance = newdistance;
										
					super.postInvalidate();
					super.notifyEventAll(this);
				}
			}
			break;
		}
		return true;
	}
	
	protected void zoomIn(){
		if(maxCandleSticksNum > 10){
			maxCandleSticksNum = maxCandleSticksNum -3;
		}
	}
	
	protected void zoomOut(){
		if(maxCandleSticksNum < OHLCData.size()-1){
			maxCandleSticksNum = maxCandleSticksNum +3;
		}
	}

	// 璁＄畻绉诲姩璺濈
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float)Math.sqrt(x * x + y * y);
	} 
	
}
