package com.example.Clinic.models.repositories;

import com.example.Clinic.models.entities.ExaminationEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExaminationRepository  extends CrudRepository<ExaminationEntity, Integer>{
    List<ExaminationEntity> findByPatientsIdOrderByExaminationTimeAsc(int id);
    List<ExaminationEntity> findByPatientsIdAndExaminationTypeOrderByExaminationTimeDesc(int id, String type);
    //@Query(value = "SELECT * FROM examination WHERE patient_id = ?1", nativeQuery = true)
    //ExaminationEntity findByPatientId(int id);
    @Query(value = "SELECT * FROM examination WHERE exam_type=?1 AND exam_time BETWEEN ?2 AND ?3 ORDER BY exam_time ASC",nativeQuery = true)
    //@Query(value = "SELECT * FROM examination WHERE exam_type=?1 AND exam_time BETWEEN ?2 AND ?3",nativeQuery = true)
    List<ExaminationEntity> findByDatesAndType(String examType ,String date1, String date2);
}
