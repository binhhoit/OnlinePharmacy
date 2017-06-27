package com.example.thanh.OnlinePharmacy.view.menu;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.GridView;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.view.about.AboutInformationActivity_;
import com.example.thanh.OnlinePharmacy.view.login.Profile_;
import com.example.thanh.OnlinePharmacy.view.pay.PayActivity_;
import com.example.thanh.OnlinePharmacy.view.prescription.ReceiverPrescriptionActivity_;
import com.example.thanh.OnlinePharmacy.view.prescription.ReceiverPrescriptionConfirmActivity_;
import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.view.prescription.SelectMethodSendPrescriptionActivity_;
import com.example.thanh.OnlinePharmacy.view.setting.SettingActivity_;
import com.example.thanh.OnlinePharmacy.view.support.SupportActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_menu)
public class Menu extends AppCompatActivity {
    @ViewById(R.id.activity_menu_toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.grid_view_image_text)
    protected GridView androidGridView;

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
        setToolbar();

        gridViewString =  getResources().getStringArray(R.array.menu);

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

    }

    private void setToolbar(){
        toolbar.setTitle("Lựa Chọn Chức Năng");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colortoolbar));
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            temp++;
            if (temp == 1) {

                Toast.makeText(Menu.this, "Nhấn back 1 lần nữa sẽ thoát chương trình", Toast.LENGTH_SHORT).show();

            }
            if (temp == 2) {
                //thoát khỏi chương trình
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startActivity(startMain);
                finish();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
