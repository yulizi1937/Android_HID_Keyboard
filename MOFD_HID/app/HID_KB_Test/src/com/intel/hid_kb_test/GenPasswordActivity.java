package com.intel.hid_kb_test;

import java.util.Random;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class GenPasswordActivity extends Activity{
	
	private TextView tv = null;
	private String str = "Generate Password";
	private int pass_len = 10;
	private static boolean isLC = true;
	private static boolean isUC = true;
	private static boolean isNum = true;
	private static boolean isSpec = true;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_genpass);
		tv = (TextView) this.findViewById(R.id.button_genpass);
		
		NumberPickerSetup();
	}
	
	private void NumberPickerSetup() {
		NumberPicker NP =  (NumberPicker) findViewById(R.id.numberPicker_pass_len);
		//NP.setFormatter((Formatter) this);
		NP.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				pass_len = arg2;
			}
		});
		//NP.setOnScrollListener(OnScrollListener);
		NP.setMaxValue(20);
		NP.setMinValue(1);
		NP.setValue(10);
		
	}
	
	public void onTBLC_clicked(View view) {
		isLC = ((ToggleButton) view).isChecked();
		
	}
	
	public void onTBUC_clicked(View view) {
		isUC = ((ToggleButton) view).isChecked();
		
	}
	
	public void onTBNum_clicked(View view) {
		isNum = ((ToggleButton) view).isChecked();
		
	}
	
	public void onTBSpec_clicked(View view) {
		isSpec = ((ToggleButton) view).isChecked();
		
	}
	
	
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("message", tv.getText().toString());
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        str = savedInstanceState.getString("message");
        tv.setText(str);
    }
	
	public void genPassword(View view) {
		str = randomString(pass_len);
		tv.setText(str);
		
	}
	
	public void cancle(View view) {
		GenPasswordActivity.this.finish();
		
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void cp2clipboard(View view) {
		ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		ClipData cpData = ClipData.newPlainText("pass", str);
		cm.setPrimaryClip(cpData);
		
		if(str.equals(cm.getPrimaryClip().getItemAt(0).getText())) {
			Toast.makeText(this, "Password copied to clipboard!", Toast.LENGTH_LONG).show();
			GenPasswordActivity.this.finish();
		}
		
	}

	public static final String randomString(int length) {
		Random randGen = null;
		StringBuffer SPOOL = new StringBuffer();
		char[] pool = null;
		if (length < 1) {
			return null;
		}
		String lowercase = "abcdefghijklmnopqrstuvwxyz";
		String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String number = "0123456789";
		String special = "!@#$%";
		Log.i("lowercase:",lowercase);
		Log.i("isLC","isLC:" + isLC);
		if (randGen == null) {
			randGen = new Random();
			if(isLC) {
				SPOOL.append(lowercase);
			}
			if(isUC) {
				SPOOL.append(uppercase);
			}
			if(isNum) {
				SPOOL.append(number);
			}
			if(isSpec) {
				SPOOL.append(special);
			}
			Log.i("SPOOL:",SPOOL.toString());
			pool = SPOOL.toString().toCharArray();
		}
		Log.i("SPOOL2:",SPOOL.toString());
		char [] randBuffer = new char[length];
		for (int i=0; i<randBuffer.length; i++) {
			randBuffer[i] = pool[randGen.nextInt(pool.length-1)];
		}
		return new String(randBuffer);
	}

}
