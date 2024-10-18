package com.lagab.blank.service.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(@NotBlank(message = "Login is mandatory") String email, String password) {
}
