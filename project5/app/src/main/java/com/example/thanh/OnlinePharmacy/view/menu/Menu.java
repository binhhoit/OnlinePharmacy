package com.example.thanh.OnlinePharmacy.view.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.view.login.Profile;
import com.example.thanh.OnlinePharmacy.view.pay.PayCardActivity;
import com.example.thanh.OnlinePharmacy.view.prescription.SelectMethodSendPrescriptionActivity;
import com.example.thanh.OnlinePharmacy.view.prescription.ReceiverPrescriptionConfirmActivity;
import com.example.thanh.OnlinePharmacy.view.prescription.ReceiverPresciptionActivity;
import com.example.thanh.OnlinePharmacy.R;

public class Menu extends AppCompatActivity {
    private int temp = 0;
    GridView androidGridView;

    String[] gridViewString = {
            "Thiết lập người dùng", "Kiểm tra tài khoản", "Đặt đơn thuốc", "Kiểm tra các đơn thuốc", "Đơn thuốc đã duyệt", "ABC",
    };
    int[] gridViewImageId = {
            R.drawable.ic_user, R.drawable.ic_checked, R.drawable.ic_list, R.drawable.ic_checkpharmacy, R.drawable.ic_browsing, R.drawable.ic_user,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(Menu.this, gridViewString, gridViewImageId);
        androidGridView = (GridView) findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                if (i == 0) {
                    Intent intent = new Intent(Menu.this, Profile.class);
                    startActivity(intent);
                }
                if (i == 1) {
                    Intent passIntentPayCard = new Intent(Menu.this, PayCardActivity.class);
                    startActivity(passIntentPayCard);
                }
                if (i == 2) {
                    Intent intent = new Intent(Menu.this, SelectMethodSendPrescriptionActivity.class);
                    startActivity(intent);
                }
                if (i == 3) {
                    Intent passPrescription = new Intent(Menu.this, ReceiverPresciptionActivity.class);
                    startActivity(passPrescription);
                }
                if (i == 4) {
                    Intent passPrescriptionConfirm = new Intent(Menu.this, ReceiverPrescriptionConfirmActivity.class);
                    startActivity(passPrescriptionConfirm);
                }
                if (i == 5) {
                    Toast.makeText(getApplicationContext(),"NULL --  tạm thời chưa thêm chức năng mới ",Toast.LENGTH_SHORT).show();
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
