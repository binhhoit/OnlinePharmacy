package com.example.thanh.OnlinePharmacy.login.network;

/**
 * Created by thanh on 3/7/2017.
 */

import com.example.thanh.OnlinePharmacy.login.model.Response;
import com.example.thanh.OnlinePharmacy.login.model.User;
import com.example.thanh.OnlinePharmacy.pay.model.PayCard;
import com.example.thanh.OnlinePharmacy.prescription.model.PhotoPrescription;
import com.example.thanh.OnlinePharmacy.prescription.model.Prescription;
import com.example.thanh.OnlinePharmacy.ResponseStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;


public interface RetrofitInterface {

    @POST("users")
    Observable<Response> register(@Body User user);
    //  ứng với router.post('/authenticate', (req, res)
    @POST("authenticate")
    Observable<Response> login();
    //  router.get('/users/:id', (req,res)
    @GET("users/{email}")
    Observable<User> getProfile(@Path("email") String email);
    //  router.put('/users/:id'
    @PUT("users/{email}")
    Observable<Response> changePassword(@Path("email") String email, @Body User user);
    //  router.post('/users/:id/password' dùng chung vs bên dưới
    @POST("users/{email}/password")
    Observable<Response> resetPasswordInit(@Path("email") String email);
    //  router.post('/users/:id/password'
    @POST("users/{email}/password")
    Observable<Response> resetPasswordFinish(@Path("email") String email, @Body User user);
    //  router.get('/Prescription'
    @GET("Prescription/{id}")
    Call<List<Prescription>> getPrescription(@Path("id") String id);
    //  router.post('/Prescription'/confirmList/
    @GET("Prescription/{id}/true")
    Call<List<Prescription>> getPrescriptionConfirm(@Path("id") String id);
    //  router.post('/Prescription'
    @POST("Prescription")
    Call<ResponseStatus> postPrescription(@Body Prescription Prescription);
    //  router.get('/prescriptionlist'
    @POST("1pay")
    Call<ResponseStatus> postPayCard(@Body PayCard payCard);
    @POST("Prescription/photo")
    Call<ResponseStatus> postPhotoPrescription(@Body PhotoPrescription PhotoPrescription);
}

