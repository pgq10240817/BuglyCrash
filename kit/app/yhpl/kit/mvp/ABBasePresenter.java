package app.yhpl.kit.mvp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import app.yhpl.kit.log.Logger;
import app.yhpl.kit.services.CancelableTask;
import app.yhpl.kit.thread.Runtask;
import app.yhpl.kit.thread.ThreadPool;

/**
 * Author: wangjie Email: tiantian.china.2@gmail.com Date: 7/17/14.
 * <p/>
 * MVP的Presenter层，作为沟通View和Model的桥梁，它从Model层检索数据后，返回给View层，它也可以决定与View层的交互操作。
 * 它包含一个View层的引用和一个Model层的引用
 */
public class ABBasePresenter<V extends ABActivityViewer, I extends ABInteractor> implements ABBaseTaskManager {

	private static final String TAG = ABBasePresenter.class.getSimpleName();

	protected V viewer;
	protected I interactor;

	protected ABBasePresenter() {
		cancelableTaskList = new ArrayList<>();
	}

	private List<CancelableTask> cancelableTaskList;

	@Override
	public void addCancelableTask(CancelableTask cancelableTask) {
		cancelableTaskList.add(cancelableTask);
	}

	@Override
	public void removeCancelableTask(CancelableTask cancelableTask) {
		Logger.i(TAG, "removeCancelableTask: " + cancelableTask);
		cancelableTaskList.remove(cancelableTask);
	}

	@Override
	public void closeAllTask() {
		Iterator<CancelableTask> iter = cancelableTaskList.iterator();
		while (iter.hasNext()) {
			CancelableTask task = iter.next();
			Logger.i(TAG, "closeAllTask: " + task);
			task.cancel(true);
			iter.remove();
		}

	}

	public <U, R> void goRuntask(Runtask<U, R> runtask) {
		addCancelableTask(runtask);
		ThreadPool.go(runtask);
	}

//	public void addHttpRequest(HippoRequest request) {
//		addCancelableTask(request);
//		HttpApplicationController.getInstance().addToRequestQueue(request);
//	}

	public V getViewer() {
		return viewer;
	}

	public void setViewer(V viewer) {
		this.viewer = viewer;
	}

	public I getInteractor() {
		return interactor;
	}

	public void setInteractor(I interactor) {
		this.interactor = interactor;
	}

}
