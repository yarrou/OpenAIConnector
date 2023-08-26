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
    public void insertMessage(SQLiteDatabase db, Message message) {
        ContentValues messageValues = new ContentValues();
        messageValues.put("TEXT", message.getText());
        messageValues.put("USER_NAME", message.getUserName());
        messageValues.put("TIME_AND_DATE", message.getTimeAndDate());
        messageValues.put("BELONGS_TO_CURRENT_USER", message.isBelongsToCurrentUser());
        db.insert("MESSAGE", null, messageValues);
    }
    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE    MESSAGE (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "TEXT TEXT, "
                    + "USER_NAME TEXT, "
                    + "TIME_AND_DATE TEXT, "
                    + "BELONGS_TO_CURRENT_USER);");
            insertMessage(db, new Message(context.getString(R.string.chat_greeting),"gpt-3,5 turbo",false));
        }
        //if (oldVersion < 2) {}
    }
    public void deleteAllMessages() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("MESSAGE", null, null);
        insertMessage(db, new Message(context.getString(R.string.chat_greeting),"gpt-3,5 turbo",false));
        db.close();
    }
}
