package de.alexkononsol.gptchatapp.connectionUtils.request;

import android.content.Context;
import android.util.Log;

import de.alexkononsol.gptchatapp.connectionUtils.Api;
import de.alexkononsol.gptchatapp.connectionUtils.ApiInterface;
import de.alexkononsol.gptchatapp.connectionUtils.ServerResponse;
import de.alexkononsol.gptchatapp.dto.UserForm;
import de.alexkononsol.gptchatapp.entity.ChatGPTMessage;

import java.io.IOException;
import java.util.Locale;

import de.alexkononsol.gptchatapp.utils.Constants;
import de.alexkononsol.gptchatapp.utils.SettingsManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class RetrofitRequestToServer {
    private final String lang;
    private final String url;
    private final String token;
    private final ServerResponse serverResponse;
    private ApiInterface apiInterface;
    private Context context;

    public RetrofitRequestToServer(Context context) {
        serverResponse = new ServerResponse();
        url = Constants.DEFAULT_HOST_URL;
        Log.i("GPT", "url = " + url);
        lang = Locale.getDefault().getLanguage();
        token = SettingsManager.getSettings().getAuthToken();
        this.context = context;
    }

    public ServerResponse testResponseExecute() {
        apiInterface = Api.getStringClient(url);
        Call<String> cityCall = apiInterface.getTestMessage();
        try {
            Response<String> response = cityCall.execute();
            if (response.isSuccessful()) {
                Log.w("RetrofitRequestToServer", "response is successful");
                serverResponse.setCode(response.code());
                serverResponse.setData(response.body());
            } else {
                Log.w("RetrofitRequestToServer", "response is unsuccessful");
                serverResponse.setCode(response.code());
                try {
                    assert response.errorBody() != null;
                    serverResponse.setData(response.errorBody().string());
                } catch (IOException e) {
                    serverResponse.setData(e.getLocalizedMessage());
                }
            }
        } catch (IOException e) {
            Log.w("RetrofitRequestToServer", "IOException : " + e.getLocalizedMessage());
            serverResponse.setCode(500);
            serverResponse.setData(e.getLocalizedMessage());
        }
        return serverResponse;
    }

    public ServerResponse chat(ChatGPTMessage gptMessage) {
        Log.i("GPT", "url = " + url);
        apiInterface = Api.getStringClient(url);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), gptMessage.toString());
        Call<String> gptCall = apiInterface.postMessage(body, token, lang);
        try {
            Response<String> response = gptCall.execute();
            if (response.isSuccessful()) {
                Log.i("GPT", "response is successful");
                serverResponse.setCode(response.code());
                serverResponse.setData(response.body());
            } else {
                Log.w("GPT", "response is unsuccessful");
                serverResponse.setCode(response.code());
                try {
                    assert response.errorBody() != null;
                    serverResponse.setData(response.errorBody().string());
                } catch (IOException e) {
                    Log.e("GPT", e.getLocalizedMessage());
                    serverResponse.setData(e.getLocalizedMessage());
                }
            }
        } catch (IOException e) {
            serverResponse.setCode(500);
            serverResponse.setData(e.getLocalizedMessage());
        }
        return serverResponse;
    }

    public ServerResponse loginOrRegistration(UserForm userForm, RetrofitRequestType type) {
        Call<String> cityCall;
        apiInterface = Api.getStringClient(url);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), userForm.toString());
        if (type.type == 2) cityCall = apiInterface.login(body, lang);
        else cityCall = apiInterface.registration(body, lang);
        try {
            Response<String> response = cityCall.execute();
            if (response.isSuccessful()) {
                serverResponse.setCode(response.code());
                serverResponse.setData(response.body());
            } else {
                serverResponse.setCode(response.code());
                try {
                    assert response.errorBody() != null;
                    serverResponse.setData(response.errorBody().string());
                } catch (IOException e) {
                    serverResponse.setData(e.getLocalizedMessage());
                }
            }
        } catch (IOException e) {
            serverResponse.setCode(500);
            serverResponse.setData(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return serverResponse;
    }
}
