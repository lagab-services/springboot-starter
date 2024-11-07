package com.lagab.blank.security.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.lagab.blank.common.exception.BadRequestException;
import com.lagab.blank.common.exception.NotFoundException;
import com.lagab.blank.security.domain.PasswordResetToken;
import com.lagab.blank.security.domain.User;
import com.lagab.blank.security.repository.PasswordResetTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    /**
     * Is e-mail verification token expired?
     *
     * @param token PasswordResetToken
     * @return boolean
     */
    public boolean isPasswordResetTokenExpired(PasswordResetToken token) {
        return token.getExpirationDate().isBefore(LocalDateTime.now());
    }

    /**
     * Create password reset token from user.
     *
     * @param user User
     * @return PasswordResetToken
     */
    public PasswordResetToken create(User user) {
        return null;
    }

    /**
     * Get password reset token by token.
     *
     * @param token String
     * @return PasswordResetToken
     */
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new NotFoundException("token not found"));
    }

    /**
     * Get password reset token by token.
     *
     * @param token String
     * @return User
     */
    public User getUserByToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                                                                            .orElseThrow(() -> new NotFoundException("token not found"));

        if (isPasswordResetTokenExpired(passwordResetToken)) {
            throw new BadRequestException("the token as expired");
        }

        return passwordResetToken.getUser();
    }

    /**
     * Delete password reset token by user ID.
     *
     * @param userId UUID
     */
    public void deleteByUserId(Long userId) {
        passwordResetTokenRepository.deleteByUserId(userId);
    }

}
