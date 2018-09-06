package com.sg.uis.LsyNewView.TimingAndDelayed;

/**
 * 
 * @author lsy
 * 定时和延时类
 */
public class TimingAndDelayed {

	public int timing;
	public int delayed;
	
	public TimingAndDelayed(int timing,int delayed)
	{
		this.timing=timing;
		this.delayed=delayed;
	}
	

	public int getTiming() {
		return timing;
	}

	public void setTiming(int timing) {
		this.timing = timing;
	}

	public int getDelayed() {
		return delayed;
	}

	public void setDelayed(int delayed) {
		this.delayed = delayed;
	}
	

}
