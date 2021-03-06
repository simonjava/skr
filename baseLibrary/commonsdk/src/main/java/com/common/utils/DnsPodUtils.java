package com.common.utils;

import android.util.Pair;

import com.common.log.MyLog;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by yurui on 4/28/16.
 */
public class DnsPodUtils {
    private static final String LogTag = DnsPodUtils.class.getName();
    private static final String encKey = "OhPy@[ml";//授权key：
    private static final String encId = "131";//授权ID：131

    public static String getAddressByHostDnsPod(String domain) {
        try {
            //初始化 密钥
            SecretKeySpec keySpec = new SecretKeySpec(encKey.getBytes("utf-8"), "DES");
            //选择使用DES算法，ECB方式，填充方式为PKCS5Padding
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            //初始化
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            //获取加密后的字符串
            byte[] encryptedString = cipher.doFinal(domain.getBytes("utf-8"));
            List<Pair<String, String>> parms = new ArrayList<>();
            parms.add(new Pair<>("id", encId));
            parms.add(new Pair<>("ttl", "1"));
            parms.add(new Pair<>("dn", bytes2HexString(encryptedString)));
            MyLog.w(LogTag + " start dnspod");
            String result = U.getHttpUtils().doHttpPost(U.app(), "http://119.29.29.29/d", parms, null, null, null);
            MyLog.w(LogTag + " http://119.29.29.29/d?dn=" + bytes2HexString(encryptedString) + "&id=" + encId + "&ttl=1 result=" + result);

            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decryptedString = cipher.doFinal(hexString2Bytes(result));
            MyLog.w(LogTag + " decryptedString=" + new String(decryptedString, "utf-8"));
            return new String(decryptedString, "utf-8");
        } catch (Exception e) {
            MyLog.e(LogTag, e);
        }
        return "";
    }

    /**
     * 16进制字符串转成byte数组
     *
     * @param src
     * @return
     */
    public static byte[] hexString2Bytes(String src) {
        byte[] tmp = src.getBytes();
        byte[] ret = new byte[tmp.length / 2];
        for (int i = 0; i < tmp.length / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }


    /**
     * byte数组转换成16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytes2HexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
