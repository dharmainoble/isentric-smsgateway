// 
// Decompiled by Procyon v0.5.32
// 

package com.isentric.bulkgateway.utility;

import com.fasterxml.uuid.Generators;

import java.util.UUID;

public class GuidUtil
{
    public static String createGUID() {
        // Use the time-based UUID generator from java-uuid-generator library
        UUID uuid = Generators.timeBasedGenerator().generate();
        return uuid.toString();
    }
}
