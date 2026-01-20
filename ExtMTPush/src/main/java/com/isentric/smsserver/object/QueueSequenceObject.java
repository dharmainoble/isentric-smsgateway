package com.isentric.smsserver.object;

import java.io.Serializable;

public class QueueSequenceObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String queueId;
    private String queueName;
    private int sequence;
    private String status;
    private String createdDate;
    private String lastProcessedDate;
    private String priority;
    private int retryCount;
    private int maxRetries;

    public QueueSequenceObject() {}

    public String getQueueId() { return queueId; }
    public void setQueueId(String queueId) { this.queueId = queueId; }

    public String getQueueName() { return queueName; }
    public void setQueueName(String queueName) { this.queueName = queueName; }

    public int getSequence() { return sequence; }
    public void setSequence(int sequence) { this.sequence = sequence; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public String getLastProcessedDate() { return lastProcessedDate; }
    public void setLastProcessedDate(String lastProcessedDate) { this.lastProcessedDate = lastProcessedDate; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
}

