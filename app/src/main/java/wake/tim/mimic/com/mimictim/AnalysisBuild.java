package wake.tim.mimic.com.mimictim;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by tf on 3/6/2018.
 */
class AnalysisBuild implements Runnable {

    static void study() {
        new Thread(new AnalysisBuild()).start();
    }

    @Override
    public void run() {
        Context context = MyApp.getAppContext();
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            Object obj = am.getClass().getDeclaredMethod("getService").invoke(am);
            Info.y("OBJ=" + obj);
        } catch (Throwable e) {
            Info.y(e);
        }

        try {
            Class<?> anClz = Class.forName("android.app.ActivityManagerNative");
            Object obj = anClz.getDeclaredMethod("getDefault").invoke(anClz);
            Info.y("OBJ=" + obj);
        } catch (Throwable e) {
            Info.y(e);
        }

        try {
            Class<?> clz = Class.forName("android.os.ServiceManager");
            HashMap<String, IBinder> cache;
            Field field = clz.getDeclaredField("sCache");
            field.setAccessible(true);
            cache = (HashMap<String, IBinder>) field.get(clz);
            Info.y(cache.toString());

            Method m = clz.getDeclaredMethod("getIServiceManager");
            m.setAccessible(true);
            Object proxy = m.invoke(clz);
            m = proxy.getClass().getDeclaredMethod("getService", String.class);
            m.setAccessible(true);
            Info.y(m.invoke(proxy, "activity").toString());

            Object o = Class.forName("android.os.ServiceManager").getMethod("getService", new Class[]{String.class}).invoke(null, new Object[]{"activity"});
            Info.y(o.toString());
        } catch (Throwable e) {
            Info.y(e);
        }

        try {
            IBinder remote = (IBinder) Class.forName("android.os.ServiceManager")
                    .getMethod("getService", new Class[]{String.class}).invoke(null, new Object[]{"activity"});
            Parcel data = Parcel.obtain();
            data.writeInterfaceToken("android.app.IActivityManager");
            data.writeStrongBinder(null);
            if (Build.VERSION.SDK_INT >= 26) {
                data.writeInt(1);
            }
            Intent intent = new Intent(context, TestService.class);
            intent.writeToParcel(data, 0);
            data.writeString(null);
            if (Build.VERSION.SDK_INT >= 26) {
                data.writeInt(0);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                data.writeString(context.getPackageName());
            }
            data.writeInt(0);
            int transactCode = Build.VERSION.SDK_INT >= 26 ? 26 : 34;
            remote.transact(transactCode, data, null, IBinder.FLAG_ONEWAY);
        } catch (Throwable e) {
            Info.y(e);
        }

//        context.bindService(new Intent(context, TestService.class), new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                Parcel reply = Parcel.obtain();
//                Parcel data = Parcel.obtain();
//                try {
//                    data.writeInterfaceToken("abc");
//                    service.transact(IBinder.FIRST_CALL_TRANSACTION, data, reply, 0);
//                    reply.readException();
//                    Info.y("onServiceConnected");
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                } finally {
//                    reply.recycle();
//                    data.recycle();
//                }
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//            }
//        }, Context.BIND_AUTO_CREATE);
    }
}
