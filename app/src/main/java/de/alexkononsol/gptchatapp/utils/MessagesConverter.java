package de.alexkononsol.gptchatapp.utils;

import de.alexkononsol.gptchatapp.entity.ChatGPTMessage;
import de.alexkononsol.gptchatapp.entity.Message;

public class MessagesConverter {
    public ChatGPTMessage convertToChatGPTMessage(Message message) {
        return  new ChatGPTMessage("gpt-3.5-turbo", message.getText());
    }

    public Message convertToGPTMessage(String text) {
        return new Message(text, "gpt-3,5 turbo", false,false);
    }
    public Message convertToMyMessage(String text) {
        return new Message(text, "I", true,false);
    }

}
