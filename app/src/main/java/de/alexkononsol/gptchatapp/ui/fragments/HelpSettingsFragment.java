package de.alexkononsol.gptchatapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import de.alexkononsol.gptchatapp.ui.HelpActivity;
import de.alexkononsol.gptchatapp.R;

public class HelpSettingsFragment extends Fragment {
    private ImageButton helpViewButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        helpViewButton = getView().findViewById(R.id.help_view_button);
        helpViewButton.setOnClickListener(view -> onSettingsHelpInfo());
    }

    private void onSettingsHelpInfo() {
        Intent intent = new Intent(getActivity(), HelpActivity.class);
        startActivity(intent);
    }

}