package com.example.Clinic.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "patient")
@Getter
@Setter
@NoArgsConstructor
public class PatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String login;
    private String name;
    private String surname;
    private String password;
    private String email;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "patients")
    List<ExaminationEntity> examinations;

}
