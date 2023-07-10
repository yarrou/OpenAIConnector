package de.alexkononsol.gptchatapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

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

public class ChatFragment extends Fragment {
    private EditText editText;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private ImageButton imageButton;
    private MessagesConverter converter;
    private Message message;
    private MessagesDBHelper dbHelper;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferenceAssistant.initSharedPreferences(getView().getContext());
        SettingsManager.initSettings();

        editText = (EditText) getView().findViewById(R.id.editText);
        dbHelper = new MessagesDBHelper(getContext());
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
            Toast toast = Toast.makeText(getView().getContext(),
                    "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        messageAdapter = new MessageAdapter(getContext(),messageList);
        messagesView = (ListView) getView().findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);
        messagesView.setSelection(messagesView.getCount() - 1);
        imageButton = getView().findViewById(R.id.send_button);

        imageButton.setOnClickListener(v -> {
            sendMessage();
        });
    }
    public void sendMessage() {
        String textMessage = editText.getText().toString();
        if (textMessage.length() > 0) {

            editText.getText().clear();
            Message message = new MessagesConverter().convertToMyMessage(textMessage);

            getActivity().runOnUiThread(new Runnable() {
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
            RetrofitRequestToServer requestToServer = new RetrofitRequestToServer(getContext());
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