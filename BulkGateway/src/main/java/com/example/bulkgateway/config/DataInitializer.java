package com.example.bulkgateway.config;

import com.example.bulkgateway.model.Message;
import com.example.bulkgateway.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Data initializer to insert sample data when the application starts.
 * This ensures there is always some test data in the database.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final MessageRepository messageRepository;

    @Autowired
    public DataInitializer(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void run(String... args) {
        logger.info("Initializing sample data...");

        // Check if data already exists
        if (messageRepository.count() == 0) {
            // Create sample messages
            List<Message> sampleMessages = Arrays.asList(
                new Message("john@example.com", "jane@example.com", "Hello Jane! This is a test message.", "PENDING"),
                new Message("admin@bulkgateway.com", "user1@example.com", "Welcome to BulkGateway API!", "SENT"),
                new Message("support@bulkgateway.com", "customer@example.com", "Your request has been received.", "DELIVERED"),
                new Message("marketing@company.com", "leads@example.com", "Check out our new features!", "PENDING"),
                new Message("notification@system.com", "user2@example.com", "Your password has been changed successfully.", "SENT")
            );

            // Save all messages to the database
            List<Message> savedMessages = messageRepository.saveAll(sampleMessages);

            logger.info("Successfully inserted {} sample messages into the database!", savedMessages.size());

            // Log the inserted messages
            savedMessages.forEach(msg ->
                logger.info("Inserted Message - ID: {}, Sender: {}, Recipient: {}, Status: {}",
                    msg.getId(), msg.getSender(), msg.getRecipient(), msg.getStatus())
            );
        } else {
            logger.info("Database already contains {} messages. Skipping initialization.", messageRepository.count());
        }

        logger.info("Data initialization complete!");
    }
}

