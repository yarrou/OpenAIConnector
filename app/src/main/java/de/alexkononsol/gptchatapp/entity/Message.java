package de.alexkononsol.gptchatapp.entity;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

public class Message {
    private final String text; // message body
    private final String userName;// name of the user that sent this message
    private final String timeAndDate;
    private final boolean belongsToCurrentUser; // is this message sent by us?


    public Message(String text, String userName, boolean belongsToCurrentUser) {
        this.text = text;
        this.userName = userName;
        this.belongsToCurrentUser = belongsToCurrentUser;
        this.timeAndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }
    public Message(String text, String userName, String timeAndDate , boolean belongsToCurrentUser) {
        this.text = text;
        this.userName = userName;
        this.belongsToCurrentUser = belongsToCurrentUser;
        this.timeAndDate = timeAndDate;
    }

    public String getText() {
        return text;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public String getTimeAndDate() {
        return timeAndDate;
    }
}
