package app.yhpl.news.adapter;

import android.view.Gravity;
import android.widget.TextView;
import app.yhpl.news.R;
import app.yhpl.news.adapter.presenter.bean.MsgBean;
import app.yhpl.news.adapter.presenter.bean.WidgetHolder;

public class V1TestCmdAdapter extends V1BaseAdapter<MsgBean> {

	@Override
	public WidgetHolder getWidgetHolderForViewType(int viewType) {
		WidgetHolder widgetHolder = new WidgetHolder();
		widgetHolder.setWidgetId(R.layout.v1_widget_msg);
		widgetHolder.putView(R.id.ext_txt, null);
		return widgetHolder;
	}

	@Override
	public void initWidgets(MsgBean bean, WidgetHolder widgetHolder) {
		TextView mTvTxt = widgetHolder.getTv(R.id.ext_txt);
		if (bean.type == MsgBean.TYPE_HINT) {
			mTvTxt.setGravity(Gravity.CENTER);
		} else if (bean.type == MsgBean.TYPE_SEND) {
			mTvTxt.setGravity(Gravity.LEFT);
		} else {
			mTvTxt.setGravity(Gravity.RIGHT);
		}
		mTvTxt.setText(bean.title);
	}
}
