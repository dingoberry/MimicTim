package wake.tim.mimic.com.mimictim;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by tf on 3/5/2018.
 */
class NormalStartService extends IStartService {

    private Intent mIntent;

    NormalStartService(Context context) {
        super(context);
        mIntent = new Intent(mCxt, MyService.class);
    }

    public boolean start() {
        ComponentName cn;
        try {
            cn = mCxt.startService(mIntent);
        } catch (Throwable e) {
            cn = null;
            Info.y(e);
        }
        return null != cn;
    }
}
