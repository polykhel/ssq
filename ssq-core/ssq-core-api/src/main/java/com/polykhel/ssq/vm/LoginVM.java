package com.polykhel.ssq.vm;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */
@Data
@NoArgsConstructor
public class LoginVM {

    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @NotNull
    @Size(min = 4, max = 100)
    @ToString.Exclude
    private String password;

    private Boolean rememberMe;

    public LoginVM(@NotNull @Size(min = 1, max = 50) String username, @NotNull @Size(min = 4, max = 100) String password, Boolean rememberMe) {
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
    }

}
