package de.alexkononsol.gptchatapp.ui.fragments;

import static java.lang.String.format;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import de.alexkononsol.gptchatapp.HelpActivity;
import de.alexkononsol.gptchatapp.R;
import de.alexkononsol.gptchatapp.utils.SettingsManager;


public class AppSettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_app_settings, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        viewInfoAboutVersionApp();
        ImageButton helpButton = getView().findViewById(R.id.helpButton);
        helpButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HelpActivity.class);
            startActivity(intent);
        });

    }

    private void viewInfoAboutVersionApp() {
        TextView versionView = requireView().findViewById(R.id.app_version);
        versionView.setText(getCurrentVersion());
    }

    private String getCurrentVersion() {
        PackageInfo pInfo = null;
        try {
            pInfo = requireActivity().getPackageManager().getPackageInfo(requireActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert pInfo != null;
        return pInfo.versionName;
    }
}