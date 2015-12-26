package app.yhpl.news.bl.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import app.yhpl.news.bl.CHexConver;
import app.yhpl.news.bl.CmdParamters;

public class ServerDuplexThread extends Thread implements IMsgListener {
	public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
	private BluetoothServerSocket mServerSocket = null;
	private BluetoothSocket socket = null;
	private BluetoothDevice device = null;
	private Handler mHandler;
	public static boolean isRun = false;
	private long mTimetLastWrite = 0;

	List<String> mReivedMsg = new ArrayList<String>();
	private boolean gloableFlag = true;
	private Object lock = new Object();
	private String mCurrentSendMsg;
	private ServerReadThread mReadThread;

	public void putOneReceiveddMsg(String msg) {
		synchronized (mWriteMsg) {
			mWriteMsg.add(msg);
		}
	}

	public long getOnceMsgDuration() {
		return SystemClock.elapsedRealtime() - mTimetLastWrite;
	}

	List<String> mWriteMsg = new ArrayList<String>();

	public void putOneWriteMsg(String msg) {
		android.util.Log.e("myTag", "add msg   " + msg + " ," + mWriteMsg.size());
		synchronized (mWriteMsg) {
			if (mWriteMsg.size() > 5) {
				mWriteMsg.clear();
			}
			mWriteMsg.add(msg);
		}

		synchronized (lock) {
			lock.notify();
		}
	}

	public void clearAllMsg() {
		synchronized (mWriteMsg) {
			if (mWriteMsg.size() > 0) {
				mWriteMsg.clear();
			}

		}
	}

	public String getOneWriteMsg() {
		synchronized (mWriteMsg) {
			if (mWriteMsg.size() > 0) {
				return mWriteMsg.remove(0);
			}

		}

		return null;

	}

	public ServerDuplexThread(Handler mHandler) {
		this.mHandler = mHandler;
	}

	public void stopConnect() {
		gloableFlag = false;
		synchronized (lock) {
			lock.notify();
		}
		if (mReadThread != null) {
			mReadThread.stopConnect();
		}
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
				e1.printStackTrace();
			}
			try {
				OutputStream out = socket.getOutputStream();
				if (out != null) {
					out.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void run() {
		isRun = true;
		try {

			// 创建一个Socket连接：只需要服务器在注册时的UUID号
			// socket =
			// device.createRfcommSocketToServiceRecord(BluetoothProtocols.OBEX_OBJECT_PUSH_PROTOCOL_UUID);
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			mServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM,
					UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

			Log.d("server", "wait cilent connect...");

			Message msg = new Message();
			msg.obj = "请稍候，正在等待客户端的连接...";
			msg.what = 0;
			mHandler.sendMessage(msg);

			/* 接受客户端的连接请求 */
			socket = mServerSocket.accept();
			Log.d("server", "accept success !");

			mTimetLastWrite = SystemClock.elapsedRealtime();
			msg = new Message();
			msg.obj = "已经连接上服务端";
			msg.what = CmdParamters.HANDLER_CONNECT_ING;
			mHandler.sendMessage(msg);

			if (mReadThread != null) {
				mReadThread.stopConnect();
				mReadThread = null;
			}
			mReadThread = new ServerReadThread(mHandler, socket, this);
			mReadThread.start();
			// putOneWriteMsg(CmdParamters.getReadXYZTest());

			while (gloableFlag) {
				mTimetLastWrite = SystemClock.elapsedRealtime();
				android.util.Log.e("myTag", "  write   begin    ");
				boolean result = write();
				android.util.Log.e("myTag", "  write   end    ");
			}

		} catch (IOException e) {
			if (mHandler != null) {
				Log.e("connect", "", e);
				Message msg = new Message();
				msg.obj = "连接异常";
				msg.what = CmdParamters.HANDLER_CONNECT_FAILED;
				mHandler.sendMessage(msg);
			}
		}

		if (mHandler != null)

		{
			Message msg = new Message();
			msg.obj = "通信完毕";
			msg.what = CmdParamters.HANDLER_CONNECT_FAILED;
			mHandler.sendMessage(msg);
		}

		isRun = false;

	}

	private boolean write() {
		boolean result = false;
		OutputStream mOutputStream = null;

		try {
			mOutputStream = socket.getOutputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			String mMsgContent = null;
			while ((mMsgContent = getOneWriteMsg()) == null) {
				Log.e("myTag", " client thread msg is null so.wait");
				try {
					synchronized (lock) {
						lock.wait();
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			mCurrentSendMsg = mMsgContent;
			// CHexConver.
			byte[] arrayOfByte2 = null;
			if (CHexConver.checkHexStr(mMsgContent)) {
				arrayOfByte2 = CHexConver.hexStr2Bytes(mMsgContent.toUpperCase());
			} else {
				Log.e("myTag2", "  ---cmd not righ " + mMsgContent);
				// Message msgReceived = new Message();
				// msgReceived.obj = "发送消息:格式不正确";
				// msgReceived.what = CmdParamters.HANDLER_SEND_FAILED;
				// mHandler.sendMessage(msgReceived);
				throw new Exception(" cmd not right");
			}

			if (arrayOfByte2 != null) {
				mOutputStream.write(arrayOfByte2);
				mOutputStream.flush();
				result = true;
				String hexStr = CHexConver.byte2HexStr(arrayOfByte2, arrayOfByte2.length);
				Log.e("myTag2", "  ---write -->" + hexStr);
				Message msgReceived = new Message();
				msgReceived.obj = "发送:" + hexStr;
				msgReceived.what = CmdParamters.HANDLER_SEND_SUCCESS;
				mHandler.sendMessage(msgReceived);
			}

		} catch (IOException e) {
			e.printStackTrace();
			gloableFlag = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void onMsgRead(byte[] data) {
		if (data != null && data.length > 0) {
			String msg = CHexConver.byte2HexStr(data, data.length);
			putOneWriteMsg(msg);

		}

	}
}
