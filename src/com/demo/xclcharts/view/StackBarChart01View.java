/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android鍥捐〃鍩虹被搴�
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.demo.xclcharts.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.xclcharts.chart.BarData;
import org.xclcharts.chart.StackBarChart;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.event.click.BarPosition;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * @ClassName StackBarChart01View
 * @Description 堆叠图 的例子
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com)
 */
public class StackBarChart01View extends TouchView {

	private String TAG = "StackBarChart01View";
	private StackBarChart chart = new StackBarChart();
	// 鏍囩杞�
	List<String> chartLabels = new LinkedList<String>();
	List<BarData> BarDataSet = new LinkedList<BarData>();

	public StackBarChart01View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	public StackBarChart01View(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public StackBarChart01View(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		chartLabels();
		chartDataSet();
		chartRender();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 鍥炬墍鍗犺寖鍥村ぇ灏�
		chart.setChartRange(w, h);
	}

	
	//获得chart控件
	public StackBarChart getChart()
	{
		   return chart;
	}
	
	
	private void chartRender() {
		try {

			// 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
			int[] ltrb = getBarLnDefaultSpadding();
			//chart.setPadding(ltrb[0], ltrb[1], ltrb[0], ltrb[0]);
			// DensityUtil.dip2px(getContext(), 15));
			chart.setPadding(20,40,0,20);
			// 显示边框
			// chart.showRoundBorder();

			chart.setChartDirection(XEnum.Direction.VERTICAL);
			// 数据源
			chart.setCategories(chartLabels);
			chart.setDataSource(BarDataSet);

			// 坐标系
			chart.getDataAxis().setAxisMax(10);
			chart.getDataAxis().setAxisMin(0);
			chart.getDataAxis().setAxisSteps(2);
			// 指定数据轴标签旋转-45度显示
			// chart.getCategoryAxis().setTickLabelRotateAngle(-45f);
			Paint labelPaint = chart.getCategoryAxis().getTickLabelPaint();
			labelPaint.setTextAlign(Align.CENTER);
			labelPaint.setColor((int) Color.rgb(174, 184, 201));
			labelPaint.setTextSize(10);

			// 轴颜色
			int axisColor = Color.rgb(172, 183, 200); // Color.rgb(222, 136,
														// 166);
			chart.getDataAxis().getAxisPaint().setColor(axisColor);
			chart.getCategoryAxis().getAxisPaint().setColor(axisColor);
			chart.getDataAxis().getTickMarksPaint().setColor(axisColor);
			chart.getCategoryAxis().getTickMarksPaint().setColor(axisColor);

			// Y轴字体颜色大小
			chart.getDataAxis().getTickLabelPaint()
					.setColor(Color.rgb(174, 184, 201));
			chart.getDataAxis().getTickLabelPaint().setTextSize(10);
			// 是否显示刻度线
			chart.getCategoryAxis().setTickMarksVisible(false);
			chart.getDataAxis().setTickMarksVisible(false);

			// 轴宽度
			chart.getDataAxis().getAxisPaint().setStrokeWidth(1);
			chart.getCategoryAxis().getAxisPaint().setStrokeWidth(1);
			chart.getDataAxis().getTickMarksPaint().setStrokeWidth(1);
			chart.getCategoryAxis().getTickMarksPaint().setStrokeWidth(1);

			// 标题
			// chart.setTitle("温度");
//			 chart.getPlotTitle().getSubtitlePaint().setTextSize(20);
//			 chart.getPlotTitle().getSubtitlePaint().setColor(Color.rgb(89, 132, 206));
//			 chart.addSubtitle("温度");
//			 chart.setTitleAlign(XEnum.ChartTitleAlign.MIDDLE);
//			 chart.setTitleVerticalAlign(XEnum.VerticalAlign.MIDDLE);

			// 轴标题
			// chart.getAxisTitle().setLeftAxisTitle("单位(TB)");

			// 背景网格
			chart.getPlotGrid().showEvenRowBgColor();
			chart.getPlotGrid().showOddRowBgColor();

			// 定义数据轴标签显示格式
			chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub

					DecimalFormat df = new DecimalFormat("#0");
					Double tmp = Double.parseDouble(value);
					String label = df.format(tmp).toString();
					return label;
				}

			});

			// 定义标签轴标签显示格式
			chart.getCategoryAxis().setLabelFormatter(
					new IFormatterTextCallBack() {

						@Override
						public String textFormatter(String value) {
							// TODO Auto-generated method stub
							String label = "" + value + "";
							return label;
						}

					});

			// 定义柱形上标签显示格式
			// chart.getBar().setItemLabelVisible(true);
			// chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
			// @Override
			// public String doubleFormatter(Double value) {
			// // TODO Auto-generated method stub
			// DecimalFormat df = new DecimalFormat("#0.00");
			// String label = df.format(value).toString();
			// return label;
			// }
			// });

			// 定义柱形上标签显示颜色
			chart.getBar().getItemLabelPaint()
					.setColor(Color.rgb(174, 184, 201));
			chart.getBar().getItemLabelPaint().setTextSize(10);
			chart.getBar().getItemLabelPaint()
					.setTypeface(Typeface.DEFAULT_BOLD);

			// 激活点击监听
		//	chart.ActiveListenItemClick();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}

	
	
	
	
	
	private void chartDataSet() {
		// 标签1对应的柱形数据集
		Random r = new Random();
		List<Double> dataSeriesA = new LinkedList<Double>();
		List<Double> dataSeriesB = new LinkedList<Double>();
		for (int i = 0; i < 24; i++) {
			dataSeriesA.add((double) r.nextInt(4)+1);
			dataSeriesB.add((double) r.nextInt(4)+1);
		}

		BarDataSet.add(new BarData("冷通道", dataSeriesA, (int) Color.rgb(89, 132,
				206)));
		BarDataSet.add(new BarData("热通道", dataSeriesB, (int) Color.rgb(239, 88,
				87)));
	}

	

	private void chartLabels() {
		for (int i = 0; i < 24; i++) {
			if (i % 2 == 0)
				chartLabels.add("上");
			else
				chartLabels.add("下");
		}

	}

	@Override
	public void render(Canvas canvas) {
		try {

			// chart.setChartRange(this.getMeasuredWidth(),
			// this.getMeasuredHeight());
			chart.render(canvas);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	@Override
	public List<XChart> bindChart() {
		// TODO Auto-generated method stub
		List<XChart> lst = new ArrayList<XChart>();
		lst.add(chart);
		return lst;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		super.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_UP) {
			triggerClick(event.getX(), event.getY());
		}
		return true;
	}

	// 瑙﹀彂鐩戝惉
	private void triggerClick(float x, float y) {

		BarPosition record = chart.getPositionRecord(x, y);
		if (null == record)
			return;

		BarData bData = BarDataSet.get(record.getDataID());
		Double bValue = bData.getDataSet().get(record.getDataChildID());

		Toast.makeText(
				this.getContext(),
				"info:" + record.getRectInfo() + " Key:" + bData.getKey()
						+ " Current Value:" + Double.toString(bValue),
				Toast.LENGTH_SHORT).show();
	}

}
