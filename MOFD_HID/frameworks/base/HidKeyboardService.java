package com.android.server;
import android.content.Context;
import android.os.IHidKeyboardService;
import android.util.Slog;

public class HidKeyboardService extends IHidKeyboardService.Stub {
	private static final String TAG = "HidKeyboardService";
	
	HidKeyboardService() {
		init_native();
	}

	public void sendReport(int len, byte[] report) {
		sendReport_native(len, report);
	}

	private static native boolean init_native();
	private static native void sendReport_native(int len, byte[] report);
};
