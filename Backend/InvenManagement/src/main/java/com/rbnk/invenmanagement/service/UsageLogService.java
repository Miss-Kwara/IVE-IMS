package com.rbnk.invenmanagement.service;

import com.rbnk.invenmanagement.entity.UsageLog;
import com.rbnk.invenmanagement.entity.Booking;
import com.rbnk.invenmanagement.repository.UsageLogRepository;
import com.rbnk.invenmanagement.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsageLogService {

    private final UsageLogRepository usageLogRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public UsageLogService(UsageLogRepository usageLogRepository, BookingRepository bookingRepository) {
        this.usageLogRepository = usageLogRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Get all usage logs.
     */
    public List<UsageLog> getAllUsageLogs() {
        return usageLogRepository.findAll();
    }

    /**
     * Get usage logs by booking ID.
     */
    public Optional<UsageLog> getUsageLogByBookingId(Long bookingId) {
        return usageLogRepository.findByBookingId(bookingId);
    }

    /**
     * Create a new usage log entry.
     */
    public UsageLog createUsageLog(Long bookingId, LocalDateTime startTime, LocalDateTime endTime, String notes) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);

        if (bookingOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid booking ID");
        }

        UsageLog usageLog = new UsageLog();
        usageLog.setBooking(bookingOpt.get());
        usageLog.setStartTime(startTime);
        usageLog.setEndTime(endTime);
        usageLog.setNotes(notes);

        return usageLogRepository.save(usageLog);
    }

    /**
     * Update an existing usage log.
     */
    public UsageLog updateUsageLog(Long usageLogId, LocalDateTime startTime, LocalDateTime endTime, String notes) {
        Optional<UsageLog> usageLogOpt = usageLogRepository.findById(usageLogId);

        if (usageLogOpt.isEmpty()) {
            throw new IllegalArgumentException("Usage log not found");
        }

        UsageLog usageLog = usageLogOpt.get();
        usageLog.setStartTime(startTime);
        usageLog.setEndTime(endTime);
        usageLog.setNotes(notes);

        return usageLogRepository.save(usageLog);
    }

    /**
     * Delete a usage log entry.
     */
    public void deleteUsageLog(Long usageLogId) {
        usageLogRepository.deleteById(usageLogId);
    }
}
