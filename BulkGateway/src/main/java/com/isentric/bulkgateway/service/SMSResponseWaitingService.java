package com.isentric.bulkgateway.service;

import com.isentric.bulkgateway.bg.model.SMSMessageSmpp;
import com.objectxp.msg.SmsMessage;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * Service to handle SMPP responses synchronously.
 * Provides a way to wait for SMPP message responses with timeout.
 */
@Service
public class SMSResponseWaitingService {

    /**
     * Container for SMS response
     */
    public static class SMSResponse {
        public String guid;
        public String smppId;
        public String status; // MESSAGE_SENT, STATE_DELIVERED, etc.
        public String error;
        public long timestamp;
        public int eventType;

        public SMSResponse(String guid) {
            this.guid = guid;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            return "SMSResponse{" +
                    "guid='" + guid + '\'' +
                    ", smppId='" + smppId + '\'' +
                    ", status='" + status + '\'' +
                    ", error='" + error + '\'' +
                    ", eventType=" + eventType +
                    '}';
        }
    }

    // Store responses indexed by GUID
    private final ConcurrentHashMap<String, SMSResponse> responses = new ConcurrentHashMap<>();

    // Completion latches for each message
    private final ConcurrentHashMap<String, CountDownLatch> latches = new ConcurrentHashMap<>();

    /**
     * Register a message as pending response
     */
    public void registerPendingMessage(String guid) {
        latches.put(guid, new CountDownLatch(1));
        responses.put(guid, new SMSResponse(guid));
    }

    /**
     * Wait for message response with timeout
     * @param guid Message GUID
     * @param timeoutSeconds Timeout in seconds
     * @return SMSResponse or null if timeout
     */
    public SMSResponse waitForResponse(String guid, int timeoutSeconds) {
        try {
            CountDownLatch latch = latches.get(guid);
            if (latch == null) {
                registerPendingMessage(guid);
                latch = latches.get(guid);
            }

            // Wait for response or timeout
            boolean completed = latch.await(timeoutSeconds, TimeUnit.SECONDS);

            if (completed) {
                return responses.get(guid);
            } else {
                return null; // Timeout occurred
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /**
     * Wait for delivery report (may come later than sent status)
     * @param guid Message GUID
     * @param timeoutSeconds Timeout in seconds
     * @return SMSResponse with delivery status
     */
    public SMSResponse waitForDeliveryReport(String guid, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeoutMillis = timeoutSeconds * 1000L;

        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            SMSResponse response = responses.get(guid);
            if (response != null && response.status != null &&
                (response.status.contains("DELIVERED") ||
                 response.status.contains("STATE_DELIVERED") ||
                 response.status.contains("NOT_SENT") ||
                 response.status.contains("UNDELIVERABLE"))) {
                return response;
            }
            try {
                Thread.sleep(100); // Poll every 100ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return responses.get(guid);
    }

    /**
     * Signal that a response has been received (called from MOListenerSmpp)
     */
    public void signalResponse(String guid, SMSResponse response) {
        responses.put(guid, response);
        CountDownLatch latch = latches.get(guid);
        if (latch != null) {
            latch.countDown();
        }
    }

    /**
     * Get current response without waiting
     */
    public SMSResponse getResponse(String guid) {
        return responses.get(guid);
    }

    /**
     * Clean up after message processing
     */
    public void cleanup(String guid) {
        responses.remove(guid);
        latches.remove(guid);
    }

    /**
     * Clean up old responses (older than specified minutes)
     */
    public void cleanupOldResponses(int olderThanMinutes) {
        long cutoffTime = System.currentTimeMillis() - (olderThanMinutes * 60 * 1000L);
        responses.entrySet().removeIf(entry -> entry.getValue().timestamp < cutoffTime);
        latches.entrySet().removeIf(entry -> !responses.containsKey(entry.getKey()));
    }

    /**
     * Get size of pending messages
     */
    public int getPendingMessageCount() {
        return latches.size();
    }
}

