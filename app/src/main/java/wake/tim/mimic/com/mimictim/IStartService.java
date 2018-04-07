package wake.tim.mimic.com.mimictim;

import android.content.Context;

/**
 * Created by tf on 3/5/2018.
 */

abstract class IStartService {

    Context mCxt;

    IStartService(Context context) {
        mCxt = context;
    }

    abstract boolean start();
}
