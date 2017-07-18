package com.example.n55.jsonandvally;

import com.example.n55.jsonandvally.model.KalaEdit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface MyApiInterface {

   // @FormUrlEncoded
    @POST("api/product")
//    Call<JsonObject> myMethod(@Field("kId") String kId , @Field("kSepId") String kSepId , @Field("kName") String kName ,
//                              @Field("kEName") String kEName , @Field("kPrice") String kPrice , @Field("kInvId") String kInvId,
//                              @Field("klComment") String klComment ,
//                              @Header("apikey") String apiKey , @Header("type") String type );

    Call<KalaEdit> myMethod(
            @Header("apikey") String apiKey,
            @Header("type") String type, @Body KalaEdit kalaClass);

}
