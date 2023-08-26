package de.alexkononsol.gptchatapp.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.alexkononsol.gptchatapp.MainActivity;
import de.alexkononsol.gptchatapp.R;
import de.alexkononsol.gptchatapp.connectionUtils.ServerResponse;
import de.alexkononsol.gptchatapp.connectionUtils.request.RetrofitRequestToServer;
import de.alexkononsol.gptchatapp.connectionUtils.request.RetrofitRequestType;
import de.alexkononsol.gptchatapp.dto.UserForm;
import de.alexkononsol.gptchatapp.ui.registration.RegistrationActivity;
import de.alexkononsol.gptchatapp.utils.SettingsManager;

public class LoginActivity extends AppCompatActivity {

    private TextView textViewLogin;
    private TextView textViewPassword;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textViewLogin = (TextView) findViewById(R.id.username);
        textViewPassword = (TextView) findViewById(R.id.password);
        textViewResult = (TextView) findViewById(R.id.resultOnLoginView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String message = intent.getStringExtra("messageSuccess");
        if (message != null) {
            textViewResult.post(() -> textViewResult.setText(message));
        }
    }

    public void onLogin(View view) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            String userName = textViewLogin.getText().toString();
            UserForm userForm = new UserForm(userName, textViewPassword.getText().toString());
            RetrofitRequestToServer requestToServer = new RetrofitRequestToServer(LoginActivity.this);

            ServerResponse response = requestToServer.loginOrRegistration(userForm, RetrofitRequestType.LOGIN);//loginRequest.send();


            handler.post(() -> {
                //UI Thread work here
                if (response.getCode() == 200) {
                    SettingsManager.getSettings().setUserName(userName);
                    SettingsManager.getSettings().setAuthToken(response.getData().toString());
                    SettingsManager.save();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else textViewResult.setText(response.getData().toString());
            });
        });
    }


    public void onRegistration(View view) {
        String login = textViewLogin.getText().toString();
        String password = textViewPassword.getText().toString();
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        intent.putExtra("username", login);
        intent.putExtra("password", password);
        startActivity(intent);
    }
}