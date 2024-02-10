package de.alexkononsol.gptchatapp.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import de.alexkononsol.gptchatapp.R;
import de.alexkononsol.gptchatapp.entity.Message;
import de.alexkononsol.gptchatapp.utils.utilInterface.OnMessageItemClickListener;

import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private OnMessageItemClickListener clickListener;
    List<Message> messages;
    Context context;

    public MessageAdapter(Context context, List<Message> messages, OnMessageItemClickListener listener) {
        this.context = context;
        this.messages = messages;
        this.clickListener = listener;
    }

    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }
    public void delete(Message message){
        this.messages.remove(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // This is the backbone of the class, it handles the creation of single ListView row (chat bubble)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(i);

        if (message.isBelongsToCurrentUser()) { // this message was sent by us so let's create a basic chat bubble on the right
            convertView = messageInflater.inflate(R.layout.my_message, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);
            holder.messageBody.setText(message.getText());

        } else { // this message was sent by someone else so let's create an advanced chat bubble on the left
            convertView = messageInflater.inflate(R.layout.their_message, null);
            holder.avatar = (View) convertView.findViewById(R.id.avatar);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);

            holder.name.setText( message.getUserName());
            holder.messageBody.setText(message.getText());
            holder.avatar.setBackgroundResource(R.drawable.ic_launcher_round);
        }
        if (message.isFavorites()){
            holder.isFavorites = (ImageView) convertView.findViewById(R.id.is_favorites);
            holder.isFavorites.setVisibility(View.VISIBLE);
        }
        holder.date = (TextView) convertView.findViewById(R.id.date_message);
        holder.date.setText(message.getDate());
        holder.messageBody.setTextSize(SettingsManager.getSettings().getTextSize());

        holder.messageBody.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.onMessageItemClick(message,view);
            }
        });

        return convertView;
    }

}

class MessageViewHolder {
    public View avatar;
    public TextView name;
    public TextView messageBody;
    public ImageView isFavorites;
    public TextView date;
}