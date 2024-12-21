package com.school.school_management_system.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.stereotype.Service;

@Service
@EnableKafka
public class AttendanceKafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "attendance-notifications";

    // Send a notification message to Kafka
    public void sendAttendanceNotification(String studentName, String courseName, Boolean present) {
        String message = String.format("Attendance for student %s in course %s is marked as %s", studentName, courseName, present ? "Present" : "Absent");
        kafkaTemplate.send(TOPIC, message);  // Send message to Kafka topic
    }
}

