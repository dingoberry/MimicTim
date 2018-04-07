package com.libwatermelon;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;

import java.lang.reflect.Field;

import wake.tim.mimic.com.mimictim.Info;

/**
 * Created by tf on 3/14/2018.
 */

abstract class CommonWaterStrategy extends BaseWaterStrategy {

    final String BINARY_DEST_DIR_NAME = "bin";
    final String BINARY_FILE_NAME = "daemon2_v2.1.2";

    Parcel mData;
    IBinder mRemote;
    int mTransactCode = 34;
    long mNativePtr;

    void initAmsBinder() {
        try {
            mRemote = (IBinder) Class.forName("android.os.ServiceManager")
                    .getMethod("getService", new Class[]{String.class})
                    .invoke(null, new Object[]{"activity"});
        } catch (Throwable e) {
            Info.y(e);
        }
    }

    void initServiceParcel(Context context, String serviceName) {
        try {
            Parcel data = Parcel.obtain();
            data.writeInterfaceToken("android.app.IActivityManager");
            data.writeStrongBinder(null);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
                data.writeInt(1);
            }
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(context.getPackageName(), serviceName));
            intent.writeToParcel(data, 0);
            data.writeString(null);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
                data.writeInt(0);
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                data.writeString(context.getPackageName());
            }
            data.writeInt(0);
            mData = data;
            Field field = Parcel.class.getDeclaredField("mNativePtr");
            field.setAccessible(true);
            mNativePtr = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                    ? ((Long) field.get(data))
                    : Long.parseLong(field.get(data).toString());
        } catch (Throwable e) {
            Info.y(e);
        }
    }

    void startProcessByAmsBinder() {
        try {
            if ((this.mRemote == null) || (this.mData == null)) {
                Info.c("REMOTE IS NULL or PARCEL IS NULL !!!");
                return;
            }
            mRemote.transact(mTransactCode, mData, null, IBinder.FLAG_ONEWAY);
        } catch (Exception localException) {
            Info.y(localException);
        }
    }
}
