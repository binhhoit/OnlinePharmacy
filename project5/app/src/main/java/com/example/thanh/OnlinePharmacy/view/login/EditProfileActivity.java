package com.example.thanh.OnlinePharmacy.view.login;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.thanh.OnlinePharmacy.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;

@EActivity(R.layout.activity_edit_profile)
public class EditProfileActivity extends AppCompatActivity {

    static final int DILOG_ID = 0;

    @ViewById(R.id.activity_edit_profile_toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.activity_edit_profile_tv_day)
    protected TextView tvBrith;
    @ViewById(R.id.activity_edit_profile_et_address_street)
    protected EditText etAddressStreet;
    @ViewById(R.id.activity_edit_profile_et_address_wards)
    protected EditText etAddressWards;
    @ViewById(R.id.activity_edit_profile_et_address_districts)
    protected EditText etAddressDistricts;
    @ViewById(R.id.activity_edit_profile_et_address_city)
    protected EditText etAddressCity;
    @ViewById(R.id.activity_edit_profile_et_mobile)
    protected EditText etMobile;


    private int yearX, monthX, dayX;

    @AfterViews
    protected void init() {
        setToolbar();

    }

    public void setToolbar() {
        toolbar.setTitle("Chỉnh sửa thông tin");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colortoolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Click(R.id.activity_edit_profile_tv_day)
    protected void showDialogCalander() {
        showDialog(DILOG_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final Calendar calendar = Calendar.getInstance();
        yearX = calendar.get(Calendar.YEAR);
        monthX = calendar.get(Calendar.MONTH);
        dayX = calendar.get(Calendar.DAY_OF_MONTH);

        if (id == DILOG_ID) {
            return new DatePickerDialog(this, dpickerListner, yearX, monthX, dayX);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker View, int year, int monthOfYear, int dayOfMonth) {
            yearX = year;
            monthX = monthOfYear;
            dayX = dayOfMonth;
            tvBrith.setText(dayX + "-" + monthX + "-" + yearX);
        }
    };

    @Click(R.id.activity_edit_profile_tv_btn_save)
    protected void saveInfoUser() {
        //put info

        etMobile.getText();
        tvBrith.getText();
        ProfileActivity_.intent(this).start();
    }

}
