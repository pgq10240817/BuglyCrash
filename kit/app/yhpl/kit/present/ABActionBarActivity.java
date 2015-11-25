package app.yhpl.kit.present;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;
import app.yhpl.kit.manager.BaseActivityManager;
import app.yhpl.kit.mvp.ABActivityViewer;
import app.yhpl.kit.mvp.ABBasePresenter;

public class ABActionBarActivity extends ActionBarActivity implements ABActivityViewer, ABActivityCommon {

	private BaseActivityManager baseActivityManager;

	private boolean isFirstFocused = true;

	protected boolean isActivityLifeCycleAutoCallBack = true;

	private ABBasePresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (shouldCallBack() && !isFinishing()) {
			baseActivityManager.dispatchActivityCreate(savedInstanceState);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (isFirstFocused && hasFocus) {
			isFirstFocused = false;
			onWindowInitialized();
		}
	}

	/**
	 * 界面渲染完毕，可在这里进行初始化工作，建议在这里启动线程进行初始化工作
	 */
	public void onWindowInitialized() {
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (shouldCallBack() && !isFinishing()) {
			baseActivityManager.dispatchActivityResume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (shouldCallBack() && !isFinishing()) {
			baseActivityManager.dispatchActivityPause();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (shouldCallBack()) {
			baseActivityManager.dispatchActivityDestory();
		}
		if (presenter != null) {
			presenter.closeAllTask();
		}
	}

	@Override
	public BaseActivityManager getBaseActivityManager() {
		ensureBaseActivityMananger();
		return baseActivityManager;
	}

	@Override
	public void setBaseActivityManager(BaseActivityManager baseActivityManager) {
		this.baseActivityManager = baseActivityManager;
	}

	private void ensureBaseActivityMananger() {
		if (null == baseActivityManager) {
			this.baseActivityManager = new BaseActivityManager(this);
		}
	}

	private boolean shouldCallBack() {
		return null != baseActivityManager && isActivityLifeCycleAutoCallBack;
	}

	public boolean isActivityLifeCycleAutoCallBack() {
		return isActivityLifeCycleAutoCallBack;
	}

	public void setActivityLifeCycleAutoCallBack(boolean isActivityLifeCycleAutoCallBack) {
		this.isActivityLifeCycleAutoCallBack = isActivityLifeCycleAutoCallBack;
	}

	/**
	 * ******************* ABActivityViewer impl ********************
	 */

	@Override
	public void showToastMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showInfoDialog(String message) {
		showInfoDialog(null, message);
	}

	@Override
	public void showInfoDialog(String title, String message) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("OK", null).show();
	}

	@Override
	public void showLoadingDialog(String message) {

	}

	@Override
	public void cancelLoadingDialog() {

	}

	@Override
	public void showInfoDialog(String title, String message, String okButtonText) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton(okButtonText, null).show();
	}

	@Override
	public void registerPresenter(ABBasePresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void closeAllTask() {
		if (null != presenter) {
			presenter.closeAllTask();
		}
	}

}
