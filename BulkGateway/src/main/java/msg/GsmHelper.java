package msg;

import com.objectxp.msg.GsmStatus;

public class GsmHelper {
    public static com.objectxp.msg.GsmStatus createStatus(int code) {
        return new GsmStatus(code);
    }

    public static byte[] decodeIA5(String text) {
        // Convert text to byte array
        if (text == null) {
            return new byte[0];
        }
        return text.getBytes();
    }
}
