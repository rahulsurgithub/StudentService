package com.project.controller;

import com.project.dto.StudentRequestDto;
import com.project.entity.Student;
import com.project.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void createStudent_shouldReturnCreatedStudent() throws Exception {
        // build JSON payload manually to avoid ObjectMapper dependency in tests
        String payload = "{\"studentName\":\"Alice\",\"address\":\"Street 1\",\"marks\":90}";

        Student saved = new Student();
        saved.setStudentid(1);
        saved.setStudentName("Alice");
        saved.setAddress("Street 1");
        saved.setMarks(90);

        when(studentService.createStudent(any(StudentRequestDto.class))).thenReturn(saved);

        mockMvc.perform(post("/students/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.studentid").value(1))
                .andExpect(jsonPath("$.studentName").value("Alice"));

        verify(studentService).createStudent(any(StudentRequestDto.class));
    }

    @Test
    void getAllStudents_shouldReturnList() throws Exception {
        List<Student> list = Arrays.asList(
                createStudent(1, "A", "Addr1", 70),
                createStudent(2, "B", "Addr2", 80)
        );
        when(studentService.getAllStudents()).thenReturn(list);

        mockMvc.perform(get("/students/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].studentid").value(1));

        verify(studentService).getAllStudents();
    }

    @Test
    void getStudentById_found_shouldReturnStudent() throws Exception {
        Student s = createStudent(5, "Charlie", "Road", 75);
        when(studentService.getStudentById(5)).thenReturn(s);

        mockMvc.perform(get("/students/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.studentid").value(5))
                .andExpect(jsonPath("$.studentName").value("Charlie"));

        verify(studentService).getStudentById(5);
    }

    @Test
    void getStudentById_notFound_shouldReturn404() throws Exception {
        when(studentService.getStudentById(123)).thenReturn(null);

        mockMvc.perform(get("/students/123"))
                .andExpect(status().isNotFound());

        verify(studentService).getStudentById(123);
    }

    @Test
    void updateStudent_found_shouldReturnUpdated() throws Exception {
        String payload = "{\"studentName\":\"D\",\"address\":\"NewAddr\",\"marks\":88}";

        Student updated = createStudent(7, "D", "NewAddr", 88);
        when(studentService.updateStudent(any(StudentRequestDto.class), eq(7))).thenReturn(updated);

        mockMvc.perform(put("/students/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentid").value(7))
                .andExpect(jsonPath("$.marks").value(88));

        verify(studentService).updateStudent(any(StudentRequestDto.class), eq(7));
    }

    @Test
    void updateStudent_notFound_shouldReturn404() throws Exception {
        String payload = "{\"studentName\":\"E\",\"address\":\"Addr\",\"marks\":50}";
        when(studentService.updateStudent(any(StudentRequestDto.class), eq(99))).thenReturn(null);

        mockMvc.perform(put("/students/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNotFound());

        verify(studentService).updateStudent(any(StudentRequestDto.class), eq(99));
    }

    @Test
    void deleteStudent_found_shouldReturnOkMessage() throws Exception {
        when(studentService.deleteStudentById(2)).thenReturn("Student deleted successfully");

        mockMvc.perform(delete("/students/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Student deleted successfully"));

        verify(studentService).deleteStudentById(2);
    }

    @Test
    void deleteStudent_notFound_shouldReturn404() throws Exception {
        when(studentService.deleteStudentById(999)).thenReturn("Student not found");

        mockMvc.perform(delete("/students/999"))
                .andExpect(status().isNotFound());

        verify(studentService).deleteStudentById(999);
    }

    // helper to create Student instances without relying on Lombok all-args constructor
    private Student createStudent(int id, String name, String address, int marks) {
        Student s = new Student();
        s.setStudentid(id);
        s.setStudentName(name);
        s.setAddress(address);
        s.setMarks(marks);
        return s;
    }
}
