package com.example.thanh.OnlinePharmacy.service.network;

/**
 * Created by thanh on 3/7/2017.
 */

import com.example.thanh.OnlinePharmacy.model.Response;
import com.example.thanh.OnlinePharmacy.model.User;
import com.example.thanh.OnlinePharmacy.model.PayCard;
import com.example.thanh.OnlinePharmacy.model.PhotoPrescription;
import com.example.thanh.OnlinePharmacy.model.Prescription;
import com.example.thanh.OnlinePharmacy.model.ResponseStatus;

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
    @GET("users/{id}")
    Observable<User> getProfile(@Path("id") String id);

    //  router.put('/users/:id'
    @PUT("users/{id}")
    Observable<Response> changePassword(@Path("id") String id, @Body User user);

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
    @POST("Prescription/{id}")
    Call<ResponseStatus> postPrescription(@Body Prescription Prescription,@Path("id") String id);

    //  router.get('/prescriptionlist'
    @POST("1pay/{id}")
    Call<ResponseStatus> postPayCard(@Body PayCard payCard,@Path("id") String id);

    @POST("Prescription/photo/{id}")
    Call<ResponseStatus> postPhotoPrescription(@Body PhotoPrescription PhotoPrescription,@Path("id") String id);
}

