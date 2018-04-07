package wake.tim.mimic.com.mimictim;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by tf on 3/5/2018.
 */

public class MainService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Info.y("onStartCommandM");
        Task.getInstance(this).start();
        return super.onStartCommand(intent, flags, startId);
    }

    private static class Task extends Thread {

        private static Task sInstance;

        static Task getInstance(Context cxt) {
            if (null == sInstance) {
                synchronized (Task.class) {
                    if (null == sInstance) {
                        sInstance = new Task(cxt.getApplicationContext());
                    }
                }
            }
            return sInstance;
        }

        private AtomicBoolean mRunning;
        private IStartService mStartService;
        private static final boolean OPTIMIZE = false;

        Task(Context cxt) {
            mRunning = new AtomicBoolean();
            mStartService = OPTIMIZE ? new BinderStartService(cxt) : new NormalStartService(cxt);
        }

        @Override
        public synchronized void start() {
            if (mRunning.compareAndSet(false, true)) {
                super.start();
            }
        }

        @Override
        public void run() {
            int errorCount = 0;
            int successCount = 0;
            while (errorCount < 3000) {
                if (mStartService.start()) {
                    successCount++;
                } else {
                    errorCount++;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Info.y(e);
                }
            }
            if (errorCount > 0) {
                Info.y("Detect error happends :" + errorCount + " white success=" + successCount + ", and quite!");
            }
            mRunning.set(false);
        }
    }
}
