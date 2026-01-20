package com.example.bulkgateway.service.impl;
import com.example.bulkgateway.dto.MessageRequest;
import com.example.bulkgateway.dto.MessageResponse;
import com.example.bulkgateway.model.Message;
import com.example.bulkgateway.repository.MessageRepository;
import com.example.bulkgateway.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
public class MessageServiceImpl implements MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
    private final MessageRepository messageRepository;
    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    @Override
    public MessageResponse createMessage(MessageRequest request) {
        logger.info("Creating new message from {} to {}", request.getSender(), request.getRecipient());
        Message message = new Message();
        message.setSender(request.getSender());
        message.setRecipient(request.getRecipient());
        message.setContent(request.getContent());
        message.setStatus("PENDING");
        Message savedMessage = messageRepository.save(message);
        logger.info("Message created with ID: {}", savedMessage.getId());
        return mapToResponse(savedMessage);
    }
    @Override
    @Transactional(readOnly = true)
    public MessageResponse getMessageById(Long id) {
        logger.info("Fetching message with ID: {}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with ID: " + id));
        return mapToResponse(message);
    }
    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> getAllMessages() {
        logger.info("Fetching all messages");
        return messageRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> getMessagesBySender(String sender) {
        logger.info("Fetching messages by sender: {}", sender);
        return messageRepository.findBySender(sender).stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> getMessagesByRecipient(String recipient) {
        logger.info("Fetching messages by recipient: {}", recipient);
        return messageRepository.findByRecipient(recipient).stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> getMessagesByStatus(String status) {
        logger.info("Fetching messages by status: {}", status);
        return messageRepository.findByStatus(status).stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    @Override
    public MessageResponse updateMessage(Long id, MessageRequest request) {
        logger.info("Updating message with ID: {}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with ID: " + id));
        message.setSender(request.getSender());
        message.setRecipient(request.getRecipient());
        message.setContent(request.getContent());
        Message updatedMessage = messageRepository.save(message);
        logger.info("Message updated with ID: {}", updatedMessage.getId());
        return mapToResponse(updatedMessage);
    }
    @Override
    public MessageResponse updateMessageStatus(Long id, String status) {
        logger.info("Updating message status with ID: {} to {}", id, status);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with ID: " + id));
        message.setStatus(status);
        Message updatedMessage = messageRepository.save(message);
        logger.info("Message status updated with ID: {}", updatedMessage.getId());
        return mapToResponse(updatedMessage);
    }
    @Override
    public void deleteMessage(Long id) {
        logger.info("Deleting message with ID: {}", id);
        if (!messageRepository.existsById(id)) {
            throw new RuntimeException("Message not found with ID: " + id);
        }
        messageRepository.deleteById(id);
        logger.info("Message deleted with ID: {}", id);
    }
    @Override
    public List<MessageResponse> sendBulkMessages(List<MessageRequest> requests) {
        logger.info("Sending {} bulk messages", requests.size());
        List<MessageResponse> responses = new ArrayList<>();
        for (MessageRequest request : requests) {
            try {
                MessageResponse response = createMessage(request);
                responses.add(response);
            } catch (Exception e) {
                logger.error("Failed to create message for recipient: {}", request.getRecipient(), e);
            }
        }
        logger.info("Successfully sent {} out of {} messages", responses.size(), requests.size());
        return responses;
    }
    @Override
    @Transactional(readOnly = true)
    public Long countMessagesByStatus(String status) {
        logger.info("Counting messages by status: {}", status);
        return messageRepository.countByStatus(status);
    }
    private MessageResponse mapToResponse(Message message) {
        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setSender(message.getSender());
        response.setRecipient(message.getRecipient());
        response.setContent(message.getContent());
        response.setStatus(message.getStatus());
        response.setCreatedAt(message.getCreatedAt());
        response.setUpdatedAt(message.getUpdatedAt());
        return response;
    }
}
