package com.isentric.bulkgateway.repository;

import com.isentric.bulkgateway.model.SMSMessageSmpp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SMSMessageSmppRepository extends JpaRepository<SMSMessageSmpp, Integer> {
}
