package msg.ota;
public class OTAMessage {
    private String id;
    private byte[] content;
    public OTAMessage() {}
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public byte[] getContent() { return content; }
    public void setContent(byte[] content) { this.content = content; }
}
