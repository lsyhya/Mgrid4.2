package cn.limc.androidcharts.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 鍧愶拷?杞翠娇鐢ㄧ殑View
 * 
 * @author limc
 * 
 */
public class GridChart extends View implements IViewConst, ITouchEventNotify,ITouchEventResponse {

	// ////////////榛樿鍊�///////////////
	/** 榛樿鑳屾櫙鑹�*/
	public static final int DEFAULT_BACKGROUD_COLOR = Color.BLACK;

	/** 榛樿X鍧愶拷?杞撮鑹�*/
	public static final int DEFAULT_AXIS_X_COLOR = Color.RED;

	/** 榛樿Y鍧愶拷?杞撮鑹�*/
	public static final int DEFAULT_AXIS_Y_COLOR = Color.RED;

	/** 榛樿缁忕嚎棰滆壊 */
	public static final int DEFAULT_LONGITUDE_COLOR = Color.RED;

	/** 榛樿绾嚎棰滆壊 */
	public static final int DEFAULT_LAITUDE_COLOR = Color.RED;

	/** 榛樿杞寸嚎宸﹁竟锟�*/
	public static final float DEFAULT_AXIS_MARGIN_LEFT = 42f;

	/** 榛樿杞寸嚎搴曡竟锟�*/
	public static final float DEFAULT_AXIS_MARGIN_BOTTOM = 16f;

	/** 榛樿杞寸嚎涓婅竟锟�*/
	public static final float DEFAULT_AXIS_MARGIN_TOP = 5f;

	/** 榛樿杞寸嚎鍙宠竟锟�*/
	public static final float DEFAULT_AXIS_MARGIN_RIGHT = 5f;

	/** 榛樿缁忕嚎鏄惁鏄剧ず鍒诲害 */
	public static final boolean DEFAULT_DISPLAY_LONGTITUDE = Boolean.TRUE;

	/** 榛樿缁忕嚎鏄惁浣跨敤铏氱嚎 */
	public static final boolean DEFAULT_DASH_LONGTITUDE = Boolean.TRUE;

	/** 榛樿绾嚎鏄惁鏄剧ず鍒诲害 */
	public static final boolean DEFAULT_DISPLAY_LATITUDE = Boolean.TRUE;

	/** 榛樿绾嚎鏄惁浣跨敤铏氱嚎 */
	public static final boolean DEFAULT_DASH_LATITUDE = Boolean.TRUE;

	/** 榛樿鏄惁鏄剧ずX杞村埢搴�*/
	public static final boolean DEFAULT_DISPLAY_AXIS_X_TITLE = Boolean.TRUE;

	/** 榛樿鏄惁鏄剧ずX杞村埢搴�*/
	public static final boolean DEFAULT_DISPLAY_AXIS_Y_TITLE = Boolean.TRUE;

	/** 榛樿鏄惁鏄剧ず杈癸拷?*/
	public static final boolean DEFAULT_DISPLAY_BORDER = Boolean.TRUE;

	/** 榛樿鏄惁鏄剧ず杈癸拷?*/
	public static final int DEFAULT_BORDER_COLOR = Color.RED;

	/** 榛樿缁忕嚎鍒诲害瀛椾綋棰滆壊 **/
	private int DEFAULT_LONGTITUDE_FONT_COLOR = Color.WHITE;

	/** 榛樿缁忕嚎鍒诲害瀛椾綋棰滆壊 **/
	private int DEFAULT_LONGTITUDE_FONT_SIZE = 12;

	/** 榛樿缁忕嚎鍒诲害瀛椾綋棰滆壊 **/
	private int DEFAULT_LATITUDE_FONT_COLOR = Color.RED;;

	/** 榛樿缁忕嚎鍒诲害瀛椾綋棰滆壊 **/
	private int DEFAULT_LATITUDE_FONT_SIZE = 12;

	/** 榛樿Y杞达拷?锟斤拷鍒诲害锟�锟斤拷鏄剧ず闀垮害 */
	private int DEFAULT_AXIS_Y_MAX_TITLE_LENGTH = 5;

	/** 榛樿铏氱嚎鏁堟灉 */
	public static final PathEffect DEFAULT_DASH_EFFECT = new DashPathEffect(
			new float[] { 3, 3, 3, 3 }, 1);

	/** 鍦ㄦ帶浠惰鐐瑰嚮鏃�锟芥樉绀哄崄瀛楋拷?锟�锟斤拷绾�*/
	public static final boolean DEFAULT_DISPLAY_CROSS_X_ON_TOUCH = true;

	/** 鍦ㄦ帶浠惰鐐瑰嚮鏃�锟芥樉绀哄崄瀛楋拷?锟�锟斤拷绾�*/
	public static final boolean DEFAULT_DISPLAY_CROSS_Y_ON_TOUCH = true;

	/**
	 * // /////////////灞烇拷?////////////////
	 * 
	 * /** 鑳屾櫙鑹�
	 */
	private int backgroudColor = DEFAULT_BACKGROUD_COLOR;

	/** 鍧愶拷?杞碭棰滆壊 */
	private int axisXColor = DEFAULT_AXIS_X_COLOR;

	/** 鍧愶拷?杞碮棰滆壊 */
	private int axisYColor = DEFAULT_AXIS_Y_COLOR;

	/** 缁忕嚎棰滆壊 */
	private int longitudeColor = DEFAULT_LONGITUDE_COLOR;

	/** 绾嚎棰滆壊 */
	private int latitudeColor = DEFAULT_LAITUDE_COLOR;

	/** 杞寸嚎宸﹁竟锟�*/
	private float axisMarginLeft = DEFAULT_AXIS_MARGIN_LEFT;

	/** 杞寸嚎搴曡竟锟�*/
	private float axisMarginBottom = DEFAULT_AXIS_MARGIN_BOTTOM;

	/** 杞寸嚎涓婅竟锟�*/
	private float axisMarginTop = DEFAULT_AXIS_MARGIN_TOP;

	/** 杞寸嚎鍙宠竟锟�*/
	private float axisMarginRight = DEFAULT_AXIS_MARGIN_RIGHT;

	/** 缁忕嚎鏄惁鏄剧ず */
	private boolean displayAxisXTitle = DEFAULT_DISPLAY_AXIS_X_TITLE;

	/** 缁忕嚎鏄惁鏄剧ず */
	private boolean displayAxisYTitle = DEFAULT_DISPLAY_AXIS_Y_TITLE;

	/** 缁忕嚎鏄惁鏄剧ず */
	private boolean displayLongitude = DEFAULT_DISPLAY_LONGTITUDE;

	/** 缁忕嚎鏄惁浣跨敤铏氱嚎 */
	private boolean dashLongitude = DEFAULT_DASH_LONGTITUDE;

	/** 绾嚎鏄惁鏄剧ず */
	private boolean displayLatitude = DEFAULT_DISPLAY_LATITUDE;

	/** 绾嚎鏄惁浣跨敤铏氱嚎 */
	private boolean dashLatitude = DEFAULT_DASH_LATITUDE;

	/** 铏氱嚎鏁堟灉 */
	private PathEffect dashEffect = DEFAULT_DASH_EFFECT;

	/** 鏄剧ず杈癸拷?*/
	private boolean displayBorder = DEFAULT_DISPLAY_BORDER;

	/** 杈癸拷?锟斤拷鑹�*/
	private int borderColor = DEFAULT_BORDER_COLOR;

	/** 缁忕嚎鍒诲害瀛椾綋棰滆壊 **/
	private int longtitudeFontColor = DEFAULT_LONGTITUDE_FONT_COLOR;

	/** 缁忕嚎鍒诲害瀛椾綋棰滆壊 **/
	private int longtitudeFontSize = DEFAULT_LONGTITUDE_FONT_SIZE;

	/** 缁忕嚎鍒诲害瀛椾綋棰滆壊 **/
	private int latitudeFontColor = DEFAULT_LATITUDE_FONT_COLOR;

	/** 缁忕嚎鍒诲害瀛椾綋棰滆壊 **/
	private int latitudeFontSize = DEFAULT_LATITUDE_FONT_SIZE;

	/** 妯酱鍒诲害锟�锟�*/
	private List<String> axisXTitles;

	/** 绾佃酱鍒诲害锟�锟�*/
	private List<String> axisYTitles;

	/** 绾佃酱鍒诲害锟�锟斤拷瀛楃鏁�*/
	private int axisYMaxTitleLength = DEFAULT_AXIS_Y_MAX_TITLE_LENGTH;

	/** 鍦ㄦ帶浠惰鐐瑰嚮鏃�锟芥樉绀哄崄瀛楃珫绾�*/
	private boolean displayCrossXOnTouch = DEFAULT_DISPLAY_CROSS_X_ON_TOUCH;

	/** 鍦ㄦ帶浠惰鐐瑰嚮鏃�锟芥樉绀哄崄瀛楁í绾跨嚎 */
	private boolean displayCrossYOnTouch = DEFAULT_DISPLAY_CROSS_Y_ON_TOUCH;

	/** 閫変腑浣嶇疆X鍧愶拷? */
	private float clickPostX = 0f;

	/** 閫変腑浣嶇疆X鍧愶拷? */
	private float clickPostY = 0f;

	/** 閫氱煡瀵硅薄鍒楄〃 */
	private List<ITouchEventResponse> notifyList;
	
	/** 褰撳墠琚�涓拷?锟斤拷 */
	private PointF touchPoint;

	// ////////////锟�?鏂癸拷?//////////////
	public GridChart(Context context) {
		super(context);
	}

	public GridChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public GridChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// //////////////鏂癸拷?//////////////
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 璁剧疆鑳屾櫙鑹�
		super.setBackgroundColor(backgroudColor);

		// 缁樺埗XY杞�
		drawXAxis(canvas);
		drawYAxis(canvas);

		// 缁樺埗杈癸拷?
		if (this.displayBorder) {
			drawBorder(canvas);
		}

		// 缁樺埗缁忕嚎绾嚎
		if (displayLongitude || displayAxisXTitle) {
			drawAxisGridX(canvas);
		}
		if (displayLatitude || displayAxisYTitle) {
			drawAxisGridY(canvas);
		}

		// 缁樺埗鍗佸瓧鍧愶拷?
		if (displayCrossXOnTouch || displayCrossYOnTouch) {
			drawWithFingerClick(canvas);
		}
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
	 * 澶卞幓鐒︾偣浜嬩欢
	 */
	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
	}

	/**
	 * 瑙︽懜浜嬩欢
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getY() > 0
				&& event.getY() < super.getBottom() - getAxisMarginBottom()
				&& event.getX() > super.getLeft() + getAxisMarginLeft()
				&& event.getX() < super.getRight()) {

			/*
			 * 鍒ゅ畾鐢ㄦ埛鏄惁瑙︽懜鍒帮拷?锟斤拷锟�濡傛灉鏄崟鐐硅Е鎽稿垯锟�锟斤拷缁樺埗鍗佸瓧绾�濡傛灉鏄�鐐硅Е鎺у垯锟�锟斤拷K绾挎斁澶�
			 */
			if (event.getPointerCount() == 1) {
				// 鑾峰彇鐐瑰嚮鍧愶拷?
				clickPostX = event.getX();
				clickPostY = event.getY();
				
				PointF point = new PointF(clickPostX,clickPostY);
				touchPoint = point;
				// super.invalidate();
				super.invalidate();

				// 閫氱煡锟�锟斤拷鍏朵粬锟�鑱擟hart
				notifyEventAll(this);

			} else if (event.getPointerCount() == 2) {
			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 缁樺埗鍗婏拷?鏄庢枃鏈拷?
	 * 
	 * @param ptStart
	 * @param ptEnd
	 * @param content
	 * @param fontSize
	 * @param canvas
	 */

	private void drawAlphaTextBox(PointF ptStart, PointF ptEnd, String content,
			int fontSize, Canvas canvas) {

		Paint mPaintBox = new Paint();
		mPaintBox.setColor(Color.BLACK);
		mPaintBox.setAlpha(80);
		

		Paint mPaintBoxLine = new Paint();
		mPaintBoxLine.setColor(Color.CYAN);
		mPaintBoxLine.setAntiAlias(true);

		// 缁樺埗鐭╁舰濉拷?
		canvas.drawRoundRect(new RectF(ptStart.x, ptStart.y, ptEnd.x, ptEnd.y),
				20.0f, 20.0f, mPaintBox);

		// 缁樺埗鐭╁舰锟�
		canvas.drawLine(ptStart.x, ptStart.y, ptStart.x, ptEnd.y,mPaintBoxLine);
		canvas.drawLine(ptStart.x, ptEnd.y, ptEnd.x, ptEnd.y, mPaintBoxLine);
		canvas.drawLine(ptEnd.x, ptEnd.y, ptEnd.x, ptStart.y, mPaintBoxLine);
		canvas.drawLine(ptEnd.x, ptStart.y, ptStart.x, ptStart.y,mPaintBoxLine);

		// 缁樺埗锟�锟斤拷
		canvas.drawText(content, ptStart.x, ptEnd.y, mPaintBoxLine);
	}

	/**
	 * 鑾峰彇X杞村埢搴︼拷??,锟�锟�?锟芥渶澶�
	 * 
	 * @param value
	 * @return
	 */
	public String getAxisXGraduate(Object value) {

		float length = super.getWidth() - axisMarginLeft - 2 * axisMarginRight;
		float valueLength = ((Float) value).floatValue() - axisMarginLeft
				- axisMarginRight;

		return String.valueOf(valueLength / length);
	}

	/**
	 * 鑾峰彇Y杞村埢搴︼拷??,锟�锟�?锟芥渶澶�
	 * 
	 * @param value
	 * @return
	 */
	public String getAxisYGraduate(Object value) {

		float length = super.getHeight() - axisMarginBottom - 2 * axisMarginTop;
		float valueLength = length
				- (((Float) value).floatValue() - axisMarginTop);

		return String.valueOf(valueLength / length);
	}

	/**
	 * 鍗曠偣鍑讳簨浠�
	 */
	protected void drawWithFingerClick(Canvas canvas) {
		Paint mPaint = new Paint();
		mPaint.setColor(Color.CYAN);

		// 姘村钩绾块暱搴�
		float lineHLength = getWidth() - 2f;
		// 鍨傜洿绾块珮搴�
		float lineVLength = getHeight() - 2f;

		// 缁樺埗妯旱绾�
		if (isDisplayAxisXTitle()) {
			lineVLength = lineVLength - axisMarginBottom;

			if (clickPostX > 0 && clickPostY > 0) {
				// 缁樺埗X杞达拷?锟斤拷锟�
				if (displayCrossXOnTouch) {
					// TODO 锟�锟斤拷锟�锟斤拷灏忔帶鍒讹拷?锟�锟斤拷
					PointF BoxVS = new PointF(clickPostX - longtitudeFontSize
							* 5f / 2f, lineVLength + 2f);
					PointF BoxVE = new PointF(clickPostX + longtitudeFontSize
							* 5f / 2f, lineVLength + axisMarginBottom - 1f);

					// 缁樺埗锟�锟斤拷锟�
					drawAlphaTextBox(BoxVS, BoxVE,
							getAxisXGraduate(clickPostX), longtitudeFontSize,
							canvas);
				}
			}
		}

		if (isDisplayAxisYTitle()) {
			lineHLength = lineHLength - getAxisMarginLeft();

			if (clickPostX > 0 && clickPostY > 0) {
				// 缁樺埗Y杞达拷?锟斤拷锟�
				if (displayCrossYOnTouch) {
					PointF BoxHS = new PointF(1f, clickPostY - latitudeFontSize
							/ 2f);
					PointF BoxHE = new PointF(axisMarginLeft, clickPostY
							+ latitudeFontSize / 2f);

					// 缁樺埗锟�锟斤拷锟�
					drawAlphaTextBox(BoxHS, BoxHE,
							getAxisYGraduate(clickPostY), latitudeFontSize,
							canvas);
				}
			}
		}

		if (clickPostX > 0 && clickPostY > 0) {
			// 鏄剧ず绾电嚎
			if (displayCrossXOnTouch) {
				canvas
						.drawLine(clickPostX, 1f, clickPostX, lineVLength,
								mPaint);
			}

			// 鏄剧ず妯嚎
			if (displayCrossYOnTouch) {
				canvas.drawLine(axisMarginLeft, clickPostY, axisMarginLeft
						+ lineHLength, clickPostY, mPaint);
			}
		}
	}

	/**
	 * 缁樺埗杈癸拷?
	 * 
	 * @param canvas
	 */
	protected void drawBorder(Canvas canvas) {
		float width = super.getWidth() - 2;
		float height = super.getHeight() - 2;

		Paint mPaint = new Paint();
		mPaint.setColor(borderColor);

		// 缁樺埗杈癸拷?锟斤拷
		canvas.drawLine(1f, 1f, 1f + width, 1f, mPaint);
		canvas.drawLine(1f + width, 1f, 1f + width, 1f + height, mPaint);
		canvas.drawLine(1f + width, 1f + height, 1f, 1f + height, mPaint);
		canvas.drawLine(1f, 1f + height, 1f, 1f, mPaint);
	}

	/**
	 * 缁樺埗X杞�
	 * 
	 * @param canvas
	 */
	protected void drawXAxis(Canvas canvas) {

		float length = super.getWidth();
		float postY = super.getHeight() - axisMarginBottom - 1;

		Paint mPaint = new Paint();
		mPaint.setColor(axisXColor);

		canvas.drawLine(0f, postY, length, postY, mPaint);

	}

	/**
	 * 缁樺埗Y杞�
	 * 
	 * @param canvas
	 */
	protected void drawYAxis(Canvas canvas) {

		float length = super.getHeight() - axisMarginBottom;
		float postX = axisMarginLeft + 1;

		Paint mPaint = new Paint();
		mPaint.setColor(axisXColor);

		canvas.drawLine(postX, 0f, postX, length, mPaint);
	}

	/**
	 * 缁樺埗缁忕嚎
	 * 
	 * @param canvas
	 */
	protected void drawAxisGridX(Canvas canvas) {

		if (null != axisXTitles) {

			int counts = axisXTitles.size();
			float length = super.getHeight() - axisMarginBottom;
			Paint mPaintLine = new Paint();
			mPaintLine.setColor(longitudeColor);
			if (dashLongitude) {
				mPaintLine.setPathEffect(dashEffect);
			}

			// 锟�锟斤拷Paint
			Paint mPaintFont = new Paint();
			mPaintFont.setColor(longtitudeFontColor);
			mPaintFont.setTextSize(longtitudeFontSize);
			mPaintFont.setAntiAlias(true);
			if (counts > 1) {
				float postOffset = (super.getWidth() - axisMarginLeft - 2 * axisMarginRight)
						/ (counts - 1);
				float offset = axisMarginLeft + axisMarginRight;
				for (int i = 0; i <= counts; i++) {
					// 缁樺埗绾挎潯
					if (displayLongitude) {
						canvas.drawLine(offset + i * postOffset, 0f, offset + i
								* postOffset, length, mPaintLine);
					}
					// 缁樺埗鍒诲害
					if (displayAxisXTitle) {
						if (i < counts && i > 0) {
							canvas.drawText(axisXTitles.get(i), offset + i
									* postOffset
									- (axisXTitles.get(i).length())
									* longtitudeFontSize / 2f, super
									.getHeight()
									- axisMarginBottom + longtitudeFontSize,
									mPaintFont);
						} else if (0 == i) {
							canvas.drawText(axisXTitles.get(i),
									this.axisMarginLeft + 2f, super.getHeight()
											- axisMarginBottom
											+ longtitudeFontSize, mPaintFont);
						}
					}
				}
			}
		}
	}

	/**
	 * 缁樺埗绾嚎
	 * 
	 * @param canvas
	 */
	protected void drawAxisGridY(Canvas canvas) {
		if (null != axisYTitles) {
			int counts = axisYTitles.size();
			float length = super.getWidth() - axisMarginLeft;
			// 绾挎潯Paint
			Paint mPaintLine = new Paint();
			mPaintLine.setColor(latitudeColor);
			if (dashLatitude) {
				mPaintLine.setPathEffect(dashEffect);
			}
			// 锟�锟斤拷Paint
			Paint mPaintFont = new Paint();
			mPaintFont.setColor(latitudeFontColor);
			mPaintFont.setTextSize(latitudeFontSize);
			mPaintFont.setAntiAlias(true);

			// 缁樺埗绾挎潯鍧愶拷?杞�
			if (counts > 1) {
				float postOffset = (super.getHeight() - axisMarginBottom - 2 * axisMarginTop)
						/ (counts - 1);
				float offset = super.getHeight() - axisMarginBottom
						- axisMarginTop;
				for (int i = 0; i <= counts; i++) {
					// 缁樺埗绾挎潯
					if (displayLatitude) {
						canvas.drawLine(axisMarginLeft,
								offset - i * postOffset, axisMarginLeft
										+ length, offset - i * postOffset,
								mPaintLine);
					}
					// 缁樺埗鍒诲害
					if (displayAxisYTitle) {
						if (i < counts && i > 0) {
							canvas.drawText(axisYTitles.get(i), 0f, offset - i
									* postOffset + latitudeFontSize / 2f,
									mPaintFont);
						} else if (0 == i) {
							canvas.drawText(axisYTitles.get(i), 0f, super
									.getHeight()
									- this.axisMarginBottom - 2f, mPaintFont);
						}
					}
				}
			}
		}
	}
	
	protected void zoomIn(){
		
	}
	
	protected void zoomOut(){
		
	}

	// 鑾峰緱鏉ヨ嚜鍏朵粬鍥捐〃锟�?鐭�
	public void notifyEvent(GridChart chart) {
		PointF point = chart.getTouchPoint();
		//濡傛灉娌℃湁锟�涓偣
		if(null != point){
			// 鑾峰彇鐐瑰嚮鍧愶拷?
			clickPostX = point.x;
			clickPostY = point.y;
		}
		//璁剧疆褰撳墠鎺т欢锟�锟斤拷鎽哥偣
		touchPoint = new PointF(clickPostX , clickPostY);	
		super.invalidate();
	}

	public void addNotify(ITouchEventResponse notify) {
		if (null == notifyList) {
			notifyList = new ArrayList<ITouchEventResponse>();
		}
		notifyList.add(notify);
	}

	public void removeNotify(int i) {
		if (null != notifyList && notifyList.size() > i) {
			notifyList.remove(i);
		}
	}

	public void removeAllNotify() {
		if (null != notifyList) {
			notifyList.clear();
		}
	}

	public void notifyEventAll(GridChart chart) {
		if (null != notifyList) {
			for (int i = 0; i < notifyList.size(); i++) {
				ITouchEventResponse ichart = notifyList.get(i);
				ichart.notifyEvent(chart);
			}
		}
	}

	// /////////灞烇拷?getter涓巗etter鏂癸拷?////////////////

	public int getBackgroudColor() {
		return backgroudColor;
	}

	public void setBackgroudColor(int backgroudColor) {
		this.backgroudColor = backgroudColor;
	}

	public int getAxisXColor() {
		return axisXColor;
	}

	public void setAxisXColor(int axisXColor) {
		this.axisXColor = axisXColor;
	}

	public int getAxisYColor() {
		return axisYColor;
	}

	public void setAxisYColor(int axisYColor) {
		this.axisYColor = axisYColor;
	}

	public int getLongitudeColor() {
		return longitudeColor;
	}

	public void setLongitudeColor(int longitudeColor) {
		this.longitudeColor = longitudeColor;
	}

	public int getLatitudeColor() {
		return latitudeColor;
	}

	public void setLatitudeColor(int latitudeColor) {
		this.latitudeColor = latitudeColor;
	}

	public float getAxisMarginLeft() {
		return axisMarginLeft;
	}

	public void setAxisMarginLeft(float axisMarginLeft) {
		this.axisMarginLeft = axisMarginLeft;

		// 濡傛灉宸﹁竟璺濅负0?锟斤拷?涓嶆樉绀篩鍧愶拷?杞�
		if (0f == axisMarginLeft) {
			this.displayAxisYTitle = Boolean.FALSE;
		}
	}

	public float getAxisMarginBottom() {
		return axisMarginBottom;
	}

	public void setAxisMarginBottom(float axisMarginBottom) {
		this.axisMarginBottom = axisMarginBottom;

		// 濡傛灉涓嬭竟璺濅负0?锟斤拷?涓嶆樉绀篨鍧愶拷?杞�
		if (0f == axisMarginBottom) {
			this.displayAxisXTitle = Boolean.FALSE;
		}
	}

	public float getAxisMarginTop() {
		return axisMarginTop;
	}

	public void setAxisMarginTop(float axisMarginTop) {
		this.axisMarginTop = axisMarginTop;
	}

	public float getAxisMarginRight() {
		return axisMarginRight;
	}

	public void setAxisMarginRight(float axisMarginRight) {
		this.axisMarginRight = axisMarginRight;
	}

	public List<String> getAxisXTitles() {
		return axisXTitles;
	}

	public void setAxisXTitles(List<String> axisXTitles) {
		if (this.axisXTitles != null && this.axisXTitles.size() != 0)
			this.axisXTitles.clear();
		this.axisXTitles = null;
		this.axisXTitles = axisXTitles;
	}

	public List<String> getAxisYTitles() {
		return axisYTitles;
	}

	public void setAxisYTitles(List<String> axisYTitles) {
		if (this.axisYTitles != null && this.axisYTitles.size() != 0)
			this.axisYTitles.clear();
		this.axisYTitles = null;
		this.axisYTitles = axisYTitles;
	}

	public boolean isDisplayLongitude() {
		return displayLongitude;
	}

	public void setDisplayLongitude(boolean displayLongitude) {
		this.displayLongitude = displayLongitude;
	}

	public boolean isDashLongitude() {
		return dashLongitude;
	}

	public void setDashLongitude(boolean dashLongitude) {
		this.dashLongitude = dashLongitude;
	}

	public boolean isDisplayLatitude() {
		return displayLatitude;
	}

	public void setDisplayLatitude(boolean displayLatitude) {
		this.displayLatitude = displayLatitude;
	}

	public boolean isDashLatitude() {
		return dashLatitude;
	}

	public void setDashLatitude(boolean dashLatitude) {
		this.dashLatitude = dashLatitude;
	}

	public PathEffect getDashEffect() {
		return dashEffect;
	}

	public void setDashEffect(PathEffect dashEffect) {
		this.dashEffect = dashEffect;
	}

	public boolean isDisplayAxisXTitle() {
		return displayAxisXTitle;
	}

	public void setDisplayAxisXTitle(boolean displayAxisXTitle) {
		this.displayAxisXTitle = displayAxisXTitle;

		// 濡傛灉涓嶆樉绀篨杞村埢搴�鍒欏簳杈硅竟璺濅负0
		if (false == displayAxisXTitle) {
			this.axisMarginBottom = 0;
		}
	}

	public boolean isDisplayAxisYTitle() {
		return displayAxisYTitle;
	}

	public void setDisplayAxisYTitle(boolean displayAxisYTitle) {
		this.displayAxisYTitle = displayAxisYTitle;

		// 濡傛灉涓嶆樉绀篩杞村埢搴�鍒欏乏杈硅竟璺濅负0
		if (false == displayAxisYTitle) {
			this.axisMarginLeft = 0;
		}
	}

	public boolean isDisplayBorder() {
		return displayBorder;
	}

	public void setDisplayBorder(boolean displayBorder) {
		this.displayBorder = displayBorder;
	}

	public int getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
	}

	public int getLongtitudeFontColor() {
		return longtitudeFontColor;
	}

	public void setLongtitudeFontColor(int longtitudeFontColor) {
		this.longtitudeFontColor = longtitudeFontColor;
	}

	public int getLongtitudeFontSize() {
		return longtitudeFontSize;
	}

	public void setLongtitudeFontSize(int longtitudeFontSize) {
		this.longtitudeFontSize = longtitudeFontSize;
	}

	public int getLatitudeFontColor() {
		return latitudeFontColor;
	}

	public void setLatitudeFontColor(int latitudeFontColor) {
		this.latitudeFontColor = latitudeFontColor;
	}

	public int getLatitudeFontSize() {
		return latitudeFontSize;
	}

	public void setLatitudeFontSize(int latitudeFontSize) {
		this.latitudeFontSize = latitudeFontSize;
	}

	public int getAxisYMaxTitleLength() {
		return axisYMaxTitleLength;
	}

	public void setAxisYMaxTitleLength(int axisYMaxTitleLength) {
		this.axisYMaxTitleLength = axisYMaxTitleLength;
	}

	public boolean isDisplayCrossXOnTouch() {
		return displayCrossXOnTouch;
	}

	public void setDisplayCrossXOnTouch(boolean displayCrossXOnTouch) {
		this.displayCrossXOnTouch = displayCrossXOnTouch;
	}

	public boolean isDisplayCrossYOnTouch() {
		return displayCrossYOnTouch;
	}

	public void setDisplayCrossYOnTouch(boolean displayCrossYOnTouch) {
		this.displayCrossYOnTouch = displayCrossYOnTouch;
	}

	public PointF getTouchPoint() {
		return touchPoint;
	}

	public void setTouchPoint(PointF touchPoint) {
		this.touchPoint = touchPoint;
	}
}
