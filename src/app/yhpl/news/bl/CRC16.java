package app.yhpl.news.bl;

public class CRC16 {
	public static void main(String args[]) { // B8C3
		long lon = GetModBusCRC("01 03 9C 40 00 06".replace(" ", ""));
		int h1, l0;
		l0 = (int) lon / 256;
		h1 = (int) lon % 256;
		String s = "";
		if (Integer.toHexString(h1).length() < 2) {
			s = "0" + Integer.toHexString(h1);
		} else {
			s = Integer.toHexString(h1);
		}
		if (Integer.toHexString(l0).length() < 2) {
			s = s + "0" + Integer.toHexString(l0);
		} else {
			s = s + Integer.toHexString(l0);
		}
		System.out.println(s);
	}

	public static String getCRC16HexString(String mhexString) {

		long lon = GetModBusCRC(mhexString.replace(" ", ""));
		int h1, l0;
		l0 = (int) lon / 256;
		h1 = (int) lon % 256;
		String s = "";
		if (Integer.toHexString(h1).length() < 2) {
			s = "0" + Integer.toHexString(h1);
		} else {
			s = Integer.toHexString(h1);
		}
		if (Integer.toHexString(l0).length() < 2) {
			s = s + "0" + Integer.toHexString(l0);
		} else {
			s = s + Integer.toHexString(l0);
		}
		return s.toUpperCase();

	}

	private static int[] strToToHexByte(String hexString) {
		hexString = hexString.replace(" ", "");
		// ���Ȳ���ż����ô������ӿո�

		if ((hexString.length() % 2) != 0) {
			hexString += " ";
		}

		// �������飬����Ϊ��ת���ַ��ȵ�һ�롣
		int[] returnBytes = new int[hexString.length() / 2];

		for (int i = 0; i < returnBytes.length; i++)
			// ����Ϊʲô����ָ�����?
			returnBytes[i] = (0xff & Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16));
		return returnBytes;
	}

	public static long GetModBusCRC(String DATA) {
		long functionReturnValue = 0;
		long i = 0;

		long J = 0;
		int[] v = null;
		byte[] d = null;
		v = strToToHexByte(DATA);

		long CRC = 0;
		CRC = 0xffffL;
		for (i = 0; i <= (v).length - 1; i++) {
			CRC = (CRC / 256) * 256L + (CRC % 256L) ^ v[(int) i];
			for (J = 0; J <= 7; J++) {
				long d0 = 0;
				d0 = CRC & 1L;
				CRC = CRC / 2;
				if (d0 == 1)
					CRC = CRC ^ 0xa001L;
			}
		}
		CRC = CRC % 65536;
		functionReturnValue = CRC;
		return functionReturnValue;
	}

}
