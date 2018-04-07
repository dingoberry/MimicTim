package wake.tim.mimic.com.mimictim;

import android.util.Log;

/**
 * Created by tf on 3/6/2018.
 */

public class Info {

    private static final String TAG1 = "yymm";
    private static final String TAG2 = "ccmm";

    public static void y(String msg) {
        Log.i(TAG1, msg);
    }

    public static void y(Throwable t) {
        Log.i(TAG1, "oooops!", t);
    }

    public static void c(String msg) {
        Log.i(TAG2, msg);
    }

    public static void c(Throwable t) {
        Log.i(TAG2, "oooops!", t);
    }
}
