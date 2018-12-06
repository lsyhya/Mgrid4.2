package com.mgrid.main.face.facelistview;

import java.util.List;

import com.mgrid.main.R;
import com.mgrid.uncaughtexceptionhandler.MyApplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter extends BaseAdapter{

	LayoutInflater inflater;
	List<FaceBean> datas;
	Context  context;
	
	public MyAdapter(Context context, List<FaceBean> datas) {
		
		inflater=LayoutInflater.from(context);
		this.datas=datas;
		this.context=context;
		
	}
	
	@Override
	public int getCount() {
		
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder=null;
		if(convertView==null)
		{
			convertView=inflater.inflate(R.layout.faceitem, parent, false);
			holder=new ViewHolder();
			holder.tv=(TextView) convertView.findViewById(R.id.facetv);
			holder.img=(ImageView) convertView.findViewById(R.id.faceimg);
			holder.btn=(Button) convertView.findViewById(R.id.facebtn);
			convertView.setTag(holder);
			
		}else
		{
			holder=(ViewHolder) convertView.getTag();
		}
		
		
		holder.tv.setText(datas.get(position).name);
		holder.img.setImageBitmap(datas.get(position).bitmap);
		holder.btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				 String name = MyApplication.mFaceDB.mRegister.get(position).mName;
				 MyApplication.mFaceDB.delete(name);				 
				 datas.remove(position);
				 notifyDataSetChanged();
			}
		});
		
		
		return convertView;
	}
	
	
	private class ViewHolder
	{
		TextView tv;
		ImageView img;
		Button btn;
	}
	

}
