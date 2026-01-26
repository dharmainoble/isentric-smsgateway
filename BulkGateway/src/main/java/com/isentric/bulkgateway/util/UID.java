package com.isentric.bulkgateway.util;

/**
 * @deprecated Use {@link UidUtil} instead. This class is retained for backward compatibility.
 */
@Deprecated
public class UID {
    /**
     * @deprecated Use {@link UidUtil#generateUid()} instead.
     */
    @Deprecated
    public static String generateUid() {
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * @deprecated Use {@link UidUtil#generateUid()} instead.
     */
    @Deprecated
    public static String genUID() {
        return generateUid();
    }
}
