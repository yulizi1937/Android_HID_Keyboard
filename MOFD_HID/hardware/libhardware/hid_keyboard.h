#ifndef ANDROID_HID_KEYBOARD_HAL_INTERFACE_H_
#define ANDROID_HID_KEYBOARD_HAL_INTERFACE_H_

#include <hardware/hardware.h>

__BEGIN_DECLS

#define HID_KEYBOARD_HARDWARE_MODULE_ID "hid_keyboard"

struct hid_keyboard_module_t {
	struct hw_module_t common;
};

struct hid_keyboard_device_t {
	struct hw_device_t common;
	int fd;
	int (*write)(struct hid_keyboard_device_t* dev, uint16_t data_len, const char* data);
	//int (*get_val)(struct hid_keyboard_t* dev, int* val);
};

__END_DECLS

#endif
