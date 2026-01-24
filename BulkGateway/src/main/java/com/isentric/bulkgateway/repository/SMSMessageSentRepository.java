package com.isentric.bulkgateway.repository;

import com.isentric.bulkgateway.model.SMSMessageSent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SMSMessageSentRepository extends JpaRepository<SMSMessageSent, Integer> {
    List<SMSMessageSent> findByGroupId(String groupId);
    SMSMessageSent findByGuid(String guid);
}

