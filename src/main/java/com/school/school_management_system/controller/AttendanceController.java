package com.school.school_management_system.controller;


import com.school.school_management_system.models.Attendance;
import com.school.school_management_system.models.Course;
import com.school.school_management_system.models.Student;
import com.school.school_management_system.kafka.AttendanceKafkaProducer;
import com.school.school_management_system.repository.AttendanceRepository;
import com.school.school_management_system.repository.CourseRepository;
import com.school.school_management_system.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AttendanceKafkaProducer kafkaProducer;  // Kafka producer for sending notifications

    // Mark attendance for students in a course
    @PostMapping("/mark")
    public ResponseEntity<?> markAttendance(@RequestParam Long courseId, @RequestParam Boolean present,
                                            @RequestParam Long teacherId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

        // Iterate over all students in the course and mark attendance
        for (Student student : course.getStudents()) {
            Attendance attendance = new Attendance();
            attendance.setStudent(student);
            attendance.setCourse(course);
            attendance.setTeacher(course.getTeacher());
            attendance.setDate(LocalDate.now());
            attendance.setPresent(present);

            // Save attendance record
            attendanceRepository.save(attendance);

            // Send a notification via Kafka
            kafkaProducer.sendAttendanceNotification(student.getName(), course.getName(), present);
        }

        return ResponseEntity.ok("Attendance marked and notifications sent for all students in the course.");
    }

    // Get attendance records for a course on a specific date
    @GetMapping("/course/{courseId}/date/{date}")
    public ResponseEntity<List<Attendance>> getAttendanceByCourseAndDate(@PathVariable Long courseId,
                                                                         @PathVariable String date) {
        LocalDate attendanceDate = LocalDate.parse(date);
        List<Attendance> attendanceRecords = attendanceRepository.findByCourseIdAndDate(courseId, attendanceDate);

        return ResponseEntity.ok(attendanceRecords);
    }

    // Get attendance for a specific student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudent(@PathVariable Long studentId) {
        List<Attendance> attendanceRecords = attendanceRepository.findByStudentId(studentId);
        return ResponseEntity.ok(attendanceRecords);
    }
}

