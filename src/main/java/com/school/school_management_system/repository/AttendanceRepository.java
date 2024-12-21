package com.school.school_management_system.repository;

import com.school.school_management_system.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Fetch attendance records for a course on a specific date
    List<Attendance> findByCourseIdAndDate(Long courseId, LocalDate date);

    // Fetch all attendance records for a specific student
    List<Attendance> findByStudentId(Long studentId);

    // Fetch all attendance records for a course
    List<Attendance> findByCourseId(Long courseId);
}
