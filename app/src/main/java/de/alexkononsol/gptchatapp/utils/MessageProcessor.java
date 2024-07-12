package de.alexkononsol.gptchatapp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;
import android.widget.ListView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.alexkononsol.gptchatapp.connectionUtils.Role;
import de.alexkononsol.gptchatapp.connectionUtils.ServerResponse;
import de.alexkononsol.gptchatapp.connectionUtils.request.RetrofitRequestToServer;
import de.alexkononsol.gptchatapp.entity.Message;
import de.alexkononsol.gptchatapp.messagesDataBase.MessagesDBHelper;
import de.alexkononsol.gptchatapp.ui.popupWindows.ChatPopupWindowManager;

public class MessageProcessor {
    private final Context context;
    private final MessagesDBHelper dbHelper;
    private final ChatPopupWindowManager windowManager;
    private final EditText editText;

    public MessageProcessor(Context context, MessagesDBHelper dbHelper, ChatPopupWindowManager windowManager,EditText editText) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.windowManager = windowManager;
        this.editText = editText;
    }

    public void processMessage(String textMessage, MessageAdapter messageAdapter, ListView messagesView, Role role) {
        if (textMessage.length() > 0) {
            Message message = new MessagesConverter().convertToMyMessage(textMessage);

            new Thread(() -> {
                // Background work here
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                dbHelper.insertMessage(db, message);
                db.close();

                // Update UI on the main thread
                new Handler(Looper.getMainLooper()).post(() -> {
                    messageAdapter.add(message);
                    messagesView.setSelection(messagesView.getCount() - 1);
                });

                RetrofitRequestToServer requestToServer = new RetrofitRequestToServer(context);
                MessagesConverter converter = new MessagesConverter();
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                executor.execute(() -> {
                    // Background work here
                    ServerResponse response = requestToServer.chat(converter.convertToChatGPTMessage(message,role));
                    String content = response.getData().toString();
                    Message gptMessage = converter.convertToGPTMessage(content,role);

                    // Update UI on the main thread
                    handler.post(() -> {
                        SQLiteDatabase dbResponse = dbHelper.getReadableDatabase();
                        dbHelper.insertMessage(dbResponse, gptMessage);
                        dbResponse.close();

                        messageAdapter.add(gptMessage);
                        messagesView.setSelection(messagesView.getCount() - 1);
                        editText.setText("");
                    });
                });
            }).start();
        }
    }
}
