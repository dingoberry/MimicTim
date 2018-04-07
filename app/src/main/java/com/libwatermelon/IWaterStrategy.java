package com.libwatermelon;

import android.content.Context;
import android.os.Build;

import wake.tim.mimic.com.mimictim.Info;

/**
 * Created by tf on 3/9/2018.
 */

interface IWaterStrategy {

    void onDaemonAssistant2Create(Context context, WaterConfigurations configurations);

    void onDaemonAssistantCreate(Context context, WaterConfigurations configurations);

    void onDaemonDead();

    boolean onInitialization(Context context, WaterConfigurations configurations);

    void onPersistentCreate(Context context, WaterConfigurations configurations);

    class Fetcher {
        private static IWaterStrategy sStrategy;

        static IWaterStrategy fetchStrategy(WaterConfigurations configurations) {
            if (null != sStrategy) {
                return sStrategy;
            }

            IWaterStrategy strategy;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (configurations.ENABLE_3P) {
                    if (null != Build.MANUFACTURER && Build.MANUFACTURER.toLowerCase().startsWith("xiaomi")) {
                        strategy = new WaterStrategy3p();
                    } else if (null == Build.MODEL || !Build.MODEL.toLowerCase().startsWith("oppo")) {
                        strategy = new WaterStrategy2p();
                    } else {
                        strategy = new WaterStrategyOppo();
                    }
                } else {
                    strategy = new WaterStrategy2p();
                }
            } else {
                if (null != Build.MODEL && Build.MODEL.toLowerCase().startsWith("mi")) {
                    strategy = new WaterStrategyMi();
                } else if (null == Build.MODEL || !Build.MODEL.toLowerCase().startsWith("a31")) {
                    strategy = new WaterStrategy1();
                } else {
                    strategy = new WaterStrategy2p();
                }
            }

            Info.y("Get strategy:" + strategy.toString());
            sStrategy = strategy;
            return strategy;
        }
    }
}
