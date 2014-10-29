package com.intel.hid_kb_test;

import java.util.Arrays;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IHidKeyboardService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SendMessageActivity extends Activity{
	
	private static final int RequestCode = 1;
	private IHidKeyboardService HidKeyboardService = null;
	public RecordDatabase db = null;
	private Cursor mCursor = null;
	private int CursorID;
	private TextView tv1, tv2;
	private EditText edit;
	private String TableName;
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendmsg);
		
		Log.i("SendMessageA","onCreate");
		
		HidKeyboardService = IHidKeyboardService.Stub.asInterface(
				ServiceManager.getService("hidkeyboard"));
		
		db = new RecordDatabase(this);
		intent = getIntent();
		getData();
		onSendMsgShow();
		
	}
	
	protected void onResume() {
		super.onResume();
		//Log.i("SendMessageA","onResume");
		getData();
		onSendMsgShow();
		
	}
	
	public void onEditRecord(View view) {
		Intent intent_edit = new Intent(SendMessageActivity.this, EditRecordActivity.class);
		
		intent_edit.putExtra("CursorID", CursorID);
		intent_edit.putExtra("TableName", TableName);
		
		startActivityForResult(intent_edit, RequestCode);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//Log.i("SendMessageA","onActivityResult");
		switch (resultCode) {
		case RESULT_OK:
			intent.putExtra("CursorID", data.getIntExtra("CursorID", 0));
			intent.putExtra("TableName", data.getStringExtra("TableName"));
			break;
		default:
			break;
		}
	}
	
	public void onDelRecord(View view) {
		
		int RecordID = mCursor.getInt(0);
		db.delete(TableName, RecordID);
		SendMessageActivity.this.finish();
	}
	
	public void onCancleRecord(View view) {
		SendMessageActivity.this.finish();
	}
	
	public void getData() {
		tv1 = (TextView) findViewById(R.id.SendMsgTextView1);
		tv2 = (TextView) findViewById(R.id.SendMsgTextView2);
		
		CursorID = intent.getIntExtra("CursorID", 0);
		TableName = intent.getStringExtra("TableName");
		
		if(TableName.matches("Email_Accounts")) {
			mCursor = db.select("Email_Accounts");
			setTitle("Email Account");
			tv1.setText("Email:");
			tv2.setText("Password:");
		}else if(TableName.matches("PC_Logins")) {
			mCursor = db.select("PC_Logins");
			setTitle("PC Login");
			tv1.setText("User:");
			tv2.setText("Password:");
		}
		
		mCursor.moveToPosition(CursorID);
	}
	
	public void onSendMsgShow() {
		edit = (EditText) findViewById(R.id.SendMsgEditText1);
		edit.setText(mCursor.getString(2));
		edit.setFocusable(false);
		
		//Log.d("String2", "String2:"+mCursor.getColumnCount()+", "+RecordID);
		edit = (EditText) findViewById(R.id.SendMsgEditText2);
		edit.setText(mCursor.getString(3));
		edit.setFocusable(false);
		
		TextView tv = (TextView) findViewById(R.id.textViewDesc);
		tv.setText(mCursor.getString(1));
	}
	
	public void onClickSend(View view) {
		Button bt = (Button) view;
		
		switch(bt.getId()) {
		case R.id.SendMsgButton1:
			edit = (EditText) findViewById(R.id.SendMsgEditText1);
			SendMsg(edit.getText().toString());
			break;
		case R.id.SendMsgButton2:
			edit = (EditText) findViewById(R.id.SendMsgEditText2);
			SendMsg(edit.getText().toString());
			break;
		case R.id.buttonSendUserDef:
			edit = (EditText) findViewById(R.id.editTextUserDef);
			SendMsg(edit.getText().toString());
			break;
		default:
				break;
		}
	}
	
	public void sendBackspace(View view) {
		byte buffer[] = new byte[8];
		Arrays.fill(buffer, (byte)0x00);
		
		buffer[2] = 0x2a;
		try {
			HidKeyboardService.sendReport(8, buffer);
			Arrays.fill(buffer, (byte)0x00);
			HidKeyboardService.sendReport(8, buffer);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendEnter(View view) {
		byte buffer[] = new byte[8];
		Arrays.fill(buffer, (byte)0x00);

		buffer[2] = 0x28;
		try {
			HidKeyboardService.sendReport(8, buffer);
			Arrays.fill(buffer, (byte)0x00);
			HidKeyboardService.sendReport(8, buffer);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendCtrlAltDel(View view) {
		byte buffer[] = new byte[8];
		Arrays.fill(buffer, (byte)0x00);

		buffer[0] = 0x05;
		buffer[2] = 0x4c;
		try {
			HidKeyboardService.sendReport(8, buffer);
			Arrays.fill(buffer, (byte)0x00);
			HidKeyboardService.sendReport(8, buffer);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void sendWinL(View view) {
		byte buffer[] = new byte[8];
		Arrays.fill(buffer, (byte)0x00);

		buffer[0] = 0x08;
		buffer[2] = 0x0f;
		try {
			HidKeyboardService.sendReport(8, buffer);
			Arrays.fill(buffer, (byte)0x00);
			HidKeyboardService.sendReport(8, buffer);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void SendMsg(String str) {
		byte buffer[] = new byte[8];
		Arrays.fill(buffer, (byte)0x00);
		
		byte[] bt = str.getBytes();
		
		for (int i=0; i<bt.length; i++){
			KeyParse(bt[i], buffer);
			try {
				HidKeyboardService.sendReport(8, buffer);
				Arrays.fill(buffer, (byte)0x00);
				HidKeyboardService.sendReport(8, buffer);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public int KeyParse(byte bt, byte report[])
	{
		if(Character.isUpperCase(bt))
		{
			bt = (byte) (bt + 0x20);
			report[0] = 0x02;
		}
		
		for(int i=0; HID_KeyShift.ch[i][0] != '\0'; i++)
		{
			if(bt == HID_KeyShift.ch[i][0]) {
				report[0] = 0x02;
				bt = (byte) HID_KeyShift.ch[i][1];
			}
		}
		
		for(int i=0; HID_Keycodes.ch[i][0] != '\0'; i++)
		{
			if(bt == HID_Keycodes.ch[i][0])
				report[2] = (byte) HID_Keycodes.ch[i][1];
		}
		
		if(Character.isLowerCase(bt))
			report[2] = (byte) (bt - 0x61 + 0x04);
		
		if(Character.isDigit(bt))
		{
			if(bt == 0x30)
				report[2] = 0x27;
			else
				report[2] = (byte) (bt - 0x31 + 0x1e);
		}
		return 0;
	}

}
