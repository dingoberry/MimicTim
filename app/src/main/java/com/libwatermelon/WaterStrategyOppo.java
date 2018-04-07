package com.libwatermelon;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import wake.tim.mimic.com.mimictim.Info;

/**
 * Created by tf on 3/14/2018.
 */

class WaterStrategyOppo extends WaterStrategy3p implements DaemonDeadListener {

    private void startService(Context cxt, String serviceName) {
        Intent i = new Intent();
        i.setComponent(new ComponentName(cxt, serviceName));
        cxt.startService(i);
    }

    private void startAllServiceProcess(final Context cxt, final WaterConfigurations configurations) {
        if (isAppForeground(cxt)) {
            return;
        }

        Thread t = new Thread() {
            @Override
            public void run() {
                startService(cxt, configurations.PERSISTENT_CONFIG.SERVICE_NAME);
                startService(cxt, configurations.DAEMON_ASSISTANT_CONFIG.SERVICE_NAME);
                startService(cxt, configurations.DAEMON_ASSISTANT_CONFIG2.SERVICE_NAME);
            }
        };
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    private void startServiceProcessDelay(final Context cxt, final String serviceName) {
        if (isAppForeground(cxt)) {
            return;
        }

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Info.y(e);
                }
                startService(cxt, serviceName);
            }
        };
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    @Override
    public void onDaemonAssistant2Create(Context context, WaterConfigurations configurations) {
        super.onDaemonAssistant2Create(context, configurations);
        startAllServiceProcess(context, configurations);
        startServiceProcessDelay(context, configurations.PERSISTENT_CONFIG.SERVICE_NAME);
    }

    @Override
    public void onDaemonAssistantCreate(Context context, WaterConfigurations configurations) {
        super.onDaemonAssistantCreate(context, configurations);
        startAllServiceProcess(context, configurations);
        startServiceProcessDelay(context, configurations.DAEMON_ASSISTANT_CONFIG2.SERVICE_NAME);
    }

    @Override
    public void onPersistentCreate(Context context, WaterConfigurations configurations) {
        super.onPersistentCreate(context, configurations);
        startAllServiceProcess(context, configurations);
        startServiceProcessDelay(context, configurations.DAEMON_ASSISTANT_CONFIG.SERVICE_NAME);
    }
}
