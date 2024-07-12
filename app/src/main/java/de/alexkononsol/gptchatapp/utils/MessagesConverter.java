package de.alexkononsol.gptchatapp.utils;

import de.alexkononsol.gptchatapp.connectionUtils.Role;
import de.alexkononsol.gptchatapp.entity.ChatGPTMessage;
import de.alexkononsol.gptchatapp.entity.Message;

public class MessagesConverter {
    public ChatGPTMessage convertToChatGPTMessage(Message message,Role role) {
        return  new ChatGPTMessage(role.getRoleName(), message.getText());
    }

    public Message convertToGPTMessage(String text, Role role) {
        return new Message(text, role.getRoleName(), false,false);
    }
    public Message convertToMyMessage(String text) {
        return new Message(text, "I", true,false);
    }

}
