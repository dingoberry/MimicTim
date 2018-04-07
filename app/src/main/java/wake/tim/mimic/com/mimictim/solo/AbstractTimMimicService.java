package wake.tim.mimic.com.mimictim.solo;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;

import wake.tim.mimic.com.mimictim.Info;
import wake.tim.mimic.com.mimictim.MyService;

/**
 * Created by tf on 3/8/2018.
 */

class AbstractTimMimicService extends Service {

    private Parcel mParcelData;
    private IBinder mRemote;
    private long mNativePtr;

    private void initAmsBinder() {
        try {
            mRemote = (IBinder) Class.forName("android.os.ServiceManager")
                    .getMethod("getService", new Class[]{String.class})
                    .invoke(null, new Object[]{"activity"});
        } catch (Exception e) {
            Info.y(e);
        }
    }

    private void initServiceParcel() {
        try {
            Parcel data = Parcel.obtain();
            data.writeInterfaceToken("android.app.IActivityManager");
            data.writeStrongBinder(null);
            if (Build.VERSION.SDK_INT >= 26) {
                data.writeInt(1);
            }
            Intent intent = new Intent(this, MyService.class);
            intent.writeToParcel(data, 0);
            data.writeString(null);
            if (Build.VERSION.SDK_INT >= 26) {
                data.writeInt(0);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                data.writeString(getPackageName());
            }
            data.writeInt(0);
            mParcelData = data;
        } catch (Throwable e) {
            Info.y(e);
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        initAmsBinder();
        initServiceParcel();
    }

    private int getTransactCode() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1 ? 26 : 34;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
