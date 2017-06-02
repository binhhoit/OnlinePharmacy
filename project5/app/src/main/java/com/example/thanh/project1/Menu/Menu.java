package com.example.thanh.project1.Menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.thanh.project1.Login.profile;
import com.example.thanh.project1.MainActivity;
import com.example.thanh.project1.Pay.fragment.Pay_card;
import com.example.thanh.project1.Prescription.Select_method_send_prescription;
import com.example.thanh.project1.Prescription.fragments.QRcode_Prescription;
import com.example.thanh.project1.Prescription.fragments.ReceiverPrescriptionConfirm;
import com.example.thanh.project1.Prescription.fragments.receiverPresciption;
import com.example.thanh.project1.Prescription.fragments.sendPresciption;
import com.example.thanh.project1.R;

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
                    Intent intent = new Intent(Menu.this, profile.class);
                    startActivity(intent);
                }
                if (i == 1) {
                    Intent passIntentPayCard = new Intent(Menu.this, Pay_card.class);
                    startActivity(passIntentPayCard);
                }
                if (i == 2) {
                    Intent intent = new Intent(Menu.this, Select_method_send_prescription.class);
                    startActivity(intent);
                }
                if (i == 3) {
                    Intent passPrescription = new Intent(Menu.this, receiverPresciption.class);
                    startActivity(passPrescription);
                }
                if (i == 4) {
                    Intent passPrescriptionConfirm = new Intent(Menu.this, ReceiverPrescriptionConfirm.class);
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
