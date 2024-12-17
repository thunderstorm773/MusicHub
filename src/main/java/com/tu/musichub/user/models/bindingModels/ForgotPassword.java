package com.tu.musichub.user.models.bindingModels;

import com.tu.musichub.user.staticData.AccountConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
public class ForgotPassword {

    @NotBlank(message = AccountConstants.EMAIL_BLANK_MESSAGE)
    @Email(regexp = AccountConstants.EMAIL_PATTERN, message = AccountConstants.INCORRECT_EMAIL_FORMAT_MESSAGE)
    private String email;
}
