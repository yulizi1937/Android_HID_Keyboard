package com.intel.hid_sendmsg;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	//private IHidKeyboardService hidKeyboardservice = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void sendMessage(View view) {
		EditText tv = (EditText)findViewById(R.id.editText_Msg);
		String str = tv.getText().toString();
		byte buffer[] = new byte[8];
		Arrays.fill(buffer, (byte)0x00);
		
		byte[] bt = str.getBytes();

		try {
			FileOutputStream fos = new FileOutputStream("/dev/hidg0");
			
			for (int i=0; i<bt.length; i++){
				KeyParse(bt[i], buffer);
				
				fos.write(buffer);
				Arrays.fill(buffer, (byte)0x00);
				fos.write(buffer);
			}
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void sendBackspace(View view) {
		byte buffer[] = new byte[8];
		Arrays.fill(buffer, (byte)0x00);
		try {
			FileOutputStream fos = new FileOutputStream("/dev/hidg0");
			buffer[2] = 0x2a;
			fos.write(buffer);
			Arrays.fill(buffer, (byte)0x00);
			fos.write(buffer);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendEnter(View view) {
		byte buffer[] = new byte[8];
		Arrays.fill(buffer, (byte)0x00);
		try {
			FileOutputStream fos = new FileOutputStream("/dev/hidg0");
			buffer[2] = 0x28;
			fos.write(buffer);
			Arrays.fill(buffer, (byte)0x00);
			fos.write(buffer);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendCtrlAltDel(View view) {
		byte buffer[] = new byte[8];
		Arrays.fill(buffer, (byte)0x00);
		try {
			FileOutputStream fos = new FileOutputStream("/dev/hidg0");
			
			buffer[0] = 0x05;
			buffer[2] = 0x4c;
			fos.write(buffer);
			Arrays.fill(buffer, (byte)0x00);
			fos.write(buffer);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendWinL(View view) {
		byte buffer[] = new byte[8];
		Arrays.fill(buffer, (byte)0x00);
		try {
			FileOutputStream fos = new FileOutputStream("/dev/hidg0");
			
			buffer[0] = 0x08;
			buffer[2] = 0x0f;
			fos.write(buffer);
			Arrays.fill(buffer, (byte)0x00);
			fos.write(buffer);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
