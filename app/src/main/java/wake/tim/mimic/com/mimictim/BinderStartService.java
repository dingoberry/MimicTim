package wake.tim.mimic.com.mimictim;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;

/**
 * Created by tf on 3/5/2018.
 */
class BinderStartService extends IStartService {

    private Parcel mData;
    private IBinder mRemote;

    BinderStartService(Context context) {
        super(context);

        try {
            mRemote = (IBinder) Class.forName("android.os.ServiceManager")
                    .getMethod("getService", new Class[]{String.class})
                    .invoke(null, new Object[]{"activity"});

            Parcel data = Parcel.obtain();
            data.writeInterfaceToken("android.app.IActivityManager");
            data.writeStrongBinder(null);
            if (Build.VERSION.SDK_INT >= 26) {
                data.writeInt(1);
            }
            Intent intent = new Intent(context, MyService.class);
            intent.writeToParcel(data, 0);
            data.writeString(null);
            if (Build.VERSION.SDK_INT >= 26) {
                data.writeInt(0);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                data.writeString(context.getPackageName());
            }
            data.writeInt(0);
            mData = data;
        } catch (Throwable e) {
            Info.y(e);
        }
    }

    private int getTransactCode() {
        return Build.VERSION.SDK_INT >= 26 ? 26 : 34;
    }

    @Override
    public boolean start() {
        try {
            if ((this.mRemote == null) || (this.mData == null)) {
                Info.c("REMOTE IS NULL or PARCEL IS NULL !!!");
                return false;
            }
            mRemote.transact(getTransactCode(), mData, null, IBinder.FLAG_ONEWAY);
            return true;
        } catch (Exception localException) {
            Info.y(localException);
        }
        return false;
    }
}
