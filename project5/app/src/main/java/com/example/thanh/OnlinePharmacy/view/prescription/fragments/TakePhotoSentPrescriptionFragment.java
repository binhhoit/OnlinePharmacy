package com.example.thanh.OnlinePharmacy.view.prescription.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.model.Crash;
import com.example.thanh.OnlinePharmacy.model.PhotoPrescription;
import com.example.thanh.OnlinePharmacy.model.ResponseStatus;
import com.example.thanh.OnlinePharmacy.service.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.utils.Constants;
import com.example.thanh.OnlinePharmacy.view.pay.PayActivity_;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by PC_ASUS on 6/23/2017.
 */
@EFragment(R.layout.fragment_take_photo_sent_presciption)
public class TakePhotoSentPrescriptionFragment extends Fragment {
    @ViewById(R.id.activity_photo_iv_camera)
    protected ImageView ivCamera;
    @ViewById(R.id.activity_photo_iv_picture)
    protected ImageView ivPicture;
    @ViewById(R.id.activity_photo_iv_upload)
    protected ImageView ivUpload;
    @ViewById(R.id.activity_photo_iv_image)
    protected ImageView ivImage;

    private final String TAG = this.getClass().getName();

    private CameraPhoto cameraPhoto;
    private GalleryPhoto galleryPhoto;
    private int CAMERA_REQUEST = 13323;
    private int GALLERY_REQUEST = 22131;

    private String selectPhoto;
    private SharedPreferences sharedPreferences;
    private String id;
    private String token;
    private String email;

    @AfterViews
    void init() {
        initSharedPreferences();

        cameraPhoto = new CameraPhoto(getContext());
        galleryPhoto = new GalleryPhoto(getContext());

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                    //addView photo to libery
                    // cameraPhoto.addToGallery();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(
                            getContext(),
                            "Khong goi duoc camera",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);

            }
        });

        ivUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bitmap bitmap = ImageLoader.init().from(selectPhoto).requestSize(512, 512).getBitmap();
                    String encodedImage = ImageBase64.encode(bitmap);
                    Log.d(TAG, encodedImage);

                    submitServer(encodedImage);

                } catch (FileNotFoundException e) {
                    Toast.makeText(
                            getActivity(),
                            "Khong the endcoding picture",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        Crash.checkForUpdates(getActivity());
    }

    private void initSharedPreferences() {

        sharedPreferences =   getActivity()
                .getSharedPreferences("account", MODE_PRIVATE);

        token = sharedPreferences.getString(Constants.TOKEN, "");
        id = sharedPreferences.getString(Constants.ID, "");
        email = sharedPreferences.getString(Constants.EMAIL, "");
    }

    private void submitServer(String encodedImage){
        PhotoPrescription photoPrescription = new PhotoPrescription();
        photoPrescription.setId(id);
        photoPrescription.setStatus("false");
        photoPrescription.setEmail(email);
        photoPrescription.setAddressReceive("làm chổ điền thêm zô sau");
        photoPrescription.setPhoto(encodedImage);
        photoPrescription.setNumberBuy(time());

        postPhoto(photoPrescription);
    }

    private void postPhoto(PhotoPrescription photoPrescription){
        Call<ResponseStatus> call = NetworkUtil
                .getRetrofit(token)
                .postPhotoPrescription(photoPrescription,id);
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                Toast.makeText(
                        getActivity(),
                        "Thành Công: " +
                                response.body().getStatus() +
                                "  " +
                                response.body().getMessage(),
                        Toast.LENGTH_SHORT).show();

                PayActivity_.intent(getActivity()).start();
                getActivity().finish();
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                Toast.makeText(
                        getActivity(),
                        "Thất Bại " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("ERROR", "" + t.getMessage());
            }
        });
    }

    private String time() {
        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
        return timeFormat.format(today.getTime());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                String photoPath = cameraPhoto.getPhotoPath();
                selectPhoto = photoPath;
                Log.d(TAG, photoPath);

                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    ivImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(
                            getActivity(),
                            getString(R.string.take_again),
                            Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == GALLERY_REQUEST) {
                Uri uri = data.getData();

                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                selectPhoto = photoPath;
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    ivImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getActivity(),
                            "Something Wrong while choosing photos", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // ... your own onResume implementation
        Crash.checkForCrashes(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        Crash.unregisterManagers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Crash.unregisterManagers();
    }

}
