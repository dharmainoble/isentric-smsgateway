package msg;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SmsMessage {
    private String sender, recipient, messageText;
    private long timestamp;
    private int type = 0;
    private byte[] bytes;
    private Map<String, String> properties = new HashMap<>();

    public SmsMessage() {}

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setMessage(String message) {
        this.messageText = message;
    }

    public String getMessage() {
        return messageText;
    }

    public void requestStatusReport(boolean request) {
        // Placeholder
    }
}
