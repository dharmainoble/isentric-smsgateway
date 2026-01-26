package msg;

import com.objectxp.msg.MessageEventListener;
import com.objectxp.msg.MessageException;
import com.objectxp.msg.SmsMessage;

public interface SmsService {
    void sendSms(SmsMessage message) throws MessageException;
    void sendMessage(Object message) throws MessageException;
    void addMessageEventListener(com.objectxp.msg.MessageEventListener listener);
    void removeMessageEventListener(MessageEventListener listener);
    void connect() throws MessageException;
    void disconnect() throws MessageException;
    void startReceiving() throws MessageException;
    void stopReceiving() throws MessageException;
    String getServiceName();
    int getWindowSize();
    int getNumberOfPendingMessages();
    boolean isAlive();
    boolean isConnected();
    boolean isInitialized();
    void destroy() throws MessageException;
}
