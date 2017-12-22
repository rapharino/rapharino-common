
package org.rapharino.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created By Rapharino on 2017/11/28 下午6:17
 */
public class MD5Util {

    private static ThreadLocal<MessageDigest> messageDigestHolder = new ThreadLocal<>();

    static Logger log = LoggerFactory.getLogger(MD5Util.class);

    // 用来将字节转换成 16 进制表示的字符
    static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    static {
        try {
            MessageDigest message = MessageDigest.getInstance("MD5");
            messageDigestHolder.set(message);
        } catch (NoSuchAlgorithmException e) {
            log.error("java.security.MessageDigest init error", e);
        }
    }

    /***
     * @Title: getMD5Format
     * @Description: 计算MD5并转换为32字节明文显示串
     * @author wujl
     * @param data
     * @return String 返回类型
     */
    public static String getMD5Format(String data) {
        try {
            MessageDigest message = messageDigestHolder.get();
            if (message == null) {
                message = MessageDigest.getInstance("MD5");
                messageDigestHolder.set(message);
            }
            message.update(data.getBytes());
            byte[] b = message.digest();

            String digestHexStr = "";
            for (int i = 0; i < 16; i++) {
                digestHexStr += byteHEX(b[i]);
            }

            return digestHexStr;
        } catch (Exception e) {
            log.error("getMD5Format error", e);
            return null;
        }
    }

    /***
     * @Title: getMD5FormatToBytes
     * @Description: 计算MD5并转换为16字节byte[]
     * @param data
     * @return byte[] 返回类型
     */
    public static byte[] getMD5FormatToBytes(String data) {
        try {
            MessageDigest message = messageDigestHolder.get();
            if (message == null) {
                message = MessageDigest.getInstance("MD5");
                messageDigestHolder.set(message);
            }
            message.update(data.getBytes());
            return message.digest();
        } catch (Exception e) {
            log.error("getMD5FormatToBytes error", e);
            return null;
        }
    }

    public static String getMD5Format(byte[] data) {
        try {
            MessageDigest message = messageDigestHolder.get();
            if (message == null) {
                message = MessageDigest.getInstance("MD5");
                messageDigestHolder.set(message);
            }

            message.update(data);
            byte[] b = message.digest();

            String digestHexStr = "";
            for (int i = 0; i < 16; i++) {
                digestHexStr += byteHEX(b[i]);
            }

            return digestHexStr;
        } catch (Exception e) {
            return null;
        }
    }

    /***
     * @Title: byteHEX
     * @Description:
     * @author wujl
     * @param ib
     * @return String 返回类型
     */
    private static String byteHEX(byte ib) {
        char[] ob = new char[2];
        ob[0] = hexDigits[(ib >>> 4) & 0X0F];
        ob[1] = hexDigits[ib & 0X0F];
        String s = new String(ob);
        return s;
    }
}
