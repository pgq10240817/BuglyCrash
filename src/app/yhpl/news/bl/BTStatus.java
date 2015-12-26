/**   
 * Filename:    Status.java
 * Copyright:   Copyright (c)2010
 * Company: Founder Mobile Media Technology(Beijing) Co.,Ltd.g
 * @version:    1.0
 * @since:       JDK 1.6.0_21
 * Create at:   2014-12-8 下午12:41:06
 * Description:
 * Modification History:
 * Date     Author           Version           Description
 * ------------------------------------------------------------------
 * 2014-12-8    pgq            1.0          1.0 Version
 */
package app.yhpl.news.bl;

/**
 * @author pgq
 * @2014-12-8 @Status.java
 * @Description:
 * @Copyright: Copyright (c) 2014 Company: Founder Mobile Media
 *             Technology(Beijing) Co.,Ltd.
 */
public class BTStatus {
	public static final int STATE_IDLE = 0;
	public static final int STATE_SEARCH_INIT = 1;
	public static final int STATE_SEARCH_ING = 2;
	public static final int STATE_SEARCH_FINISH = 3;
	public static final int STATE_SEARCH_CANCEL = 4;
	public static final int STATE_BL_DISABLE = 5;
	public static final int STATE_BL_ENABLE = 6;

	/**
	 * Common is bl bonded
	 */
	public static final int STATE_BL_STATUS = 7;
	public static String TAG = "myTag";

	public static void tag(String msg) {

		android.util.Log.e(TAG, msg);

	}

	public static void tagV(String msg) {

		android.util.Log.v(TAG, msg);

	}

	public static void tagD(String msg) {

		android.util.Log.d(TAG, msg);

	}
}
