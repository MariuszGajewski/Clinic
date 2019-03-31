package com.example.Clinic.models.forms;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class ExaminationForm {
    private LocalDate examinationDate;
    private LocalDateTime examinationTime;
    private LocalTime examinationHour;
    private String examinationType;
    private String examinationNote;
}
