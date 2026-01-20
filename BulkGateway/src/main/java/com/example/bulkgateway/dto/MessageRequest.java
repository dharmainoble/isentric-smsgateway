package com.example.bulkgateway.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class MessageRequest {
    @NotBlank(message = "Sender is required")
    private String sender;
    @NotBlank(message = "Recipient is required")
    private String recipient;
    @NotBlank(message = "Content is required")
    @Size(max = 1000, message = "Content must not exceed 1000 characters")
    private String content;
    public MessageRequest() {}
    public MessageRequest(String sender, String recipient, String content) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
    }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
