package msg;

public class UcpStatus {
    private int status;

    public UcpStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getReasonCode() {
        return status;
    }

    public String getMessage() {
        return "Status: " + status;
    }
}
