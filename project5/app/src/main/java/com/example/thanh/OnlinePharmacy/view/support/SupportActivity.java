package com.example.thanh.OnlinePharmacy.view.support;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.view.application.MainApplication;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

@EActivity(R.layout.activity_support)
public class SupportActivity extends MvpActivity<SupportView, SupportPresenter> implements SupportView {
    @Inject
    protected SupportPresenter presenter;

    @App
    MainApplication application;

    @AfterInject
    void afterInject() {
        DaggerSupportComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    public SupportPresenter createPresenter() {
        return presenter;
    }

    @ViewById(R.id.activity_support_toolbar)
    protected Toolbar toolbar;

    @AfterViews
    protected void init() {
        setToolbar();
    }

    private void setToolbar() {
        toolbar.setTitle("Hỗ trợ người dùng");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colortoolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }


}
