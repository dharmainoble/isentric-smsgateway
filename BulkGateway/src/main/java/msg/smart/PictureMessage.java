package msg.smart;

import java.util.HashMap;
import java.util.Map;

public class PictureMessage {
    private String id;
    private byte[] content;
    private String sender;
    private String recipient;
    private String messageId;
    private boolean statusReportRequested = false;
    private Map<String, String> properties = new HashMap<>();

    public PictureMessage() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public boolean isStatusReportRequested() {
        return statusReportRequested;
    }

    public void requestStatusReport(boolean request) {
        this.statusReportRequested = request;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
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

    public Object[] getParts() {
        return new Object[0];
    }

    public void setParent(PictureMessage parent) {
        // Placeholder implementation
    }

    public void setPictureMessage(String url, String description) {
        // Placeholder implementation
    }
}
