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
public class LoginForm {
    @Pattern(regexp = "[A-Za-z]+[\\w]+")
    @Size(min = 3, max = 20)
    @NotBlank
    @NotEmpty
    private String login;
    @Pattern(regexp = "^(?=\\S*[a-z])(?=\\S*[A-Z])(?=\\S*[!@#$&*_?%^\"(){}\\[\\]<>|/\\-+=\\';:,\\.])(?=\\S*[0-9]).{6,30}$")
    @Size(min = 6, max = 30)
    @NotBlank
    @NotEmpty
    private String password;
}
