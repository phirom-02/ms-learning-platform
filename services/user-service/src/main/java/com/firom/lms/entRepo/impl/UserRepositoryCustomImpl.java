package com.firom.lms.entRepo.impl;

import com.firom.lms.dto.mapper.UserMapper;
import com.firom.lms.dto.response.UserResponse;
import com.firom.lms.entRepo.User;
import com.firom.lms.entRepo.UserRepositoryCustom;
import com.firom.lms.entRepo.UserRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final MongoTemplate mongoTemplate;
    private final UserMapper userMapper;

    @Override
    public Page<UserResponse> findUsersByFilters(
            String username,
            String email,
            String firstName,
            String lastName,
            List<UserRoles> roles,
            Pageable pageable
    ) {
        List<Criteria> criteriaList = new ArrayList<>();

        if (username != null && !username.isBlank()) {
            criteriaList.add(Criteria.where("username").regex(username, "i"));
        }

        if (email != null && !email.isBlank()) {
            criteriaList.add(Criteria.where("email").regex(email, "i"));
        }

        if (firstName != null && !firstName.isBlank()) {
            criteriaList.add(Criteria.where("firstName").regex(firstName, "i"));
        }

        if (lastName != null && !lastName.isBlank()) {
            criteriaList.add(Criteria.where("lastName").regex(lastName, "i"));
        }

        if (roles != null && !roles.isEmpty()) {
            criteriaList.add(Criteria.where("roles").in(roles));
        }

        Query query = new Query();

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        // Count query before pagination
        long total = mongoTemplate.count(query, User.class);

        // Apply pagination & sorting
        query.with(pageable);

        List<UserResponse> users = mongoTemplate.find(query, User.class)
                .stream().map(userMapper::entityToUserResponse)
                .toList();

        return new PageImpl<>(users, pageable, total);
    }
}
