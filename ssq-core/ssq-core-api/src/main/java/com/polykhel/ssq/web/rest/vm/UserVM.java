package com.polykhel.ssq.web.rest.vm;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * View Model object for representing a user, with his authorities.
 */
@Data
@NoArgsConstructor
public class UserVM {

    @NotNull
    @Size(min = 1, max = 50)
    private String login;

    private Set<String> authorities;

    public UserVM(@NotNull @Size(min = 1, max = 50) String login, Set<String> authorities) {
        this.login = login;
        this.authorities = authorities;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserVM{" +
                "login='" + login + '\'' +
                ", authorities=" + authorities +
                "}";
    }
}
