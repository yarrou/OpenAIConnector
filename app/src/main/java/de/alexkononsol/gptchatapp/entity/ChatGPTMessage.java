package de.alexkononsol.gptchatapp.entity;

import com.google.gson.Gson;

public class ChatGPTMessage {

    private String role;
    private String content;

    public ChatGPTMessage() {
    }

    public ChatGPTMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}



