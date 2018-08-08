package com.sg.uis.myfragment;

import com.mgrid.main.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabFragment extends Fragment{

	
	String id;
	
	public TabFragment(String id) {
		this.id=id;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		
		View view =inflater.inflate(R.layout.tab1,container, false);
		TextView tv= (TextView) view.findViewById(R.id.tv1);
		tv.setText(id);
		
		return view;
	}
}
