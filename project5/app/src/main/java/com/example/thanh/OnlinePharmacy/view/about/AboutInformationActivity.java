package com.example.thanh.OnlinePharmacy.view.about;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.thanh.OnlinePharmacy.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_about_information)
public class AboutInformationActivity extends AppCompatActivity {

    @ViewById(R.id.activity_about_toolbar)
    protected Toolbar toolbar;

    @AfterViews
    protected void init() {
        setToolbar();
    }

    private void setToolbar() {
        toolbar.setTitle("Thông tin thêm");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colortoolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
