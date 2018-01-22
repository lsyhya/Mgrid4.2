package com.mgrid.fuction;

import java.io.File;
import java.util.ArrayList;

import com.mgrid.main.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


//made author:fjw0312
//date:2016
//notice:
public class PhotoGridview extends GridView{

	//Fields
	Context m_contect;
	String path = "/mgrid/log/vtu_camera/";
	String[] fileName;
	ArrayList<String> list = new ArrayList<String>();  //图片路劲数组
	ArrayList<Bitmap> lstBitmap = new ArrayList<Bitmap>(); //图片数组
	public Bitmap select_bitmap;
	public String select_name = "";
	public boolean flag = false;
	
	public PhotoGridview(Context context) {
		super(context);
		m_contect = context;

		fileName = new File(path.trim()).list();
		
		
		for(int i=0;i<fileName.length;i++){
			String imageName = path+fileName[i].trim();	
			list.add(imageName);
			Bitmap bitmap = BitmapFactory.decodeFile(imageName); 	
			lstBitmap.add(bitmap);			
		} 
		
	
		ImageAdapter imageAda = new ImageAdapter(context,list); 
		this.setAdapter(imageAda);
		

		this.setNumColumns(3);

		

		this.setOnItemClickListener(new ItemClickListener());
	}
	

	public 	class ImageAdapter extends BaseAdapter{
		private Context context;
		ArrayList<String> lst = new ArrayList<String>();
		int count;
		ImageView imageview;
		public ImageAdapter(Context context, ArrayList<String> list){
			this.context = context;
			lst = list;
			count = lst.size();
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return count;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return lst.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub

			View view = View.inflate(context, R.layout.gridview, null);
			RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.gridCellID);
			ImageView l_imageview = (ImageView) layout.findViewById(R.id.itemImage);
			TextView  l_textview = (TextView) layout.findViewById(R.id.itemText);
			
			String a[] = lst.get(arg0).split("/");
			String strname = a[a.length-1];

			l_textview.setText(strname); 
			Bitmap bitmap = BitmapFactory.decodeFile(lst.get(arg0)); 
			l_imageview.setImageBitmap(bitmap);    
			l_imageview.setPadding(2, 2, 2, 2); 
			l_imageview.setScaleType(ImageView.ScaleType.FIT_XY);
		
			return layout;
		}
		
	}
	

	public 	class ItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub	

			select_bitmap = lstBitmap.get(arg2);
			select_name = list.get(arg2);
			flag = true;

		}
		
	}	


}
