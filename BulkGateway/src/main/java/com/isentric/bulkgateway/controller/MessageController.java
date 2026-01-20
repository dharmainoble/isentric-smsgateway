package com.isentric.bulkgateway.controller;
import com.isentric.bulkgateway.dto.ApiResponse;
import com.isentric.bulkgateway.dto.MessageRequest;
import com.isentric.bulkgateway.dto.MessageResponse;
import com.isentric.bulkgateway.service.MessageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final MessageService messageService;
    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    @PostMapping
    public ResponseEntity<ApiResponse<MessageResponse>> createMessage(@Valid @RequestBody MessageRequest request) {
        logger.info("REST request to create message");
        MessageResponse response = messageService.createMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Message created successfully", response));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> getMessageById(@PathVariable Long id) {
        logger.info("REST request to get message by ID: {}", id);
        MessageResponse response = messageService.getMessageById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getAllMessages() {
        logger.info("REST request to get all messages");
        List<MessageResponse> responses = messageService.getAllMessages();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
    @GetMapping("/sender/{sender}")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getMessagesBySender(@PathVariable String sender) {
        logger.info("REST request to get messages by sender: {}", sender);
        List<MessageResponse> responses = messageService.getMessagesBySender(sender);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
    @GetMapping("/recipient/{recipient}")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getMessagesByRecipient(@PathVariable String recipient) {
        logger.info("REST request to get messages by recipient: {}", recipient);
        List<MessageResponse> responses = messageService.getMessagesByRecipient(recipient);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getMessagesByStatus(@PathVariable String status) {
        logger.info("REST request to get messages by status: {}", status);
        List<MessageResponse> responses = messageService.getMessagesByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> updateMessage(@PathVariable Long id, @Valid @RequestBody MessageRequest request) {
        logger.info("REST request to update message with ID: {}", id);
        MessageResponse response = messageService.updateMessage(id, request);
        return ResponseEntity.ok(ApiResponse.success("Message updated successfully", response));
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<MessageResponse>> updateMessageStatus(@PathVariable Long id, @RequestParam String status) {
        logger.info("REST request to update message status with ID: {} to {}", id, status);
        MessageResponse response = messageService.updateMessageStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Message status updated successfully", response));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable Long id) {
        logger.info("REST request to delete message with ID: {}", id);
        messageService.deleteMessage(id);
        return ResponseEntity.ok(ApiResponse.success("Message deleted successfully", null));
    }
    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> sendBulkMessages(@Valid @RequestBody List<MessageRequest> requests) {
        logger.info("REST request to send {} bulk messages", requests.size());
        List<MessageResponse> responses = messageService.sendBulkMessages(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Bulk messages sent successfully", responses));
    }
    @GetMapping("/count/status/{status}")
    public ResponseEntity<ApiResponse<Long>> getMessageCountByStatus(@PathVariable String status) {
        logger.info("REST request to get message count by status: {}", status);
        Long count = messageService.countMessagesByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}
