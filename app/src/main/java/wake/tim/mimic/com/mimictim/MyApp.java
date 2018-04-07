package wake.tim.mimic.com.mimictim;

import android.app.Application;
import android.content.Context;

import com.libwatermelon.DaemonUtils;

/**
 * Created by tf on 3/6/2018.
 */

public class MyApp extends Application {

    private static Context sCxt;

    public static Context getAppContext() {
        return sCxt;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        DaemonUtils.startDaemon(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sCxt = this;
    }
}
