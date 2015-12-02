package com.chinacnit.elevatorguard.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    private static final byte[] ToHex_ = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String getMD5(String s) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] dataToHash = s.getBytes();
        md5.update(dataToHash, 0, dataToHash.length);
        return hexStringFromBytes(md5.digest());
    }

    private static String hexStringFromBytes(byte[] b) {
        byte[] hex_bytes = new byte[b.length * 2];
        int i = 0, j = 0;

        for (i = 0; i < b.length; i++) {
            hex_bytes[j] = ToHex_[(b[i] & 0x000000F0) >> 4];
            hex_bytes[j + 1] = ToHex_[b[i] & 0x0000000F];
            j += 2;
        }
        return new String(hex_bytes);
    }
}
