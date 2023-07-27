package de.alexkononsol.gptchatapp.ui.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.alexkononsol.gptchatapp.R;
import de.alexkononsol.gptchatapp.ui.login.LoginActivity;
import de.alexkononsol.gptchatapp.utils.SettingsManager;


public class AccountSettingsFragment extends Fragment {
    private Button logoutButton;
    private TextView authInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        authInfo = getView().findViewById(R.id.authSettingsStatus);
        logoutButton = getView().findViewById(R.id.logoutButton);
    }

    private void viewInfoAboutAccount() {
        Drawable img;
        if (SettingsManager.getSettings().getAuthToken() != null) {
            authInfo.setText(getString(R.string.authInfo) + SettingsManager.getSettings().getUserName());
            img = getContext().getDrawable(R.drawable.baseline_logout_24);
        } else {
            authInfo.setText(getString(R.string.anonimous));
            logoutButton.setText(getString(R.string.sign_in_button_text));
            img = getContext().getDrawable(R.drawable.baseline_login_24);
        }
        logoutButton.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewInfoAboutAccount();
        logoutButton.setOnClickListener(v -> {
            if (SettingsManager.getSettings().getUserName() != null) {
                signOut();
            } else {
                signIn();
            }
        });
    }

    public void signOut() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            SettingsManager.getSettings().setAuthToken(null);
            SettingsManager.getSettings().setUserName(null);
            SettingsManager.save();
            Drawable img = getContext().getDrawable(R.drawable.baseline_login_24);

            handler.post(() -> {
                //UI Thread work here
                authInfo.setText(getString(R.string.anonimous));
                logoutButton.setText(getString(R.string.sign_in_button_text));
                logoutButton.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
            });
        });
    }

    private void signIn() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}