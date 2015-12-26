package app.yhpl.news.adapter.presenter.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MsgBean extends BaseBean implements Parcelable {
	public final static int TYPE_RECEIED = 1;
	public final static int TYPE_SEND = 2;
	public final static int TYPE_HINT = 3;

	public String title;
	public int type;

	@Override
	public int describeContents() {
		return 0;
	}

	// 添加一个静态成员,名为CREATOR,该对象实现了Parcelable.Creator接口
	public static final Parcelable.Creator<MsgBean> CREATOR = new Parcelable.Creator<MsgBean>() {
		@Override
		public MsgBean createFromParcel(Parcel source) {// 从Parcel中读取数据，返回person对象
			MsgBean bean = new MsgBean();
			bean.type = source.readInt();
			bean.title = source.readString();
			return bean;
		}

		@Override
		public MsgBean[] newArray(int size) {
			return new MsgBean[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(type);
		dest.writeString(title);

	}

	@Override
	public String toString() {
		return "MsgBean [title=" + title + ", type=" + type + "]";
	}

}
