package com.example.bulkgateway.service;
import com.example.bulkgateway.dto.MessageRequest;
import com.example.bulkgateway.dto.MessageResponse;
import java.util.List;
public interface MessageService {
    MessageResponse createMessage(MessageRequest request);
    MessageResponse getMessageById(Long id);
    List<MessageResponse> getAllMessages();
    List<MessageResponse> getMessagesBySender(String sender);
    List<MessageResponse> getMessagesByRecipient(String recipient);
    List<MessageResponse> getMessagesByStatus(String status);
    MessageResponse updateMessage(Long id, MessageRequest request);
    MessageResponse updateMessageStatus(Long id, String status);
    void deleteMessage(Long id);
    List<MessageResponse> sendBulkMessages(List<MessageRequest> requests);
    Long countMessagesByStatus(String status);
}
