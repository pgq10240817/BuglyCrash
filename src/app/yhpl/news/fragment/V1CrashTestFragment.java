package app.yhpl.news.fragment;

import java.io.File;

import com.android.volley.toolbox.DiskBasedCache;
import com.google.okhttp.OkHttpConnection;
import com.google.okhttp.libcore.util.Libcore;
import com.jakewharton.disklrucache.DiskLruCache;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import app.yhpl.news.R;
import app.yhpl.news.crash.JNI;
import app.yhpl.news.util.Jni;

public class V1CrashTestFragment extends V1NormalFragment {

	@Override
	public void initViews(View root) {
		super.initViews(root);
		root.findViewById(R.id.crash_156).setOnClickListener(this);

		root.findViewById(R.id.crash_161).setOnClickListener(this);
		root.findViewById(R.id.crash_167).setOnClickListener(this);

		// root.findViewById(R.id.crash_147).setEnabled(false);
		// root.findViewById(R.id.crash_151).setEnabled(false);
		// root.findViewById(R.id.crash_164).setEnabled(false);
		// root.findViewById(R.id.crash_166).setEnabled(false);
	}

	@Override
	public boolean processOnClick(View view) {
		boolean result = false;
		switch (view.getId()) {
		case R.id.crash_156:
			test156();
			result = true;
			break;
		case R.id.crash_161:
			test161();
			result = true;
			break;
		case R.id.crash_167:
			test167();
			result = true;
			break;
		default:
			break;
		}
		return result || super.processOnClick(view);
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

	private void test156() {

	}

	// public void SubmitPost(String url, String filename1) {
	//
	// HttpClient httpclient = new DefaultHttpClient();
	// try {
	// Log.e("myTag", "~~~~~~~~~~~~~~  1111111111");
	// HttpPost httppost = new HttpPost(url);
	// FileBody bin = new FileBody(new File(filename1));
	// StringBody comment = new StringBody(filename1);
	// MultipartEntity reqEntity = new MultipartEntity();
	// reqEntity.addPart("file1", bin);// file1为请求后台的File upload;属性
	// // reqEntity.addPart("file2", bin2);// file2为请求后台的File upload;属性
	// reqEntity.addPart("filename1", comment);// filename1为请求后台的普通参数;属性
	// httppost.setEntity(reqEntity);
	// Log.e("myTag", "~~~~~~~~~~~~~~ 222 ");
	//
	// HttpResponse response = httpclient.execute(httppost);
	//
	// int statusCode = response.getStatusLine().getStatusCode();
	//
	// if (statusCode == HttpStatus.SC_OK) {
	//
	// System.out.println("服务器正常响应.....");
	//
	// HttpEntity resEntity = response.getEntity();
	//
	// System.out.println(EntityUtils.toString(resEntity));//
	// httpclient自带的工具类读取返回数据
	//
	// }
	//
	// } catch (ParseException e) {
	// Log.e("myTag", "ParseException:" + e.getMessage());
	// e.printStackTrace();
	// } catch (IOException e) {
	// Log.e("myTag", "IOException:" + e.getMessage());
	// e.printStackTrace();
	// } finally {
	// try {
	// httpclient.getConnectionManager().shutdown();
	// } catch (Exception ignore) {
	//
	// }
	// }
	//
	// Log.e("myTag", "~~~~~~~~~~~~~~ 333333333 ");
	// }

	private void test161() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				File f = new File(Environment.getExternalStorageDirectory(), "1424745742580.jpeg");
				if (f.isFile()) {
					throw new IllegalArgumentException("not a directory:");
				}
				return null;
			}
		}.execute();

	}

	private void test167() {
		Jni.SetVersionCode();

	}

	@Override
	public int getLayoutRes() {
		return R.layout.v1_fragment_crash;
	}
}
