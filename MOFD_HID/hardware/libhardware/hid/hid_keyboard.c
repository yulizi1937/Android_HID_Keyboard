#define LOG_TAG "hid_keyboard_default"

#include <hardware/hardware.h>
#include <hardware/hid_keyboard.h>
#include <fcntl.h>
#include <errno.h>

#include <cutils/log.h>
#include <cutils/atomic.h>

#define DEVICE_NAME "/dev/hidg0"
#define MODULE_NAME "Hid Keyboard"
#define MODULE_AUTHOR "yadong.qi@intel.com"

static int hid_keyboard_device_close(struct hw_device_t* device){
	struct hid_keyboard_device_t* hid_keyboard_device = (struct hid_keyboard_device_t*)device;
	if(hid_keyboard_device) {
		close(hid_keyboard_device->fd);
		free(hid_keyboard_device);
	}
	return 0;
}

static int hid_keyboard_send_report(struct hid_keyboard_device_t* dev, uint16_t report_len, const char* report){
	if(write(dev->fd, report, report_len) != report_len) {
		LOGI("Hid_Keyboard:write report to /dev/hidg0 failed!");
		return -EFAULT;
	}
	//LOGI("Hid_Keyboard:write report to /dev/hidg0 successfully.");
	return 0;	
}


static int hid_keyboard_device_open(const struct hw_module_t* module, const char* name, struct hw_device_t** device){
	struct hid_keyboard_device_t* dev;
	dev = (struct hid_keyboard_device_t*)malloc(sizeof(struct hid_keyboard_device_t));
	if(!dev) {
		LOGE("hid_keyboard: failed to alloc space!");
		return -EFAULT;
	}
	
	memset(dev, 0, sizeof(struct hid_keyboard_device_t));
	dev->common.tag = HARDWARE_DEVICE_TAG;
	dev->common.version = 0;
	dev->common.module = (hw_module_t*)module;
	dev->common.close = hid_keyboard_device_close;
	dev->write = hid_keyboard_send_report;

	if((dev->fd = open(DEVICE_NAME, O_RDWR)) == -1) {
		LOGE("Hid Stub: failed to open /dev/hidg0 -- %s.",strerror(errno));
		free(dev);
		return -EFAULT;
	}

	*device = &(dev->common);
	LOGI("Hid_Keyboard:open /dev/hidg0 successfully.");

	return 0;

}

static struct hw_module_methods_t hid_keyboard_module_methods = {
	.open = hid_keyboard_device_open,
};

struct hid_keyboard_module_t HAL_MODULE_INFO_SYM = {
	.common = {
		.tag = HARDWARE_MODULE_TAG,
		.version_major = 1,
		.version_minor = 0,
		.id = HID_KEYBOARD_HARDWARE_MODULE_ID,
		.name = MODULE_NAME,
		.author = MODULE_AUTHOR,
		.methods = &hid_keyboard_module_methods,
	},
};

#if 0
static int hid_keyboard_device_open(const struct hw_module_t* module, const char* name, struct hw_device_t** device)
{
	struct hid_keyboard_device_t* dev;
	dev = (struct hid_keyboard_device_t*)malloc(sizeof(struct hid_keyboard_device_t));
	if(!dev) {
		LOGE("hid_keyboard: failed to alloc space!");
		return -EFAULT;
	
	memset(dev, 0, sizeof(struct hid_keyboard_device_t));
	dev->common.tag = HARDWARE_DEVICE_TAG;
	dev->common.version = 0;
	dev->common.module = (hw_module_t*)module;
	dev->common.close = hid_keyboard_device_close;
	dev->write = hid_keyboard_send_report;

	if((dev->fd = open(DEVICE_NAME, O_RDWR)) == -1) {
		LOGE("Hid Stub: failed to open /dev/hidg0 -- %s.",strerror(errno));
		free(dev);
		return -EFAULT;
	}

	*device = &(dev->common);
	LOGI("Hid_Keyboard:open /dev/hidg0 successfully.");

	return 0;
}

static int hid_keyboard_device_close (struct hw_device_t* device) {
	struct hid_keyboard_device_t* hid_keyboard_device = (struct hid_keyboard_device_t*)device;
	if(hid_keyboard_device) {
		close(hid_keyboard_device->fd);
		free(hid_keyboard_device);
	}
	return 0;
}

static int hid_keyboard_send_report(struct hid_keyboard_device_t* dev, uint16_t report_len, const char* report) {
	
	write(dev->fd, report, report_len);

	return 0;
}
#endif
