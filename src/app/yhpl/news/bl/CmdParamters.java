package app.yhpl.news.bl;

public class CmdParamters {

	/*
	 * 00 主机给从机的握手信号
	 * 
	 * 01 读地�?��00000~00099区间的变�?
	 * 
	 * 02 读地�?��10000~10099区间的变�?
	 * 
	 * 03 读地�?��40000~40999区间的变�?
	 * 
	 * 05 写地�?��00000~00099区间的变量（写单个地�?��
	 * 
	 * 06 写地�?��40000~40999区间的变量（写单个地�?��
	 * 
	 * 15 写地�?��00000~00099区间的变量（写多个地�?��
	 * 
	 * 16 写地�?��40000~40999区间的变量（写多个地�?��
	 */

	public static final int HANDLER_CONNECT_BEGIN = 1;
	public static final int HANDLER_CONNECT_ING = 2;
	public static final int HANDLER_MSG_REIVIED = 3;
	public static final int HANDLER_CONNECT_FAILED = 4;
	public static final int HANDLER_SEND_SUCCESS = 5;

	public static long TIME_SAFTEY = 1000;
	public static long HANDLER_TIME_SAFTEY = 2000;

	public static String PING = "00";
	public static String READ_WORDS = "03";
	public static String READ_HEAD_SINGLE = "02";
	public static String READ_BYTES = "01";
	public static String WRITE_BYTE = "05";
	public static String WRITE_WORD = "06";
	public static String WRITE_BYTES = "0F";
	public static String WRITE_WORDS = "10";
	public static String OFFSET_XYZ_ABS_START = "0000";
	public static String OFFSET_XYZ_RELATIVE_START = "000C";
	public static String OFFSET_XYZ_TRANSLATE_START = "0012";
	public static String OFFSET_XYZ_ROTATE_START = "001e";
	public static String XYZ_COUNT = "0003";
	public static String XYZ_BYTES_LENGTH = "06";
	public static String CMD_SEPARATOR = "";
	public static String PREFIX = "01";

	public static String START_40 = "9C40";

	public static String getXYZAbsCmd() {
		return READ_WORDS + CMD_SEPARATOR + OFFSET_XYZ_ABS_START + CMD_SEPARATOR + XYZ_COUNT;

	}

	public static String getXYZRelativeCmd() {
		return READ_WORDS + CMD_SEPARATOR + OFFSET_XYZ_RELATIVE_START + CMD_SEPARATOR + XYZ_COUNT;

	}

	public static String getReadXYZTest() {
		String cmd = "01039C400006";

		cmd = "01039C4C0006";
		// String cmd = "01039C400006";
		String crc = CRC16.getCRC16HexString(cmd);

		return cmd + crc;
		// return "30313033394334303030303645413443";
		// return PREFIX + READ_WORDS + START_40 + "0006EA4C";

	}

	public static String getReadDefaultCmd() {
		// String cmd = "01039C400006";
		// if (MainActivity.mXyzWay == GlobleParameters.XYZ_WAY_MCS) {
		// cmd = "01039C400006";
		// } else {
		// cmd = "01039C4C0006";
		// }

		// String cmd = "01039C400038";
		// if (MainActivity.mXyzWay == GlobleParameters.XYZ_WAY_CHECK_HEAD) {
		// cmd = "010227100001";
		// } else {
		// cmd = "01039C400038";
		// }

		String cmd = "01039C4000" + Integer.toHexString(4).toUpperCase();
		String crc = CRC16.getCRC16HexString(cmd);

		return cmd + crc;
		// return "30313033394334303030303645413443";
		// return PREFIX + READ_WORDS + START_40 + "0006EA4C";

	}

	public static String getPingCmd() {

		String cmd = "0100";
		String crc = CRC16.getCRC16HexString(cmd);
		return cmd + crc;

	}

	public static String getWriteTranSlasteCmd(float dx, float dy, float dz) {
		// 01 10 9c52 0003 0c
		StringBuilder cmd = new StringBuilder("01109C4C00060C");
		long mDx = (long) (dx * 1000);
		long mDy = (long) (dy * 1000);
		long mDz = (long) (dz * 1000);
		// cmd.append(Utils.getHexFloat4BytesString(dx));
		// cmd.append(Utils.getHexFloat4BytesString(dy));
		// cmd.append(Utils.getHexFloat4BytesString(dz));

		cmd.append(Utils.getHexLong4BytesString(mDx));
		cmd.append(Utils.getHexLong4BytesString(mDy));
		cmd.append(Utils.getHexLong4BytesString(mDz));
		String crc = CRC16.getCRC16HexString(cmd.toString());
		return cmd.toString() + crc;

	}

	public static String getWriteExchangeCmd(float dx, float dy, float dz) {
		// 01 10 9c52 0003 0c
		// StringBuilder cmd = new StringBuilder("01109C4C00060C");
		// long mDx = (long) (dx * 1000);
		// long mDy = (long) (dy * 1000);
		// long mDz = (long) (dz * 1000);
		// // cmd.append(Utils.getHexFloat4BytesString(dx));
		// // cmd.append(Utils.getHexFloat4BytesString(dy));
		// // cmd.append(Utils.getHexFloat4BytesString(dz));
		//
		// cmd.append(Utils.getHexLong4BytesString(mDx));
		// cmd.append(Utils.getHexLong4BytesString(mDy));
		// cmd.append(Utils.getHexLong4BytesString(mDz));

		StringBuilder cmd = new StringBuilder("01109C7600060C");
		cmd.append(Utils.getHexFloat4BytesString(dx));
		cmd.append(Utils.getHexFloat4BytesString(dy));
		cmd.append(Utils.getHexFloat4BytesString(dz));
		String crc = CRC16.getCRC16HexString(cmd.toString());
		return cmd.toString() + crc;

	}

	public static String getWriteRotateCmd(float angle, int xyz) {
		String moff = "9C62";

		if (xyz == 1) {
			moff = "9C5E";
		} else if (xyz == 2) {
			moff = "9C60";
		} else {
			moff = "9C62";
		}

		// 01 10 9c52 0003 0c
		// 01 10
		// StringBuilder cmd = new StringBuilder("0106" + moff);
		// cmd.append(Utils.getHexFloat2BytesString(angle));
		// String crc = CRC16.getCRC16HexString(cmd.toString());
		// return cmd.toString() + crc;
		moff += "000204";
		StringBuilder cmd = new StringBuilder("0110" + moff);
		cmd.append(Utils.getHexFloat4BytesString(angle));
		String crc = CRC16.getCRC16HexString(cmd.toString());
		return cmd.toString() + crc;

	}

	public static String getWriteRotatePCSCms(float angleX, float angleY, float angleZ) {
		StringBuilder cmd = new StringBuilder("01109C7000060C");

		cmd.append(Utils.getHexFloat4BytesString(angleX));
		cmd.append(Utils.getHexFloat4BytesString(angleY));
		cmd.append(Utils.getHexFloat4BytesString(angleZ));
		String crc = CRC16.getCRC16HexString(cmd.toString());
		return cmd.toString() + crc;
	}

	// touch head
	public static String getReadTouchHeadSwitchCmd() {
		String cmd = "010100060001";

		String crc = CRC16.getCRC16HexString(cmd);

		return cmd + crc;

	}

	// 打开测头开关信号
	public static String getWriteTocuhHeadSwitchCmd(boolean turnOn) {
		String cmd = "01050006";
		String mBoolean = turnOn ? "FF00" : "0000";
		cmd += mBoolean;
		String crc = CRC16.getCRC16HexString(cmd);

		return cmd + crc;

	}

	public static String getWritePickTouchHeadCmd() {
		String cmd = "01050007FF00";
		String crc = CRC16.getCRC16HexString(cmd);
		return cmd + crc;
	}

	public static String getWriteOkCmd(boolean isOk) {
		String cmd = "01050008";
		String mBoolean = isOk ? "FF00" : "0000";
		cmd += mBoolean;
		String crc = CRC16.getCRC16HexString(cmd);
		return cmd + crc;
	}

	public static String getWriteDelCmd(boolean isDel) {
		String cmd = "01050009";
		String mBoolean = isDel ? "FF00" : "0000";
		cmd += mBoolean;
		String crc = CRC16.getCRC16HexString(cmd);
		return cmd + crc;
	}

	public static String getWrite21ItemsCmd(boolean is21Items) {
		String cmd = "0105000A";
		String mBoolean = is21Items ? "FF00" : "0000";
		cmd += mBoolean;
		String crc = CRC16.getCRC16HexString(cmd);
		return cmd + crc;
	}

	public static String getRead21ItemsCmd() {
		String cmd = "0101000A0001";
		String crc = CRC16.getCRC16HexString(cmd);
		return cmd + crc;
	}

	// 测头信号
	public static String getReadTouchHeadSignCmd() {
		// String cmd = "010100060001";
		String cmd = "010227100001";
		String crc = CRC16.getCRC16HexString(cmd);

		return cmd + crc;
	}

	// 获取测头锁存位置
	public static String getReadTouchHeadXYZ() {
		String cmd = "01039C460006";

		// String cmd = "01039C400006";
		String crc = CRC16.getCRC16HexString(cmd);

		return cmd + crc;
		// return "30313033394334303030303645413443";
		// return PREFIX + READ_WORDS + START_40 + "0006EA4C";

	}

	public static String getWritePCSMCSStatus(boolean isMcs) {
		String cmd = "010F0003000301";
		if (isMcs) {
			cmd += "07";
		} else {
			cmd += "00";
		}
		String crc = CRC16.getCRC16HexString(cmd);

		return cmd + crc;
	}

	// // 6 reset
	//
	// // 打开测头开关信号
	// public static String getWritePCSResetCmd() {
	// String cmd = "01050006";
	// String mBoolean = turnOn ? "FF00" : "0000";
	// cmd += mBoolean;
	// String crc = CRC16.getCRC16HexString(cmd);
	//
	// return cmd + crc;
	//
	// }

	// 8 xyz reverse

	public static String getWriteXyzReverseCmd(boolean isXReverse, boolean isYReverse, boolean isZReverse) {

		String cmd = "010F0000000301";
		String value = "";
		if (isXReverse) {
			value = "0";
		} else {
			value = "1";
		}
		if (isYReverse) {
			value = "0" + value;
		} else {
			value = "1" + value;
		}
		if (isZReverse) {
			value = "0" + value;
		} else {
			value = "1" + value;
		}
		int mBinaryValue = Integer.parseInt(value, 2);
		String mHexValue = "0" + Integer.toHexString(mBinaryValue);
		cmd = cmd + mHexValue;

		String crc = CRC16.getCRC16HexString(cmd);

		return cmd + crc;

	}

	// public static String getReadStettingXYZ() {
	// String cmd = "01039C64000C";
	// // String cmd = "01039C400006";
	// String crc = CRC16.getCRC16HexString(cmd);
	//
	// return cmd + crc;
	// // return "30313033394334303030303645413443";
	// // return PREFIX + READ_WORDS + START_40 + "0006EA4C";
	//
	// }

	public static String getWriteSettingXYZCmd(float kx, float ky, float kz, float xy, float yz, float xz) {
		// 01 10 9c52 0003 0c
		StringBuilder cmd = new StringBuilder("01109C64000C18");
		long mDx = (long) (kx * 1000);
		long mDy = (long) (ky * 1000);
		long mDz = (long) (kz * 1000);
		// cmd.append(Utils.getHexFloat4BytesString(dx));
		// cmd.append(Utils.getHexFloat4BytesString(dy));
		// cmd.append(Utils.getHexFloat4BytesString(dz));

		cmd.append(Utils.getHexLong4BytesString(mDx));
		cmd.append(Utils.getHexLong4BytesString(mDy));
		cmd.append(Utils.getHexLong4BytesString(mDz));
		cmd.append(Utils.getHexFloat4BytesString(xy));
		cmd.append(Utils.getHexFloat4BytesString(yz));
		cmd.append(Utils.getHexFloat4BytesString(xz));

		String crc = CRC16.getCRC16HexString(cmd.toString());
		return cmd.toString() + crc;

	}

	public static String getWriteSoftCmd(int ver) {
		StringBuilder cmd = new StringBuilder("01069C7C");
		cmd.append(Utils.getHexShort2BytesString((short) ver));
		String crc = CRC16.getCRC16HexString(cmd.toString());
		return cmd.toString() + crc;
	}
}
