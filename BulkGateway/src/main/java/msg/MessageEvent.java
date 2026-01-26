package msg;

import com.objectxp.msg.Message;

import java.util.EventObject;

public class MessageEvent extends EventObject {
    private com.objectxp.msg.Message message;
    private int type = 0;

    public MessageEvent(Object source, com.objectxp.msg.Message message) {
        super(source);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
