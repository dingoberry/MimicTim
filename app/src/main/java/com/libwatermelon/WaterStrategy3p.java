package com.libwatermelon;

import android.content.Context;

import java.io.File;

import wake.tim.mimic.com.mimictim.Info;

/**
 * Created by tf on 3/9/2018.
 */
class WaterStrategy3p extends CommonWaterStrategy implements DaemonDeadListener {

    private static final String INDICATOR_DIR_NAME = "indicators";
    private static final String INDICATOR__A1 = "indicator_a1";
    private static final String INDICATOR__B1 = "indicator_b1";
    private static final String INDICATOR__C1 = "indicator_c1";
    private static final String OBSERVER_A_CHILD = "observer_a_child";
    private static final String OBSERVER_B_CHILD = "observer_b_child";
    private static final String OBSERVER_C_CHILD = "observer_c_child";
    private static final String OBSERVER__A1 = "observer_a1";
    private static final String OBSERVER__B1 = "observer_b1";
    private static final String OBSERVER__C1 = "observer_c1";

    private void doDaemonLogic(Context cxt, File binary, File lockFolder, int restartTime, String indicator1,
                               String indicator2, String observer1, String observer2, String childObserver) {
        new WaterDaemon(cxt, WaterStrategy3p.this)
                .doDaemon3_(WaterStrategy3p.this.mTransactCode,
                        WaterStrategy3p.this.mNativePtr,
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
    public void onDaemonAssistant2Create(final Context context, final WaterConfigurations configurations) {
        Info.y("onDaemonAssistant2Create");
        mTransactCode = getTransactCode();
        initAmsBinder();
        initServiceParcel(context, configurations.PERSISTENT_CONFIG.SERVICE_NAME);
        startProcessByAmsBinder();

        new Thread() {
            public void run() {
                File binary = new File(context.getDir(BINARY_DEST_DIR_NAME, 0), BINARY_FILE_NAME);
                File lockDir = context.getDir(INDICATOR_DIR_NAME, 0);
                for (int i = 0; i < 50; i++) {
                    doDaemonLogic(context, binary, lockDir,
                            getRestartTime(configurations.DAEMON_ASSISTANT_CONFIG2.RADICAL_MODE),
                            INDICATOR__C1,
                            INDICATOR__A1,
                            OBSERVER__C1,
                            OBSERVER__A1,
                            OBSERVER_C_CHILD);
                }
            }
        }.start();
    }

    @Override
    public void onDaemonAssistantCreate(final Context context, final WaterConfigurations configurations) {
        Info.y("onDaemonAssistantCreate");
        mTransactCode = getTransactCode();
        initAmsBinder();
        initServiceParcel(context, configurations.DAEMON_ASSISTANT_CONFIG2.SERVICE_NAME);
        startProcessByAmsBinder();

        new Thread() {
            @Override
            public void run() {
                if (DaemonUtils.isWaterOn(context)) {
                    File binary = new File(context.getDir(BINARY_DEST_DIR_NAME, 0), BINARY_FILE_NAME);
                    File lockDir = context.getDir(INDICATOR_DIR_NAME, 0);
                    for (int i = 0; i < 50; i++) {
                        doDaemonLogic(context, binary, lockDir,
                                getRestartTime(configurations.DAEMON_ASSISTANT_CONFIG.RADICAL_MODE),
                                INDICATOR__B1,
                                INDICATOR__C1,
                                OBSERVER__B1,
                                OBSERVER__C1,
                                OBSERVER_B_CHILD);
                    }
                }
            }
        }.start();
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
                for (int i = 0; i < 50; i++) {
                    doDaemonLogic(context, binary, lockDir,
                            getRestartTime(configurations.PERSISTENT_CONFIG.RADICAL_MODE),
                            INDICATOR__A1,
                            INDICATOR__B1,
                            OBSERVER__A1,
                            OBSERVER__B1,
                            OBSERVER_A_CHILD);
                }
            }
        }.start();
    }
}
