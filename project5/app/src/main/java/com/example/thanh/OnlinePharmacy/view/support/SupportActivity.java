package com.example.thanh.OnlinePharmacy.view.support;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.thanh.OnlinePharmacy.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_support)
public class SupportActivity extends AppCompatActivity {
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
    }
}
