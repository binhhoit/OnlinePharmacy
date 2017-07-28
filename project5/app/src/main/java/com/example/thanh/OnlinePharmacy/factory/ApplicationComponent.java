package com.example.thanh.OnlinePharmacy.factory;


import android.app.Activity;

import com.example.thanh.OnlinePharmacy.scope.ApplicationScope;
import com.example.thanh.OnlinePharmacy.view.application.MainApplication;

import dagger.Component;

/**
 * -> Created by phong.nguyen@beesightsoft.com on 7/10/2017.
 */

@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    /* AuthenticationService authenticationService();*/
    //Activity activity();

    //void inject(MainApplication mainApplication);
}
