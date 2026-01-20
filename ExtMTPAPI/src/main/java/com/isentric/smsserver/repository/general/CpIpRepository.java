package com.isentric.smsserver.repository.general;

import com.isentric.smsserver.model.general.CpIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CpIpRepository extends JpaRepository<CpIp, Long> {
    
    Optional<CpIp> findByShortcodeAndCpidentityAndCpIp(String shortcode, String cpidentity, String cpIp);
}

