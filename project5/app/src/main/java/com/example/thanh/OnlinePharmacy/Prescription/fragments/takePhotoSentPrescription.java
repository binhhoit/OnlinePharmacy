package com.example.thanh.OnlinePharmacy.Prescription.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.thanh.OnlinePharmacy.CheckCrask.Crask;
import com.example.thanh.OnlinePharmacy.Login.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.Login.utils.Constants;
import com.example.thanh.OnlinePharmacy.Pay.fragment.Pay;
import com.example.thanh.OnlinePharmacy.Prescription.model.photoPrescription;
import com.example.thanh.OnlinePharmacy.R;
import com.example.thanh.OnlinePharmacy.ResponseStatus;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class takePhotoSentPrescription extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    ImageView ivCamera, ivPicture, ivUpload, ivImage;

    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;

    String selectPhoto;
    private SharedPreferences mSharedPreferences;
    private String mId;
    private String mEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take__photo_sent_prescription);
        initSharedPreferences();
        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());

        ivCamera = (ImageView) findViewById(R.id.iv_camera);
        ivPicture = (ImageView) findViewById(R.id.iv_picture);
        ivUpload = (ImageView) findViewById(R.id.iv_upload);
        ivImage = (ImageView) findViewById(R.id.iv_image);

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                    //add photo to libery
                    // cameraPhoto.addToGallery();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Khong goi duoc camera", Toast.LENGTH_SHORT).show();
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
                    photoPrescription photoPrescription = new photoPrescription();
                    photoPrescription.setId(mId);
                    photoPrescription.setStatus("false");
                    photoPrescription.setEmail(mEmail);
                    photoPrescription.setAddressReceive("làm chổ điền thêm zô sau");
                    photoPrescription.setPhoto(encodedImage);
                    photoPrescription.setNumber_buy(time());
                    Call<ResponseStatus> call = NetworkUtil.getRetrofit().postPhotoPrescription(photoPrescription);
                    call.enqueue(new Callback<ResponseStatus>() {
                        @Override
                        public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                            Toast.makeText(takePhotoSentPrescription.this, "Thành Công: " + response.body().getStatus() +
                                    "  " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(takePhotoSentPrescription.this, Pay.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseStatus> call, Throwable t) {
                            Toast.makeText(takePhotoSentPrescription.this, "Thất Bại " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("ERROR", "" + t.getMessage());
                        }
                    });

                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Khong the endcoding picture", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Crask.checkForUpdates(this);
    }

    private void initSharedPreferences() {

        mSharedPreferences = getApplication().getSharedPreferences("account", MODE_PRIVATE);
        mId = mSharedPreferences.getString(Constants.ID, "");
        mEmail = mSharedPreferences.getString(Constants.EMAIL, "");
    }

    private String time() {
        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
        return timeFormat.format(today.getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    Toast.makeText(getApplicationContext(), "Load khong duoc picture sau khi chup ", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while choosing photos", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // ... your own onResume implementation
        Crask.checkForCrashes(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Crask.unregisterManagers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Crask.unregisterManagers();
    }

}
