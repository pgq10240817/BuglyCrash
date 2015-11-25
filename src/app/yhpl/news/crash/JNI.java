package app.yhpl.news.crash;

import android.util.Log;

public class JNI {
	private final static String TAG = "myTag";
	static {
		System.loadLibrary("pppmtools");
	};

	public static void test() {
		Log.e(TAG, "JNI  -->  test");

	}
}
