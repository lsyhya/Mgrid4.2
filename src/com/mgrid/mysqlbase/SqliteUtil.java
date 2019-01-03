package com.mgrid.mysqlbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mgrid.data.oid.MyOID;
import com.mgrid.main.user.UserEvent;
import com.sg.common.lsyBase.MyDoorEvent;
import com.sg.common.lsyBase.MyDoorUser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqliteUtil {

	private Context context;
	private List<MyDoorUser> listUs = new ArrayList<>();

	private List<Map<String, Object>> lists = new ArrayList<>();
	private static SQLiteDatabase sql;

	public SqliteUtil(Context context) {

		this.context = context;
	}

	public SQLiteDatabase openorgetSql() {

		if (sql == null) {

			MySqlBase base = new MySqlBase(context);
			sql = base.getWritableDatabase();
		}

		return sql;
	}

	/**
	 * 方法增 增加用户
	 */
	public void addUserValue(MyDoorUser my) {
		ContentValues values = new ContentValues();

		values.put("UserName", my.getName());
		values.put("CardId", my.getCardid());
		values.put("UserId", my.getUid());
		values.put("PassWord", my.getPw());
		values.put("Time", my.getTime());

		if (sql != null)
			sql.insert(MySqlBase.doorTable, null, values);
	}

	/**
	 * 语法增 增加用户
	 */

	public void addUsersValues(MyDoorUser my) {

		// String sql1="insert into " + SQLITE.TABLE_NAME + " (oid, digit, mean) values
		// ('10', '13', '0.0')";
		String exe = "insert OR IGNORE into " + MySqlBase.doorTable
				+ " (UserName,CardId,UserId,PassWord,Time) values ('" + my.getName() + "','" + my.getCardid() + "','"
				+ my.getCardid() + "','" + my.getPw() + "','" + my.getTime() + "')";

		if (sql != null)
			sql.execSQL(exe);

	}

	/**
	 * 添加记录
	 * 
	 * @param my
	 */

	public void addEventValue(MyDoorEvent my) {
		ContentValues values = new ContentValues();

		values.put("CardId", my.getCardid());
		values.put("Time", my.getTime());
		values.put("Event", my.getEvent());

		if (sql != null)
			sql.insert(MySqlBase.doorEventTable, null, values);
	}

	/**
	 * 添加记录
	 * 
	 * @param my
	 */

	public void addEventValue(MyDoorEvent my, int check) {
		ContentValues values = new ContentValues();

		values.put("CardId", my.getCardid());
		values.put("Time", my.getTime());
		values.put("Event", my.getEvent());
		values.put("CheckData", check);

		if (sql != null)
			sql.insert(MySqlBase.doorEventTable, null, values);
	}

	/**
	 * 添加电控锁记录
	 * 
	 * @param my
	 */

	public void addXuNiEventValue(UserEvent my, int check) {
		ContentValues values = new ContentValues();

		values.put("CardId", my.getUid());
		values.put("Time", my.getTime());
		values.put("Event", my.getEvent());
		values.put("EventResult", my.getEventresult());
		values.put("CheckData", check);

		if (sql != null)
			sql.insert(MySqlBase.doorXuNiEventTable, null, values);
	}

	/**
	 * 查询用户
	 */

	public List<Map<String, Object>> setListValues(List<Map<String, Object>> list, List<MyDoorUser> listU) {

		setvalue(list, listU);
		return list;
	}

	private void setvalue(List<Map<String, Object>> list, List<MyDoorUser> listU) {
		list.clear();
		listU.clear();
		if (sql != null) {

			Cursor cursor = sql.rawQuery("select * from " + MySqlBase.doorTable, null);
			while (cursor.moveToNext()) {

				String name = cursor.getString(cursor.getColumnIndex("UserName"));
				String CID = cursor.getString(cursor.getColumnIndex("CardId"));
				String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
				String PassWord = cursor.getString(cursor.getColumnIndex("PassWord"));
				String Time = cursor.getString(cursor.getColumnIndex("Time"));

				MyDoorUser my = new MyDoorUser(name, CID, UserId, PassWord, Time);
				listU.add(my);

				Map<String, Object> hh = new HashMap<String, Object>();
				hh.put("text", "用户:" + name);
				hh.put("text1", " 卡号:" + CID);
				hh.put("text2", " 有效时间:" + Time);
				list.add(hh);

			}
		}

	}

	/**
	 * 得到用户list
	 */
	public List<MyDoorUser> getUser() {
		setvalue(lists, listUs);
		return listUs;
	}

	/**
	 * 得到所有记录
	 * 
	 * @return
	 */

	public List<MyDoorEvent> getListValues() {
		List<MyDoorEvent> list = new ArrayList<MyDoorEvent>();
		Cursor cursor = sql.rawQuery("select * from " + MySqlBase.doorEventTable, null);
		while (cursor.moveToNext()) {

			String CID = cursor.getString(cursor.getColumnIndex("CardId"));
			String time = cursor.getString(cursor.getColumnIndex("Time"));
			String event = cursor.getString(cursor.getColumnIndex("Event"));
			String id = cursor.getString(cursor.getColumnIndex("id"));
			String checkData = cursor.getString(cursor.getColumnIndex("CheckData"));
			MyDoorEvent my = new MyDoorEvent(CID, time, event);
			list.add(my);

			if (checkData.equals("0"))
				;
			{
				String exe = "update " + MySqlBase.doorEventTable + " set CheckData = 1 where id = " + id;
				// 执行SQL
				if (sql != null)
					sql.execSQL(exe);
			}

		}

		return list;

	}

	/**
	 * 得到未查看的记录
	 * 
	 * @return
	 */
	public List<MyDoorEvent> getNowListValues() {
		List<MyDoorEvent> list = new ArrayList<MyDoorEvent>();
		Cursor cursor = sql.query(MySqlBase.doorEventTable, null, "CheckData=?", new String[] { "0" }, null, null,
				null);
		while (cursor.moveToNext()) {

			String id = cursor.getString(cursor.getColumnIndex("id"));
			String CID = cursor.getString(cursor.getColumnIndex("CardId"));
			String time = cursor.getString(cursor.getColumnIndex("Time"));
			String event = cursor.getString(cursor.getColumnIndex("Event"));
			MyDoorEvent my = new MyDoorEvent(CID, time, event);
			list.add(my);

			String exe = "update " + MySqlBase.doorEventTable + " set CheckData = 1 where id = " + id;
			// 执行SQL
			if (sql != null)
				sql.execSQL(exe);

		}

		return list;

	}

	/**
	 * 时间段查询
	 * 
	 * @return
	 */

	public List<MyDoorEvent> getListValues(String start, String end) {
		List<MyDoorEvent> list = new ArrayList<MyDoorEvent>();
		Cursor cursor = sql.rawQuery("select * from " + MySqlBase.doorEventTable + " where Time>=? and Time<=?",
				new String[] { start, end });

		while (cursor.moveToNext()) {

			String CID = cursor.getString(cursor.getColumnIndex("CardId"));
			String time = cursor.getString(cursor.getColumnIndex("Time"));
			String event = cursor.getString(cursor.getColumnIndex("Event"));
			MyDoorEvent my = new MyDoorEvent(CID, time, event);

			list.add(my);
		}
		return list;

	}

	/**
	 * 时间段查询电控锁门禁记录
	 * 
	 * @return
	 */

	public List<UserEvent> getXuNiListValues(String start, String end) {
		List<UserEvent> list = new ArrayList<UserEvent>();
		Cursor cursor = sql.rawQuery("select * from " + MySqlBase.doorXuNiEventTable + " where Time>=? and Time<=?",
				new String[] { start, end });

		while (cursor.moveToNext()) {

			String CID = cursor.getString(cursor.getColumnIndex("CardId"));
			String time = cursor.getString(cursor.getColumnIndex("Time"));
			String event = cursor.getString(cursor.getColumnIndex("Event"));
			String eventresult = cursor.getString(cursor.getColumnIndex("EventResult"));
			UserEvent my = new UserEvent(CID, time, event, eventresult);

			list.add(my);
		}
		return list;

	}

	/**
	 * 得到所有记录
	 * 
	 * @return
	 */

	public List<UserEvent> getXuNiListValues() {
		List<UserEvent> list = new ArrayList<UserEvent>();
		Cursor cursor = sql.rawQuery("select * from " + MySqlBase.doorXuNiEventTable, null);
		while (cursor.moveToNext()) {

			String CID = cursor.getString(cursor.getColumnIndex("CardId"));
			String time = cursor.getString(cursor.getColumnIndex("Time"));
			String event = cursor.getString(cursor.getColumnIndex("Event"));
			String id = cursor.getString(cursor.getColumnIndex("id"));
			String checkData = cursor.getString(cursor.getColumnIndex("CheckData"));
			String eventR = cursor.getString(cursor.getColumnIndex("EventResult"));
			UserEvent my = new UserEvent(CID, time, event, eventR);

			list.add(my);
			if (checkData.equals("0"))
				;
			{
				String exe = "update " + MySqlBase.doorXuNiEventTable + " set CheckData = 1 where id = " + id;
				// 执行SQL
				if (sql != null)
					sql.execSQL(exe);
			}

		}

		return list;

	}

	/**
	 * 得到未查看的记录
	 * 
	 * @return
	 */
	public List<UserEvent> getXuNiNowListValues() {
		List<UserEvent> list = new ArrayList<UserEvent>();
		Cursor cursor = sql.query(MySqlBase.doorXuNiEventTable, null, "CheckData=?", new String[] { "0" }, null, null,
				null);
		while (cursor.moveToNext()) {

			String CID = cursor.getString(cursor.getColumnIndex("CardId"));
			String time = cursor.getString(cursor.getColumnIndex("Time"));
			String event = cursor.getString(cursor.getColumnIndex("Event"));
			String id = cursor.getString(cursor.getColumnIndex("id"));
			String eventR = cursor.getString(cursor.getColumnIndex("EventResult"));
			UserEvent my = new UserEvent(CID, time, event, eventR);
			list.add(my);

			String exe = "update " + MySqlBase.doorXuNiEventTable + " set CheckData = 1 where id = " + id;
			// 执行SQL
			if (sql != null)
				sql.execSQL(exe);

		}

		return list;

	}

	/*
	 * 根据ID删除用户
	 */
	public void deleteValue(String str) {
		// String exe="delete from "+MySqlBase.doorTable+"where CardId="+str;
		// sql.execSQL(exe);
		if (sql != null)
			sql.delete(MySqlBase.doorTable, "CardId=?", new String[] { str });

	}

	/*
	 * 判断CID是否存在
	 */
	public boolean getUserValue(String CID) {

		Cursor CS = null;

		if (sql != null)
			CS = sql.query(MySqlBase.doorTable, null, "CardId=?", new String[] { CID }, null, null, null);

		if (CS != null && CS.getCount() > 0) {
			return false;
		} else {
			return true;
		}

	}

	public MyDoorUser getUser(String cid) {

		Cursor cursor = null;

		if (sql != null)
			cursor = sql.query(MySqlBase.doorTable, null, "CardId=?", new String[] { cid }, null, null, null);

		while (cursor != null && cursor.moveToNext()) {

			String name = cursor.getString(cursor.getColumnIndex("UserName"));
			String CID = cursor.getString(cursor.getColumnIndex("CardId"));
			String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
			String PassWord = cursor.getString(cursor.getColumnIndex("PassWord"));
			String Time = cursor.getString(cursor.getColumnIndex("Time"));
			MyDoorUser my = new MyDoorUser(name, CID, UserId, PassWord, Time);

			return my;

		}

		return null;

	}

	/*
	 * 删除所有用户
	 */
	public void cleanUserTable() {
		String exe = "delete from " + MySqlBase.doorTable;
		if (sql != null)
			sql.execSQL(exe);
	}

	/*
	 * 删除所有记录
	 */
	public void cleanEventTable() {
		String exe = "delete from " + MySqlBase.doorEventTable;
		if (sql != null)
			sql.execSQL(exe);
	}

	/*
	 * 删除所有记录
	 */
	public void cleanXuniEventTable() {
		String exe = "delete from " + MySqlBase.doorXuNiEventTable;
		if (sql != null)
			sql.execSQL(exe);
	}

	/**
	 * 判断表是否存在
	 * 
	 * @param db
	 * @param tableName
	 * @return
	 */
	public boolean tabbleIsExist(SQLiteDatabase db, String tableName) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='" + tableName.trim()
					+ "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

		} catch (Exception e) {

		}
		return result;
	}

	/*
	 * 删除表的所有记录
	 */
	public void cleanTable(String tableName) {
		String exe = "delete from " + tableName;
		if (sql != null)
			sql.execSQL(exe);
	}

	// 添加OID Name Value

	public void addOIDValue(MyOID my) {
		ContentValues values = new ContentValues();

		values.put("oid", my.getOid());
		values.put("Name", my.getName());
		values.put("Value", my.getValue());

		if (sql != null)
			sql.insert(MySqlBase.EQUIP_SIGNAL_OID, null, values);
	}

	// 添加OID Name Value
	public void addOIDValueInSql(MyOID my) {

		String exe = "insert OR IGNORE into " + MySqlBase.EQUIP_SIGNAL_OID + " (oid,Name,Value) values ('" + my.getOid()
				+ "','" + my.getName() + "','" + my.getValue() + "')";

		if (sql != null)
			sql.execSQL(exe);
	}

	// 根据OID 修改value

	public void setOIDValue(String oid, String value) {

		String exe = "update " + MySqlBase.EQUIP_SIGNAL_OID + " set Value='" + value + "' where oid='" + oid + "'";

		if (sql != null)
			sql.execSQL(exe);
	}

	// 根据OID 获取Value

	public String getOIDValue(String oid) {

		Cursor cursor = null;

		if (sql != null)

			cursor = sql.query(MySqlBase.EQUIP_SIGNAL_OID, null, "oid=?", new String[] { oid }, null, null, null);
		
		while (cursor != null && cursor.moveToNext()) {

			String Value = cursor.getString(cursor.getColumnIndex("Value"));						
			return Value;

		}

		return "";
	}

}
