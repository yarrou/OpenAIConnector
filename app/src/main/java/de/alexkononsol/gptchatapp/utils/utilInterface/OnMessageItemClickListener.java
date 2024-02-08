package de.alexkononsol.gptchatapp.utils.utilInterface;

import android.view.View;

import de.alexkononsol.gptchatapp.entity.Message;

public interface OnMessageItemClickListener {
    void onMessageItemClick(Message message, View view);
}
