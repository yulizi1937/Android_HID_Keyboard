package android.os;

interface IHidKeyboardService {
	void sendReport(int len, inout byte[] report);
}
