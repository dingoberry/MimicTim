package com.libwatermelon;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Build;

import java.util.List;

import wake.tim.mimic.com.mimictim.Info;

/**
 * Created by tf on 3/14/2018.
 */

abstract class BaseWaterStrategy implements IWaterStrategy {

    static final String CHIP_MODE_ARMEABI = "armeabi";
    static final String CHIP_MODE_X86 = "x86";

    private static final String[] SOME_BRANDS = {"oppo", "honor"};

    private static boolean isSpecialModel() {
        for (String brand : SOME_BRANDS) {
            if (Build.BRAND != null && (Build.BRAND.toLowerCase().startsWith(brand.toLowerCase()))) {
                return true;
            }
        }
        return false;
    }

    int getRestartTime(boolean radicalMode) {
        return !radicalMode || !isSpecialModel() ? 0 : 1;
    }

    int getTransactCode() {
        return Build.VERSION.SDK_INT >= 26 ? 26 : 34;
    }

    boolean isAppForeground(Context cxt) {
        try {
            ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
            if (null != am) {
                List<RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
                String pkgName = cxt.getPackageName();
                if (null != infoList) {
                    for (RunningAppProcessInfo info : infoList) {
                        if (info.processName.equals(pkgName)
                                && RunningAppProcessInfo.IMPORTANCE_FOREGROUND == info.importance) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Info.y(e);
        }
        return false;
    }

    @Override
    public void onDaemonAssistant2Create(Context context, WaterConfigurations configurations) {
    }
}
