package com.tu.musichub.user.models.bindingModels;

import com.tu.musichub.user.staticData.AccountConstants;
import com.tu.musichub.user.validations.Email;
import com.tu.musichub.user.validations.PasswordMatching;
import com.tu.musichub.user.validations.Username;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@PasswordMatching
@NoArgsConstructor
@Getter
@Setter
public class RegisterUser extends ConfirmPassword {

    @NotBlank(message = AccountConstants.USERNAME_BLANK_MESSAGE)
    @Size(min = AccountConstants.USERNAME_MIN_LEN,
            max = AccountConstants.USERNAME_MAX_LEN,
            message = AccountConstants.USERNAME_REQUIRED_LEN_MESSAGE)
    @Username
    private String username;

    @NotBlank(message = AccountConstants.EMAIL_BLANK_MESSAGE)
    @Pattern(regexp = AccountConstants.EMAIL_PATTERN,
            message = AccountConstants.INCORRECT_EMAIL_FORMAT_MESSAGE)
    @Email
    private String email;
}
