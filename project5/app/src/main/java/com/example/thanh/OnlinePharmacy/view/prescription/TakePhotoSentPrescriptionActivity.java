package com.example.thanh.OnlinePharmacy.prescription;

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

import com.example.thanh.OnlinePharmacy.check_crash.Crash;
import com.example.thanh.OnlinePharmacy.view.login.network.NetworkUtil;
import com.example.thanh.OnlinePharmacy.view.login.utils.Constants;
import com.example.thanh.OnlinePharmacy.view.main.pay.PayActivity;
import com.example.thanh.OnlinePharmacy.prescription.model.PhotoPrescription;
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

public class TakePhotoSentPrescriptionActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    ImageView ivCamera, ivPicture, ivUpload, ivImage;

    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    int CAMERA_REQUEST = 13323;
    int GALLERY_REQUEST = 22131;

    String selectPhoto;
    private SharedPreferences sharedPreferences;
    private String Id;
    private String Email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo_sent_prescription);
        initSharedPreferences();

        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());

        ivCamera = (ImageView) findViewById(R.id.activity_photo_iv_camera);
        ivPicture = (ImageView) findViewById(R.id.activity_photo_iv_picture);
        ivUpload = (ImageView) findViewById(R.id.activity_photo_iv_upload);
        ivImage = (ImageView) findViewById(R.id.activity_photo_iv_image);

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
                    PhotoPrescription photoPrescription = new PhotoPrescription();
                    photoPrescription.setId(Id);
                    photoPrescription.setStatus("false");
                    photoPrescription.setEmail(Email);
                    photoPrescription.setAddressReceive("làm chổ điền thêm zô sau");
                    photoPrescription.setPhoto(encodedImage);
                    photoPrescription.setNumberBuy(time());
                    Call<ResponseStatus> call = NetworkUtil.getRetrofit().postPhotoPrescription(photoPrescription);
                    call.enqueue(new Callback<ResponseStatus>() {
                        @Override
                        public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                            Toast.makeText(TakePhotoSentPrescriptionActivity.this, "Thành Công: " + response.body().getStatus() +
                                    "  " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TakePhotoSentPrescriptionActivity.this, PayActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseStatus> call, Throwable t) {
                            Toast.makeText(TakePhotoSentPrescriptionActivity.this, "Thất Bại " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("ERROR", "" + t.getMessage());
                        }
                    });

                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Khong the endcoding picture", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Crash.checkForUpdates(this);
    }

    private void initSharedPreferences() {

        sharedPreferences = getApplication().getSharedPreferences("account", MODE_PRIVATE);
        Id = sharedPreferences.getString(Constants.ID, "");
        Email = sharedPreferences.getString(Constants.EMAIL, "");
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
        Crash.checkForCrashes(this);
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
