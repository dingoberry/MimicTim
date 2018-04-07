package com.libwatermelon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import wake.tim.mimic.com.mimictim.Info;

/**
 * Created by tf on 3/8/2018.
 */

public class DaemonTimService extends Service {

    public static final String ACTION_START_DAEMON = "startDaemon";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent) {
            String action = intent.getAction();
            Info.y("onStartCommand|action=" + action);
            if (ACTION_START_DAEMON.equals(action)) {
                DaemonUtils.startDaemon(this);
            }
        }
        return START_NOT_STICKY;
    }
}
