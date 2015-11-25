package app.yhpl.news.fragment;

import com.tencent.bugly.crashreport.CrashReport;

import android.view.View;
import app.yhpl.news.R;
import app.yhpl.news.crash.JNI;

public class V1CrashTestFragment extends V1NormalFragment {

	@Override
	public void initViews(View root) {
		super.initViews(root);
		root.findViewById(R.id.crash_test).setOnClickListener(this);
		root.findViewById(R.id.crash_jni_error_name).setOnClickListener(this);
	}

	@Override
	public boolean processOnClick(View view) {
		boolean result = false;
		switch (view.getId()) {
		case R.id.crash_test:
			crashTest();
			result = true;
			break;
		case R.id.crash_jni_error_name:
			crashTestLoadErrorSo();
			result = true;
			break;

		default:
			break;
		}
		return result || super.processOnClick(view);
	}

	private void crashTest() {
		// CrashReport.testJavaCrash();
		int i = 10 / 0;
	}

	private void crashTestLoadErrorSo() {
		JNI.test();
	}

	@Override
	public int getLayoutRes() {
		return R.layout.v1_fragment_crash;
	}
}
