package com.project.service;

import com.project.dto.StudentRequestDto;
import com.project.entity.Student;
import com.project.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service that handles student business logic.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Create and save a Student from a request DTO.
     * @param dto incoming student data
     * @return saved Student
     */
    public Student createStudent(StudentRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("StudentRequestDto must not be null");
        }
        Student student = toEntity(dto);
        return studentRepository.save(student);
    }

    private Student toEntity(StudentRequestDto dto) {
        // use no-arg constructor and setters to avoid depending on Lombok-generated constructors
        Student student = new Student();
        student.setStudentName(dto.getStudentName());
        student.setAddress(dto.getAddress());
        student.setMarks(dto.getMarks());
        return student;
    }

    /**
     * Return all students.
     * @return list of students
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Return a student by id.
     * @param studentId id of the student to retrieve
     * @return Student if found, otherwise null
     */
    public Student getStudentById(int studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }

    /**
     * Update an existing student with data from the DTO.
     * @param dto incoming data to apply
     * @param studentId id of the student to update
     * @return updated Student if found, otherwise null
     */
    public Student updateStudent(StudentRequestDto dto, int studentId) {
        if (dto == null) {
            throw new IllegalArgumentException("StudentRequestDto must not be null");
        }
        return studentRepository.findById(studentId).map(existing -> {
            existing.setStudentName(dto.getStudentName());
            existing.setAddress(dto.getAddress());
            existing.setMarks(dto.getMarks());
            return studentRepository.save(existing);
        }).orElse(null);
    }

    /**
     * Delete a student by id.
     * @param studentId id of the student to delete
     * @return success message when deleted, or not-found message
     */
    public String deleteStudentById(int studentId) {
        if (!studentRepository.existsById(studentId)) {
            return "Student not found";
        }
        studentRepository.deleteById(studentId);
        return "Student deleted successfully";
    }

}
