package app.yhpl.news.bl;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

public class BluetoothUtils {

	public static BluetoothAdapter getBluetoothAdapter(Context context) {
		BluetoothAdapter mBluetoothAdapter = null;
		// if
		// (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
		// {

		// 初始化 Bluetooth adapter,
		// 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
		final BluetoothManager bluetoothManager = (BluetoothManager) context
				.getSystemService(Context.BLUETOOTH_SERVICE);
		if (bluetoothManager != null) {
			mBluetoothAdapter = bluetoothManager.getAdapter();
		}
		// }
		return mBluetoothAdapter;

	}

}
