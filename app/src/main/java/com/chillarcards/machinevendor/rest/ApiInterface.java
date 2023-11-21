package com.chillarcards.machinevendor.rest;

//import chillarcards.com.networktest.Example;
import com.chillarcards.machinevendor.OnlineModel.OnlinePOJO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

//import info.androidhive.retrofit.model.MoviesResponse;


public interface ApiInterface {
//    @GET("movie/top_rated")
//    Call<HomeResponse> getHomeResponse(@Query("api_key") String apiKey);

    @GET("{path}")
    Call<OnlinePOJO> getOnline1(@Path("path") String path, @Query("machineUserName") String machineUserName, @Query("machineUserPassword") String machineUserPassword,
                                @Query("schoolMachineCode") String schoolMachineCode, @Query("cardSerial") String cardSerial,
                                @Query("onlineTransactionID") String onlineTransactionI);


    @GET("{path}")
    Call<OnlinePOJO> getOnline2(@Path("path") String path, @Query("machineUserName") String machineUserName, @Query("machineUserPassword") String machineUserPassword,
                                  @Query("schoolMachineCode") String schoolMachineCode, @Query("cardSerial") String cardSerial);
}
