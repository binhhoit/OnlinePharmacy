package com.example.thanh.OnlinePharmacy.view.prescription;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.view.prescription.fragments.PagerFragmentAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_select_method_send_prescription)
public class SelectMethodSendPrescriptionActivity extends AppCompatActivity {

    @ViewById(R.id.activity_select_method_toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.activity_select_method_tab_layout)
    protected TabLayout tabLayout;
    @ViewById(R.id.activity_select_method_view_pager)
    protected ViewPager pager;

    @AfterViews
    protected void init() {

        setToolbar();

        setAdapterFragment();
    }

    private void setToolbar() {
        toolbar.setTitle("Nhập đơn thuốc");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colortoolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setAdapterFragment(){
        FragmentManager manager = getSupportFragmentManager();
        PagerFragmentAdapter adapter = new PagerFragmentAdapter(manager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);
    }
}
