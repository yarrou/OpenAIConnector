package de.alexkononsol.gptchatapp.ui.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.alexkononsol.gptchatapp.R;
import de.alexkononsol.gptchatapp.connectionUtils.Role;
import de.alexkononsol.gptchatapp.connectionUtils.ServerResponse;
import de.alexkononsol.gptchatapp.connectionUtils.request.RetrofitRequestToServer;
import de.alexkononsol.gptchatapp.entity.Message;
import de.alexkononsol.gptchatapp.messagesDataBase.MessagesDBHelper;
import de.alexkononsol.gptchatapp.ui.MainActivity;
import de.alexkononsol.gptchatapp.ui.popupWindows.ChatPopupWindowManager;
import de.alexkononsol.gptchatapp.utils.MessageAdapter;
import de.alexkononsol.gptchatapp.utils.MessageProcessor;
import de.alexkononsol.gptchatapp.utils.MessagesConverter;
import de.alexkononsol.gptchatapp.utils.utilInterface.OnMessageItemClickListener;

public class ChatFragment extends Fragment implements OnMessageItemClickListener {
    public static final int NORMAL_MODE = 0;
    public static final int FAVORITES_MODE = 1;
    public static final int SEARCH_MODE = 2;
    private static final String KEY_MODE = "mode";
    private static final String KEY_SEARCH_REQUEST = "searchRequest";
    private EditText editText;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private ImageButton imageButton;
    private MessagesConverter converter;
    private Message message;
    private MessagesDBHelper dbHelper;
    private ChatPopupWindowManager windowManager;
    private  int mode;
    private String searchRequest;
    List<Message> messageList;
    Cursor cursor;
    private MessageProcessor messageProcessor;


    public ChatFragment() {
        this.mode = 0;
    }

    public ChatFragment(int mode, String request) {
        this.mode = mode;
        this.searchRequest = request;
    }

    private Role getCurrentRole() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            return ((MainActivity) getActivity()).getSelectedRole();
        }
        return Role.GPT_3_5_TURBO_0125;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        editText = getView().findViewById(R.id.editText);
        dbHelper = new MessagesDBHelper(getContext());
        messageList = new ArrayList<>();



        loadMessages();

        messageAdapter = new MessageAdapter(getContext(), messageList, this);
        windowManager = new ChatPopupWindowManager(getContext(), dbHelper, messageAdapter);

        messagesView = getView().findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);
        messagesView.setSelection(messagesView.getCount() - 1);

        imageButton = getView().findViewById(R.id.send_button);


        messageProcessor = new MessageProcessor(getContext(), dbHelper, windowManager,editText);

        imageButton.setOnClickListener(v -> {

            messageProcessor.processMessage(editText.getText().toString(), messageAdapter, messagesView,getCurrentRole());
        });
    }

    @Override
    public void onMessageItemClick(Message message, View view) {
        windowManager.showPopupWindow(message, view);
    }
    public void updateFragmentState(int mode, String searchRequest){
        this.mode = mode;
        this.searchRequest = searchRequest;
        loadMessages();
        messageAdapter.notifyDataSetChanged();
    }
    private void loadMessages() {
        if (messageList != null) {
            messageList.clear();}
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            if (mode == NORMAL_MODE) {
                cursor = db.query("MESSAGE", new String[]{"_id", "TEXT", "USER_NAME", "TIME_IN_MILLISECONDS", "BELONGS_TO_CURRENT_USER", "IS_FAVORITES"},
                        null, null, null, null, null);
            } else if (mode == FAVORITES_MODE) {
                cursor = db.query("MESSAGE", new String[]{"_id", "TEXT", "USER_NAME", "TIME_IN_MILLISECONDS", "BELONGS_TO_CURRENT_USER", "IS_FAVORITES"},
                        "IS_FAVORITES > 0", null, null, null, null);
            }else if (mode == SEARCH_MODE){
                cursor = db.query("MESSAGE", new String[]{"_id", "TEXT", "USER_NAME", "TIME_IN_MILLISECONDS", "BELONGS_TO_CURRENT_USER", "IS_FAVORITES"},
                        "TEXT LIKE ?", new String[]{"%" + searchRequest + "%"},
                        null, null, null);
            }
            if (cursor.moveToFirst()) {
                Message dbMessage = new Message(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getInt(4) > 0, cursor.getInt(5) > 0);
                messageList.add(dbMessage);
                while (cursor.moveToNext()) {
                    dbMessage = new Message(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getInt(4) > 0, cursor.getInt(5) > 0);
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
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_MODE, mode);
        outState.putString(KEY_SEARCH_REQUEST, searchRequest);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            mode = savedInstanceState.getInt(KEY_MODE, NORMAL_MODE);
            searchRequest = savedInstanceState.getString(KEY_SEARCH_REQUEST, null);
        }
    }
}