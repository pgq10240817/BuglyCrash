package app.yhpl.news.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.yhpl.util.log.MyLog;
import app.yhpl.news.R;
import app.yhpl.news.cross.V1Crash148Activity;

public class V1CrashTestFragment extends V1NormalFragment {

	@Override
	public void initViews(View root) {
		super.initViews(root);
		root.findViewById(R.id.crash_147).setOnClickListener(this);

		root.findViewById(R.id.crash_148).setOnClickListener(this);
		root.findViewById(R.id.crash_149).setOnClickListener(this);
		root.findViewById(R.id.crash_151).setOnClickListener(this);
		root.findViewById(R.id.crash_164).setOnClickListener(this);
		root.findViewById(R.id.crash_166).setOnClickListener(this);

		// root.findViewById(R.id.crash_147).setEnabled(false);
		// root.findViewById(R.id.crash_151).setEnabled(false);
		// root.findViewById(R.id.crash_164).setEnabled(false);
		// root.findViewById(R.id.crash_166).setEnabled(false);
	}

	@Override
	public boolean processOnClick(View view) {
		boolean result = false;
		switch (view.getId()) {
		case R.id.crash_147:
			test147();
			result = true;
			break;
		case R.id.crash_148:
			test148();
			result = true;
			break;
		case R.id.crash_149:
			test149();
			result = true;
			break;
		case R.id.crash_151:
			test151();
			result = true;
			break;
		case R.id.crash_164:
			test164();
			result = true;
			break;
		case R.id.crash_166:
			test166();
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

	private void test147() {
		// getActivity().finish();
		PackageManager mPackageManager = getActivity().getPackageManager();
		String featureName = getLargeString();
		MyLog.logD("featureName size " + featureName.length());
		boolean hasFeture = mPackageManager.hasSystemFeature(featureName);
		MyLog.logD("hasFeture size " + hasFeture);
	}

	private void test148() {
		Intent intent = new Intent(getActivity(), V1Crash148Activity.class);
		getActivity().startActivityForResult(intent, 100);
	}

	private void test149() {
		MyLog.logD("test149 begin");
		// Callable pAccount = new PrivateAccount();
		// FutureTask futureTask = new FutureTask(pAccount);
		// Thread pAccountThread = new Thread(futureTask);
		// pAccountThread.start();
		// try {
		//
		// futureTask.get(1000, TimeUnit.MILLISECONDS);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// } catch (ExecutionException e) {
		// e.printStackTrace();
		// } catch (TimeoutException e) {
		// e.printStackTrace();
		// }
		// MyLog.logD("test149 end");

	}

	private void test151() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setCancelable(false).setTitle("title").setMessage("msg")
						.setNegativeButton(android.R.string.cancel, null);
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		}).start();
	}

	private void test164() {
		FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		Fragment fragment = new V1Crash164Fragment();
		ft.add(R.id.content, fragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	private void test166() {
		Dialog d = new Dialog(getActivity());
		d.setTitle(getLargeString());
		d.show();

	}

	@Override
	public int getLayoutRes() {
		return R.layout.v1_fragment_crash;
	}
}
