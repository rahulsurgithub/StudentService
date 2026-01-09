package com.project.controller;

import com.project.dto.StudentRequestDto;
import com.project.entity.Student;
import com.project.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing student endpoints.
 */
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/create")
    public Student createStudent(@RequestBody StudentRequestDto dto) {
        return studentService.createStudent(dto);
    }

    // GET /students -> list all students
    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // GET /students/{studentId} -> get student by id or 404
    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable("studentId") int studentId) {
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    // PUT /students/{studentId} -> update student or 404
    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateStudent(@PathVariable("studentId") int studentId,
                                                 @RequestBody StudentRequestDto dto) {
        Student updated = studentService.updateStudent(dto, studentId);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // DELETE /students/{studentId} -> delete student or 404
    @DeleteMapping("/{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable("studentId") int studentId) {
        String result = studentService.deleteStudentById(studentId);
        if ("Student not found".equals(result)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
