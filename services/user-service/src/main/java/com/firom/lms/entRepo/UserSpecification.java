package com.firom.lms.entRepo;

import com.firom.lms.constants.UserRoles;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class UserSpecification {

    public static Specification<User> unrestricted() {
        return Specification.unrestricted();
    }

    public static Specification<User> usernameContains(String username) {
        return (root, query, cb) ->
                username == null || username.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
    }

    public static Specification<User> emailContains(String email) {
        return (root, query, cb) ->
                email == null || email.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<User> firstNameContains(String firstName) {
        return (root, query, cb) ->
                firstName == null || firstName.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<User> lastNameContains(String lastName) {
        return (root, query, cb) ->
                lastName == null || lastName.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<User> hasAnyRole(List<UserRoles> roles) {
        return (root, query, cb) -> {
            if (roles == null || roles.isEmpty()) return null;
            Join<User, UserRoles> rolesJoin = root.join("roles");
            return rolesJoin.in(roles);
        };
    }
}
