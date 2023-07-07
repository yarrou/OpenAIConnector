package de.alexkononsol.gptchatapp.utils;

import de.alexkononsol.gptchatapp.entity.ChatGPTMessage;
import de.alexkononsol.gptchatapp.entity.MemberData;
import de.alexkononsol.gptchatapp.entity.Message;

public class MessagesConverter {
    public ChatGPTMessage convertToChatGPTMessage(Message message) {
        ChatGPTMessage gptMessage = new ChatGPTMessage("gpt-3.5-turbo", message.getText());
        return gptMessage;
    }

    public Message convertToMessage(String text, String name, String color) {
        return new Message(text, new MemberData(name, color), false);
    }

    public Message convertToMessage(String text, MemberData data) {
        return new Message(text, data, false);
    }

    public Message convertToGPTMessage(String text) {
        return new Message(text, new MemberData("gpt-3.5-turbo", "red"), false);
    }
    public Message convertToMyMessage(String text) {
        return new Message(text, new MemberData("I", "blue"), true);
    }

}
