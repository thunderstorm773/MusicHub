package com.tu.musichub.user.models.bindingModels;

import com.tu.musichub.user.staticData.AccountConstants;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public abstract class ConfirmPassword {

    @NotBlank(message = AccountConstants.PASSWORD_BLANK_MESSAGE)
    @Size(min = AccountConstants.PASSWORD_MIN_LEN,
            message = AccountConstants.PASSWORD_MIN_LEN_MESSAGE)
    private String password;

    @NotBlank(message = AccountConstants.CONFIRM_PASSWORD_BLANK_MESSAGE)
    private String confirmPassword;
}
