package com.rbnk.invenmanagement.repository;

import com.rbnk.invenmanagement.entity.UsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsageLogRepository extends JpaRepository<UsageLog, Long> {
}
