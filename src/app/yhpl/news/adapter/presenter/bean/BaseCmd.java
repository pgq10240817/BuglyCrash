package app.yhpl.news.adapter.presenter.bean;

public abstract class BaseCmd {
	public byte mCmdType;
	public byte[] mContent;

	public BaseCmd() {
	}

	public abstract int getRespCount();

}
