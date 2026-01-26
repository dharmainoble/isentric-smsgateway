package com.isentric.bulkgateway.util;

public class UID {
    public static String generateUid() {
        return java.util.UUID.randomUUID().toString();
    }

    public static String genUID() {
        return generateUid();
    }
}
