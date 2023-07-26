package de.alexkononsol.gptchatapp.connectionUtils;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("test")
    Call<String> getTestMessage();

    @POST("chat")
    Call<String> postMessage(@Body RequestBody body, @Header("auth-token") String token, @Query("lang") String lang);

    @POST("login")
    Call<String> login(@Body RequestBody body, @Query("lang") String lang);

    @POST("registration")
    Call<String> registration(@Body RequestBody body,@Query("lang") String lang);

}