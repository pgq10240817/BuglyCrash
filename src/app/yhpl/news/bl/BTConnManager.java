package app.yhpl.news.bl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import app.yhpl.news.util.ToastUtils;

public class BTConnManager {
	private Context context = null;
	private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private BTDeviceInterface mBluetoothDeviceInterface;
	private int mState = -1;

	public static boolean isEnable() {
		return BluetoothAdapter.getDefaultAdapter() == null ? false : BluetoothAdapter.getDefaultAdapter().isEnabled();
	}

	public static BluetoothDevice getDevicesByMac(String mac) {
		if (BluetoothAdapter.getDefaultAdapter() == null) {
			return null;
		} else {
			BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			return bluetoothAdapter.getRemoteDevice(mac);

		}
	}

	// public static void autoPair(BluetoothDevice device) {
	// try {
	// ClsUtils.createBond(device.getClass(), device);// 创建绑定
	// ClsUtils.setPin(device.getClass(), device, "000000"); // 手机和蓝牙采集器配对
	// ClsUtils.createBond(device.getClass(), device);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } // 手机和蓝牙采集器配对
	//
	// }

	public static void pairDevice(BluetoothDevice device) {
		try {
			Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
			createBondMethod.invoke(device);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean isDeviceBounded(BluetoothDevice device) {
		if (device != null && device.getBondState() == BluetoothDevice.BOND_BONDED) {
			return true;
		}
		return false;
	}

	public int getStatus() {
		return mState;
	}

	public boolean isStatusSearching() {
		if (mState == BTStatus.STATE_SEARCH_INIT || mState == BTStatus.STATE_SEARCH_ING) {
			return true;
		} else {
			return false;
		}
	}

	public BTConnManager(Context context, BTDeviceInterface bluetoothDeviceInterface) {
		this.context = context;
		this.mBluetoothDeviceInterface = bluetoothDeviceInterface;
		mState = BTStatus.STATE_IDLE;

	}

	private void regReiceiver() {
		BTStatus.tag("regReiceiver");
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);// 配对状态
		context.registerReceiver(receiver, intentFilter);
	}

	private void unRegReiceiver() {
		BTStatus.tag("unRegReiceiver");
		mState = BTStatus.STATE_IDLE;
		context.unregisterReceiver(receiver);

	}

	public void onStartSearch() {
		if (mState == BTStatus.STATE_IDLE || mState == BTStatus.STATE_SEARCH_FINISH) {
			regReiceiver();
			mState = BTStatus.STATE_SEARCH_INIT;
			if (mBluetoothDeviceInterface != null) {
				mBluetoothDeviceInterface.onBlueToothStatusChanged(mState);
			}
			bluetoothAdapter.startDiscovery();
		}
	}

	public void onStopSearch() {
		if (mState == BTStatus.STATE_SEARCH_ING || mState == BTStatus.STATE_SEARCH_INIT
				|| mState == BTStatus.STATE_SEARCH_FINISH || mState == BTStatus.STATE_SEARCH_CANCEL) {
			unRegReiceiver();
			mState = BTStatus.STATE_IDLE;
			if (mBluetoothDeviceInterface != null) {
				mBluetoothDeviceInterface.onBlueToothStatusChanged(mState);
			}
			bluetoothAdapter.cancelDiscovery();
		}
	}

	public List<BluetoothDevice> getBondedDevicesList() {
		if (bluetoothAdapter != null) {
			Set<BluetoothDevice> set = bluetoothAdapter.getBondedDevices();
			if (set == null || set.isEmpty()) {
				return null;
			} else {
				return new ArrayList<BluetoothDevice>(set);
			}
		} else
			return null;
	}

	public void releaseResrouce() {
		BTStatus.tag("releaseResrouce");
		onStopSearch();
		// if (isOpen()) {
		// closeBluetooth();
		// }
		mBluetoothDeviceInterface = null;
	}

	public void openBluetooth(Activity activity) {
		Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		activity.startActivityForResult(enableBtIntent, 1000);

	}

	public void closeBluetooth() {
		this.bluetoothAdapter.disable();
	}

	public boolean isOpen() {
		return this.bluetoothAdapter.isEnabled();

	}

	public void searchDevices() {
		this.bluetoothAdapter.startDiscovery();
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			BTStatus.tag("onReceive -->" + action);
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (mBluetoothDeviceInterface != null) {
					mBluetoothDeviceInterface.onSearchDiscover(device);
				}
				// if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
				// addBandDevices(device);
				// } else {
				// addUnbondDevices(device);
				// }
			} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				mState = BTStatus.STATE_SEARCH_ING;
				if (mBluetoothDeviceInterface != null) {
					mBluetoothDeviceInterface.onBlueToothStatusChanged(mState);
				}

			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				mState = BTStatus.STATE_SEARCH_FINISH;
				if (mBluetoothDeviceInterface != null) {
					mBluetoothDeviceInterface.onBlueToothStatusChanged(mState);
				}
				// bluetoothAdapter.cancelDiscovery();
			} else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
				if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
					mState = BTStatus.STATE_BL_ENABLE;
					if (mBluetoothDeviceInterface != null) {
						mBluetoothDeviceInterface.onBlueToothStatusChanged(mState);
					}
					// switchBT.setText("�ر�����");
					// searchDevices.setEnabled(true);
					// bondDevicesListView.setEnabled(true);
					// unbondDevicesListView.setEnabled(true);
				} else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
					mState = BTStatus.STATE_BL_DISABLE;
					if (mBluetoothDeviceInterface != null) {
						mBluetoothDeviceInterface.onBlueToothStatusChanged(mState);
					}
				}
			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				// 当设备的连接状态改变
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				switch (device.getBondState()) {
				case BluetoothDevice.BOND_BONDING:
					BTStatus.tag(device.getName() + "正在配对......");
					if (mBluetoothDeviceInterface != null) {
						mBluetoothDeviceInterface.onBlueToothStatusChanged(BTStatus.STATE_BL_STATUS);
					}
					break;
				case BluetoothDevice.BOND_BONDED:
					if (mBluetoothDeviceInterface != null) {
						if (!TextUtils.isEmpty(device.getName())) {
							try {
								ToastUtils.showToast("配对成功");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						mBluetoothDeviceInterface.onBlueToothStatusChanged(BTStatus.STATE_BL_STATUS);
					}
					BTStatus.tag(device.getName() + "完成配对......");
					break;
				case BluetoothDevice.BOND_NONE:
					BTStatus.tag(device.getName() + "取消配对......");
					if (mBluetoothDeviceInterface != null) {
						mBluetoothDeviceInterface.onBlueToothStatusChanged(BTStatus.STATE_BL_STATUS);
					}
				default:
					break;
				}
			}

		}

	};

}