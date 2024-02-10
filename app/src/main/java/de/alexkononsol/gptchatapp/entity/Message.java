package de.alexkononsol.gptchatapp.entity;

import android.icu.text.SimpleDateFormat;
import java.util.Date;


public class Message {
    private long id;
    private final String text;
    private final String userName;
    private final long timeInMilliseconds;
    private final boolean belongsToCurrentUser;
    private boolean isFavorites;


    public Message(String text, String userName, boolean belongsToCurrentUser, boolean isFavorites) {
        this.text = text;
        this.userName = userName;
        this.belongsToCurrentUser = belongsToCurrentUser;
        this.timeInMilliseconds = System.currentTimeMillis();
        this.isFavorites = isFavorites;
    }

    public Message(long id, String text, String userName, long timeInMilliseconds, boolean belongsToCurrentUser,boolean isFavorites) {
        this.id = id;
        this.text = text;
        this.userName = userName;
        this.belongsToCurrentUser = belongsToCurrentUser;
        this.timeInMilliseconds = timeInMilliseconds;
        this.isFavorites = isFavorites;
    }

    public String getText() {
        return text;
    }

    public String getUserName() {
        return userName;
    }

    public long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public boolean isFavorites() {
        return isFavorites;
    }

    public void setFavorites(boolean favorites) {
        isFavorites = favorites;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
       return  sdf.format(new Date(this.timeInMilliseconds));
    }

}
