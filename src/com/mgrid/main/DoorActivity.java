package com.mgrid.main;

import java.util.ArrayList;
import java.util.List;

import com.sg.uis.myfragment.TabFragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class DoorActivity extends AppCompatActivity {

	TabLayout table;
	ViewPager viewPager;

	private List<Fragment> list;
	private MyAdapter adapter;
	private String[] titles = { "查询", "控制", "授权" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		table = (TabLayout) findViewById(R.id.tab_layout2);
		viewPager = (ViewPager) findViewById(R.id.viewpager);

		
		list = new ArrayList<>();
        list.add(new TabFragment("Tab1"));
        list.add(new TabFragment("Tab2"));
        list.add(new TabFragment("Tab3"));
        //ViewPager的适配器
        adapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //绑定
        table.setupWithViewPager(viewPager);
		// table.addTab(table.newTab().setText("查询").setIcon(R.mipmap.ic_launcher));
		// table.addTab(table.newTab().setText("控制"));
		// table.addTab(table.newTab().setText("授权"));

	}

	class MyAdapter extends FragmentPagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		// 重写这个方法，将设置每个Tab的标题
		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}
	}

}