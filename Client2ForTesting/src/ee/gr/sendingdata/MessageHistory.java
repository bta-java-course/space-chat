package ee.gr.sendingdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessageHistory implements Serializable {

    private volatile List<Message> messageList;
    private volatile List<Message> systemMessageList;
    private volatile boolean isUpdated;
    private Message lastMessage;

    public MessageHistory() {
        this.messageList = new ArrayList<>();
        this.systemMessageList = new ArrayList<>();
        this.isUpdated = true;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public List<Message> getSystemMessageList() {
        return systemMessageList;
    }

    public void setSystemMessageList(List<Message> systemMessageList) {
        this.systemMessageList = systemMessageList;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public Message getLastMessage() {
        if (lastMessage == null) {
            isUpdated = false;
            return null;
        }
        else {
             isUpdated = false;
             return lastMessage;
        }
    }

    public void addMessage(Message message) {
        if (message.getType().equals(Message.Type.SYSTEM_ALL) ||
                message.getType().equals(Message.Type.SYSTEM_CHANNEL) ||
                message.getType().equals(Message.Type.SYSTEM_FOR_USER)) {
            systemMessageList.add(message);
        } else messageList.add(message);
        lastMessage = message;
        this.isUpdated = true;
    }
}
