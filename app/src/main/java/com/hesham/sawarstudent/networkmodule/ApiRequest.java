package com.hesham.sawarstudent.networkmodule;


import com.hesham.sawarstudent.data.model.AddOrderPojo;
import com.hesham.sawarstudent.data.model.SubjectPojo;
import com.hesham.sawarstudent.data.model.UserPojo;
import com.hesham.sawarstudent.data.response.CategoryResponse;
import com.hesham.sawarstudent.data.response.CenterResponse;
import com.hesham.sawarstudent.data.response.CheckPromoResponse;
import com.hesham.sawarstudent.data.response.CustomResponse;
import com.hesham.sawarstudent.data.response.DelayResponse;
import com.hesham.sawarstudent.data.response.DepartmentResponse;
import com.hesham.sawarstudent.data.response.DetailsResponse;
import com.hesham.sawarstudent.data.response.FacultyResponse;
import com.hesham.sawarstudent.data.response.ImageResponse;
import com.hesham.sawarstudent.data.response.LoginResponse;
import com.hesham.sawarstudent.data.response.OrderResponse;
import com.hesham.sawarstudent.data.response.PaperResponse;
import com.hesham.sawarstudent.data.response.SignUpResponse;
import com.hesham.sawarstudent.data.response.SubjectResponse;
import com.hesham.sawarstudent.data.response.WaitingResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by ayman on 2019-05-13.
 */

public interface ApiRequest {

    @POST("student/login")
    Call<LoginResponse> SignIn(@Body UserPojo body);

    @POST("student/signup")
    Call<SignUpResponse> signup(@Body UserPojo body);

    @POST("student/update")
    Call<SignUpResponse> updateProfile(@Body UserPojo body);

    @Multipart
    @POST("image/")
    Call<ImageResponse> uploadProfileImages(@Part("file\";filename=\"files.jpg\" ") RequestBody file);

    @FormUrlEncoded
    @POST("student/home")
    Call<CenterResponse> getAllCenter(
            @Field("univ_id") int univ_id,
            @Field("f_id") int f_id,
            @Field("stud_id") int stud_id
    );


    @FormUrlEncoded
    @POST("student/orders")
    Call<OrderResponse> getMyOrders(@Field("stud_id") int stud_id);

    @GET("faculty/all")
    Call<FacultyResponse> getFaculties();

    @FormUrlEncoded
    @POST("faculty/departments")
    Call<DepartmentResponse> getAllDepartments(@Field("f_id") int f_id);


    @POST("faculty/subjects/filter")
    Call<SubjectResponse> getFilteredSubjects(@Body SubjectPojo body);
    @POST("faculty/subjects/all")
    Call<SubjectResponse> getAllSubjects(@Body SubjectPojo body);


    @GET("/faculty/category/get")
    Call<CategoryResponse> getCategory(@Query("sub_id") int subId);

    @GET("faculty/paper/get/stud")
    Call<PaperResponse> getPapers(@Query("category_id") int category_id, @Query("sub_id") int subId, @Query("stud_id") int stud_id);


    @FormUrlEncoded
    @POST("/student/notification")
    Call<CustomResponse> applyNotification(
            @Field("cc_id") int cc_id ,
            @Field("stud_id") int stud_id ,
            @Field("f_id") int f_id ,
            @Field("dep_id") Integer dep_id ,
            @Field("year") int year);


    @FormUrlEncoded
    @POST("faculty/paper/manualmark")
    Call<CustomResponse> manualmarkPaper(
            @Field("stud_id") int stud_id,
            @Field("sub_id") int sub_id,
            @Field("paper_id") int paper_id
    );

    @GET("univ/all")
    Call<FacultyResponse> getUniversities();

    @GET("cc/home")
    Call<FacultyResponse> getHomeFaculties(@Query("cc_id") int cc_id);

    @GET("cc/problem/check")
    Call<DelayResponse> checkProblem(@Query("cc_id") int cc_id);


    @GET("order/promocode/check")
    Call<CheckPromoResponse> checkPromocode(
            @Query("stud_id") int stud_id,
            @Query("code") String code
    );

    //    @FormUrlEncoded
    @POST("order/add")
    Call<CustomResponse> addOrder(
            @Body AddOrderPojo addOrderPojo
    );

    @FormUrlEncoded
    @POST("student/token/update")
    Call<CustomResponse> pushNotificationToken(
            @Field("token") String token,
            @Field("id") int id
    );


    @GET("order/waiting")
    Call<WaitingResponse> getWaiting(
            @Query("cc_id") int cc_id
    );

    @GET("student/points")
    Call<LoginResponse> getPoints(@Query("stud_id") int stud_id);

    @GET("order/service")
    Call<CheckPromoResponse> checkForfreeServiceFromPoints(@Query("stud_id") int stud_id);


    @GET("order/details")
    Call<DetailsResponse> getOrderDetails(@Query("order_id") int order_id);

    @GET("order/status")
    Call<OrderResponse> getOrderStatus(@Query("o_id") int order_id);

    @GET("order/cancelstud")
    Call<CustomResponse> cancelOrder(@Query("order_id") int order_id, @Query("comment") String ordcommenter_id);

    @GET("order/studremove")
    Call<CustomResponse> removeOrder(@Query("order_id") int order_id);

    @GET("order/resend")
    Call<CustomResponse> resendOrder(@Query("order_id") int order_id,
                                     @Query("cc_id") int cc_id);

    @GET("order/recieverate")
    Call<CustomResponse> recieveRateOrder(@Query("order_id") int order_id,
                                          @Query("stud_id") int stud_id,
                                          @Query("rate") int rate,
                                          @Query("comment") String comment
    );


    @FormUrlEncoded
    @POST("student/favorite/add")
    Call<CustomResponse> addFavourite(
            @Field("stud_id") int stud_id,
            @Field("sub_id") int sub_id,
            @Field("category_id") int category_id
    );

    @FormUrlEncoded
    @POST("student/favorite/remove")
    Call<CustomResponse> removeFavourite(
            @Field("stud_id") int stud_id,
            @Field("sub_id") int sub_id,
            @Field("category_id") int category_id
    );

    @GET("student/favorite/get")
    Call<SubjectResponse> getFavourite(@Query("stud_id") int stud_id);


    @GET("student/AppUpdates")
    Call<CustomResponse> checkAppUpdates();

}





