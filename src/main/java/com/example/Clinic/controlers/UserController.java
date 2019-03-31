package com.example.Clinic.controlers;

import com.example.Clinic.models.entities.ExaminationEntity;
import com.example.Clinic.models.forms.ExaminationForm;
import com.example.Clinic.models.forms.PasswordChangeForm;
import com.example.Clinic.models.services.ExaminationService;
import com.example.Clinic.models.services.PatientService;
import com.example.Clinic.models.repositories.ExaminationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class UserController {
    private enum examinationTypeEnum {USG, RTG, TK};
    final
    ExaminationService examinationService;
    final PatientService patientService;
    final ExaminationRepository examinationRepository;

    @Autowired
    public UserController(ExaminationService examinationService, PatientService patientService, ExaminationRepository examinationRepository) {
        this.examinationService = examinationService;
        this.patientService = patientService;
        this.examinationRepository = examinationRepository;
    }

    @ModelAttribute
    public Model startingModel(Model model) {
        model.addAttribute("firstDayOfMonth", examinationService.getFirstDayOfMonth());
        model.addAttribute("lastDayOfMonth", examinationService.getLastDayOfMonth());
        model.addAttribute("monthLength", examinationService.getMonthLength());
        model.addAttribute("numberOfWeeks", examinationService.getNumberOfWeeks());
        model.addAttribute("monthName", examinationService.getNameOfMonth());
        model.addAttribute("daysName", examinationService.getNameOfDays());
        model.addAttribute("monthNumber", examinationService.getMonthNumber());
        model.addAttribute("blockedDays", examinationService.getBlockedDays());
        model.addAttribute("userName",patientService.getPatientEntity().getName());
        model.addAttribute("userLastName", patientService.getPatientEntity().getSurname());
        model.addAttribute("userEmail",patientService.getPatientEntity().getEmail());
        return model;
    }

    @GetMapping("/patientZone")
    public String patientZone(Model model) {
        patientService.getPatientEntity().setExaminations(examinationRepository.findByPatientsIdOrderByExaminationTimeAsc(patientService.getPatientEntity().getId()));
        model.addAttribute("examinationList",patientService.getPatientEntity().getExaminations());
        return "/patientZone";
    }

    @GetMapping("/patientZone/{month}/{examType}")
    public String getMonth(@PathVariable("month") int monthNumber, @PathVariable("examType") String examinationType,
                           Model model) {
        System.out.println("z tempate: " + monthNumber);
        int newMontNumber=monthNumber - LocalDate.now().getMonthValue();
        System.out.println("nowy miesiac: "+newMontNumber);
        examinationService.getCalendarMonth(newMontNumber, examinationType);
        model.addAttribute("typeOfExam",examinationType);
        startingModel(model);
        return "/patientZone";
    }

    @GetMapping("/patientZone/change/{examinationId}")
    public String changeExamination(@PathVariable("examinationId") int examinationId, RedirectAttributes redirectAttributes, Model model) {
        System.out.println("zmiana badania");
        ExaminationEntity examinationToChange=patientService.getPatientEntity().getExaminations().stream()
                .filter(s->s.getId() == examinationId &&
                        s.getPatients().getId() == patientService.getPatientEntity().getId())
                .findAny().get();
        String examinationType = examinationToChange.getExaminationType();
        System.out.println("znaleziony typ badania" + examinationType);
        examinationRepository.delete(examinationToChange);
        redirectAttributes.addFlashAttribute("typeOfExam", examinationType);
        examinationService.getCalendarMonth(0,examinationType);
        startingModel(model);
        return "redirect:/patientZone";
    }

    @GetMapping("/patientZone/delete/{examinationId}")
    public String deleteExamination(@PathVariable("examinationId") int examinationId, Model model) {
        ExaminationEntity examinationToDelete=patientService.getPatientEntity().getExaminations().stream()
                .filter(s->s.getId() == examinationId &&
                        s.getPatients().getId() == patientService.getPatientEntity().getId())
                .findAny().get();
        examinationRepository.delete(examinationToDelete);
       // patientService.getPatientEntity().setExaminations(examinationRepository.findByPatientsIdOrderByIdDesc(patientService.getPatientEntity().getId()));
        return "redirect:/patientZone";
    }

    @PostMapping("/patientZone/type")
    public String setExaminationType(@RequestParam("examType") String examinationType,
                                     RedirectAttributes redirectAttributes, Model model) {
        System.out.println("typ badani: " + examinationType);
        redirectAttributes.addFlashAttribute("typeOfExam", examinationType);
        examinationService.getCalendarMonth(0,examinationType);
        startingModel(model);
        return "redirect:/patientZone";
    }

    @GetMapping("patientZone/{examType}")
    public String setExaminationTypeByGet(@PathVariable("examType") String examinationType,
                                     RedirectAttributes redirectAttributes, Model model) {
        boolean corectType=false;
        for (examinationTypeEnum typeEnum : examinationTypeEnum.values()) {
            if(typeEnum.toString().equals(examinationType)){
                corectType=true;
            }
        }
        if(!corectType){
            return "redirect:/index";
        }
        redirectAttributes.addFlashAttribute("typeOfExam", examinationType);
        examinationService.getCalendarMonth(0,examinationType);
        startingModel(model);
        return "redirect:/patientZone";
    }

    @GetMapping("/patientZone/exam/{dayValue}/{examType}")
    public String examinationSecondPage(@PathVariable("dayValue") int dayValue,
                                        @PathVariable("examType") String examinationTime,
                                        RedirectAttributes redirectAttributes){
        LocalDate examinationDate= examinationService.getSelectedDate().plusDays(dayValue- examinationService.getSelectedDate().getDayOfMonth());
        ExaminationForm examinationForm=new ExaminationForm();
        System.out.println("wybrana data: " + examinationDate);
        examinationForm.setExaminationDate(examinationDate);
        examinationForm.setExaminationType(examinationTime);
        examinationForm.setExaminationTime(null);
        redirectAttributes.addFlashAttribute("examinationForm", examinationForm);
        redirectAttributes.addFlashAttribute("typeOfExam",examinationTime);
        examinationService.getAvailableHoursAtDay(examinationDate);
        redirectAttributes.addFlashAttribute("availableHours" , examinationService.getAvailableHoursAtDay(examinationDate));
        return "redirect:/patientZone";

    }
    @PostMapping("/registerExamination")
    public String registerExamination (@ModelAttribute("examinationForm") ExaminationForm examinationForm, RedirectAttributes redirectAttributes ){
        System.out.println("Badanie z formularza:");
        System.out.println("data: " + examinationForm.getExaminationDate());
        System.out.println("czas: " + examinationForm.getExaminationHour());
        System.out.println(examinationForm.getExaminationType());
        LocalDateTime examinationTime=LocalDateTime.of(examinationForm.getExaminationDate(),examinationForm.getExaminationHour());
        examinationForm.setExaminationTime(examinationTime);
        System.out.println("czas badania: " + examinationTime);
        System.out.println();
        PatientService.userOperationStatus operationStatus= examinationService.saveExamination(examinationForm);
        if(operationStatus == PatientService.userOperationStatus.OK ) {
            redirectAttributes.addFlashAttribute("status","Rejestracja badania zakończona, prosimy o pojawienie się w naszej przychodnie 15 min przed ustalonym terminem");
            System.out.println("ok");
        }
        else {
            redirectAttributes.addFlashAttribute("status", "Upss, coś poszło nie tak. Prosimy spróbować ponownie za kilka chwil");
            System.out.println("cos nie tak");
        }
        return "redirect:/patientZone";

    }
    @GetMapping("patientZone/changePassword")
    public String changePassword(Model model){
        model.addAttribute("passwordForm",new PasswordChangeForm());
        return "/changeUserPassword";
    }
    @PostMapping("/changeUserPassword")
    public String changeUserPassword (@Valid @ModelAttribute("passwordForm") PasswordChangeForm passwordForm,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      HttpServletRequest request, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "redirect:/changeUserPassword";
        }
        else if(!passwordForm.getPassword().equals(passwordForm.getRepeatPassword())){
            redirectAttributes.addFlashAttribute("result", "podane hasła są różne");
            return "redirect:/changeUserPassword";
        }
        PatientService.userOperationStatus userOperationStatus = patientService.changePassword(passwordForm.getPassword());
        if(userOperationStatus.equals(PatientService.userOperationStatus.OK)){
            patientService.logout(request,response);
            return "redirect:/patientPanel?logout";
        }
        redirectAttributes.addFlashAttribute("result","nie udało się zmienić hasła, proszę spróbować ponownie");
        return "redirect:/patientPanel";

    }
}
