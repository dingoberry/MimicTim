package com.libwatermelon;

import android.content.Context;

import java.io.File;

import wake.tim.mimic.com.mimictim.Info;

/**
 * Created by tf on 3/14/2018.
 */

class WaterStrategyMi extends WaterStrategy2p implements DaemonDeadListener {

    private static final String OBSERVER_DAEMON_CHILD = "observer_daemon_child";
    private static final String OBSERVER_PERSISTENT_CHILD = "observer_persistent_child";

    private void doDaemonLogic(Context cxt, File binary, File lockFolder, int restartTime, String indicator1,
                               String indicator2, String observer1, String observer2, String childObserver) {
        new WaterDaemon(cxt, WaterStrategyMi.this)
                .doDaemon3_(WaterStrategyMi.this.mTransactCode,
                        WaterStrategyMi.this.mNativePtr,
                        restartTime,
                        binary.getAbsolutePath(),
                        new File(lockFolder, indicator1).getAbsolutePath(),
                        new File(lockFolder, indicator2).getAbsolutePath(),
                        new File(lockFolder, observer1).getAbsolutePath(),
                        new File(lockFolder, observer2).getAbsolutePath(),
                        new File(lockFolder, childObserver).getAbsolutePath());
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
                            OBSERVER_DAEMON_CHILD);

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        Info.y(e);
                    }
                }
            }
        };
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    @Override
    public void onPersistentCreate(final Context context, final WaterConfigurations configurations) {
        Info.y("onPersistentCreate");
        mTransactCode = getTransactCode();
        initAmsBinder();
        initServiceParcel(context, configurations.PERSISTENT_CONFIG.SERVICE_NAME);
        startProcessByAmsBinder();

        new Thread() {
            @Override
            public void run() {
                File binary = new File(context.getDir(BINARY_DEST_DIR_NAME, 0), BINARY_FILE_NAME);
                File lockDir = context.getDir(INDICATOR_DIR_NAME, 0);

                for (int i = 0; i < 50; i++) {
                    doDaemonLogic(context, binary, lockDir, getRestartTime(configurations.DAEMON_ASSISTANT_CONFIG.RADICAL_MODE),
                            INDICATOR_PERSISTENT_FILENAME,
                            INDICATOR_DAEMON_ASSISTANT_FILENAME,
                            OBSERVER_PERSISTENT_FILENAME,
                            OBSERVER_DAEMON_ASSISTANT_FILENAME,
                            OBSERVER_PERSISTENT_CHILD);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        Info.y(e);
                    }
                }
            }
        }.start();
    }
}
