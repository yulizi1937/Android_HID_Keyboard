package com.intel.hid_kb_test;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewRecordActivity extends Activity{
	
	private String[] RecordTypes = {"Email Accounts",
								    "PC Logins"};
	private RecordDatabase rd = null;
	public Cursor mCursor = null;
	private Spinner Spin_Rec = null;
	String Table = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newrecord);
		rd = new RecordDatabase(this);
		SpinnerSetup();
	}
	
	private void SpinnerSetup() {
		Spin_Rec = (Spinner) findViewById(R.id.spinner_newrecord);
		
		ArrayAdapter<String> recordtypes = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,RecordTypes);
		Spin_Rec.setAdapter(recordtypes);
		
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
	
	public void Record_save(View view) {
		mCursor = rd.select(Table);
		EditText et_desc = (EditText) findViewById(R.id.editText_desc);
		EditText et1 = (EditText) findViewById(R.id.editText1);
		EditText et2 = (EditText) findViewById(R.id.editText2);
		rd.insert(Table, et_desc.getText().toString(), et1.getText().toString(), et2.getText().toString());
		Toast.makeText(this, "Record Saved!", Toast.LENGTH_LONG).show();
		NewRecordActivity.this.finish();
	}
	
	public void Cancle_NewRecord(View view) {
		NewRecordActivity.this.finish();
	}

}
