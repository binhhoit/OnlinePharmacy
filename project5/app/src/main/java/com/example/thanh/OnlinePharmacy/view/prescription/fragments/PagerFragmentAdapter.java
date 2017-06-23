package com.example.thanh.OnlinePharmacy.view.prescription.fragments;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by PC_ASUS on 6/23/2017.
 */

public class PagerFragmentAdapter extends FragmentStatePagerAdapter {
    public PagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                frag = new SendPrescriptionFragment_();
                break;
            case 1:
                frag = new QRcodePrescriptionFragment_();
                break;
            case 2:
                frag = new TakePhotoSentPrescriptionFragment_();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Nhập Tay";
                break;
            case 1:
                title = "Quét Mã QR";
                break;
            case 2:
                title = "Chụp Hình";
                break;
        }

        return title;
    }
}
