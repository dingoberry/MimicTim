package com.libwatermelon;

import android.content.Context;

import wake.tim.mimic.com.mimictim.Info;

/**
 * Created by tf on 3/8/2018.
 */
public class WaterDaemon {

    private static boolean mEnable = false;
    private Context mContext;
    private DaemonDeadListener mDaemonDeadListener;

    static {
        try {
            System.loadLibrary("daemon_acc_v2.1.2");
            mEnable = true;
            Info.y("Load success!");
        } catch (UnsatisfiedLinkError error) {
            mEnable = false;
            Info.y(error);
        }
    }


    public WaterDaemon(Context paramContext, DaemonDeadListener paramDaemonDeadListener) {
        this.mContext = paramContext;
        this.mDaemonDeadListener = paramDaemonDeadListener;
    }

    private native void doDaemon1(String paramString1, String paramString2, String paramString3);

    private native void doDaemon2(int paramInt1, long paramLong, int paramInt2, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9);

    private native void doDaemon3(int paramInt1, long paramLong, int paramInt2, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6);

    public native void doDaemon(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, String paramString11);

    public void doDaemon1_(String paramString1, String paramString2, String paramString3) {
        if (mEnable) {
            doDaemon1(paramString1, paramString2, paramString3);
        }
    }

    public void doDaemon2_(int paramInt1, long paramLong, int paramInt2, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9) {
        if (mEnable) {
            doDaemon2(paramInt1, paramLong, paramInt2, paramString1, paramString2, paramString3, paramString4, paramString5, paramString6, paramString7, paramString8, paramString9);
        }
    }

    public void doDaemon3_(int paramInt1, long paramLong, int paramInt2, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) {
        if (mEnable) {
            Info.y("doDaemon3_native");
            doDaemon3(paramInt1, paramLong, paramInt2, paramString1, paramString2, paramString3, paramString4, paramString5, paramString6);
        }
    }

    public void onDaemonDead() {
        this.mDaemonDeadListener.onDaemonDead();
    }
}
