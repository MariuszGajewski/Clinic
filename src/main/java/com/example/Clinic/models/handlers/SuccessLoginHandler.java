package com.example.Clinic.models.handlers;

import com.example.Clinic.models.repositories.PatientRepository;
import com.example.Clinic.models.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SuccessLoginHandler implements AuthenticationSuccessHandler {

   final
   PatientRepository patientRepository;
   final
   PatientService patientService;

    @Autowired
    public SuccessLoginHandler(PatientRepository patientRepository, PatientService patientService) {
        this.patientRepository = patientRepository;
        this.patientService = patientService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        System.out.println(patientRepository.findByLogin(authentication.getName()));
        System.out.println(authentication.getName());
        patientService.setPatientEntity(patientRepository.findByLogin(authentication.getName()));
        System.out.println(authentication.getName());
        httpServletResponse.sendRedirect("/patientZone");
    }
}
