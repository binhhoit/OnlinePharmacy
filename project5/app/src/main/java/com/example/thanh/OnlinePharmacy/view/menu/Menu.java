package com.example.thanh.OnlinePharmacy.view.menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.utils.Constants;
import com.example.thanh.OnlinePharmacy.view.about.AboutInformationActivity_;
import com.example.thanh.OnlinePharmacy.view.login.Profile_;
import com.example.thanh.OnlinePharmacy.view.main.MainActivity_;
import com.example.thanh.OnlinePharmacy.view.pay.PayActivity_;
import com.example.thanh.OnlinePharmacy.view.prescription.ReceiverPrescriptionActivity_;
import com.example.thanh.OnlinePharmacy.view.prescription.ReceiverPrescriptionConfirmActivity_;
import com.example.thanh.OnlinePharmacy.view.prescription.SelectMethodSendPrescriptionActivity_;
import com.example.thanh.OnlinePharmacy.view.setting.SettingActivity_;
import com.example.thanh.OnlinePharmacy.view.support.SupportActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_menu)
public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @ViewById(R.id.activity_menu_toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.fab)
    FloatingActionButton fab;
    @ViewById(R.id.drawer_layout)
    DrawerLayout drawer;
    @ViewById(R.id.nav_view)
    NavigationView navigationView;
    @ViewById(R.id.grid_view_image_text)
    protected GridView androidGridView;

    private SharedPreferences sharedPreferences;
    private String token;
    private String email;

    private int temp = 0;
    private String[] gridViewString;
    private int[] gridViewImageId = {
            R.drawable.ic_user,
            R.drawable.ic_checked,
            R.drawable.ic_list,
            R.drawable.ic_check_pharmacy,
            R.drawable.ic_browsing,
            R.drawable.ic_support,
            R.drawable.ic_setting,
            R.drawable.ic_info_about,

    };

    @AfterViews
    void init() {
        setToolbarAndNavDrawer();
        initSharedPreferences();
        gridViewString = getResources().getStringArray(R.array.menu);

        CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(
                Menu.this,
                gridViewString,
                gridViewImageId);
        androidGridView.setAdapter(adapterViewAndroid);

        androidGridView.setOnItemClickListener((parent, view, i, id) -> {
            if (i == 0) {

                Profile_.intent(Menu.this).start();
            }
            if (i == 1) {

                PayActivity_.intent(Menu.this).start();
            }
            if (i == 2) {

                SelectMethodSendPrescriptionActivity_.intent(Menu.this).start();
            }
            if (i == 3) {

                ReceiverPrescriptionActivity_.intent(Menu.this).start();
            }
            if (i == 4) {

                ReceiverPrescriptionConfirmActivity_.intent(Menu.this).start();
            }
            if (i == 5) {
                SupportActivity_.intent(Menu.this).start();
            }
            if (i == 6) {
                SettingActivity_.intent(Menu.this).start();
            }
            if (i == 7) {
                AboutInformationActivity_.intent(Menu.this).start();
            }

        });

        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

    }

    private void initSharedPreferences() {

        sharedPreferences = getApplication().getSharedPreferences("account", MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, "");
        email = sharedPreferences.getString(Constants.EMAIL, "");
    }
    private void setToolbarAndNavDrawer() {
        toolbar.setTitle("Lựa Chọn Chức Năng");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colortoolbar));

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            temp++;
            if (temp == 1) {

                Toast.makeText(Menu.this, "Nhấn back 1 lần nữa sẽ thoát chương trình", Toast.LENGTH_SHORT).show();
            }
            if (temp == 2) {
                //exit application
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startActivity(startMain);
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_sent_prescription) {

            SelectMethodSendPrescriptionActivity_.intent(Menu.this).start();
        } else if (id == R.id.nav_receiver_prescription) {
            ReceiverPrescriptionActivity_.intent(Menu.this).start();
        } else if (id == R.id.nav_seting) {
            SettingActivity_.intent(Menu.this).start();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.EMAIL, "");
            editor.putString(Constants.TOKEN, "");
            editor.apply();
            MainActivity_.intent(this).start();
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
