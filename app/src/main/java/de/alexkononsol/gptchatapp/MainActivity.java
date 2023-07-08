package de.alexkononsol.gptchatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.alexkononsol.gptchatapp.connectionUtils.ServerResponse;
import de.alexkononsol.gptchatapp.connectionUtils.request.RetrofitRequestToServer;
import de.alexkononsol.gptchatapp.entity.Message;
import de.alexkononsol.gptchatapp.messagesDataBase.MessagesDBHelper;
import de.alexkononsol.gptchatapp.utils.MessageAdapter;
import de.alexkononsol.gptchatapp.utils.MessagesConverter;
import de.alexkononsol.gptchatapp.utils.SettingsManager;
import de.alexkononsol.gptchatapp.utils.SharedPreferenceAssistant;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private ImageButton imageButton;
    private MessagesConverter converter;
    private Message message;
    private MessagesDBHelper dbHelper;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // This is where we write the message
        SharedPreferenceAssistant.initSharedPreferences(MainActivity.this);
        SettingsManager.initSettings();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editText = (EditText) findViewById(R.id.editText);
        dbHelper = new MessagesDBHelper(this);
        List<Message> messageList = new ArrayList<>();
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query("MESSAGE", new String[]{"TEXT", "USER_NAME", "TIME_AND_DATE", "BELONGS_TO_CURRENT_USER"},
                    null, null, null, null, null);
            if (cursor.moveToFirst()) {
                Message dbMessage = new Message(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3) > 0);
                messageList.add(dbMessage);
                while (cursor.moveToNext()){
                    dbMessage = new Message(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3) > 0);
                    messageList.add(dbMessage);
                }
                cursor.close();
                db.close();
            }


        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this,
                    "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        messageAdapter = new MessageAdapter(this,messageList);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);
        messagesView.setSelection(messagesView.getCount() - 1);
        imageButton = findViewById(R.id.send_button);
    }

    public void sendMessage(View view) {
        String textMessage = editText.getText().toString();
        if (textMessage.length() > 0) {

            editText.getText().clear();
            Message message = new MessagesConverter().convertToMyMessage(textMessage);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    dbHelper.insertMessage(db,message);
                    db.close();
                    messageAdapter.add(message);
                    // scroll the ListView to the last added element
                    messagesView.setSelection(messagesView.getCount() - 1);
                }
            });
            RetrofitRequestToServer requestToServer = new RetrofitRequestToServer(getBaseContext());
            MessagesConverter converter = new MessagesConverter();
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(() -> {
                //Background work here
                ServerResponse response = requestToServer.chat(converter.convertToChatGPTMessage(message));
                String content = response.getData().toString();
                Message gptMessage = converter.convertToGPTMessage(content);
                handler.post(() -> {
                    //UI Thread work here
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    dbHelper.insertMessage(db,gptMessage);
                    db.close();
                    messageAdapter.add(gptMessage);
                    // scroll the ListView to the last added element
                    messagesView.setSelection(messagesView.getCount() - 1);

                });
            });


        }
    }
}