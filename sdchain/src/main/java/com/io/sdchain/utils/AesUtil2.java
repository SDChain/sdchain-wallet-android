package com.io.sdchain.utils;

import com.orhanobut.logger.Logger;

import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.io.sdchain.BuildConfig.SIGNKEY;

public final class AesUtil2 {
    public static final String VIPARA = "0102030405060708";
    public static final String bm = "GBK";


    public static String encrypt(String dataPassword, String cleartext) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
        SecretKeySpec key = new SecretKeySpec(dataPassword.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(cleartext.getBytes(bm));

        return Base64Util.encode(encryptedData);
    }

    public static String decrypt(String dataPassword, String encrypted) throws Exception {
        byte[] byteMi = Base64Util.decode(encrypted);
        IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
        SecretKeySpec key = new SecretKeySpec(dataPassword.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte[] decryptedData = cipher.doFinal(byteMi);

        return new String(decryptedData, bm);
    }

    public static String encryptGET(String content) {
        String Key = null;
        try {
            Key = encrypt(EncryptUtil.decrypt(SecurityUtils.getSign(), SIGNKEY), content);
            Key = URLEncoder.encode("" + Key, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("encrypt wrong：" + e.toString());
        }
        return Key;
    }

    public static String encryptPOST(String content) {
        String Key = null;
        try {
            Key = encrypt(EncryptUtil.decrypt(SecurityUtils.getSign(), SIGNKEY), content);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("encrypt wrong：" + e.toString());
        }
        return Key;
    }

}
