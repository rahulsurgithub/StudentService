package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for incoming student create/update requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequestDto {
    private String studentName;
    private String address;
    private int marks;

    // explicit getters
    public String getStudentName() {
        return studentName;
    }

    public String getAddress() {
        return address;
    }

    public int getMarks() {
        return marks;
    }
}
