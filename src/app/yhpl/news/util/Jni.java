package app.yhpl.news.util;

public class Jni {
	static String LIB = "Bugly";
	static {
		System.loadLibrary(LIB);
	}

	public static native void SetVersionCode();

}
