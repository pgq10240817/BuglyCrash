/**   
 * Filename:    BluetoothOpenInterface.java
 * Copyright:   Copyright (c)2010
 * Company: Founder Mobile Media Technology(Beijing) Co.,Ltd.g
 * @version:    1.0
 * @since:       JDK 1.6.0_21
 * Create at:   2014-12-8 下午9:08:39
 * Description:
 * Modification History:
 * Date     Author           Version           Description
 * ------------------------------------------------------------------
 * 2014-12-8    pgq            1.0          1.0 Version
 */
package app.yhpl.news.bl;

import android.bluetooth.BluetoothDevice;

/**
 * @author pgq
 * @2014-12-8
 * @BluetoothOpenInterface.java
 * @Description:
 * @Copyright: Copyright (c) 2014 Company: Founder Mobile Media Technology(Beijing) Co.,Ltd.
 */
public interface BTOpenInterface {
    void openBluetooth();

    void onDeviceSelect(BluetoothDevice device);
}
