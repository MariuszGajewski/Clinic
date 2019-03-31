package com.example.Clinic.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Entity
@Table(name = "examination")
@Setter
@Getter
@NoArgsConstructor
public class ExaminationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "exam_time")
    private LocalDateTime examinationTime;
    @Column(name = "exam_type")
    private String examinationType;
    @Column(name = "exam_note")
    private String examinationNote;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patients;


    public Date getDateAsJavaUtil(){
        return Date.from(examinationTime.toInstant(ZoneOffset.UTC));
    }
}
