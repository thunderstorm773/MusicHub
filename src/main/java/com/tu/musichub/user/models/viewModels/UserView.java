package com.tu.musichub.user.models.viewModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserView {

    private String id;

    private String username;

    private String email;

    private Set<String> roleNames;
}
