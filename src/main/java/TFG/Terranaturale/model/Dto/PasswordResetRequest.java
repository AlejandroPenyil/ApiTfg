package TFG.Terranaturale.model.Dto;

import lombok.Data;

/**
 * DTO for password reset requests.
 * Contains the username or email to identify the user and the new password.
 */
@Data
public class PasswordResetRequest {
    private String identifier; // Can be username or email
    private String newPassword;
}