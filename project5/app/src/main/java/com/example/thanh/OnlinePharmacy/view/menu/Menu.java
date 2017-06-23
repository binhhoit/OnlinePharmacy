package com.example.thanh.OnlinePharmacy.view.menu;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.view.login.Profile_;
import com.example.thanh.OnlinePharmacy.view.pay.PayCardActivity_;
import com.example.thanh.OnlinePharmacy.view.prescription.ReceiverPresciptionActivity_;
import com.example.thanh.OnlinePharmacy.view.prescription.ReceiverPrescriptionConfirmActivity_;
import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.view.prescription.SelectMethodSendPrescriptionActivity_;

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
            R.drawable.ic_checkpharmacy,
            R.drawable.ic_browsing,
            R.drawable.ic_info_about,
    };

    @AfterViews
    void init() {
        toolbar.setTitle("Lựa Chọn Chức Năng");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colortoolbar));
        gridViewString =  getResources().getStringArray(R.array.menu);

        CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(
                Menu.this,
                gridViewString,
                gridViewImageId);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                if (i == 0) {
                    Intent intent = new Intent(Menu.this, Profile_.class);
                    startActivity(intent);
                }
                if (i == 1) {
                    Intent passIntentPayCard = new Intent(Menu.this, PayCardActivity_.class);
                    startActivity(passIntentPayCard);
                }
                if (i == 2) {
                    Intent intent = new Intent(Menu.this, SelectMethodSendPrescriptionActivity_.class);
                    startActivity(intent);
                }
                if (i == 3) {
                    Intent passPrescription = new Intent(Menu.this, ReceiverPresciptionActivity_.class);
                    startActivity(passPrescription);
                }
                if (i == 4) {
                    Intent passPrescriptionConfirm = new Intent(Menu.this, ReceiverPrescriptionConfirmActivity_.class);
                    startActivity(passPrescriptionConfirm);
                }
                if (i == 5) {
                    Toast.makeText(getApplicationContext(), "about ", Toast.LENGTH_SHORT).show();
                }

            }
        });

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
