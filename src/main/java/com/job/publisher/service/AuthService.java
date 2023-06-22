package com.job.publisher.service;

import com.job.publisher.dto.LoginRequestDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> login(LoginRequestDto requestDto);
}
