package com.tu.musichub.user.models.viewModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class PasswordResetTokenView {

    private String id;

    private String token;

    private Date expiryDate;
}
