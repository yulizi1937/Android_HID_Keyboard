package com.intel.hid_kb_test;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class EditRecordActivity extends Activity{
	
	private String[] RecordTypes = {"Email Accounts",
    								"PC Logins"};
	private RecordDatabase db;
	private Cursor mCursor;
	private int CursorID;
	private String TableName;
	private Spinner Spin_Rec = null;
	private String Table = null;
	
	private Intent intentBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newrecord);
		db = new RecordDatabase(this);
		SpinnerSetup();
	}
	
	void SpinnerSetup() {
		
		Intent intent = getIntent();
		intentBack = intent;
		CursorID = intent.getIntExtra("CursorID", 0);
		TableName = intent.getStringExtra("TableName");
		Table = TableName;
		
		mCursor = db.select(TableName);
		mCursor.moveToPosition(CursorID);
		
		Spin_Rec = (Spinner) findViewById(R.id.spinner_newrecord);
		
		ArrayAdapter<String> recordtypes = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,RecordTypes);
		Spin_Rec.setAdapter(recordtypes);
		
		if (TableName.matches("Email_Accounts")) {
			Spin_Rec.setSelection(0);
		} else {
			Spin_Rec.setSelection(1);
		}
		
		load_data();
		
		Spin_Rec.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String str = arg0.getItemAtPosition(position).toString();
				TextView tv1 = (TextView) findViewById(R.id.TextView1);
				if (str.matches(RecordTypes[0])) {
					Table = "Email_Accounts";
					tv1.setText("Email:");
				} else {
					Table = "PC_Logins";
					tv1.setText("Username:");
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void load_data() {
		EditText et_desc = (EditText) findViewById(R.id.editText_desc);
		EditText et1 = (EditText) findViewById(R.id.editText1);
		EditText et2 = (EditText) findViewById(R.id.editText2);
		
		et_desc.setText(mCursor.getString(1));
		et1.setText(mCursor.getString(2));
		et2.setText(mCursor.getString(3));
	}
	
	public void Record_save(View view) {
		mCursor = db.select(Table);
		mCursor.moveToPosition(CursorID);
		EditText et_desc = (EditText) findViewById(R.id.editText_desc);
		EditText et1 = (EditText) findViewById(R.id.editText1);
		EditText et2 = (EditText) findViewById(R.id.editText2);
		if(Table.matches(TableName)) {
			db.update(Table, mCursor.getInt(0),et_desc.getText().toString(), et1.getText().toString(), et2.getText().toString());
		}else {
			mCursor = db.select(TableName);
			mCursor.moveToPosition(CursorID);
			db.delete(TableName, mCursor.getInt(0));
			db.insert(Table, et_desc.getText().toString(), et1.getText().toString(), et2.getText().toString());
			mCursor = db.select(Table);
			mCursor.moveToLast();
			Log.i("SendMessageA","CursorID:"+CursorID+",pos"+mCursor.getPosition());
			intentBack.putExtra("CursorID", mCursor.getPosition());
			intentBack.putExtra("TableName", Table);
		}
		
		Toast.makeText(this, "Record Updated!", Toast.LENGTH_LONG).show();
		
		setResult(RESULT_OK, intentBack);
		EditRecordActivity.this.finish();
	}
	
	public void Cancle_NewRecord(View view) {
		EditRecordActivity.this.finish();
	}

}
