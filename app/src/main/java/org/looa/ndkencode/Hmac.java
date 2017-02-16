package org.looa.ndkencode;

/**
 * Created by ranxiangwei on 2017/2/6.
 */

import android.util.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Hmac<br/>
 * algorithm HmacMD5/HmacSHA/HmacSHA256/HmacSHA384/HmacSHA512
 *
 * @author Aub
 */
public class Hmac {

    private static final String UTF_8 = "UTF-8";
    private static final String HMAC_SHA256 = "HmacSHA256";

    static String encode(String key, String password) {
        byte passwordBytes[];
        byte codeBytes[];
        Mac mac;
        String resultString = null;
        try {
            passwordBytes = password.getBytes(UTF_8);
            codeBytes = key.getBytes(UTF_8);
            SecretKey secret_key = new SecretKeySpec(codeBytes, HMAC_SHA256);
            mac = Mac.getInstance(secret_key.getAlgorithm());
            mac.init(secret_key);
            byte resultBytes[] = mac.doFinal(passwordBytes);
            resultString = byteToBASE64(resultBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    private static String byteToBASE64(byte[] data) {
        return Base64.encodeToString(data, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
    }
}
