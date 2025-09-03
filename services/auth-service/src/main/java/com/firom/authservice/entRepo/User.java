package com.firom.authservice.entRepo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String password;

    private Set<UserRoles> roles;

    private boolean enabled;


}
