package org.safehaus.uuid;
public class UUID {
    private String value;
    public UUID() {
        this.value = java.util.UUID.randomUUID().toString();
    }
    public String toString() { return value; }
}
