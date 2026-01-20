package com.isentric.smsserver.repository.avatar;

import com.isentric.smsserver.model.avatar.ExtMtPushReceive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExtMtPushReceiveRepository extends JpaRepository<ExtMtPushReceive, Long> {
    
    Optional<ExtMtPushReceive> findByMtid(String mtid);
    
    List<ExtMtPushReceive> findByCustidAndReceivedDateBetween(String custid, LocalDateTime start, LocalDateTime end);
    
    List<ExtMtPushReceive> findByProcessFlag(String processFlag);
    
    long countByProcessFlag(String processFlag);
}

