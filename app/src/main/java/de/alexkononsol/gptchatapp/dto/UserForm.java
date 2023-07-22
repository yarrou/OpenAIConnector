package de.alexkononsol.gptchatapp.dto;

import com.google.gson.Gson;

import java.io.Serializable;

public class UserForm implements Serializable {
    private final String userName;
    private final String password;

    public UserForm(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
