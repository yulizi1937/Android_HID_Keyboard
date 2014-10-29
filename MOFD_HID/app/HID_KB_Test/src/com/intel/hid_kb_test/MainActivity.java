package com.intel.hid_kb_test;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	public RecordDatabase db = null;
	public Cursor mCursor = null;
	ListView lv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		db = new RecordDatabase(this);
		
		onRecordShow();
	}
	
	protected void onResume() {
		super.onResume();
		onRecordShow();
		
	}
	
	public void onRecordShow() {
		
		lv = (ListView) findViewById(R.id.listView_record);
		
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		
		mCursor = db.select("Email_Accounts");
		for(int i=0;i<mCursor.getCount();i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			mCursor.moveToPosition(i);
			
			map.put("CursorID", i);
			map.put("Database", db);
			map.put("TableName", "Email_Accounts");
			map.put("mCursor", mCursor);
			map.put("ItemIcon", R.drawable.email);
			map.put("ItemRemove", R.drawable.removefromdatabase);
			map.put("ItemTitle", mCursor.getString(1));
			map.put("ItemText", "Email:"+mCursor.getString(2));
			listItem.add(map);
		}
		mCursor = db.select("PC_Logins");
		for(int i=0;i<mCursor.getCount();i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			mCursor.moveToPosition(i);
			
			map.put("CursorID", i);
			map.put("Database", db);
			map.put("TableName", "PC_Logins");
			map.put("mCursor", mCursor);
			map.put("ItemIcon", R.drawable.unlock);
			map.put("ItemRemove", R.drawable.removefromdatabase);
			map.put("ItemTitle", mCursor.getString(1));
			map.put("ItemText", "User:"+mCursor.getString(2));
			listItem.add(map);
		}
		
		RecordAdapter mSimpleAdapter = new RecordAdapter(this,
														listItem,
														R.layout.item,
														new String[]{"ItemIcon", "ItemTitle", "ItemText", "ItemRemove"},
														new int[]{R.id.ItemIcon, R.id.ItemTitle, R.id.ItemText, R.id.ItemRemove});
		lv.setAdapter(mSimpleAdapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent_sendmsg = new Intent(MainActivity.this, SendMessageActivity.class);
				//Log.d("arg3", "arg2:"+arg2+"ID:"+arg3);
				ListView listview = (ListView) arg0;
				
				@SuppressWarnings("unchecked")
				HashMap<String, Object> data = (HashMap<String, Object>) listview.getItemAtPosition(arg2);
				
				int CursorID = Integer.parseInt(data.get("CursorID").toString());
				intent_sendmsg.putExtra("CursorID", CursorID);
				
				String TableName = (String) data.get("TableName".toString());
				intent_sendmsg.putExtra("TableName", TableName);
				
				startActivity(intent_sendmsg);
			}
		});
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);
		switch(item.getItemId()){
		case R.id.action_genpass:
			Intent intent_genpass = new Intent(MainActivity.this, GenPasswordActivity.class);
			startActivity(intent_genpass);
			break;
		case R.id.action_newrecord:
			Intent intent_newrecord = new Intent(MainActivity.this, NewRecordActivity.class);
			startActivity(intent_newrecord);
			break;
		case R.id.action_settings:
			break;
		
		}
		return true;
		
	}
	
	public void RemoveFromDB(View view) {
		Log.i("Remove", "Remove");
	}

}
