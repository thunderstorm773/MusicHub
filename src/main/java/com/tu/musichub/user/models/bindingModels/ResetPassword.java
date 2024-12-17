package com.tu.musichub.user.models.bindingModels;

import com.tu.musichub.user.validations.PasswordMatching;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@PasswordMatching
@NoArgsConstructor
@Getter
@Setter
public class ResetPassword extends ConfirmPassword {

    private String token;
}
