package wake.tim.mimic.com.mimictim;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dianxinos.optimizer.alive.VerrDeFichier;
import com.libwatermelon.DaemonTimService;

public class MainActivity extends Activity {

    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new VerrDeFichier();

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText("MODEL:" + Build.MODEL + "\nMANU:" + Build.MANUFACTURER);
//        tv.setText(stringFromJNI());
        Info.c("onCreate");
//        AnalysisBuild.study();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Info.c("onDestroy");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();

    IStartService ms;

    public void launchService(View view) {
        Intent intent = new Intent(this, DaemonTimService.class);
        intent.setAction(DaemonTimService.ACTION_START_DAEMON);
        startService(intent);

//        startService(new Intent(this, MainService.class));

//        if (null == ms) {
//            ms = new W3pStartService(this.getApplicationContext());
//        }
//        ms.start();

    }


}
