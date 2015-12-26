package app.yhpl.news.bl.task;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import app.yhpl.news.bl.CHexConver;
import app.yhpl.news.bl.CmdParamters;

public class ServerReadThread extends Thread {

	private BluetoothSocket socket = null;
	private Handler mHandler;
	private long mTimetLastWrite = 0;

	List<String> mReivedMsg = new ArrayList<String>();
	private boolean gloableFlag = true;
	private long mLastReadTime = 0;
	private final static int DELAY_SEND_MSG = 100;
	private ByteArrayBuffer mBytesBuffer = new ByteArrayBuffer(1024);
	private static String CMD_PREFIX = "AAAAAA";

	private IMsgListener mMsgReadListener;
	private Runnable mSendMsgRun = new Runnable() {
		public void run() {
			sendMsg();
		}
	};

	public void sendMsg() {
		byte[] buf_data = null;
		synchronized (mBytesBuffer) {
			buf_data = mBytesBuffer.toByteArray();
			mBytesBuffer.clear();
		}
		Message msgReceived = new Message();
		if (buf_data != null && buf_data.length > 0) {
			String s = CHexConver.byte2HexStr(buf_data, buf_data.length);
			s = s.replaceAll(" ", "");
			if (s.startsWith(CMD_PREFIX)) {
				s = s.replace(CMD_PREFIX, "#");
			}
			msgReceived.obj = s;
			msgReceived.what = CmdParamters.HANDLER_MSG_REIVIED;
		} else {
			msgReceived.obj = "read null";
			msgReceived.what = CmdParamters.HANDLER_MSG_REIVIED;
		}
		if (mHandler != null) {
			mHandler.sendMessage(msgReceived);
		}
		if (mMsgReadListener != null) {
			if (buf_data != null && buf_data.length > 0) {
				mMsgReadListener.onMsgRead(buf_data);
			}
		}
	}

	public long getOnceMsgDuration() {
		return SystemClock.elapsedRealtime() - mTimetLastWrite;
	}

	public ServerReadThread(Handler mHandler, BluetoothSocket socket, IMsgListener listener) {
		this.mHandler = mHandler;
		this.socket = socket;
		this.mMsgReadListener = listener;
	}

	public void stopConnect() {
		gloableFlag = false;
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
		if (socket != null) {
			try {
				InputStream in = socket.getInputStream();
				if (in != null) {
					in.close();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public void run() {
		while (gloableFlag) {
			read();
		}

		if (mHandler != null) {
			Message msg = new Message();
			msg.obj = "通信完毕";
			msg.what = CmdParamters.HANDLER_CONNECT_FAILED;
			mHandler.sendMessage(msg);
		}
		gloableFlag = false;

	}

	private boolean read() {
		boolean result = false;
		InputStream mmInStream = null;
		byte[] buffer = new byte[1024];
		int bytes;
		try {
			mmInStream = socket.getInputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			android.util.Log.e("myTag", "`````````````````````````  --- begin");
			int mOutResult = -1;

			while ((mOutResult = mmInStream.read()) != -1 && gloableFlag) {
				mLastReadTime = SystemClock.elapsedRealtime();

				synchronized (mBytesBuffer) {

					int length = mBytesBuffer.length();
					if (length < 3) {
						if (mOutResult != 0xAA) {
							mBytesBuffer.clear();
							android.util.Log.e("myTag", "read" + Integer.toHexString(mOutResult) + " error so,ignore");
							continue;
						}
					} else if (length >= 1024) {
						android.util.Log.e("myTag", "full ------------------");
						mBytesBuffer.clear();
						continue;
					}
					mTimetLastWrite = SystemClock.elapsedRealtime();
					try {
						mBytesBuffer.append(mOutResult);
					} catch (Exception e1) {
						e1.printStackTrace();
						throw new IOException(e1.getMessage());
					}
					byte[] temp = mBytesBuffer.toByteArray();
					Log.e("myTag", " -- >" + CHexConver.byte2HexStr(temp, temp.length));
				}
				if (mHandler != null) {
					mHandler.removeCallbacks(mSendMsgRun);
					mHandler.postDelayed(mSendMsgRun, DELAY_SEND_MSG);
				}
			}

			android.util.Log.e("myTag", "`````````````````````````  --- end");
			byte[] buf_data = mBytesBuffer.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				mmInStream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			gloableFlag = false;
		}
		return result;
	}
}
