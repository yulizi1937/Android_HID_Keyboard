package com.intel.hid_kb_test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecordDatabase extends SQLiteOpenHelper{
	
	private final static String DATEBASE_NAME = "SecureInfo.db";
	private final static int DATABASE_VERSION = 1;
	private final static String[] TABLE_NAME = {"Email_Accounts",
												"PC_Logins"};
	
	public final static String Record_ID = "Record_ID";
	public final static String Desc = "Description";
	public final static String User = "User";
	public final static String Pass = "Password";

	public RecordDatabase(Context context) {
		super(context, DATEBASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql_email = "CREATE TABLE " + TABLE_NAME[0] + " (" 
					+ Record_ID + " INTEGER primary key autoincrement, "
					+ Desc + " TEXT, "
					+ User + " TEXT, "
					+ Pass + " TEXT);";
		db.execSQL(sql_email);
		
		String sql_logins = "CREATE TABLE " + TABLE_NAME[1] + " (" 
				+ Record_ID + " INTEGER primary key autoincrement, "
				+ Desc + " TEXT, "
				+ User + " TEXT, "
				+ Pass + " TEXT);";
		db.execSQL(sql_logins);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME[0]; 
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS " + TABLE_NAME[1]; 
		db.execSQL(sql);
		onCreate(db);
		
	}
	
	public Cursor select(String table_name) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(table_name, null, null, null, null, null, null);
		return cursor;
		
	}
	
	public long insert(String table, String desc, String user, String pass) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(Desc, desc);
		cv.put(User, user);
		cv.put(Pass, pass);
		long row = db.insert(table, null, cv);
		
		return row;
	}
	
	public void delete(String table, int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String where = Record_ID + " = ?";
		String[] whereValue = {Integer.toString(id)};
		
		db.delete(table, where, whereValue);
		
	}
	
	public void update(String table, int id, String desc, String user, String pass) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String where = Record_ID + " = ?";
		String[] whereValue = {Integer.toString(id)};
		ContentValues cv = new ContentValues();
		cv.put(Desc, desc);
		cv.put(User, user);
		cv.put(Pass, pass);
		
		db.update(table, cv, where, whereValue);
	}

}
