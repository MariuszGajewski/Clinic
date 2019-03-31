package com.example.Clinic.models.forms;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class PasswordChangeForm {
    @Pattern(regexp = "^(?=\\S*[a-z])(?=\\S*[A-Z])(?=\\S*[!@#$&*_?%^\"(){}\\[\\]<>|/\\-+=\\';:,\\.])(?=\\S*[0-9]).{6,30}$")
    @Size(min = 6, max = 30)
    @NotBlank
    @NotEmpty
    private String oldPassword;
    @Pattern(regexp = "^(?=\\S*[a-z])(?=\\S*[A-Z])(?=\\S*[!@#$&*_?%^\"(){}\\[\\]<>|/\\-+=\\';:,\\.])(?=\\S*[0-9]).{6,30}$")
    @Size(min = 6, max = 30)
    @NotBlank
    @NotEmpty
    private String password;
    @Pattern(regexp = "^(?=\\S*[a-z])(?=\\S*[A-Z])(?=\\S*[!@#$&*_?%^\"(){}\\[\\]<>|/\\-+=\\';:,\\.])(?=\\S*[0-9]).{6,30}$")
    @Size(min = 6, max = 30)
    @NotBlank
    @NotEmpty
    private String repeatPassword;
}
