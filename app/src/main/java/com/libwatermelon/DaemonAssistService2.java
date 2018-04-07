package com.libwatermelon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by tf on 3/9/2018.
 */

public class DaemonAssistService2 extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
