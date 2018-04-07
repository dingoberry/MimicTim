package wake.tim.mimic.com.mimictim;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public class TestService extends Service {
    public TestService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new BinderImpl();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Info.y("TestService:Start");
        return super.onStartCommand(intent, flags, startId);
    }

    private static class BinderImpl extends Binder implements IInterface {
        BinderImpl() {
            attachInterface(this, "abc");
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case INTERFACE_TRANSACTION:
                    reply.writeString("abc");
                    return true;
                case IBinder.FIRST_CALL_TRANSACTION:
                    data.enforceInterface("abc");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Info.y(e);
                    }
                    Info.y("Call pid=" + Binder.getCallingPid());
                    reply.writeNoException();
                    return true;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}
