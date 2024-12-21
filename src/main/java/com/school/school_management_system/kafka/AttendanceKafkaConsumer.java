//package com.school.school_management_system.kafka;
//
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AttendanceKafkaConsumer {
//
//    // Kafka listener method
//    @KafkaListener(topics = "attendance-notifications", groupId = "attendance-notifications-group")
//    public void listen(String message) {
//        System.out.println("Received Kafka message: " + message);
//        // Here you can implement your logic for notification (e.g., send an email to the student)
//    }
//}