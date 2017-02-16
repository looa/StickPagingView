package org.looa.ndkencode;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ranxiangwei on 2017/2/6.
 */

public class SNNative {

    // Used to load the 'sn-native' library on application startup.
    static {
        System.loadLibrary("sn-native");
    }

    /**
     * A native method that is implemented by the 'sn-native' native library,
     * which is packaged with this application.
     */
    public static native String stringFromJNI();

    public static native String encode(String token, String date, String nonce, List<HashMap<String, String>> list);
}
