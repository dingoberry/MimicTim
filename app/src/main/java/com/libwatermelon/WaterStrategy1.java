package com.libwatermelon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Process;
import android.os.SystemClock;

import java.io.File;

import wake.tim.mimic.com.mimictim.Info;

/**
 * Created by tf on 3/14/2018.
 */

class WaterStrategy1 extends BaseWaterStrategy implements DaemonDeadListener {

    private final String BINARY_DEST_DIR_NAME;
    private final String BINARY_FILE_NAME;
    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;

    WaterStrategy1() {
        BINARY_DEST_DIR_NAME = "bin";
        BINARY_FILE_NAME = "daemon1_v2.1.2";
    }

    private void initAlarm(Context cxt, String service) {
        if (null == mAlarmManager) {
            mAlarmManager = (AlarmManager) cxt.getSystemService(Context.ALARM_SERVICE);
        }

        if (null == mPendingIntent) {
            Intent i = new Intent();
            i.setComponent(new ComponentName(cxt.getPackageName(), service));
            i.setFlags(Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);
            mPendingIntent = PendingIntent.getService(cxt, 0, i, 0);
        }
        mAlarmManager.cancel(mPendingIntent);
    }

    @Override
    public void onDaemonAssistantCreate(Context context, WaterConfigurations configurations) {
        Intent i = new Intent();
        i.setComponent(new ComponentName(context.getPackageName(), configurations.PERSISTENT_CONFIG.SERVICE_NAME));
        context.startService(i);
        Process.killProcess(Process.myPid());
    }

    @Override
    public void onDaemonDead() {
        Info.y("onDaemonDead");
        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
                100, mPendingIntent);
        Process.killProcess(Process.myPid());
    }

    @Override
    public boolean onInitialization(Context context, WaterConfigurations configurations) {
        return DaemonUtils.copyAssets(context, BINARY_FILE_NAME,
                Build.CPU_ABI.startsWith(CHIP_MODE_X86) ? CHIP_MODE_X86 : CHIP_MODE_ARMEABI,
                BINARY_DEST_DIR_NAME);
    }

    @Override
    public void onPersistentCreate(final Context context, final WaterConfigurations configurations) {
        initAlarm(context, configurations.DAEMON_ASSISTANT_CONFIG.SERVICE_NAME);

        Thread t = new Thread() {
            @Override
            public void run() {
                new WaterDaemon(context, WaterStrategy1.this).doDaemon1_(context.getPackageName(),
                        configurations.DAEMON_ASSISTANT_CONFIG.SERVICE_NAME,
                        new File(context.getDir(BINARY_DEST_DIR_NAME, Context.MODE_PRIVATE), BINARY_FILE_NAME).getAbsolutePath());
            }
        };
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }
}
