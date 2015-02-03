package com.nwnu.lucky.model;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HouseWorkService {
	private DataBaseHelper mDataBaseHelper;
	private SQLiteDatabase db;

	public HouseWorkService(Context mContext) {
		super();
		mDataBaseHelper = new DataBaseHelper(mContext);
	}

	/**
	 * ��hw_selected��ӵ�����Ŀ
	 * 
	 * @param name
	 */
	public void add_selected(Integer hwsid,String name) {
		if(!db.isOpen()){
			db = mDataBaseHelper.getWritableDatabase();
		}
		db.execSQL("INSERT INTO hw_selected(hwsid,name) values(?,?)",
				new Object[] { hwsid,name });
		db.close();
	}

	/**
	 * ��hw_selected����ָ��id��Ŀ
	 * 
	 * @param id
	 * @param name
	 */
	public void update_selected(Integer id, String name) {
		db = mDataBaseHelper.getWritableDatabase();
		db.execSQL("UPDATE hw_selected set name=? WHERE id=?", new Object[] {
				name, id });
		db.close();
	}

	/**
	 * ��hw_selected��ѯָ��id��Ŀ
	 * 
	 * @param id
	 * @return
	 */
	public String find_selected(Integer id) {
		db = mDataBaseHelper.getReadableDatabase();
		String res = null;
		Cursor cursor = db.rawQuery("SELECT name FROM hw_selected WHERE id=?",
				new String[] { id + "" });
		// ������¼��
		if (cursor.moveToNext()) {
			res = cursor.getString(cursor.getColumnIndex("name"));
		}
		db.close();
		return res;
	}

	/**
	 * ��hw_selectedɾ��ָ��di��Ŀ
	 * 
	 * @param id
	 */
	public void delete_selected(Integer id) {
		db = mDataBaseHelper.getWritableDatabase();
		db.execSQL("delete from hw_selected WHERE id=?", new Object[] { id });
		db.close();
	}
	/**
	 * ��hw_selectedɾ������ָ��id��Ŀ
	 * 
	 * @param id
	 */
	public void delete_selected(ArrayList<Integer> ids) {
		db = mDataBaseHelper.getWritableDatabase();
		for (int i = 0; i < ids.size(); i++) {
			db.execSQL("delete from hw_selected WHERE id=?", new Object[] { ids.get(i) });
		}
		db.close();
	}

	/**
	 * ��hw_selected��ƫ����6 ��ҳ����
	 * 
	 * @param firstIndex
	 * @param offset
	 * @return
	 */
	public ArrayList<String> getScrollData_selected(int firstIndex, int offset) {
		ArrayList<String> res = new ArrayList<String>();
		db = mDataBaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select name from hw_selected limit ? offset ?", new String[] {
						offset + "", firstIndex + "" });
		while (cursor.moveToNext()) {
			res.add(cursor.getString(cursor.getColumnIndex("name")));
		}
		cursor.close();
		return res;
	}

	/**
	 * ��hw_selected��ƫ����6 ��ҳ����
	 * 
	 * @param firstIndex
	 * @param offset
	 * @return
	 */
	public ArrayList<Integer> getScrollDataID_selected(int firstIndex,
			int offset) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		db = mDataBaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select id from hw_selected limit ? offset ?", new String[] {
						offset + "", firstIndex + "" });
		while (cursor.moveToNext()) {
			res.add(cursor.getInt(cursor.getColumnIndex("id")));
		}
		cursor.close();
		return res;
	}

	/**
	 * ��hw_selected��ȡ��������ID
	 * 
	 * @return
	 */
	public ArrayList<Integer> getAllDataID_selected() {
		ArrayList<Integer> res = new ArrayList<Integer>();
		db = mDataBaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select id from hw_selected", null);
		while (cursor.moveToNext()) {
			res.add(cursor.getInt(cursor.getColumnIndex("id")));
		}
		cursor.close();
		return res;
	}
	/**
	 * ��hw_selected��ȡ��������hwsid
	 * 
	 * @return
	 */
	public ArrayList<Integer> getAllDataHWSID_selected() {
		ArrayList<Integer> res = new ArrayList<Integer>();
		db = mDataBaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select hwsid from hw_selected", null);
		while (cursor.moveToNext()) {
			res.add(cursor.getInt(cursor.getColumnIndex("hwsid")));
		}
		cursor.close();
		return res;
	}
	/**
	 * ��hw_selected��ȡ��������
	 * 
	 * @return
	 */
	public ArrayList<String> getAllData_selected() {
		ArrayList<String> res = new ArrayList<String>();
		db = mDataBaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select name from hw_selected", null);
		while (cursor.moveToNext()) {
			res.add(cursor.getString(cursor.getColumnIndex("name")));
		}
		cursor.close();
		return res;
	}

	/**
	 * ��hw_selected��ȡ�ܼ�¼��
	 * 
	 * @return
	 */
	public long getCount_selected() {
		db = mDataBaseHelper.getReadableDatabase();
		// û��ռλ����ʱ����Ϊ��null
		Cursor cursor = db.rawQuery("select count(*)from hw_selected", null);
		cursor.moveToFirst();
		long count = cursor.getLong(0);
		cursor.close();
		return count;
	}

	/**
	 * *************************************************************************
	 * ��hw_houseworks ��ز���
	 */
	/**
	 * ��hw_houseworks�����Ŀ
	 * 
	 * @param name
	 */
	public void add_hourseworks(String name) {
		db = mDataBaseHelper.getWritableDatabase();
		db.execSQL("INSERT INTO hw_houseworks(name) values(?)",
				new Object[] { name });
		db.close();
	}

	/**
	 * ��hw_houseworksɾ��ָ��di��Ŀ
	 * 
	 * @param id
	 */
	public void delete_hourseworks(Integer id) {
		db = mDataBaseHelper.getWritableDatabase();
		db.execSQL("delete form hw_houseworks WHERE id=?", new Object[] { id });
		db.close();
	}

	/**
	 * ��hw_houseworks��ȡ��������
	 * 
	 * @return
	 */
	public ArrayList<String> getAllData_hourseworks() {
		ArrayList<String> res = new ArrayList<String>();
		db = mDataBaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select name from hw_houseworks", null);
		while (cursor.moveToNext()) {
			res.add(cursor.getString(cursor.getColumnIndex("name")));
		}
		cursor.close();
		return res;
	}

	/**
	 * ��hw_houseworks��ȡ��������ID
	 * 
	 * @return
	 */
	public ArrayList<Integer> getAllDataID_hourseworks() {
		ArrayList<Integer> res = new ArrayList<Integer>();
		db = mDataBaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select id from hw_houseworks", null);
		while (cursor.moveToNext()) {
			res.add(cursor.getInt(cursor.getColumnIndex("id")));
		}
		cursor.close();
		return res;
	}
	/**
	 * ��hw_houseworks��ȡ�ܼ�¼��
	 * 
	 * @return
	 */
	public long getCount_hourseworks() {
		db = mDataBaseHelper.getReadableDatabase();
		// û��ռλ����ʱ����Ϊ��null
		Cursor cursor = db.rawQuery("select count(*)from hw_houseworks", null);
		cursor.moveToFirst();
		long count = cursor.getLong(0);
		cursor.close();
		return count;
	}
	/**
	 * *************************************************************************
	 * ���ű���ز���
	 */
	/**
	 * ���ѡ�hw_selected
	 * @param listID
	 * @param list
	 */
	public void addToGrids(ArrayList<Integer> listID){
		db = mDataBaseHelper.getReadableDatabase();
		String name;
		for (int i = 0; i < listID.size(); i++) {
			Cursor cursor = db.rawQuery("SELECT name FROM hw_houseworks WHERE id=?",
					new String[] { listID.get(i) + "" });
			// ������¼��
			if (cursor.moveToNext()) {
				name = cursor.getString(cursor.getColumnIndex("name"));
				db.execSQL("INSERT INTO hw_selected(hwsid,name) values(?,?)",
						new Object[] { listID.get(i),name });
			}
		}
		db.close();
	}
	/**
	 * ɾ������ �����ű���
	 * @param listID
	 */
	public void deleteReal(ArrayList<Integer> listID){
		db = mDataBaseHelper.getReadableDatabase();
		for (int i = 0; i < listID.size(); i++) {
			db.execSQL("delete from hw_houseworks WHERE id=?", new Object[] { listID.get(i) });
			db.execSQL("delete from hw_selected WHERE hwsid=?", new Object[] { listID.get(i) });
		}
		db.close();
	}
	
}
