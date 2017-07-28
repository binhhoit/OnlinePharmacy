package com.example.thanh.OnlinePharmacy.view.support;

import com.example.thanh.OnlinePharmacy.factory.ApplicationComponent;
import com.example.thanh.OnlinePharmacy.scope.ActivityScope;

import dagger.Component;

/**
 * Created by PC_ASUS on 7/28/2017.
 */

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface SupportComponent {
    void inject(SupportActivity ProfileActivity);
}
