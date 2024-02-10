package de.alexkononsol.gptchatapp.messagesDataBase;

import android.content.ContentValues;
import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import de.alexkononsol.gptchatapp.R;
import de.alexkononsol.gptchatapp.entity.Message;


public class MessagesDBHelper extends SQLiteOpenHelper {
    private final Context context;
    private static final String DB_NAME = "openai";
    private static final int DB_VERSION = 1;

    public MessagesDBHelper( Context context) {
        super(context, DB_NAME,null, DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public long insertMessage(SQLiteDatabase db, Message message) {
        ContentValues values = new ContentValues();
        values.put("TEXT", message.getText());
        values.put("USER_NAME", message.getUserName());
        values.put("TIME_IN_MILLISECONDS", message.getTimeInMilliseconds());
        values.put("BELONGS_TO_CURRENT_USER", message.isBelongsToCurrentUser() ? 1 : 0);
        values.put("IS_FAVORITES", message.isFavorites() ? 1 : 0);
        long id = db.insert("MESSAGE", null, values);

        message.setId(id);

        return id;
    }
    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE MESSAGE (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "TEXT TEXT, "
                    + "USER_NAME TEXT, "
                    + "TIME_IN_MILLISECONDS INTEGER, "
                    + "BELONGS_TO_CURRENT_USER INTEGER, "
                    + "IS_FAVORITES INTEGER)");
            insertMessage(db, new Message(context.getString(R.string.chat_greeting), "gpt-3,5 turbo", false,false));
        }
        //if (oldVersion < 2) {}
    }

    public void deleteAllMessages() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("MESSAGE", null, null);
        insertMessage(db, new Message(context.getString(R.string.chat_greeting), "gpt-3,5 turbo", false,false));
        db.close();
    }
    public void updateMessageFavorites(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IS_FAVORITES", message.isFavorites());


        db.update("MESSAGE", values, "_id=?", new String[]{String.valueOf(message.getId())});
        db.close();
    }
    public void deleteMessage(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("MESSAGE","_id=?", new String[]{String.valueOf(id)});
        db.close();
    }
}