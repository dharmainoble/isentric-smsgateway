package com.isentric.bulkgateway.repository;

import com.isentric.bulkgateway.bg.model.SMSMessageResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SMSMessageRepository  extends JpaRepository<SMSMessageResponse, Integer> {




}
