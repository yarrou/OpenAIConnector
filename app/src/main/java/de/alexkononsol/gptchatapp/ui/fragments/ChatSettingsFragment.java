package de.alexkononsol.gptchatapp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import de.alexkononsol.gptchatapp.R;
import de.alexkononsol.gptchatapp.utils.Constants;
import de.alexkononsol.gptchatapp.utils.SettingsManager;

public class ChatSettingsFragment extends Fragment {
    private RadioGroup radioGroup; //responsible for the size of the help text

    @Override
    public void onStart() {
        super.onStart();
        radioGroup = (RadioGroup) getView().findViewById(R.id.radioTextSize);
        radioGroup.setOnCheckedChangeListener((radioGroup, id) -> {
            float textSize;
            if (id == R.id.textSizeLargeRadio) {
                textSize = Constants.TEXT_SIZE_LARGE;
            } else if (id == R.id.textSizeSmallRadio) {
                textSize = Constants.TEXT_SIZE_SMALL;
            } else textSize = Constants.TEXT_SIZE_NORMAL;
            SettingsManager.getSettings().setTextSize(textSize);
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_settings, container, false);
    }

    public void onResume() {
        super.onResume();
        float textSize = SettingsManager.getSettings().getTextSize();
        int idRadioTextSize = 1;
        if (textSize == Constants.TEXT_SIZE_SMALL) {
            idRadioTextSize = 0;
        } else if (textSize == Constants.TEXT_SIZE_LARGE) {
            idRadioTextSize = 2;
        }
        RadioButton savedCheckedRadioButton = (RadioButton) radioGroup.getChildAt(idRadioTextSize);
        savedCheckedRadioButton.setChecked(true);

    }
}