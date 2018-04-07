package com.libwatermelon;

/**
 * Created by tf on 3/8/2018.
 */

public class WaterConfigurations {

    final DaemonConfiguration DAEMON_ASSISTANT_CONFIG;
    final DaemonConfiguration DAEMON_ASSISTANT_CONFIG2;
    boolean ENABLE_3P = true;
    final DaemonConfiguration PERSISTENT_CONFIG;

    WaterConfigurations(DaemonConfiguration paramDaemonConfiguration1, DaemonConfiguration paramDaemonConfiguration2, DaemonConfiguration paramDaemonConfiguration3) {
        this.PERSISTENT_CONFIG = paramDaemonConfiguration1;
        this.DAEMON_ASSISTANT_CONFIG = paramDaemonConfiguration2;
        this.DAEMON_ASSISTANT_CONFIG2 = paramDaemonConfiguration3;
    }

    static class DaemonConfiguration {
        final String PROCESS_NAME;
        boolean RADICAL_MODE;
        final String SERVICE_NAME;

        DaemonConfiguration(String paramString1, String paramString2) {
            this.PROCESS_NAME = paramString1;
            this.SERVICE_NAME = paramString2;
        }
    }
}
