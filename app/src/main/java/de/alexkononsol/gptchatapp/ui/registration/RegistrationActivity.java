package de.alexkononsol.gptchatapp.ui.registration;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.alexkononsol.gptchatapp.R;
import de.alexkononsol.gptchatapp.connectionUtils.ServerResponse;
import de.alexkononsol.gptchatapp.connectionUtils.request.RetrofitRequestToServer;
import de.alexkononsol.gptchatapp.connectionUtils.request.RetrofitRequestType;
import de.alexkononsol.gptchatapp.dto.RegistrationForm;
import de.alexkononsol.gptchatapp.ui.login.LoginActivity;
import de.alexkononsol.gptchatapp.utils.RegistrationFormValidator;

public class RegistrationActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordRepeatEditText;
    private TextView resultRegistrationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        usernameEditText = (EditText)findViewById(R.id.username_reg);
        passwordEditText =(EditText) findViewById(R.id.password_reg);
        passwordRepeatEditText = (EditText) findViewById(R.id.repeat_password);
        resultRegistrationView = (TextView) findViewById(R.id.resultRegistrationView);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        String login = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        if(login!=null) usernameEditText.setText(login);
        if(password!=null) passwordEditText.setText(password);

    }
    private RegistrationForm getRegistrationForm(){
        return new RegistrationForm(usernameEditText.getText().toString(),passwordEditText.getText().toString(),passwordRepeatEditText.getText().toString());
    }
    public void onRegistration(View view){
        Log.d("chatgpt","start registration");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        RegistrationForm form = getRegistrationForm();
        executor.execute(() -> {
            //Background work here
            RegistrationFormValidator validator = new RegistrationFormValidator(this);
            ServerResponse result = validator.regFormValidate(form);
            if(result.getCode()==200){
                Log.d("chatgpt","registration form is valid");
                RetrofitRequestToServer requestToServer = new RetrofitRequestToServer(RegistrationActivity.this);
                result = requestToServer.loginOrRegistration(form.getUserForm(), RetrofitRequestType.REGISTRATION);
            }
            ServerResponse finalResult = result;
            handler.post(() -> {
                //UI Thread work here
                if(finalResult.getCode()==200){
                    Log.d("chatgpt","registration is successful");
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    intent.putExtra("messageSuccess",getString(R.string.registration_success_message));
                    startActivity(intent);
                    finish();
                }else resultRegistrationView.setText(finalResult.getData().toString());
            });
        });
    }
}