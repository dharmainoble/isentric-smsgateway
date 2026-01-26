package msg;

public class StatusReportMessage {
    private String messageId;
    private int status;
    private long timestamp;
    private String sender;
    private String recipient;
    private int type = 0;
    private short codingGroup = 0;
    private byte[] bytes;

    public StatusReportMessage() {}

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public short getCodingGroup() {
        return codingGroup;
    }

    public void setCodingGroup(short codingGroup) {
        this.codingGroup = codingGroup;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getID() {
        return messageId;
    }

    public void setID(String id) {
        this.messageId = id;
    }
}
