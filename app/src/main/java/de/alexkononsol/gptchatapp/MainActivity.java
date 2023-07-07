package de.alexkononsol.gptchatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.alexkononsol.gptchatapp.connectionUtils.ServerResponse;
import de.alexkononsol.gptchatapp.connectionUtils.request.RetrofitRequestToServer;
import de.alexkononsol.gptchatapp.entity.Message;
import de.alexkononsol.gptchatapp.utils.Constants;
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


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // This is where we write the message
        SharedPreferenceAssistant.initSharedPreferences(MainActivity.this);
        Log.i("MainActivity","init Settings");
        Log.i("Constants.url", Constants.DEFAULT_HOST_URL);
        SettingsManager.initSettings();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editText = (EditText) findViewById(R.id.editText);
        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);
        imageButton = findViewById(R.id.send_button);
        Log.i("Constants.url", "Constants.DEFAULT_HOST_URL");
    }

    public void sendMessage(View view) {
        String textMessage = editText.getText().toString();
        if (textMessage.length() > 0) {

            editText.getText().clear();
            Message message = new MessagesConverter().convertToMyMessage(textMessage);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
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
                Log.w("MainActivity","content = " + content);
                Message gptMessage = converter.convertToGPTMessage(content);
                handler.post(() -> {
                    //UI Thread work here
                    messageAdapter.add(gptMessage);
                    // scroll the ListView to the last added element
                    messagesView.setSelection(messagesView.getCount() - 1);

                });
            });
        }
    }
}