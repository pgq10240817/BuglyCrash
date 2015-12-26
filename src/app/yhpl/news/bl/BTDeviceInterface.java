/**   
 * Filename:    BluetoothDeviceInterface.java
 * Copyright:   Copyright (c)2010
 * Company: Founder Mobile Media Technology(Beijing) Co.,Ltd.g
 * @version:    1.0
 * @since:       JDK 1.6.0_21
 * Create at:   2014-12-8 上午11:54:28
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
 * @BluetoothDeviceInterface.java
 * @Description:
 * @Copyright: Copyright (c) 2014 Company: Founder Mobile Media Technology(Beijing) Co.,Ltd.
 */
public interface BTDeviceInterface {

    void onBlueToothStatusChanged(int status);

    void onSearchDiscover(BluetoothDevice device);
    

}
