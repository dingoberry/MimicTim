package com.libwatermelon;

import android.content.Context;

import java.io.File;

import wake.tim.mimic.com.mimictim.Info;

/**
 * Created by tf on 3/14/2018.
 */
class WaterStrategy2p extends CommonWaterStrategy implements DaemonDeadListener {

    static final String INDICATOR_DAEMON_ASSISTANT_FILENAME = "indicator_d1";
    private static final String INDICATOR_DAEMON_ASSISTANT_FILENAME2 = "indicator_d2";
    static final String INDICATOR_DIR_NAME = "indicators";
    static final String INDICATOR_PERSISTENT_FILENAME = "indicator_p1";
    private static final String INDICATOR_PERSISTENT_FILENAME2 = "indicator_p2";
    static final String OBSERVER_DAEMON_ASSISTANT_FILENAME = "observer_d1";
    private static final String OBSERVER_DAEMON_ASSISTANT_FILENAME2 = "observer_d2";
    static final String OBSERVER_PERSISTENT_FILENAME = "observer_p1";
    private static final String OBSERVER_PERSISTENT_FILENAME2 = "observer_p2";

    private void doDaemonLogic(Context cxt, File binary, File lockFolder, int restartTime, String p1,
                       String p2, String p3, String p4, String p5, String p6, String p7, String p8) {
        new WaterDaemon(cxt, WaterStrategy2p.this)
                .doDaemon2_(mTransactCode, mNativePtr, restartTime, binary.getAbsolutePath(),
                        new File(lockFolder, p1).getAbsolutePath(),
                        new File(lockFolder, p2).getAbsolutePath(),
                        new File(lockFolder, p3).getAbsolutePath(),
                        new File(lockFolder, p4).getAbsolutePath(),
                        new File(lockFolder, p5).getAbsolutePath(),
                        new File(lockFolder, p6).getAbsolutePath(),
                        new File(lockFolder, p7).getAbsolutePath(),
                        new File(lockFolder, p8).getAbsolutePath());
        startProcessByAmsBinder();
    }

    @Override
    public void onDaemonAssistantCreate(final Context context, final WaterConfigurations configurations) {
        Info.y("onDaemonAssistantCreate");
        mTransactCode = getTransactCode();
        initAmsBinder();
        initServiceParcel(context, configurations.PERSISTENT_CONFIG.SERVICE_NAME);
        startProcessByAmsBinder();

        Thread t = new Thread() {
            @Override
            public void run() {
                File binary = new File(context.getDir(BINARY_DEST_DIR_NAME, 0), BINARY_FILE_NAME);
                File lockDir = context.getDir(INDICATOR_DIR_NAME, 0);
                for (int i = 0; i < 50; i++) {
                    doDaemonLogic(context, binary, lockDir, getRestartTime(configurations.DAEMON_ASSISTANT_CONFIG.RADICAL_MODE),
                            INDICATOR_DAEMON_ASSISTANT_FILENAME,
                            INDICATOR_PERSISTENT_FILENAME,
                            OBSERVER_DAEMON_ASSISTANT_FILENAME,
                            OBSERVER_PERSISTENT_FILENAME,
                            INDICATOR_PERSISTENT_FILENAME2,
                            INDICATOR_DAEMON_ASSISTANT_FILENAME2,
                            OBSERVER_PERSISTENT_FILENAME2,
                            OBSERVER_DAEMON_ASSISTANT_FILENAME2);
                }
            }
        };
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    @Override
    public void onDaemonDead() {
        Info.y("onDaemonDead");
    }

    @Override
    public boolean onInitialization(Context context, WaterConfigurations configurations) {
        Info.y("onInitialization");
        return DaemonUtils.copyAssets(context, BINARY_FILE_NAME, CHIP_MODE_ARMEABI, BINARY_DEST_DIR_NAME);
    }

    @Override
    public void onPersistentCreate(final Context context, final WaterConfigurations configurations) {
        Info.y("onPersistentCreate");
        mTransactCode = getTransactCode();
        initAmsBinder();
        initServiceParcel(context, configurations.DAEMON_ASSISTANT_CONFIG.SERVICE_NAME);
        startProcessByAmsBinder();

        new Thread() {
            @Override
            public void run() {
                File binary = new File(context.getDir(BINARY_DEST_DIR_NAME, 0), BINARY_FILE_NAME);
                File lockDir = context.getDir(INDICATOR_DIR_NAME, 0);
                for (int i = 0; i < 50 && DaemonUtils.isWaterOn(context); i++) {
                    doDaemonLogic(context, binary, lockDir, getRestartTime(configurations.PERSISTENT_CONFIG.RADICAL_MODE),
                            INDICATOR_PERSISTENT_FILENAME,
                            INDICATOR_DAEMON_ASSISTANT_FILENAME,
                            OBSERVER_PERSISTENT_FILENAME,
                            OBSERVER_DAEMON_ASSISTANT_FILENAME,
                            INDICATOR_DAEMON_ASSISTANT_FILENAME2,
                            INDICATOR_PERSISTENT_FILENAME2,
                            OBSERVER_DAEMON_ASSISTANT_FILENAME2,
                            OBSERVER_PERSISTENT_FILENAME2);
                }
            }
        }.start();
    }
}
