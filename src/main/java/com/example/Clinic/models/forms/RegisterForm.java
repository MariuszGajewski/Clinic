package com.example.Clinic.models.forms;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class RegisterForm {
    @Pattern(regexp = "[A-Za-z]+[\\w]+")
    @Size(min = 3, max = 20)
    @NotBlank
    @NotEmpty
    private String login;
    @Pattern(regexp = "[A-Za-z]+")
    @Size(min = 3, max = 20)
    @NotBlank
    @NotEmpty
    private String name;
    @Pattern(regexp = "[A-Za-z]+")
    @Size(min = 2, max = 30)
    @NotBlank
    @NotEmpty
    private String surname;
    @Pattern(regexp = "^(?=\\S*[a-z])(?=\\S*[A-Z])(?=\\S*[!@#$&*_?%^\"(){}\\[\\]<>|/\\-+=\\';:,\\.])(?=\\S*[0-9]).{6,30}$")
    @Size(min = 6, max = 30)
    @NotBlank
    @NotEmpty
    private String password;
    @Pattern(regexp = "^(?=\\S*[a-z])(?=\\S*[A-Z])(?=\\S*[!@#$&*_?%^\"(){}\\[\\]<>|/\\-+=\\';:,\\.])(?=\\S*[0-9]).{6,30}$")
    @Size(min = 6, max = 30)
    @NotBlank
    @NotEmpty
    private String passwordRepeat;
    @Pattern(regexp = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$")
    @Size(min = 5, max = 320)
    private String email;

}
