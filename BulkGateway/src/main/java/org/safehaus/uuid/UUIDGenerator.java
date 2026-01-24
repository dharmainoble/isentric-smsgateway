package org.safehaus.uuid;

public class UUIDGenerator {
    private static UUIDGenerator instance = new UUIDGenerator();

    public static UUIDGenerator getInstance() {
        return instance;
    }

    public String generateTimeBasedUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    public String generateRandomBasedUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    public static String generateTimeBasedUUID_static() {
        return java.util.UUID.randomUUID().toString();
    }

    public static String generateRandomBasedUUID_static() {
        return java.util.UUID.randomUUID().toString();
    }
}
