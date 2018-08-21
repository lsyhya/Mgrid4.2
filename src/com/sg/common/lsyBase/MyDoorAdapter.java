package com.sg.common.lsyBase;

import java.util.List;
import java.util.Map;

import com.mgrid.main.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MyDoorAdapter extends SimpleAdapter {

	
	List<Map<String, Object>> list;
	private Context context=null;
	String titleColor="#7B68EE";
	String infoColor="#7B68EE";
	String linColor="#FFFFFF";
	
	public MyDoorAdapter(Context context,List<Map<String,Object>> list,int resourse,String[] from,int[] to) {
		
		super(context,list,resourse,from,to);
		this.list=list;
	}
	
	public void setTitleColor(String titleColor)
	{
		this.titleColor=titleColor;
	}
	
	public void setInfoColor(String infoColor)
	{
		this.infoColor=infoColor;
	}
	public void setLinColor(String linColor)
	{
		this.linColor=linColor;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		
		  View view =  super.getView(position, convertView, parent);
		  view.setTag(0);
		  
		  if(view==null) return null;
		  
		  Map<String,Object> map=list.get(position);
		  
		  LinearLayout linearLayout=(LinearLayout) view.findViewById(R.id.lin_door);
		  linearLayout.setBackgroundColor(Color.parseColor(linColor)); 
		  TextView title=(TextView) view.findViewById(R.id.tv_doorID);
		  title.setTextColor(Color.parseColor(titleColor));
		 // title.setText((String)map.get("time"));
		 		  
		  return view;
	}
	
	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
