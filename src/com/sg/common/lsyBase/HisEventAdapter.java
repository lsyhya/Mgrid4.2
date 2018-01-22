package com.sg.common.lsyBase;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sg.common.AlwaysMarqueeTextView;

public class HisEventAdapter extends BaseAdapter{

	 public HisEventAdapter(Context context,ArrayList<TableRow> table) {
	        this.context = context;
	        this.table = table;
	    }
	    
	    @Override  
	    public int getCount() {
	    	
	    	return table.size();
	    }
	    
	    @Override  
	    public long getItemId(int position) {
	        return position;
	    }
	    
	    public TableRow getItem(int position) {
	        return table.get(position);
	    }
	    
	    public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView)
			{
				TableRow tableRow = table.get(position);
				return new TableRowView(this.context, tableRow);
			}
			
			((TableRowView) convertView).updatavalue(table.get(position));
			return convertView;
	    }
	    
	    public void addRow(TableRow row) {
	    	table.add(row);
	    }
	   
	    public void cleanUp() {
	    	table.clear();
	    }
	    
	    /** 
	     * TableRowView 实现表格行的样式 
	     * @author  
	     */  
		class TableRowView extends LinearLayout
		{
			public TableRowView(Context context, TableRow tableRow)
			{
				super(context);
				this.setOrientation(LinearLayout.HORIZONTAL);
				
				mlstCellView = new ArrayList<AlwaysMarqueeTextView>();
				for (int i = 0; i < tableRow.getSize(); i++)
				{
					
					TableCell tableCell = tableRow.getCellValue(i);
					if(tableCell==null) continue;
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(tableCell.width, tableCell.height);// 按照格单元指定的大小设置空间
					layoutParams.setMargins(0, 0, 1, 1);
					
					if (tableCell.type == TableCell.STRING)
					{				
						AlwaysMarqueeTextView textCell = new AlwaysMarqueeTextView(context);
						textCell.setTextColor(m_cTexColor);
						textCell.setGravity(Gravity.CENTER);
						textCell.setBackgroundColor(tableCell.cRowColor);
						textCell.setText(String.valueOf(tableCell.value));
						addView(textCell, layoutParams);
						mlstCellView.add(textCell);
					} else if (tableCell.type == TableCell.IMAGE)
					{
						
						ImageView imgCell = new ImageView(context);
						imgCell.setBackgroundColor(Color.GRAY);
						imgCell.setImageResource((Integer) tableCell.value);
						addView(imgCell, layoutParams);
					}
				}
				
				this.setBackgroundColor(Color.WHITE);
			}
			
			public void updatavalue(TableRow tableRow)
			{
				int count = tableRow.getSize();
				for (int i = 0; i < count; i++)
				{
					TableCell tableCell = tableRow.getCellValue(i);
					if(i>=mlstCellView.size())
					return;
					AlwaysMarqueeTextView cellview = mlstCellView.get(i);
					cellview.setBackgroundColor(tableCell.cRowColor);
					cellview.setText(String.valueOf(tableCell.value));
					
				}
			}
			
			private List<AlwaysMarqueeTextView> mlstCellView = null;
		}

	    /** 
	     * TableRow 实现表格的行 
	     * @author  
	     */  
		static public class TableRow
		{
			private TableCell[] cell;

			public TableRow(TableCell[] cell)
			{
				this.cell = cell;
			}

			public int getSize() {
				return cell.length;
			}

			public TableCell getCellValue(int index) {
				if (index >= cell.length)
					return null;
				return cell[index];
			}
		}
	    /** 
	     * TableCell 实现表格的格单元 
	     * @author  
	     */  
		static public class TableCell
		{
			static public final int STRING = 0;
			static public final int IMAGE = 1;
			public Object value;
			public int width;
			public int height;
			public int type;
			public int cRowColor;

			public TableCell(Object value, int width, int height, int type, int rowcolor)
			{
				this.value = value;
				this.width = width;
				this.height = height;
				this.type = type;
				this.cRowColor = rowcolor;
			}
		}
	    
	    private Context context;
	    public int m_cTexColor = Color.GREEN;
	    
	   
	    private List<TableRow> table;


}
