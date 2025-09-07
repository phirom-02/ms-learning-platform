package com.firom.lms.entRepo;


import com.firom.lms.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {
    Page<UserResponse> findUsersByFilters(
            String username,
            String email,
            String firstName,
            String lastName,
            List<UserRoles> roles,
            Pageable pageable
    );
}
