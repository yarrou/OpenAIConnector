package de.alexkononsol.gptchatapp.ui.popupWindows;


import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import de.alexkononsol.gptchatapp.R;
import de.alexkononsol.gptchatapp.entity.Message;
import de.alexkononsol.gptchatapp.messagesDataBase.MessagesDBHelper;
import de.alexkononsol.gptchatapp.utils.MessageAdapter;


public class ChatPopupWindowManager {

    private final Context context;
    private PopupWindow popupWindow;
    private Message currentMessage;
    private MessageAdapter messageAdapter;
    private MessagesDBHelper dbHelper;

    public ChatPopupWindowManager(Context context,MessagesDBHelper dbHelper,MessageAdapter messageAdapter) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.messageAdapter = messageAdapter;

        initPopupWindow();
    }


    private void initPopupWindow() {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.chat_popup_layout, null);

        popupWindow = new PopupWindow(
                contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);

        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_background));
        popupWindow.setFocusable(true);

        LinearLayout favoriteContainer = contentView.findViewById(R.id.favoriteContainer);
        LinearLayout deleteContainer = contentView.findViewById(R.id.deleteContainer);
        LinearLayout shareContainer = contentView.findViewById(R.id.shareContainer);

        View.OnClickListener containerClickListener = v -> {
            dismissPopupWindow();

            if (v == favoriteContainer) {
                boolean isFavorites = !currentMessage.isFavorites();
                currentMessage.setFavorites(isFavorites);
                dbHelper.updateMessageFavorites(currentMessage);
                messageAdapter.notifyDataSetChanged();
            } else if (v == deleteContainer) {
                dbHelper.deleteMessage(currentMessage.getId());
                messageAdapter.delete(currentMessage);
            } else if (v == shareContainer) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, currentMessage.getText());

                context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_by)));
            }
        };

        favoriteContainer.setOnClickListener(containerClickListener);
        deleteContainer.setOnClickListener(containerClickListener);
        shareContainer.setOnClickListener(containerClickListener);
    }

    public void showPopupWindow(Message message, View anchorView) {
        this.currentMessage = message;
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
    }

    private void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
}
