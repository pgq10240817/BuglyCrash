/**   
 * Filename:    Utils.java
 * Copyright:   Copyright (c)2010
 * Company:     Fang zheng
 * @version:    1.0
 * @since:       JDK 1.6.0_21
 * Create at:   2014-6-15 下午4:08:57
 * Description:
 * Modification History:
 * Date     Author           Version           Description
 * ------------------------------------------------------------------
 * 2014-6-15    pgq            1.0          1.0 Version
 */
package app.yhpl.news.bl;

/**
 * @author pgq
 * @2014-6-15
 * @Utils.java
 * @Description:
 * @Copyright: Copyright (c) 2014 Company: Founder Mobile Media Technology(Beijing) Co.,Ltd.
 */
public class Utils {
    public static long bytes2long(byte[] b) {
        long temp = 0;
        long res = 0;
        for (int i = 0; i < b.length; i++) {
            res <<= 8;
            temp = b[i] & 0xff;
            res |= temp;
        }
        return res;
    }

    private static byte[] long2Bytes(long num) {
        int bytesCount = 8;
        byte[] byteNum = new byte[bytesCount];
        for (int ix = 0; ix < bytesCount; ++ix) {
            int offset = bytesCount * bytesCount - (ix + 1) * bytesCount;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    private static byte[] get4BytesLong(long num) {
        byte[] mTempBytes = long2Bytes(num);
        byte[] byteNum = new byte[4];
        if (mTempBytes != null && mTempBytes.length > 4) {

            for (int i = 0; i < 4; i++) {
                byteNum[i] = mTempBytes[4 + i];
            }

        }

        return byteNum;

    }

    public static String getHexLong4BytesString(long num) {
        byte[] bytes = get4BytesLong(num);
        return CHexConver.byte2HexStr(bytes, bytes.length).replace(" ", "");
    }

    public static String getHexFloat4BytesString(float num) {
        byte[] bytes = float2byte(num);
        return CHexConver.byte2HexStr(bytes, bytes.length).replace(" ", "");
    }

    public static String getHexFloat2BytesString(float num) {
        byte[] bytes = float2byte(num);
        byte[] m2Bytes = new byte[2];
        m2Bytes[0] = bytes[2];
        m2Bytes[1] = bytes[3];
        return CHexConver.byte2HexStr(m2Bytes, m2Bytes.length).replace(" ", "");
    }

    public static String getHexShort2BytesString(short num) {
        byte[] bytes = short2Byte(num);
        return CHexConver.byte2HexStr(bytes, 2).replace(" ", "");
    }

    public static float getDValueFromString(String host, String client) {
        float result = 0l;
        try {
            float mHost = Float.parseFloat(host);
            float mClient = Float.parseFloat(client);
            result = mHost - mClient;
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 浮点转换为字节
     * 
     * @param f
     * @return
     */
    private static byte[] float2byte(float f) {

        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }

        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }

        return dest;

    }

    public static byte[] short2Byte(short a) {
        byte[] b = new byte[2];

        b[0] = (byte) (a >> 8);
        b[1] = (byte) (a);

        return b;
    }

    /**
     * 字节转换为浮点
     * 
     * @param b
     *            字节（至少4个字节）
     * @param index
     *            开始位置
     * @return
     */
    public static float byte2float(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);

    }

}
