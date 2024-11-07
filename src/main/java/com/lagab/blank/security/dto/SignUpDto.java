package com.lagab.blank.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SignUpDto {
    private String fullName;
    @NotEmpty(message = "{registration.email.not_empty}")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "{registration.email.not_valid}")
    private String email;

    @NotEmpty(message = "{registration.password.not_empty}")
    @Size(min = 8, message = "{registration.password.size.too_short}")
    @Size(max = 32, message = "{registration.password.size.too_long}")
    private String password;
}
