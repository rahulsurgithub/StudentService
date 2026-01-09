package com.project.service;

import com.project.dto.StudentRequestDto;
import com.project.entity.Student;
import com.project.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        // Mockito will initialize mocks
    }

    @Test
    void createStudent_shouldSaveAndReturnStudent() {
        StudentRequestDto dto = new StudentRequestDto();
        dto.setStudentName("Test");
        dto.setAddress("Addr");
        dto.setMarks(55);

        Student saved = new Student();
        saved.setStudentid(10);
        saved.setStudentName(dto.getStudentName());
        saved.setAddress(dto.getAddress());
        saved.setMarks(dto.getMarks());

        when(studentRepository.save(any(Student.class))).thenReturn(saved);

        Student result = studentService.createStudent(dto);

        assertNotNull(result);
        assertEquals(10, result.getStudentid());
        assertEquals("Test", result.getStudentName());

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(captor.capture());
        Student passed = captor.getValue();
        assertEquals("Test", passed.getStudentName());
        assertEquals("Addr", passed.getAddress());
        assertEquals(55, passed.getMarks());
    }

    @Test
    void createStudent_nullDto_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudent(null));
        verifyNoInteractions(studentRepository);
    }

    @Test
    void getAllStudents_shouldReturnList() {
        List<Student> list = Arrays.asList(createStudent(1), createStudent(2));
        when(studentRepository.findAll()).thenReturn(list);

        List<Student> result = studentService.getAllStudents();

        assertEquals(2, result.size());
        verify(studentRepository).findAll();
    }

    @Test
    void getStudentById_found_shouldReturn() {
        Student s = createStudent(5);
        when(studentRepository.findById(5)).thenReturn(Optional.of(s));

        Student result = studentService.getStudentById(5);

        assertNotNull(result);
        assertEquals(5, result.getStudentid());
        verify(studentRepository).findById(5);
    }

    @Test
    void getStudentById_notFound_shouldReturnNull() {
        when(studentRepository.findById(99)).thenReturn(Optional.empty());

        Student result = studentService.getStudentById(99);

        assertNull(result);
        verify(studentRepository).findById(99);
    }

    @Test
    void updateStudent_found_shouldUpdateAndReturn() {
        Student existing = createStudent(7);
        existing.setStudentName("Old");
        when(studentRepository.findById(7)).thenReturn(Optional.of(existing));

        StudentRequestDto dto = new StudentRequestDto();
        dto.setStudentName("New");
        dto.setAddress("NewAddr");
        dto.setMarks(99);

        Student saved = new Student();
        saved.setStudentid(7);
        saved.setStudentName("New");
        saved.setAddress("NewAddr");
        saved.setMarks(99);

        when(studentRepository.save(any(Student.class))).thenReturn(saved);

        Student result = studentService.updateStudent(dto, 7);

        assertNotNull(result);
        assertEquals(7, result.getStudentid());
        assertEquals("New", result.getStudentName());

        verify(studentRepository).findById(7);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void updateStudent_notFound_shouldReturnNull() {
        when(studentRepository.findById(123)).thenReturn(Optional.empty());
        StudentRequestDto dto = new StudentRequestDto();
        dto.setStudentName("X");
        dto.setAddress("Y");
        dto.setMarks(10);

        Student result = studentService.updateStudent(dto, 123);

        assertNull(result);
        verify(studentRepository).findById(123);
        verify(studentRepository, times(0)).save(any(Student.class));
    }

    @Test
    void updateStudent_nullDto_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> studentService.updateStudent(null, 1));
        verifyNoInteractions(studentRepository);
    }

    @Test
    void deleteStudentById_found_shouldDeleteAndReturnMessage() {
        when(studentRepository.existsById(2)).thenReturn(true);

        String res = studentService.deleteStudentById(2);

        assertEquals("Student deleted successfully", res);
        verify(studentRepository).deleteById(2);
    }

    @Test
    void deleteStudentById_notFound_shouldReturnNotFoundMessage() {
        when(studentRepository.existsById(999)).thenReturn(false);

        String res = studentService.deleteStudentById(999);

        assertEquals("Student not found", res);
        verify(studentRepository, times(0)).deleteById(anyInt());
    }

    private Student createStudent(int id) {
        Student s = new Student();
        s.setStudentid(id);
        s.setStudentName("Name" + id);
        s.setAddress("Addr" + id);
        s.setMarks(50 + id);
        return s;
    }
}

