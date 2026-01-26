package msg.ems;

import com.objectxp.msg.SmsMessage;

import java.util.HashMap;
import java.util.Map;

public class EMSMessage extends SmsMessage {
    private String id;
    private byte[] content;
    private String sender;
    private String recipient;
    private short codingGroup;
    private boolean statusReportRequested = false;
    private Map<String, String> properties = new HashMap<>();

    public EMSMessage() {}

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

    public short getCodingGroup() {
        return codingGroup;
    }

    public void setCodingGroup(short codingGroup) {
        this.codingGroup = codingGroup;
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
        // Return empty array by default
        return new Object[0];
    }

    public void setParent(EMSMessage parent) {
        // Placeholder implementation
    }

    public void setUserData(byte[] userData) {
        this.content = userData;
    }

    public byte[] getUserData() {
        return this.content;
    }

    public void setAlphabet(short alphabet) {
        // Placeholder implementation
    }

    public void setMessageClass(short messageClass) {
        // Placeholder implementation
    }

    public void setPictureMessage(String url, String description) {
        // Placeholder implementation
    }

    public void setMessage(String message) {
        // Store message text
        if (message != null) {
            this.content = message.getBytes();
        }
    }

    public String getMessage() {
        if (this.content != null) {
            return new String(this.content);
        }
        return null;
    }

    public int getType() {
        // Placeholder implementation - return 0 by default
        return 0;
    }

    public void setMessageId(String messageId) {
        this.id = messageId;
    }

    public String getMessageId() {
        return this.id;
    }
}
