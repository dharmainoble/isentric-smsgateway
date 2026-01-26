package msg;

import com.objectxp.msg.MessageEventListener;
import com.objectxp.msg.MessageException;
import com.objectxp.msg.SmsMessage;
import com.objectxp.msg.SmsService;

import java.util.ArrayList;
import java.util.List;

public class SmppSmsService implements SmsService {
    private List<com.objectxp.msg.MessageEventListener> listeners = new ArrayList<>();
    private boolean connected = false;
    private boolean initialized = false;
    private int windowSize = 10;
    private int pendingMessages = 0;
    private String serviceName = "SMPP";

    @Override
    public void sendSms(SmsMessage message) throws MessageException {}

    @Override
    public void sendMessage(Object message) throws MessageException {}

    @Override
    public void addMessageEventListener(com.objectxp.msg.MessageEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeMessageEventListener(MessageEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void connect() throws MessageException {
        this.connected = true;
        this.initialized = true;
    }

    @Override
    public void disconnect() throws MessageException {
        this.connected = false;
    }

    @Override
    public void startReceiving() {}

    @Override
    public void stopReceiving() {}

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public int getWindowSize() {
        return windowSize;
    }

    @Override
    public int getNumberOfPendingMessages() {
        return pendingMessages;
    }

    @Override
    public boolean isAlive() {
        return connected;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void destroy() throws MessageException {
        this.connected = false;
        this.initialized = false;
    }
}
