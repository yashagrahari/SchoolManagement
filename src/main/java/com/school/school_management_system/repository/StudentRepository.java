package com.school.school_management_system.repository;

import com.school.school_management_system.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Custom query methods can be added if required
    Optional<Student> findByEmail(String email);

    Student findById(int studentId);

}
