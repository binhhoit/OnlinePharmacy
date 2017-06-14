package com.example.thanh.OnlinePharmacy.check_crash;

import android.app.Activity;
import android.content.Context;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

/**
 * Created by ASUS_K46CM on 6/12/2017.
 */

public class Crash {
    static public void checkForCrashes(Context context) {
        CrashManager.register(context);
    }

    static public void checkForUpdates(Activity activity) {
        // Remove this for store builds!
        UpdateManager.register(activity);
    }

    static public void unregisterManagers() {
        UpdateManager.unregister();
    }
}

