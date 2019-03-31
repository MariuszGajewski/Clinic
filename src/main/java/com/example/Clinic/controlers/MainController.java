package com.example.Clinic.controlers;

import com.example.Clinic.models.entities.DoctorEntity;
import com.example.Clinic.models.entities.PatientEntity;
import com.example.Clinic.models.forms.LoginForm;
import com.example.Clinic.models.forms.PasswordResetForm;
import com.example.Clinic.models.forms.RegisterForm;
import com.example.Clinic.models.repositories.DoctorRepository;
import com.example.Clinic.models.services.EmailService;
import com.example.Clinic.models.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

    final
    DoctorRepository doctorRepository;
    final PatientService patientService;
    final EmailService emailService;

    @Autowired
    public MainController(DoctorRepository doctorRepository, PatientService patientService, EmailService emailService) {
        this.doctorRepository = doctorRepository;
        this.patientService = patientService;
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/index")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/doctors")
    public String getDoctorsList(Model model) {
        Page<DoctorEntity> page=doctorRepository.findAllByOrderByIdDesc(new PageRequest(0,2));
        model.addAttribute("pagesNumber", page.getTotalPages());
        model.addAttribute("activePage",1);
        model.addAttribute("doctors", page);
        return "doctors";
    }

    @GetMapping("/doctors/{page}")
    public String getDoctorsNextPage(@PathVariable ("page") int pageNumber, Model model) {
        System.out.println(pageNumber);
        Page<DoctorEntity> page= doctorRepository.findAllByOrderByIdDesc(new PageRequest(pageNumber-1,2));
        System.out.println("strony " + page.getTotalPages());
        model.addAttribute("pagesNumber", page.getTotalPages());
        model.addAttribute("activePage",pageNumber);
        model.addAttribute("doctors", page);

        return "/doctors";
    }

    @GetMapping("/gallery")
    public String getGallery() {
        return "gallery";
    }

    @GetMapping("/ourstory")
    public String getStory() {
        return "ourstory";
    }

    @GetMapping("/contact")
    public String getContact() {
        return "contact";
    }

    @GetMapping("/patientPanel")
    public String getPatientZone(Model model) {
        model.addAttribute("registerForm",new RegisterForm());
        model.addAttribute("loginForm", new LoginForm());
        return "patientPanel";
    }

    @PostMapping("/register")
    public String registerPatient(@Valid @ModelAttribute("registerForm")RegisterForm registerForm,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        System.out.println(registerForm.getLogin());
        if(bindingResult.hasErrors()) {
            return "redirect:/patientPanel";
        }
        else if(!registerForm.getPassword().equals(registerForm.getPasswordRepeat())) {
            redirectAttributes.addFlashAttribute("result","Podane hasła różnią się");
            System.out.println("rózne hasła");
            return "redirect:/patientPanel";
        }
        PatientService.userOperationStatus operationStatus = patientService.registerNewPatient(registerForm);
        if(operationStatus == PatientService.userOperationStatus.BUSY_LOGIN) {
            redirectAttributes.addFlashAttribute("result", "Podany login jest zajęty");
        }else if(operationStatus == PatientService.userOperationStatus.ERROR) {
            redirectAttributes.addFlashAttribute("result", "Przepraszamy, podczas próby rejestracji wystąpił błąd. Prosimy sprówbować raz jeszcze za kilka chwil");
        }
        else {
            redirectAttributes.addFlashAttribute("result", "Rejestracja podwiodła się.");
        }
        return "redirect:/patientPanel";
    }

    @GetMapping("/permissionDenied")
    @ResponseBody
    public String denied(){
        return "tylko dla zalogowanych";
    }


    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("registerForm",new RegisterForm());
        model.addAttribute("loginForm", new LoginForm());
        return "/patientPanel";
    }


    @GetMapping("patientZone/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        patientService.logout(request,response);
        return "redirect:/patientPanel?logout";
    }
    
    @GetMapping("/passwordReset")
    public String resetPasswordTemplate(Model model){
        model.addAttribute("passwordResetForm",new PasswordResetForm());
        return "/passwordReset";
    }
    @PostMapping("/passwordReset")
    public String resetUserPassword(@ModelAttribute("passwordResetForm")PasswordResetForm resetForm, Model model){ //, BindingResult bindingResult) {
        /*if(bindingResult.hasErrors()){
            return "/passwordReset";
        }*/
        PatientEntity patient = patientService.passwordReset(resetForm.getLogin(), resetForm.getEmail());
        Map<String, String> emailModel= new HashMap<>();
        emailModel.put("userName",patient.getName());
        emailModel.put("userPassword", patientService.getRandomPassword());
        emailService.sendEmail(patient.getEmail(),"KrakDiag - odzyskanie hasła",emailModel);

        model.addAttribute("result" ,"ok");
        return "/passwordReset";
    }
}
