#define LOG_TAG "HidKeyboardService"

#include "jni.h"
#include "JNIHelp.h"
#include "android_runtime/AndroidRuntime.h"
#include "android_runtime/Log.h"

#include <utils/Log.h>
#include <utils/misc.h>
#include <hardware/hardware.h>
#include <hardware/hid_keyboard.h>

#include <stdio.h>

namespace android
{
	struct hid_keyboard_device_t* hid_keyboard_device = NULL;

	static void hid_keyboard_sendReport(JNIEnv* env, jobject clazz, jint len, jbyteArray value){
		//LOGI("Hid JNI:send report to device.(len=%d)", len);
		jbyte* report = env->GetByteArrayElements(value, 0);
		if(!hid_keyboard_device) {
			LOGI("Hid Keyboard JNI:device is not open.");
			return;
		}
		hid_keyboard_device->write(hid_keyboard_device, len, (char *)report);
		
		env->ReleaseByteArrayElements(value, report, 0);

	}

	static inline int hid_keyboard_device_open(const hw_module_t* module, struct hid_keyboard_device_t** device) {
		return module->methods->open(module, HID_KEYBOARD_HARDWARE_MODULE_ID, (struct hw_device_t**)device);
	}

	static jboolean hid_keyboard_init(JNIEnv* env, jclass clazz) {
		hid_keyboard_module_t* module;

		LOGI("Hid Keyboard JNI:initializing......");
		if(hw_get_module(HID_KEYBOARD_HARDWARE_MODULE_ID, (const struct hw_module_t**)&module) == 0){
			LOGI("Hid Keyboard JNI:hid keyboard Stub found.");
			if(hid_keyboard_device_open(&(module->common), &hid_keyboard_device) == 0) {
				LOGI("Hid Keyboard JNI:hid keyboard is open.");
				return 0;
			}
			LOGI("Hid Keyboard JNI:hid keyboard failed open.");
			return -1;
		}
		LOGI("Hid Keyboard JNI:hid keyboard Stub found failed.");
		return -1;
	}

	static const JNINativeMethod method_table[] = {
		{"init_native", "()Z", (void*)hid_keyboard_init},
		{"sendReport_native", "(I[B)V", (void*)hid_keyboard_sendReport},
	};

	int register_android_server_HidKeyboardService(JNIEnv *env) {
		return jniRegisterNativeMethods(env, "com/android/server/HidKeyboardService", method_table, NELEM(method_table));
	}
};
