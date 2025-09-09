package com.firom.lms.remotes.client;

import com.firom.lms.dto.response.ApiResponse;
import com.firom.lms.entRepo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "user-service",
        url = "${application.remotes.user-url}"
)
public interface UserClient {

    @GetMapping("/{user-id}")
    ResponseEntity<ApiResponse<User>> getUserById(@PathVariable("user-id") UUID userId);

}

