package com.isentric.bulkgateway.bc.repository;

import com.isentric.bulkgateway.bc.model.BulkDestinationSMS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BulkDestinationSMSRepository extends JpaRepository<BulkDestinationSMS, Long> {

}
