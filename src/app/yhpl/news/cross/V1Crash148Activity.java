package app.yhpl.news.cross;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.yhpl.util.log.MyLog;
import app.yhpl.news.R;

public class V1Crash148Activity extends Activity {
	private SQLiteDatabase mDb;
	View rootView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.v1_activity_default);
		rootView = findViewById(R.id.div_fragment);
		// timer.start();

	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();

	}

	// private void fakeIllegalStateException() {
	// Animation animation = new TranslateAnimation(0, 0, 0, -500);
	// animation.setDuration(-100);
	// animation.setStartTime(System.currentTimeMillis() + 1000);
	// rootView.startAnimation(animation);
	// // mBluetoothAdapter.startLeScan(mLeScanCallback);
	// }

	@Override
	protected void onStop() {
		MyLog.logD("onStop ");
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public static void loginScreen(Activity context) {
		Intent intent = new Intent(context, V1Crash148Activity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

	public static void logout(Activity context) {
		Intent intent = new Intent(context, V1CrossActivity.class);
		intent = Intent.makeRestartActivityTask(intent.getComponent());
		context.startActivity(intent);
	}

	private String getLargeString() {
		StringBuilder sb = new StringBuilder();
		String targetStr = PackageManager.FEATURE_BLUETOOTH_LE;
		int step = (1024 * 1000 - 1) / targetStr.length() + 1;
		for (int i = 0; i < step; i++) {
			sb.append(targetStr);
		}
		return sb.toString();
	}

	// @Override
	// protected void onSaveInstanceState(Bundle outState) {
	// MyLog.logD("onSaveInstanceState ");
	// outState.clear();
	// outState.putString("large", getLargeString());
	// super.onSaveInstanceState(outState);
	// outState.clear();
	// }

	@Override
	protected void onPause() {
		MyLog.logD("onPause track1");

		// ActivityManager activityManager = (ActivityManager)
		// getSystemService(Context.ACTIVITY_SERVICE);
		// activityManager.killBackgroundProcesses(getPackageName());
		// try {
		// Thread.currentThread().sleep(10000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		super.onPause();

		// App.runDelay(new Runnable() {
		//
		// @Override
		// public void run() {
		// V1Crash148Activity.this.onPause();
		//
		// }
		// }, 1000);

		// android.os.Process.killProcess(android.os.Process.myPid());
		MyLog.logD("onPause track2");
	}
}
