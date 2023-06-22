package com.job.publisher.service;

import com.job.publisher.domain.User;
import com.job.publisher.dto.RegisterUserRequestDto;

public interface UserService {
    void registerUser(RegisterUserRequestDto registerUserRequestDto);

    User findByUsername(String username);
}
