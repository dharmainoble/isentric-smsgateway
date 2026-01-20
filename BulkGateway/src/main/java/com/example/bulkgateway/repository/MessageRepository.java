package com.example.bulkgateway.repository;
import com.example.bulkgateway.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySender(String sender);
    List<Message> findByRecipient(String recipient);
    List<Message> findByStatus(String status);
    List<Message> findBySenderAndStatus(String sender, String status);
    List<Message> findByCreatedAtAfter(LocalDateTime dateTime);
    List<Message> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    @Query("SELECT COUNT(m) FROM Message m WHERE m.status = :status")
    Long countByStatus(@Param("status") String status);
    @Query("SELECT m FROM Message m WHERE m.status = 'PENDING' ORDER BY m.createdAt ASC")
    List<Message> findPendingMessages();
}
