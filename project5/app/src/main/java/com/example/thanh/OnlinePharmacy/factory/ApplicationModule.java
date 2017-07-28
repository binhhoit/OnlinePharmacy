package com.example.thanh.OnlinePharmacy.factory;

import com.example.thanh.OnlinePharmacy.scope.ApplicationScope;
import com.example.thanh.OnlinePharmacy.view.application.MainApplication;

import dagger.Module;
import dagger.Provides;

/**
 * ->
 */

@Module
public class ApplicationModule {

    private MainApplication mainApplication;

    public ApplicationModule(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
    }

  /*  @Provides
    @ApplicationScope
    MainApplication provideApplication() {
        return mainApplication;
    }*/
}
