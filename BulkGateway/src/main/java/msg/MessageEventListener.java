package msg;
import com.objectxp.msg.MessageEvent;

import java.util.EventListener;
public interface MessageEventListener extends EventListener {
    void onMessageReceived(MessageEvent event);
    void onMessageSent(MessageEvent event);
}
