package com.isentric.smsserver.repository.avatar;

import com.isentric.smsserver.model.avatar.ExtMtId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExtMtIdRepository extends JpaRepository<ExtMtId, Long> {
    
    Optional<ExtMtId> findByMtidAndCustid(String mtid, String custid);
    
    boolean existsByMtidAndCustid(String mtid, String custid);
}

