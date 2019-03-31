package com.example.Clinic.models.repositories;

import com.example.Clinic.models.entities.PatientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends CrudRepository<PatientEntity, Integer> {
    boolean existsByLogin(String login);
    Optional<PatientEntity> findByLoginAndPassword(String login, String password);
    PatientEntity findByLogin(String login);
    PatientEntity findByLoginOrEmail(String login, String emial);

}
