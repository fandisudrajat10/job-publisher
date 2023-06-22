package com.job.publisher.controller;

import com.job.publisher.dto.RegisterUserRequestDto;
import com.job.publisher.dto.WebResponse;
import com.job.publisher.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "")
    public WebResponse<String> registerUser(@RequestBody RegisterUserRequestDto registerUserRequestDto) {
        userService.registerUser(registerUserRequestDto);
        return WebResponse.<String>builder().data("OK").build();
    }
}
