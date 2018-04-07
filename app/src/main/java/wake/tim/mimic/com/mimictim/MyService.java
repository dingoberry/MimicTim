package wake.tim.mimic.com.mimictim;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

public class MyService extends Service {

    private long mCallTime;
    private Intent mIntent;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        mIntent = new Intent(this, MainService.class);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long cur = SystemClock.elapsedRealtime();
        if (cur - mCallTime > 200) {
            Info.y("onStartCommandX");
            mCallTime = cur;
        }
//        startService(mIntent);
        return START_NOT_STICKY;
    }
}
