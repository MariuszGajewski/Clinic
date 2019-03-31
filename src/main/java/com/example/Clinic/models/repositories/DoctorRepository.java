package com.example.Clinic.models.repositories;

import com.example.Clinic.models.entities.DoctorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends CrudRepository<DoctorEntity, Integer> {
    Page<DoctorEntity> findAllByOrderByIdDesc(Pageable pageable);
}
