package com.example.thanh.project1.Login.network;

/**
 * Created by thanh on 3/7/2017.
 */

import com.example.thanh.project1.Login.model.Response;
import com.example.thanh.project1.Login.model.User;
import com.example.thanh.project1.Pay.fragment.Pay_card;
import com.example.thanh.project1.Pay.model.PayCard;
import com.example.thanh.project1.Prescription.model.PhotoPrescription;
import com.example.thanh.project1.Prescription.model.Prescription;
import com.example.thanh.project1.ResponseStatus;

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
    //  router.get('/prescription'
    @GET("prescription/{id}")
    Call<List<Prescription>> getPrescription(@Path("id") String id);
    //  router.post('/prescription'/confirmList/
    @GET("prescription/{id}/true")
    Call<List<Prescription>> getPrescriptionConfirm(@Path("id") String id);
    //  router.post('/prescription'
    @POST("prescription")
    Call<ResponseStatus> postPrescription(@Body Prescription prescription);
    //  router.get('/prescriptionlist'
    @POST("1pay")
    Call<ResponseStatus> postPayCard(@Body PayCard payCard);
    @POST("prescription/photo")
    Call<ResponseStatus> postPhotoPrescription(@Body PhotoPrescription photoPrescription);
}

