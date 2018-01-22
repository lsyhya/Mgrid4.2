package com.sg.uis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.MutiThreadShareObject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import cn.limc.androidcharts.entity.LineEntity;
import cn.limc.androidcharts.entity.OHLCEntity;
import cn.limc.androidcharts.entity.StickEntity;
import cn.limc.androidcharts.entity.TitleValueColorEntity;
import cn.limc.androidcharts.view.CandleStickChart;
import cn.limc.androidcharts.view.GridChart;
import cn.limc.androidcharts.view.MACandleStickChart;
import cn.limc.androidcharts.view.MAChart;
import cn.limc.androidcharts.view.MAStickChart;
import cn.limc.androidcharts.view.MinusStickChart;
import cn.limc.androidcharts.view.PieChart;
import cn.limc.androidcharts.view.StickChart;
import data_model.ipc_data_signal;
// Chart控件
public class SgChart extends View implements IObject {
	
	public SgChart(Context context) {
		super(context);
		initVOL();
        initOHLC();
       
        initGridChart();
	    initMAChart();  /* 未知的注释块 */
	    initStickChart();
	    initMAStickChart();  /* 未知的去注释 -- CharlesChen */
	    //initMinusStickChart();
	    //initCandleStickChart();
	    //initMACandleStickChart();   
        initPieChart();
        this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
        });
        m_rBBox = new Rect();
        m_arrColors = new ArrayList<Integer>();
        m_arrColors.add(Color.GREEN);
        m_arrColors.add(Color.BLUE);
        m_arrColors.add(Color.LTGRAY);
        m_arrColors.add(Color.RED);
        m_arrColors.add(Color.WHITE);
        m_arrColors.add(Color.YELLOW);
        m_arrColors.add(Color.CYAN);
	}

	List<OHLCEntity> ohlc;
	List<StickEntity> vol;
	
	GridChart gridchart;
	MAChart machart;
	StickChart stickchart;
	MAStickChart mastickchart;
	MinusStickChart minusstickchart;
	CandleStickChart candlestickchart;
	MACandleStickChart macandlestickchart;
	PieChart piechart;

    private void initGridChart()
	{
    	this.gridchart= new GridChart(this.getContext());//(GridChart)findViewById(R.id.gridchart);

		List<String> ytitle=new ArrayList<String>();
		ytitle.add("241");
		ytitle.add("242");
		ytitle.add("243");
		ytitle.add("244");
		ytitle.add("245");
		List<String> xtitle=new ArrayList<String>();
		xtitle.add("9:00");
		xtitle.add("10:00");
		xtitle.add("11:00");
		xtitle.add("13:00");
		xtitle.add("14:00");
		xtitle.add("15:00");
		xtitle.add(" ");
        
		gridchart.setAxisXColor(Color.LTGRAY);
		gridchart.setAxisYColor(Color.LTGRAY);
		gridchart.setBorderColor(Color.LTGRAY);
		gridchart.setAxisMarginTop(10);
		gridchart.setAxisMarginLeft(20);
		gridchart.setAxisYTitles(ytitle);
		gridchart.setAxisXTitles(xtitle);
		gridchart.setLongtitudeFontSize(10);
		gridchart.setLongtitudeFontColor(Color.WHITE);
		gridchart.setLatitudeColor(Color.GRAY);
		gridchart.setLatitudeFontColor(Color.WHITE);
		gridchart.setLongitudeColor(Color.GRAY);
		gridchart.setDisplayAxisXTitle(true);
		gridchart.setDisplayAxisYTitle(true);
		gridchart.setDisplayLatitude(true);
		gridchart.setDisplayLongitude(true);
	}
    
	private void initMAChart()
	{
        this.machart = new MAChart(this.getContext());//(MAChart)findViewById(R.id.machart);
        List<LineEntity> lines = new ArrayList<LineEntity>();
        
        //閻犱緤绱曢悾锟介柡鍐﹀劚濞煎海鐥敓锟�      
        LineEntity MA5 = new LineEntity();
        MA5.setTitle("MA5");
        MA5.setLineColor(Color.WHITE);
        MA5.setLineData(initMA(5));
        lines.add(MA5);
        
        //閻犱緤绱曢悾锟�闁哄啨鍎卞搴ｇ棯閿燂拷       
        LineEntity MA10 = new LineEntity();
        MA10.setTitle("MA10");
        MA10.setLineColor(Color.RED);
        MA10.setLineData(initMA(10));
        lines.add(MA10);
        
        //閻犱緤绱曢悾锟�闁哄啨鍎卞搴ｇ棯閿燂拷       
        LineEntity MA25 = new LineEntity();
        MA25.setTitle("MA25");
        MA25.setLineColor(Color.GREEN);
        MA25.setLineData(initMA(25));
        lines.add(MA25);
        
		List<String> ytitle=new ArrayList<String>();
		ytitle.add("240");
		ytitle.add("250");
		ytitle.add("260");
		ytitle.add("270");
		ytitle.add("280");
		List<String> xtitle=new ArrayList<String>();
		xtitle.add("9:00");
		xtitle.add("10:00");
		xtitle.add("11:00");
		xtitle.add("13:00");
		xtitle.add("14:00");
		xtitle.add("15:00");
		xtitle.add(" ");
        
        machart.setAxisXColor(Color.LTGRAY);
		machart.setAxisYColor(Color.LTGRAY);
		machart.setBorderColor(Color.LTGRAY);
		machart.setAxisMarginTop(10);
		machart.setAxisMarginLeft(20);
		machart.setAxisYTitles(ytitle);
		machart.setAxisXTitles(xtitle);
		machart.setLongtitudeFontSize(10);
		machart.setLongtitudeFontColor(Color.WHITE);
		machart.setLatitudeColor(Color.GRAY);
		machart.setLatitudeFontColor(Color.WHITE);
		machart.setLongitudeColor(Color.GRAY);
		machart.setMaxValue(280);
		machart.setMinValue(240);
		machart.setMaxPointNum(36);
		machart.setDisplayAxisXTitle(true);
		machart.setDisplayAxisYTitle(true);
		machart.setDisplayLatitude(true);
		machart.setDisplayLongitude(true);
        
        //濞戞挾顕玥art1濠⒀呭仜婵偤宕搁崶鈺佹疇
        machart.setLineData(lines);
	}
	
	private void initStickChart()
	{
       this.stickchart = new StickChart(this.getContext());//(StickChart)findViewById(R.id.stickchart);
     
       stickchart.setAxisXColor(Color.LTGRAY);
       stickchart.setAxisYColor(Color.LTGRAY);
       stickchart.setLatitudeColor(Color.GRAY);
       stickchart.setLongitudeColor(Color.GRAY);
       stickchart.setBorderColor(Color.LTGRAY);
       stickchart.setLongtitudeFontColor(Color.WHITE);
       stickchart.setLatitudeFontColor(Color.WHITE);
       stickchart.setStickFillColor(Color.YELLOW);
       stickchart.setAxisMarginTop(5);
       stickchart.setAxisMarginRight(1);
       
       //闁哄牞鎷烽妵鍥及閸撗佷粵閻℃帟娅曢弳锟�      
       stickchart.setMaxStickDataNum(52);
       //闁哄牞鎷烽妵鍥╃棯椤掑倸娈犻柡渚婃嫹      
       stickchart.setLatitudeNum(2);
       //闁哄牞鎷烽妵鍥╃磼韫囨洖娈犻柡渚婃嫹      
       stickchart.setLongtitudeNum(3);
       //闁哄牞鎷烽妵鍥ㄧ闁垮澹�       
       stickchart.setMaxValue(10000);
       //闁哄牞鎷烽惃顒佺闁垮澹�       
       stickchart.setMinValue(100);
       
       stickchart.setDisplayAxisXTitle(true);
       stickchart.setDisplayAxisYTitle(true);
       stickchart.setDisplayLatitude(true);
       stickchart.setDisplayLongitude(true);
       stickchart.setBackgroudColor(Color.BLACK);
       
      //濞戞挾顕玥art1濠⒀呭仜婵偤宕搁崶鈺佹疇
       stickchart.setStickData(vol);
	}
	private void initMAStickChart()
	{
        this.mastickchart = new MAStickChart(this.getContext());//(MAStickChart)findViewById(R.id.mastickchart);
        
        //濞寸姰鍎扮粭鍛媼閿涘嫮鏆琕OL
        List<LineEntity> vlines = new ArrayList<LineEntity>();
        
        //閻犱緤绱曢悾锟介柡鍐﹀劚濞煎海鐥敓锟�      
        LineEntity VMA5 = new LineEntity();
        VMA5.setTitle("MA5");
        VMA5.setLineColor(Color.WHITE);
        VMA5.setLineData(initVMA(5));
        vlines.add(VMA5);
        
        //閻犱緤绱曢悾锟�闁哄啨鍎卞搴ｇ棯閿燂拷       
        LineEntity VMA10 = new LineEntity();
        VMA10.setTitle("MA10");
        VMA10.setLineColor(Color.RED);
        VMA10.setLineData(initVMA(10));
        vlines.add(VMA10);
        
        //閻犱緤绱曢悾锟�闁哄啨鍎卞搴ｇ棯閿燂拷       
        LineEntity VMA25 = new LineEntity();
        VMA25.setTitle("MA25");
        VMA25.setLineColor(Color.GREEN);
        VMA25.setLineData(initVMA(25));
        vlines.add(VMA25);
        
        mastickchart.setAxisXColor(Color.LTGRAY);
        mastickchart.setAxisYColor(Color.LTGRAY);
        mastickchart.setLatitudeColor(Color.GRAY);
        mastickchart.setLongitudeColor(Color.GRAY);
        mastickchart.setBorderColor(Color.LTGRAY);
        mastickchart.setLongtitudeFontColor(Color.WHITE);
        mastickchart.setLatitudeFontColor(Color.WHITE);
        mastickchart.setStickFillColor(Color.YELLOW);
        mastickchart.setAxisMarginTop(5);
        mastickchart.setAxisMarginRight(1);
        
        //闁哄牞鎷烽妵鍥及閸撗佷粵閻℃帟娅曢弳锟�      
        mastickchart.setMaxStickDataNum(52);
        //闁哄牞鎷烽妵鍥╃棯椤掑倸娈犻柡渚婃嫹       
        mastickchart.setLatitudeNum(2);
        //闁哄牞鎷烽妵鍥╃磼韫囨洖娈犻柡渚婃嫹        
        mastickchart.setLongtitudeNum(3);
        //闁哄牞鎷烽妵鍥ㄧ闁垮澹�        
        mastickchart.setMaxValue(10000);
        //闁哄牞鎷烽惃顒佺闁垮澹�        
        mastickchart.setMinValue(100);
        
        mastickchart.setDisplayAxisXTitle(true);
        mastickchart.setDisplayAxisYTitle(true);
        mastickchart.setDisplayLatitude(true);
        mastickchart.setDisplayLongitude(true);
        mastickchart.setBackgroudColor(Color.BLACK);

        //濞戞挾顕玥art1濠⒀呭仜婵偤宕搁崶鈺佹疇
        mastickchart.setLineData(vlines);
        //濞戞挾顕玥art1濠⒀呭仜婵偤宕搁崶鈺佹疇
        mastickchart.setStickData(vol);
	}
	
	private void initMinusStickChart()
	{
        this.minusstickchart = new MinusStickChart(this.getContext());//(MinusStickChart)findViewById(R.id.minusstickchart);
		
		List<StickEntity> data = new ArrayList<StickEntity>();
		data.add(new StickEntity(50000,0,20110603));
		data.add(new StickEntity(42000,0,20110703));
		data.add(new StickEntity(32000,0,20110803));
		data.add(new StickEntity(21000,0,20110903));
		data.add(new StickEntity(0,-12000,20111003));
		data.add(new StickEntity(0,-28000,20111103));
		data.add(new StickEntity(0,-41000,20111203));
		data.add(new StickEntity(0,-25000,20120103));
		data.add(new StickEntity(0,-18000,20120203));
		data.add(new StickEntity(14000,0,20120303));
		data.add(new StickEntity(24000,0,20120303));
		data.add(new StickEntity(36000,0,20120303));
		data.add(new StickEntity(46000,0,20120303));
		minusstickchart.setStickData(data);
		minusstickchart.setMaxValue(50000);
		minusstickchart.setMinValue(-50000);
		minusstickchart.setAxisMarginRight(1);
		minusstickchart.setAxisMarginTop(5);
		
		minusstickchart.setBorderColor(Color.GRAY);
		minusstickchart.setAxisXColor(Color.WHITE);
		minusstickchart.setAxisYColor(Color.WHITE);
		minusstickchart.setLatitudeFontColor(Color.WHITE);
		minusstickchart.setLatitudeColor(Color.GRAY);
		minusstickchart.setLongtitudeFontColor(Color.WHITE);
		minusstickchart.setLongitudeColor(Color.GRAY);
		//闁哄牞鎷烽妵鍥╃棯椤掑倸娈犻柡渚婃嫹		
		minusstickchart.setLatitudeNum(3);
		//闁哄牞鎷烽妵鍥╃磼韫囨洖娈犻柡渚婃嫹		
		minusstickchart.setLongtitudeNum(2);
		minusstickchart.setDisplayAxisXTitle(true);
		minusstickchart.setDisplayAxisYTitle(true);
		minusstickchart.setDisplayCrossXOnTouch(false);
		minusstickchart.setDisplayCrossYOnTouch(false);
		minusstickchart.setDisplayLatitude(true);
		minusstickchart.setDisplayLongitude(true);
		minusstickchart.setStickBorderColor(Color.WHITE);
		minusstickchart.setStickFillColor(Color.BLUE);
	}
	
	private void initCandleStickChart()
	{
        this.candlestickchart = new CandleStickChart(this.getContext());//(CandleStickChart)findViewById(R.id.candlestickchart);
        candlestickchart.setAxisXColor(Color.LTGRAY);
        candlestickchart.setAxisYColor(Color.LTGRAY);
        candlestickchart.setLatitudeColor(Color.GRAY);
        candlestickchart.setLongitudeColor(Color.GRAY);
        candlestickchart.setBorderColor(Color.LTGRAY);
        candlestickchart.setLongtitudeFontColor(Color.WHITE);
        candlestickchart.setLatitudeFontColor(Color.WHITE);
        candlestickchart.setAxisMarginRight(1);
        
        //闁哄牞鎷烽妵鍥及閸撗佷粵閻℃帟娅曢弳锟�       
        candlestickchart.setMaxCandleSticksNum(52);
        //闁哄牞鎷烽妵鍥╃棯椤掑倸娈犻柡渚婃嫹        
        candlestickchart.setLatitudeNum(5);
        //闁哄牞鎷烽妵鍥╃磼韫囨洖娈犻柡渚婃嫹        
        candlestickchart.setLongtitudeNum(3);
        //闁哄牞鎷烽妵鍥ㄧ闁垮澹�        
        candlestickchart.setMaxPrice(1000);
        //闁哄牞鎷烽惃顒佺闁垮澹�        
        candlestickchart.setMinPrice(200);
        
        candlestickchart.setDisplayAxisXTitle(true);
        candlestickchart.setDisplayAxisYTitle(true);
        candlestickchart.setDisplayLatitude(true);
        candlestickchart.setDisplayLongitude(true);
        candlestickchart.setBackgroudColor(Color.BLACK);
        
        //濞戞挾顕玥art2濠⒀呭仜婵偤宕搁崶鈺佹疇
        candlestickchart.setOHLCData(ohlc);
	}
	
	private void initMACandleStickChart()
	{
        this.macandlestickchart = new MACandleStickChart(this.getContext());//(MACandleStickChart)findViewById(R.id.macandlestickchart);
      List<LineEntity> lines = new ArrayList<LineEntity>();
      
      //閻犱緤绱曢悾锟介柡鍐﹀劚濞煎海鐥敓锟�    
      LineEntity MA5 = new LineEntity();
      MA5.setTitle("MA5");
      MA5.setLineColor(Color.WHITE);
      MA5.setLineData(initMA(5));
      lines.add(MA5);
      
      //閻犱緤绱曢悾锟�闁哄啨鍎卞搴ｇ棯閿燂拷     
      LineEntity MA10 = new LineEntity();
      MA10.setTitle("MA10");
      MA10.setLineColor(Color.RED);
      MA10.setLineData(initMA(10));
      lines.add(MA10);
      
      //閻犱緤绱曢悾锟�闁哄啨鍎卞搴ｇ棯閿燂拷     
      LineEntity MA25 = new LineEntity();
      MA25.setTitle("MA25");
      MA25.setLineColor(Color.GREEN);
      MA25.setLineData(initMA(25));
      lines.add(MA25);
      
      macandlestickchart.setAxisXColor(Color.LTGRAY);
      macandlestickchart.setAxisYColor(Color.LTGRAY);
      macandlestickchart.setLatitudeColor(Color.GRAY);
      macandlestickchart.setLongitudeColor(Color.GRAY);
      macandlestickchart.setBorderColor(Color.LTGRAY);
      macandlestickchart.setLongtitudeFontColor(Color.WHITE);
      macandlestickchart.setLatitudeFontColor(Color.WHITE);
      macandlestickchart.setAxisMarginRight(1);
      
      //闁哄牞鎷烽妵鍥及閸撗佷粵閻℃帟娅曢弳锟�     
      macandlestickchart.setMaxCandleSticksNum(52);
      //闁哄牞鎷烽妵鍥╃棯椤掑倸娈犻柡渚婃嫹      
      macandlestickchart.setLatitudeNum(5);
      //闁哄牞鎷烽妵鍥╃磼韫囨洖娈犻柡渚婃嫹     
      macandlestickchart.setLongtitudeNum(3);
      //闁哄牞鎷烽妵鍥ㄧ闁垮澹�      
      macandlestickchart.setMaxPrice(1000);
      //闁哄牞鎷烽惃顒佺闁垮澹�     
      macandlestickchart.setMinPrice(200);
      
      macandlestickchart.setDisplayAxisXTitle(true);
      macandlestickchart.setDisplayAxisYTitle(true);
      macandlestickchart.setDisplayLatitude(true);
      macandlestickchart.setDisplayLongitude(true);
      macandlestickchart.setBackgroudColor(Color.BLACK);

      
	  //濞戞挾顕玥art2濠⒀呭仜婵偤宕搁崶鈺佹疇
	  macandlestickchart.setLineData(lines);
	    
	  //濞戞挾顕玥art2濠⒀呭仜婵偤宕搁崶鈺佹疇
	  macandlestickchart.setOHLCData(ohlc);
        
	}
    
    private void initPieChart()
    {
        this.piechart = new PieChart(this.getContext());//(PieChart)findViewById(R.id.piechart);
		List<TitleValueColorEntity> data3 = new ArrayList<TitleValueColorEntity>();
		data3.add(new TitleValueColorEntity("Pie0",2,Color.RED));
		data3.add(new TitleValueColorEntity("Pie1",3,Color.BLUE));
		data3.add(new TitleValueColorEntity("Pie2",6,Color.YELLOW));
		data3.add(new TitleValueColorEntity("Pie3",2,Color.GREEN));
		data3.add(new TitleValueColorEntity("Pie4",2,Color.LTGRAY));
		piechart.setData(data3);
    }
	
	private void initVOL(){
		List<StickEntity> stick = new ArrayList<StickEntity>();
		this.vol = new ArrayList<StickEntity>();
		
		stick.add(new StickEntity(406000,0,20110825));
		stick.add(new StickEntity(232000,0,20110824));
		stick.add(new StickEntity(355000,0,20110823));
		stick.add(new StickEntity(437000,0,20110822));
		stick.add(new StickEntity(460000,0,20110819));
		stick.add(new StickEntity(422000,0,20110818));
		stick.add(new StickEntity(502000,0,20110817));
		stick.add(new StickEntity(509000,0,20110816));
		stick.add(new StickEntity(110000,0,20110815));
		stick.add(new StickEntity(110000,0,20110812));
		stick.add(new StickEntity(310000,0,20110811));
		stick.add(new StickEntity(210000,0,20110810));
		stick.add(new StickEntity(211000,0,20110809));
		stick.add(new StickEntity(577000,0,20110808));
		stick.add(new StickEntity(493000,0,20110805));
		stick.add(new StickEntity(433000,0,20110804));
		stick.add(new StickEntity(479000,0,20110803));
		stick.add(new StickEntity(360000,0,20110802));
		stick.add(new StickEntity(437000,0,20110801));
		stick.add(new StickEntity(504000,0,20110729));
		stick.add(new StickEntity(520000,0,20110728));
		stick.add(new StickEntity(494000,0,20110727));
		stick.add(new StickEntity(312000,0,20110726));
		stick.add(new StickEntity(424000,0,20110725));
		stick.add(new StickEntity(557000,0,20110722));
		stick.add(new StickEntity(596000,0,20110721));
		stick.add(new StickEntity(311000,0,20110720));
		stick.add(new StickEntity(312000,0,20110719));
		stick.add(new StickEntity(312000,0,20110718));
		stick.add(new StickEntity(312000,0,20110715));
		stick.add(new StickEntity(410000,0,20110714));
		stick.add(new StickEntity(311000,0,20110713));
		stick.add(new StickEntity(210000,0,20110712));
		stick.add(new StickEntity(410000,0,20110711));
		stick.add(new StickEntity(214000,0,20110708));
		stick.add(new StickEntity(312000,0,20110707));
		stick.add(new StickEntity(212000,0,20110706));
		stick.add(new StickEntity(414000,0,20110705));
		stick.add(new StickEntity(310000,0,20110704));
		stick.add(new StickEntity(210000,0,20110701));
		stick.add(new StickEntity(190000,0,20110630));
		stick.add(new StickEntity(210000,0,20110629));
		stick.add(new StickEntity(116000,0,20110628));
		stick.add(new StickEntity(142000,0,20110627));
		stick.add(new StickEntity(524000,0,20110624));
		stick.add(new StickEntity(246000,0,20110623));
		stick.add(new StickEntity(432000,0,20110622));
		stick.add(new StickEntity(352000,0,20110621));
		stick.add(new StickEntity(243000,0,20110620));
		stick.add(new StickEntity(165000,0,20110617));
		stick.add(new StickEntity(554000,0,20110616));
		stick.add(new StickEntity(552000,0,20110615));
		stick.add(new StickEntity(431000,0,20110614));
		stick.add(new StickEntity(317000,0,20110613));
		stick.add(new StickEntity(512000,0,20110610));
		stick.add(new StickEntity(283000,0,20110609));
		stick.add(new StickEntity(144000,0,20110608));
		stick.add(new StickEntity(273000,0,20110607));
		stick.add(new StickEntity(278000,0,20110603));
		stick.add(new StickEntity(624000,0,20110602));
		stick.add(new StickEntity(217000,0,20110601));
		stick.add(new StickEntity(116000,0,20110531));
		stick.add(new StickEntity(191000,0,20110530));
		stick.add(new StickEntity(204000,0,20110527));
		stick.add(new StickEntity(236000,0,20110526));
		stick.add(new StickEntity(421000,0,20110525));
		stick.add(new StickEntity(114000,0,20110524));
		stick.add(new StickEntity(403000,0,20110523));
		stick.add(new StickEntity(205000,0,20110520));
		stick.add(new StickEntity(328000,0,20110519));
		stick.add(new StickEntity(109000,0,20110518));
		stick.add(new StickEntity(286000,0,20110517));
		stick.add(new StickEntity(103000,0,20110516));
		stick.add(new StickEntity(114000,0,20110513));
		stick.add(new StickEntity(107000,0,20110512));
		stick.add(new StickEntity(106000,0,20110511));
		stick.add(new StickEntity(146000,0,20110510));
		stick.add(new StickEntity(105000,0,20110509));
		stick.add(new StickEntity(312000,0,20110506));
		stick.add(new StickEntity(530000,0,20110505));
		stick.add(new StickEntity(275000,0,20110504));
		stick.add(new StickEntity(432000,0,20110503));
		
        for(int i= stick.size(); i > 0 ; i--){
        	this.vol.add(stick.get(i -1));
        }
	}
	
	private List<Float> initVMA(int days){
		if (days < 2){
			return null;
		}
		
        List<Float> MA5Values = new ArrayList<Float>();
        
    	float sum = 0;
    	float avg = 0;
        for(int i = 0 ; i < this.vol.size(); i++){
        	float close =(float)vol.get(i).getHigh();
        	if(i< days){
        		sum = sum + close;
        		avg = sum / (i + 1f);
        	}else{
        		sum = sum + close - (float)vol.get(i-days).getHigh();
        		avg = sum / days;
        	}
        	MA5Values.add(avg);
        }
        
        return MA5Values;
	}
	
	private void initOHLC(){
        List<OHLCEntity> ohlc = new ArrayList<OHLCEntity>();
        
        this.ohlc = new ArrayList<OHLCEntity>();
        ohlc.add(new OHLCEntity(246 ,248 ,235 ,235 ,20110825));
        ohlc.add(new OHLCEntity(240 ,242 ,236 ,242 ,20110824));
        ohlc.add(new OHLCEntity(236 ,240 ,235 ,240 ,20110823));
        ohlc.add(new OHLCEntity(232 ,236 ,231 ,236 ,20110822));
        ohlc.add(new OHLCEntity(240 ,240 ,235 ,235 ,20110819));
        ohlc.add(new OHLCEntity(240 ,241 ,239 ,240 ,20110818));
        ohlc.add(new OHLCEntity(242 ,243 ,240 ,240 ,20110817));
        ohlc.add(new OHLCEntity(239 ,242 ,238 ,242 ,20110816));
        ohlc.add(new OHLCEntity(239 ,240 ,238 ,239 ,20110815));
        ohlc.add(new OHLCEntity(230 ,238 ,230 ,238 ,20110812));
        ohlc.add(new OHLCEntity(236 ,237 ,234 ,234 ,20110811));
        ohlc.add(new OHLCEntity(226 ,233 ,223 ,232 ,20110810));
        ohlc.add(new OHLCEntity(239 ,241 ,229 ,232 ,20110809));
        ohlc.add(new OHLCEntity(242 ,244 ,240 ,242 ,20110808));
        ohlc.add(new OHLCEntity(248 ,249 ,247 ,248 ,20110805));
        ohlc.add(new OHLCEntity(245 ,248 ,245 ,247 ,20110804));
        ohlc.add(new OHLCEntity(249 ,249 ,245 ,247 ,20110803));
        ohlc.add(new OHLCEntity(249 ,251 ,248 ,250 ,20110802));
        ohlc.add(new OHLCEntity(250 ,252 ,248 ,250 ,20110801));
        ohlc.add(new OHLCEntity(250 ,251 ,248 ,250 ,20110729));
        ohlc.add(new OHLCEntity(249 ,252 ,248 ,252 ,20110728));
        ohlc.add(new OHLCEntity(248 ,250 ,247 ,250 ,20110727));
        ohlc.add(new OHLCEntity(256 ,256 ,248 ,248 ,20110726));
        ohlc.add(new OHLCEntity(257 ,258 ,256 ,257 ,20110725));
        ohlc.add(new OHLCEntity(259 ,260 ,256 ,256 ,20110722));
        ohlc.add(new OHLCEntity(261 ,261 ,257 ,259 ,20110721));
        ohlc.add(new OHLCEntity(260 ,260 ,259 ,259 ,20110720));
        ohlc.add(new OHLCEntity(262 ,262 ,260 ,261 ,20110719));
        ohlc.add(new OHLCEntity(260 ,262 ,259 ,262 ,20110718));
        ohlc.add(new OHLCEntity(259 ,261 ,258 ,261 ,20110715));
        ohlc.add(new OHLCEntity(255 ,259 ,255 ,259 ,20110714));
        ohlc.add(new OHLCEntity(258 ,258 ,255 ,255 ,20110713));
        ohlc.add(new OHLCEntity(258 ,260 ,258 ,260 ,20110712));
        ohlc.add(new OHLCEntity(259 ,260 ,258 ,259 ,20110711));
        ohlc.add(new OHLCEntity(261 ,262 ,259 ,259 ,20110708));
        ohlc.add(new OHLCEntity(261 ,261 ,258 ,261 ,20110707));
        ohlc.add(new OHLCEntity(261 ,261 ,259 ,261 ,20110706));
        ohlc.add(new OHLCEntity(257 ,261 ,257 ,261 ,20110705));
        ohlc.add(new OHLCEntity(256 ,257 ,255 ,255 ,20110704));
        ohlc.add(new OHLCEntity(253 ,257 ,253 ,256 ,20110701));
        ohlc.add(new OHLCEntity(255 ,255 ,252 ,252 ,20110630));
        ohlc.add(new OHLCEntity(256 ,256 ,253 ,255 ,20110629));
        ohlc.add(new OHLCEntity(254 ,256 ,254 ,255 ,20110628));
        ohlc.add(new OHLCEntity(247 ,256 ,247 ,254 ,20110627));
        ohlc.add(new OHLCEntity(244 ,249 ,243 ,248 ,20110624));
        ohlc.add(new OHLCEntity(244 ,245 ,243 ,244 ,20110623));
        ohlc.add(new OHLCEntity(242 ,244 ,241 ,244 ,20110622));
        ohlc.add(new OHLCEntity(243 ,243 ,241 ,242 ,20110621));
        ohlc.add(new OHLCEntity(246 ,247 ,244 ,244 ,20110620));
        ohlc.add(new OHLCEntity(248 ,249 ,246 ,246 ,20110617));
        ohlc.add(new OHLCEntity(251 ,253 ,250 ,250 ,20110616));
        ohlc.add(new OHLCEntity(249 ,253 ,249 ,253 ,20110615));
        ohlc.add(new OHLCEntity(248 ,250 ,246 ,250 ,20110614));
        ohlc.add(new OHLCEntity(249 ,250 ,247 ,250 ,20110613));
        ohlc.add(new OHLCEntity(254 ,254 ,250 ,250 ,20110610));
        ohlc.add(new OHLCEntity(254 ,255 ,251 ,255 ,20110609));
        ohlc.add(new OHLCEntity(252 ,254 ,251 ,254 ,20110608));
        ohlc.add(new OHLCEntity(250 ,253 ,250 ,252 ,20110607));
        ohlc.add(new OHLCEntity(251 ,252 ,247 ,250 ,20110603));
        ohlc.add(new OHLCEntity(253 ,254 ,252 ,254 ,20110602));
        ohlc.add(new OHLCEntity(250 ,254 ,250 ,254 ,20110601));
        ohlc.add(new OHLCEntity(250 ,252 ,248 ,250 ,20110531));
        ohlc.add(new OHLCEntity(253 ,254 ,250 ,251 ,20110530));
        ohlc.add(new OHLCEntity(255 ,256 ,253 ,253 ,20110527));
        ohlc.add(new OHLCEntity(256 ,257 ,253 ,254 ,20110526));
        ohlc.add(new OHLCEntity(256 ,257 ,254 ,256 ,20110525));
        ohlc.add(new OHLCEntity(265 ,265 ,257 ,257 ,20110524));
        ohlc.add(new OHLCEntity(265 ,266 ,265 ,265 ,20110523));
        ohlc.add(new OHLCEntity(267 ,268 ,265 ,266 ,20110520));
        ohlc.add(new OHLCEntity(264 ,267 ,264 ,267 ,20110519));
        ohlc.add(new OHLCEntity(264 ,266 ,262 ,265 ,20110518));
        ohlc.add(new OHLCEntity(266 ,267 ,264 ,264 ,20110517));
        ohlc.add(new OHLCEntity(264 ,267 ,263 ,267 ,20110516));
        ohlc.add(new OHLCEntity(266 ,267 ,264 ,264 ,20110513));
        ohlc.add(new OHLCEntity(269 ,269 ,266 ,268 ,20110512));
        ohlc.add(new OHLCEntity(267 ,269 ,266 ,269 ,20110511));
        ohlc.add(new OHLCEntity(266 ,268 ,266 ,267 ,20110510));
        ohlc.add(new OHLCEntity(264 ,268 ,263 ,266 ,20110509));
        ohlc.add(new OHLCEntity(265 ,268 ,265 ,267 ,20110506));
        ohlc.add(new OHLCEntity(271 ,271 ,266 ,266 ,20110505));
        ohlc.add(new OHLCEntity(271 ,273 ,269 ,273 ,20110504));
        ohlc.add(new OHLCEntity(268 ,271 ,267 ,271 ,20110503));
        
        for(int i= ohlc.size(); i > 0 ; i--){
        	this.ohlc.add(ohlc.get(i -1));
        }
	}
	
	private List<Float> initMA(int days){
		
		if (days < 2){
			return null;
		}
		
        List<Float> MA5Values = new ArrayList<Float>();
        
    	float sum = 0;
    	float avg = 0;
        for(int i = 0 ; i < this.ohlc.size(); i++){
        	float close =(float)ohlc.get(i).getClose();
        	if(i< days){
        		sum = sum + close;
        		avg = sum / (i + 1f);
        	}else{
        		sum = sum + close - (float)ohlc.get(i-days).getClose();
        		avg = sum / days;
        	}
        	MA5Values.add(avg);
        }
        
        return MA5Values;
	}

	public void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		super.onDraw(canvas);
	}
	
	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		
		int nX = l + (int) (((float)m_nPosX / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nY = t + (int) (((float)m_nPosY / (float)MainWindow.FORM_HEIGHT) * (b-t));
		int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (b-t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX+nWidth;
		m_rBBox.bottom = nY+nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			//gridchart.layout(nX, nY, nX+nWidth, nY+nHeight);
			//machart.layout(nX, nY, nX+nWidth, nY+nHeight);
			if ("Bar".equals(m_strChartType))
				stickchart.layout(nX, nY, nX+nWidth, nY+nHeight);
			else if ("Pie".equals(m_strChartType))
				piechart.layout(nX, nY, nX+nWidth, nY+nHeight);
			else
				machart.layout(nX, nY, nX+nWidth, nY+nHeight);
			//mastickchart.layout(nX, nY, nX+nWidth, nY+nHeight);
			//minusstickchart.layout(nX, nY, nX+nWidth, nY+nHeight);
			//candlestickchart.layout(nX, nY, nX+nWidth, nY+nHeight);
			//macandlestickchart.layout(nX, nY, nX+nWidth, nY+nHeight);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		//rWin.addView(gridchart);
		//rWin.addView(machart);
		if ("Bar".equals(m_strChartType))
			rWin.addView(stickchart);
		else if ("Pie".equals(m_strChartType))
			rWin.addView(piechart);
		else {
			rWin.addView(machart);
		//	System.out.println("哈哈："+m_strChartType);
		}
		//rWin.addView(mastickchart);
		//rWin.addView(minusstickchart);
		//rWin.addView(candlestickchart);
		//rWin.addView(macandlestickchart);
		m_rRenderWindow = rWin;
	}
	public View getView() {
		return this;
	}
	
	public int getZIndex()
	{
		return m_nZIndex;
	}
	
	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		if ("Bar".equals(m_strChartType))
			rWin.removeView(stickchart);
		else if ("Pie".equals(m_strChartType))
			rWin.removeView(piechart);
		else {
			rWin.removeView(machart);
		}
	}
	
	public void parseProperties(String strName, String strValue, String strResFolder) {

        if ("ZIndex".equals(strName)) {
       	 	m_nZIndex = Integer.parseInt(strValue);
       	    if (MainWindow.MAXZINDEX < m_nZIndex) MainWindow.MAXZINDEX = m_nZIndex;
        }
        else if ("Location".equals(strName)) {
       	 	String[] arrStr = strValue.split(",");
       	 	m_nPosX = Integer.parseInt(arrStr[0]);
       	 	m_nPosY = Integer.parseInt(arrStr[1]);
        }
        else if ("Size".equals(strName)) {
       	 	String[] arrSize = strValue.split(",");
       	 	m_nWidth = Integer.parseInt(arrSize[0]);
       	 	m_nHeight = Integer.parseInt(arrSize[1]);
        }
        else if ("Alpha".equals(strName)) {
       	 	m_fAlpha = Float.parseFloat(strValue);
        }
        else if ("ChartType".equals(strName))
    		m_strChartType = strValue;
        else if ("Expression".equals(strName))
    		m_strExpression = strValue;
	}
	
	public String getBindingExpression() {
		return m_strExpression;
	}

	@Override
	public void initFinished()
	{
	}

	public void setUniqueID(String strID) {
		m_strID = strID;
	}
	public void setType(String strType) {
		m_strType = strType;
	}
	
	public String getUniqueID() {
		return m_strID;
	}
	
	public String getType() {
		return m_strType;
	}

	@Override
	public void updateWidget() {
		if (piechart != null) {
			piechart.invalidate();
		}
		else if (stickchart != null) {
	        stickchart.invalidate();
		}
	
		this.invalidate();
	}
	
	@Override
	public boolean updateValue()
	{
		m_bneedupdate = false;
		

		List<String> listChartData = m_rRenderWindow.m_oShareObject.getMutiChartData(this.getUniqueID());
		if (listChartData == null || listChartData.size() == 0)
			return false;
		
		// 是否使用随机数据
		if (m_rRenderWindow.m_bHasRandomData == true) {
			listChartData.clear();
			Random rand = new Random(); 
			listChartData.add(rand.nextInt(50)+"");
			listChartData.add(rand.nextInt(100)+"");
			listChartData.add( rand.nextInt(100)+"");
		}
		
		// 更新chart数据
		int nLength = listChartData.size();
		int nMin = 999999;
		int nMax = 0;
		/* 曲线被注释  -- CharlesChen
		if (machart != null) {
			List<LineEntity> lines = new ArrayList<LineEntity>(); 
			for (int i = 0; i < nLength; ++i) {
			    int nChart = 0;
			    String strValue = listChartData.get(i).value;
			    if (strValue != null && "".equals(strValue) == false)
			    	nChart = (int)Double.parseDouble(strValue);
				LineEntity MAi = new LineEntity();
				MAi.setTitle("MA"+i);
				int cColor = 0xffffffff;
				if (i < m_arrColors.size())
					cColor = m_arrColors.get(i);
				MAi.setLineColor(cColor);
				MAi.setLineData(initMA(nChart));
				lines.add(MAi);
				
				if (nChart < nMin)
					nMin = nChart;
				if (nChart > nMax)
					nMax = nChart;
			}
			int nDetal = (nMax - nMin) / 4;
			List<String> ytitle=new ArrayList<String>();
			if (nLength == 1) {
				nDetal = nMax / 4;
				ytitle.add(0+"");
				ytitle.add(0+nDetal+"");
				ytitle.add(0+nDetal*2+"");
				ytitle.add(0+nDetal*3+"");
				ytitle.add(nMax+nDetal+"");
			}
			else {
				ytitle.add(nMin+"");
				ytitle.add(nMin+nDetal+"");
				ytitle.add(nMin+nDetal*2+"");
				ytitle.add(nMin+nDetal*3+"");
				ytitle.add(nMax+"");
			}
			
			List<String> xtitle=new ArrayList<String>();
			xtitle.add("9:00");
			xtitle.add("10:00");
			xtitle.add("11:00");
			xtitle.add("13:00");
			xtitle.add("14:00");
			xtitle.add("15:00");
			xtitle.add(" ");
			
			machart.setAxisYTitles(ytitle);
			machart.setAxisXTitles(xtitle);
			machart.setLineData(lines);
			machart.invalidate();
		}
		else */if (piechart != null) {
			List<TitleValueColorEntity> data = new ArrayList<TitleValueColorEntity>();
			for (int i = 0; i < nLength; ++i) {
				float fChart = 0.0f;
			    String strValue = listChartData.get(i);
			    if (strValue != null && "".equals(strValue) == false)
			    	fChart = (float)Double.parseDouble(strValue);
			    int cColor = 0xffffffff;
				if (i < m_arrColors.size())
					cColor = m_arrColors.get(i);
			    data.add(new TitleValueColorEntity("Pie"+i,fChart,cColor));
			}
			piechart.setData(data);
			//piechart.invalidate();
		}
		else if (stickchart != null) {
			List<StickEntity> stick = new ArrayList<StickEntity>();
			this.vol = new ArrayList<StickEntity>();
			// 获取最大最小值
			for (int i = 0; i < nLength; ++i) {
				 int nChart = 0;
				 String strValue = listChartData.get(i);
				 if (strValue != null && "".equals(strValue) == false)
				    nChart = (int)Double.parseDouble(strValue);
				 if (nChart < nMin)
					nMin = nChart;
				 if (nChart > nMax)
					nMax = nChart;
			}
			// 将数据添加到柱状图
			for (int i = 0; i < nLength; ++i) {
				 int nChart = 0;
				 String strValue = listChartData.get(i);
				 if (strValue != null && "".equals(strValue) == false)
				    nChart = (int)Double.parseDouble(strValue);
				 stick.add(new StickEntity(nMax*1.25f,0.0,nChart));
			}
	        for(int i= stick.size(); i > 0 ; i--){
	        	this.vol.add(stick.get(i -1));
	        }	
	        stickchart.setStickData(vol);
	        //stickchart.invalidate();
		}
	
		//this.invalidate();
		return true;
	}

	@Override
    public boolean needupdate()
    {
	    return m_bneedupdate;
    }
	
	@Override
    public void needupdate(boolean bNeedUpdate)
    {
	    m_bneedupdate = bNeedUpdate;
    }
	
	public Rect getBBox() {
		return m_rBBox;
	}
	
	public void setChartType(String strChartType) {
		m_strChartType = strChartType;
	}
	
//params:
	String m_strID = "";
	String m_strType = "";    
	int m_nZIndex = 14;
	int m_nPosX = 69;
	int m_nPosY = 392;
	int m_nWidth = 372;
	int m_nHeight = 186;
	float m_fAlpha = 1.0f;
	String m_strChartType = "Bar";
	String m_strExpression = "Binding{[Value[Equip:114-Temp:173-Signal:4]]|[Value[Equip:114-Temp:173-Signal:5]]|[Value[Equip:114-Temp:173-Signal:6]]}";
	MainWindow m_rRenderWindow = null;	
	Rect m_rBBox = null;
	String m_strTcpValue = "";
	List<Integer> m_arrColors = null;
	
	public boolean m_bneedupdate = true;
}