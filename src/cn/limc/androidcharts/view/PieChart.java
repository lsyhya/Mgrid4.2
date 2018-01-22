/**
 * 
 */
package cn.limc.androidcharts.view;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import cn.limc.androidcharts.entity.TitleValueColorEntity;

/**
 * @author limc
 * 
 */
public class PieChart extends View {

	////////////////榛樿灞烇拷?////////////
	
	/** 榛樿铔涚綉鑳屾櫙鑹�*/
	public static final String DEFAULT_TITLE = "Pie Chart";
	
	/** 榛樿鏄惁鏄剧ず铔涚綉缁忕嚎 */
	public static final boolean DEFAULT_DISPLAY_RADIUS = true;
	
	/** 榛樿鍗婂渾鍗婏拷?*/
	public static final int DEFAULT_RADIUS_LENGTH = 80;
	
	/** 榛樿缁忕嚎棰滆壊 */
	public static final int DEFAULT_RADIUS_COLOR = Color.WHITE;
	
	/** 榛樿锟�锟斤拷锟�锟斤拷鑹�*/
	public static final int DEFAULT_CIRCLE_BORDER_COLOR = Color.WHITE;
	
	/** 榛樿涓拷?锟斤拷缃�*/
	public static final Point DEFAULT_POSITION = new Point(0,0);
	
	// ///////////////灞烇拷?/////////////////

	/** 鍥捐〃鏁版嵁  */
	private List<TitleValueColorEntity> data;
	
	/** 鍥捐〃锟�锟�*/
	private String title = DEFAULT_TITLE;

	/** 缁樺浘浣嶇疆 */
	private Point position = DEFAULT_POSITION;
	
	/** 锟�锟斤拷鍗婏拷?*/
	private int radiusLength = DEFAULT_RADIUS_LENGTH;
	
	/** 缁忕嚎棰滆壊 */
	private int radiusColor = DEFAULT_RADIUS_COLOR;
	
	/** 缁忕嚎棰滆壊 */
	private int circleBorderColor = DEFAULT_CIRCLE_BORDER_COLOR;
	
	/** 鏄剧ず缁忕嚎 */
	private boolean displayRadius = DEFAULT_DISPLAY_RADIUS;
	

	// ////////////锟�?鍑芥暟/////////////////

	public PieChart(Context context) {
		super(context);
	}

	public PieChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


	public PieChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// ////////////鏂癸拷?///////////////////
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		//鑾峰緱瀹夛拷?楂樺害瀹藉害
		int rect = super.getWidth() > super.getHeight()? super.getHeight(): super.getWidth();
		
		//缁樺浘楂樺搴�
		radiusLength = (int)((rect / 2f) * 0.90); 
		
		position = new Point((int)(getWidth() / 2f),(int)(getHeight() / 2f));
		
		//缁樺埗鍥捐〃
		drawCircle(canvas);
		
		drawData(canvas);
	}
	
	/**
	 * 閲嶆柊鎺т欢澶э拷?
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else if (specMode == MeasureSpec.AT_MOST) {
			result = Math.min(result, specSize);
		}
		return result;
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else if (specMode == MeasureSpec.AT_MOST) {
			result = Math.min(result, specSize);
		}
		return result;
	}

	
	/**
	 * 缁樺埗澶栧洿锟�锟斤拷
	 * @param canvas
	 */
	protected void drawCircle(Canvas canvas){
	
		Paint mPaintCircleBorder =new Paint();
		mPaintCircleBorder.setColor(Color.WHITE);
		//濉拷?锟斤拷鍒惰竟锟�
		mPaintCircleBorder.setStyle(Style.STROKE);
		mPaintCircleBorder.setStrokeWidth(2);
		mPaintCircleBorder.setAntiAlias(true);
		
		//缁樺埗锟�锟斤拷
		canvas.drawCircle(position.x, position.y, radiusLength, mPaintCircleBorder);
	}
	
	/**
	 * 缁樺埗鏁版嵁
	 * 
	 * @param canvas
	 */
	protected void drawData(Canvas canvas) {
		if (null != data) {
			
			//鑾峰緱锟�鏁�
			float sum = 0;
			for (int i = 0; i < data.size(); i++) {
				sum = sum + data.get(i).getValue();
			}
			
			Paint mPaintFill = new Paint();
			mPaintFill.setStyle(Style.FILL);
			mPaintFill.setAntiAlias(true);
			
			Paint mPaintBorder = new Paint();
			mPaintBorder.setStyle(Style.STROKE);
			mPaintBorder.setColor(radiusColor);
			mPaintBorder.setAntiAlias(true);
			
			int offset = -90;
			// 閬嶅巻姣忥拷?锟斤拷鏁版嵁鍒楄〃
			for (int j = 0; j < data.size(); j++) {
				TitleValueColorEntity e = data.get(j);
				
				//锟�锟斤拷濉拷?锟斤拷
				mPaintFill.setColor(e.getColor());

				RectF oval = new RectF(position.x - radiusLength,
									   position.y - radiusLength,
									   position.x + radiusLength,
									   position.y + radiusLength);
				//瑙掑害
				int sweep = Math.round(e.getValue() / sum * 360f);
				//缁樺埗锟�锟斤拷
				canvas.drawArc(oval, offset, sweep, true, mPaintFill);
				//缁樺埗锟�锟斤拷
				canvas.drawArc(oval, offset, sweep, true, mPaintBorder);
				//锟�锟斤拷鍋忕Щ
				offset = offset + sweep;
			}
			
			float sumvalue = 0f;
			//
			for (int k = 0; k < data.size(); k++) {
				TitleValueColorEntity e = data.get(k);
				//鍊�
				float value = e.getValue();
				//娣伙拷?锟斤拷绉�
				sumvalue = sumvalue + value;
				//姣旓拷?
				float rate = (sumvalue - value /2)/ sum ;
				//锟�锟斤拷濉拷?锟斤拷
				mPaintFill.setColor(Color.BLUE);
				
				//鐧撅拷?锟�
				float percentage = (int)( value / sum * 10000) / 100f;
				
				float offsetX = (float) (position.x - radiusLength * 0.5 * Math.sin(rate * -2 * Math.PI ));
				float offsetY = (float) (position.y - radiusLength * 0.5 * Math.cos(rate * -2 * Math.PI ));
				
				
				Paint mPaintFont =new Paint();
				mPaintFont.setColor(Color.LTGRAY);
				
				//缁樺埗锟�锟�
				String title =e.getTitle();
				float  realx = 0;
				float  realy = 0;
				
				//閲嶆柊璁＄畻鍧愶拷?
				//TODO 璁＄畻绠楁硶鏃ュ悗瀹屽杽
				if(offsetX < position.x){
					realx = offsetX - mPaintFont.measureText(title) -5;
				}else if(offsetX > position.x){
					realx  = offsetX + 5;
				}
				
				if(offsetY > position.y){
					if(value / sum < 0.2f){
						realy = offsetY + 10;
					}else{
						realy = offsetY + 5;
					}
				}else if(offsetY < position.y){
					if(value / sum < 0.2f){
						realy = offsetY - 10;
					}else{
						realy = offsetY + 5;
					}
				}
				
								
				canvas.drawText(title ,realx , realy ,mPaintFont );
				
				canvas.drawText(String.valueOf(percentage)+ "%",realx,realy+12, mPaintFont);
				
			}
		}
	}

	///////////////Getter Setter////////////////
	
	public List<TitleValueColorEntity> getData() {
		return data;
	}

	public void setData(List<TitleValueColorEntity> data) {
		if (this.data != null)
			this.data.clear();
		this.data = data;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public int getRadiusLength() {
		return radiusLength;
	}

	public void setRadiusLength(int radiusLength) {
		this.radiusLength = radiusLength;
	}

	public int getRadiusColor() {
		return radiusColor;
	}

	public void setRadiusColor(int radiusColor) {
		this.radiusColor = radiusColor;
	}

	public int getCircleBorderColor() {
		return circleBorderColor;
	}

	public void setCircleBorderColor(int circleBorderColor) {
		this.circleBorderColor = circleBorderColor;
	}

	public boolean isDisplayRadius() {
		return displayRadius;
	}

	public void setDisplayRadius(boolean displayRadius) {
		this.displayRadius = displayRadius;
	}

}
