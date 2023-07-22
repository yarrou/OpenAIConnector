package de.alexkononsol.gptchatapp.utils;

import android.content.Context;

import de.alexkononsol.gptchatapp.R;
import de.alexkononsol.gptchatapp.connectionUtils.ServerResponse;
import de.alexkononsol.gptchatapp.dto.RegistrationForm;

public class RegistrationFormValidator {
    public RegistrationFormValidator(Context context) {
        this.context = context;
    }

    private Context context;

    public ServerResponse regFormValidate(RegistrationForm form){
        ServerResponse result = new ServerResponse();
        if(!form.getPassword().equals(form.getRepeatPassword())){
            result.setCode(400);
            result.setData(context.getString(R.string.warning_passwords_don_t_match));
        }else if(form.getPassword().trim().length()<5){
            result.setCode(400);
            result.setData(context.getString(R.string.warning_password_is_too_short));
        }
        else {
            result.setCode(200);
            result.setData("");
        }
        return result;
    }
}