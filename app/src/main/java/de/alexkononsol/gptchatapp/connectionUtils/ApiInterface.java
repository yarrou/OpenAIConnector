package de.alexkononsol.gptchatapp.connectionUtils;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @GET("test")
    Call<String> getTestMessage();

    @POST("chat")
    Call<String> postMessage(@Body RequestBody body);

}