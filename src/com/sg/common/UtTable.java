package com.sg.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.widget.ListView;

import com.sg.common.UtTableAdapter.TableCell;
import com.sg.common.UtTableAdapter.TableRow;

public class UtTable extends ListView {
	public UtTable(Context context) {
		super(context);
		m_tableAdapter = new UtTableAdapter(this.getContext());
		this.setAdapter(m_tableAdapter);
	}

	public void notifyTableLayoutChange(int l, int t, int r, int b) {
		m_nLeft = l;
		m_nTop = t;
		m_nRight = r;
		m_nBottom = b;

		m_tableAdapter.notifyDataSetChanged();
		this.layout(m_nLeft, m_nTop, m_nRight, m_nBottom);
	}

	public void update() {
		m_tableAdapter.notifyDataSetChanged(); 
		this.layout(m_nLeft, m_nTop, m_nRight, m_nBottom
				- m_nLayoutBottomOffset);
		m_nLayoutBottomOffset = -m_nLayoutBottomOffset;
	}

	public void updateContends(List<String> listTitles,
			List<List<String>> listContends) {
		if (listTitles == null || listContends == null)  //如果标题或者内容为空 return；
			return;
		int column = listTitles.size(); //得到标题的个数
		int width = (m_nRight - m_nLeft) / column; //得到每个标题的宽度
		
		int updatecount = m_bUseTitle ? listContends.size() + 1 : listContends
				.size();  //如果使用标题  列表行数为内容的数量+1，不然为内容数量。
		int count = Math.min(m_tableAdapter.getCount(), updatecount); //
		if (m_bUseTitle && count < 1) {//如果使用标题 并且count<1

			m_tableAdapter.m_cTexColor = m_cFontColor;

			TableCell[] titles = new TableCell[listTitles.size()];
			for (int i = 0; i < column; i++) {
				titles[i] = new TableCell(listTitles.get(i), width,
						LayoutParams.MATCH_PARENT, TableCell.STRING, 0x00000000);
			}
			m_tableAdapter.addRow(new TableRow(titles)); 		

		}

		if (m_bUseTitle) {

			for (int i = 1; i < count; ++i) {
				List<String> lst = listContends.get(i - 1);

				TableRow contendRow = m_tableAdapter.getItem(i);
				for (int j = 0; j < contendRow.getSize(); ++j) {
					TableCell cell = contendRow.getCellValue(j);
					cell.value = lst.get(j);
					cell.width = width;
				}
			}
		
		} else {

			for (int i = 0; i < count; ++i) {
 
				if(i>=listContends.size())
					continue;
				List<String> lst = listContends.get(i);

				TableRow contendRow = m_tableAdapter.getItem(i);
				for (int j = 0; j < contendRow.getSize(); ++j) {
					TableCell cell = contendRow.getCellValue(j);
					cell.value = lst.get(j);
					cell.width = width;
				}
			}
			

		}

		if (m_tableAdapter.getCount() < updatecount) {

			int i = m_bUseTitle ? count - 1 : count;
			int lstcontendsize = listContends.size();
			m_tableAdapter.m_cTexColor = m_cFontColor;

			if (i < 0)
				i = 0;
			for (; i < lstcontendsize; i++) {
				List<String> lst = listContends.get(i);

				TableCell[] cells = new TableCell[listTitles.size()];
				int cColor = i % 2 == 0 ? m_cEvenRowBackground
						: m_cOddRowBackground;
				for (int j = 0; j < lst.size(); ++j) {
					cells[j] = new TableCell(lst.get(j), width,
							LayoutParams.MATCH_PARENT, TableCell.STRING, cColor);
				}

				m_tableAdapter.addRow(new TableRow(cells));

			}

		}

		// 调整水位
		m_tableAdapter.m_nWaterMarker = updatecount;

	}

	public void setFontColor(int cColor) {
		m_cFontColor = cColor;
	}

	public static String getDate(long milliSeconds, String dateFormat) {
		if (0 == milliSeconds)
			return ""; // 0 表示未获取到有效时间 -- CharlesChen

		DateFormat formatter = new SimpleDateFormat(dateFormat);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return new String(formatter.format(calendar.getTime()));
	}

	// params :
	protected UtTableAdapter m_tableAdapter = null;

	protected int m_nLeft = 0;
	protected int m_nTop = 0;
	protected int m_nRight = 0;
	protected int m_nBottom = 0;

	protected int m_nTableWidth = 0;
	protected int m_nTableHeight = 0;

	protected int m_cFontColor = Color.GREEN;
	public int m_cOddRowBackground = 0xFF000000; // 奇数 
	public int m_cEvenRowBackground =0xFF000000; // 偶数
	public boolean m_bUseTitle = true;
	int m_nLayoutBottomOffset = 1; // 动态调节layout大小
}
