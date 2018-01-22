package cn.limc.androidcharts.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;

import cn.limc.androidcharts.entity.LineEntity;

public class MAChart extends GridChart {
	/** 锟�锟斤拷鏄剧ず鏁版嵁 */
	private List<LineEntity> lineData;

	/** 锟�锟斤拷鐐规暟 */
	private int maxPointNum;

	/** 锟�锟斤拷浠锋牸 */
	private int minValue;

	/** 锟�锟斤拷浠锋牸 */
	private int maxValue;
	
	ArrayList<HashMap<String, Object>> listItem;

	public MAChart(Context context) {
		super(context);
	}

	public MAChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MAChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// //////////鏂癸拷?//////////////
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 缁樺埗骞筹拷?锟斤拷
		if (null != this.lineData) {
			drawLines(canvas);
		}
	}
	
	protected void drawLines(Canvas canvas){
		// 鐐圭嚎璺濈
		float lineLength = ((super.getWidth() - super.getAxisMarginLeft()-this.getMaxPointNum()) / this.getMaxPointNum())-1;
		// 璧峰浣嶇疆
		float startX;
		
		//閫愭潯杈擄拷?MA绾�
		for (int i = 0; i < lineData.size(); i++) {
			LineEntity line = (LineEntity)lineData.get(i);
			if(line.isDisplay()){
				Paint mPaint = new Paint();
				mPaint.setColor(line.getLineColor());
				mPaint.setAntiAlias(true);
				List<Float> lineData = line.getLineData();
				//杈擄拷?锟�锟斤拷绾�
				startX = super.getAxisMarginLeft() + lineLength / 2f;
				//瀹氫箟璧峰鐐�
				PointF ptFirst = null;
				if(lineData !=null){
					for(int j=0 ; j <lineData.size();j++){
						float value = lineData.get(j).floatValue();
						//鑾峰彇缁堢偣Y鍧愶拷?
						float valueY = (float) ((1f - (value - this.getMinValue())
								/ (this.getMaxValue() - this.getMinValue())) 
								* (super.getHeight() - super.getAxisMarginBottom()));
						
						//缁樺埗绾挎潯
						if (j > 0){
							canvas.drawLine(ptFirst.x,ptFirst.y,startX,valueY,mPaint);
						}
						//閲嶇疆璧峰鐐�
						ptFirst = new PointF(startX , valueY);
						//X浣嶇Щ
						startX = startX + 1 + lineLength;
					}
				}
			}
		}
	}

	// //////////灞烇拷?GetterSetter//////////////

	public List<LineEntity> getLineData() {
		return lineData;
	}

	public void setLineData(List<LineEntity> lineData) {
		if (this.lineData != null)
			this.lineData.clear();
		this.lineData = lineData;
	}

	public int getMaxPointNum() {
		return maxPointNum;
	}

	public void setMaxPointNum(int maxPointNum) {
		this.maxPointNum = maxPointNum;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}
}
