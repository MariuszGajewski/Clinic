package com.example.Clinic.models.services;

import com.example.Clinic.models.Utils;
import com.example.Clinic.models.entities.PatientEntity;
import com.example.Clinic.models.forms.RegisterForm;
import com.example.Clinic.models.repositories.ExaminationRepository;
import com.example.Clinic.models.repositories.PatientRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PatientService {
    public enum userOperationStatus {
        OK, BUSY_LOGIN, ERROR, INCORRECT_LOGIN_DATA;
    }

    @Getter @Setter
    private boolean isLogin;
    @Getter @Setter
    private PatientEntity patientEntity;
    @Getter
    private String randomPassword;
    final
    PatientRepository patientRepository;
    final ExaminationRepository examinationRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository, ExaminationRepository examinationRepository) {
        this.patientRepository = patientRepository;
        this.examinationRepository = examinationRepository;
    }

    public userOperationStatus registerNewPatient(RegisterForm registerForm) {

        if(busyLoginCheck(registerForm.getLogin())) {
            return userOperationStatus.BUSY_LOGIN;
        }
        Utils utils= new Utils();
        PatientEntity patient=new PatientEntity();
        patient.setLogin(registerForm.getLogin());
        patient.setName(registerForm.getName());
        patient.setSurname(registerForm.getSurname());
        patient.setEmail(registerForm.getEmail());
        patient.setPassword(
                utils.hashPassword(
                        registerForm.getPassword()
                ));
        patient=patientRepository.save(patient);
        if(patient == null) {
            return  userOperationStatus.ERROR;
        }
        return userOperationStatus.OK;
    }

    private boolean busyLoginCheck(String login) {
        return patientRepository.existsByLogin(login);
    }

    private String hashPassword(String password) {
        ShaPasswordEncoder encoder=new ShaPasswordEncoder();
        return encoder.encodePassword(password,"");
    }

    public PatientEntity passwordReset(String login, String password){
        Utils utils= new Utils();
        randomPassword = utils.passwordRandomizer();
        PatientEntity patient = patientRepository.findByLoginOrEmail(login,password);
        patient.setPassword(utils.hashPassword(randomPassword));
        patientRepository.save(patient);
        System.out.println("nowe hasło: " + randomPassword);
        return patient;
    }
    public userOperationStatus changePassword(String newPassword){
        Utils utils = new Utils();
        patientEntity.setPassword(utils.hashPassword(newPassword));
        patientEntity = patientRepository.save(patientEntity);
        if(patientEntity == null){
            return  userOperationStatus.ERROR;
        }
        return userOperationStatus.OK;
    }

    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        return "redirect:/patientPanel?logout";
    }
}
