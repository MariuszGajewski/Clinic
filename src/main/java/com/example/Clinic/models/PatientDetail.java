package com.example.Clinic.models;

import com.example.Clinic.models.entities.PatientEntity;
import com.example.Clinic.models.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class PatientDetail implements UserDetailsService {

    final
    PatientRepository patientRepository;

    @Autowired
    public PatientDetail(PatientRepository patientRepository) {
        System.out.println("Sfsfsf");
        this.patientRepository = patientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println(s);
        PatientEntity patientEntity=patientRepository.findByLogin(s);
        if(patientEntity == null) {
            throw new UsernameNotFoundException("User" + s +" not found");
        }
        GrantedAuthority authority=new SimpleGrantedAuthority("patient");

        return new User(patientEntity.getLogin(), patientEntity.getPassword(), Collections.singletonList(authority));
    }
}
