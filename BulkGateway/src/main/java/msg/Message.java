package msg;

public interface Message {
    String getId();
    void setId(String id);
    String getSender();
    void setSender(String sender);
    String getMessage();
    void setMessage(String message);
}
