package app.yhpl.news.fragment;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import app.yhpl.news.R;
import app.yhpl.news.adapter.presenter.bean.MsgBean;
import app.yhpl.news.adapter.presenter.bean.RespCmd;
import app.yhpl.news.bl.CHexConver;
import app.yhpl.news.bl.CmdParamters;
import app.yhpl.news.bl.task.ServerDuplexThread;

public class V1BlListViewFragment extends V1ListViewFragment {
	private TextView mBlStart, mBlClear;
	private ServerDuplexThread mServerThread;
	private Handler mServerHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			MsgBean bean = new MsgBean();
			Object target = msg.obj;
			if (target instanceof String) {
				bean.title = (String) target;
			} else if (target instanceof RespCmd) {
				RespCmd resp = (RespCmd) target;
				byte[] data = resp.mContent;
				String respStr = CHexConver.byte2HexStr(data, data.length);
				bean.title = "收到：" + respStr;
			} else {
				bean.title = "???";
			}
			switch (msg.what) {
			case CmdParamters.HANDLER_MSG_REIVIED:
				bean.type = MsgBean.TYPE_RECEIED;
				break;
			case CmdParamters.HANDLER_SEND_SUCCESS:
				bean.type = MsgBean.TYPE_SEND;
				break;

			default:
				bean.type = MsgBean.TYPE_HINT;
				break;
			}
			onMsgReceived(bean);
		};

	};

	private void onMsgReceived(MsgBean msg) {
		if (baseAdapter == null) {
			baseAdapter = initAdapter(null);
		}
		if (baseAdapter != null) {
			baseAdapter.appendSingleData(msg);
			int count = baseAdapter.getCount();
			mListview.setSelection(count - 1);
		}
	}

	@Override
	public void initViews(View root) {
		super.initViews(root);
		mBlStart = (TextView) root.findViewById(R.id.bl_start);
		mBlStart.setOnClickListener(this);
		mBlClear = (TextView) root.findViewById(R.id.bl_clear);
		mBlClear.setOnClickListener(this);
	}

	@Override
	public void onDataBeginLoading() {
		log("onDataBeginLoading");
		showContentView();
	}

	@Override
	public boolean processOnClick(View view) {
		boolean isHandle = false;
		switch (view.getId()) {
		case R.id.bl_start:
			isHandle = true;
			onBlStart();
			break;
		case R.id.bl_clear:
			isHandle = true;
			if (baseAdapter != null) {
				baseAdapter.clear();
			}
			break;

		default:
			break;
		}
		return isHandle || super.processOnClick(view);
	}

	private void onBlStart() {
		reset();
		mServerThread = new ServerDuplexThread(mServerHandler);
		mServerThread.start();
	}

	private void reset() {
		if (mServerThread != null) {
			mServerThread.stopConnect();
			mServerThread = null;
		}
	}

	@Override
	public void onDestroyView() {
		reset();
		super.onDestroyView();
	}

	@Override
	public int getLayoutRes() {
		return R.layout.v1_fragment_listview_titlebar_bl;
	}

}
