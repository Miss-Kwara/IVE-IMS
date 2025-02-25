package com.rbnk.invenmanagement.service;

import com.rbnk.invenmanagement.entity.Equipment;
import com.rbnk.invenmanagement.entity.User;
import com.rbnk.invenmanagement.repository.BookingRepository;
import com.rbnk.invenmanagement.repository.EquipmentRepository;
import com.rbnk.invenmanagement.repository.UserRepository;
import com.rbnk.invenmanagement.entity.Booking;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, EquipmentRepository equipmentRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.equipmentRepository = equipmentRepository;
        this.userRepository = userRepository;
    }

    public Booking createBooking(Long equipmentId, Long bookerId, LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime) || endTime.isEqual(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        if (bookingRepository.existsOverlappingBooking(equipmentId, startTime, endTime)) {
            throw new IllegalStateException("Booking overlaps with an existing one");
        }

        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Booking booking = new Booking(equipment, booker, startTime, endTime, "CONFIRMED");
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsForEquipment(Long equipmentId) {
        return bookingRepository.findByEquipmentId(equipmentId);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
    }

    public Booking updateBooking(Long id, LocalDateTime startTime, LocalDateTime endTime) {
        Booking booking = getBookingById(id);

        if (endTime.isBefore(startTime) || endTime.isEqual(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        if (bookingRepository.existsOverlappingBooking(booking.getEquipment().getId(), startTime, endTime)) {
            throw new IllegalStateException("Updated booking overlaps with an existing one");
        }

        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        return bookingRepository.save(booking);
    }

    public void cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        bookingRepository.delete(booking);
    }
}

