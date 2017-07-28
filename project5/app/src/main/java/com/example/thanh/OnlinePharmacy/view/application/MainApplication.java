package com.example.thanh.OnlinePharmacy.view.application;

import android.support.multidex.MultiDexApplication;

import com.example.thanh.OnlinePharmacy.factory.ApplicationComponent;
import com.example.thanh.OnlinePharmacy.factory.ApplicationModule;
import com.example.thanh.OnlinePharmacy.factory.DaggerApplicationComponent;

import org.androidannotations.annotations.EApplication;

/**
 * Created by PC_ASUS on 7/28/2017.
 */


@EApplication
public class MainApplication extends MultiDexApplication {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        this.applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        //set up to show fonts properly
     /*   CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/SFUIText_Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());*/

        //set up Hawk to encrypt and save token local
      /*  Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSharedPrefStorage(this))
                .build();*/
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}




