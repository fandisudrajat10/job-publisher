package com.job.publisher.service.impl;

import com.job.publisher.domain.Role;
import com.job.publisher.domain.User;
import com.job.publisher.dto.RegisterUserRequestDto;
import com.job.publisher.repository.UserRepository;
import com.job.publisher.service.UserService;
import com.job.publisher.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    @Override
    public void registerUser(RegisterUserRequestDto registerUserRequestDto) {

        validationService.validateRequest(registerUserRequestDto);

        if (userRepository.findByUsername(registerUserRequestDto.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exist");
        }

        User user = new User();
        user.setUsername(registerUserRequestDto.getUsername());
        user.setPassword(BCrypt.hashpw(registerUserRequestDto.getPassword(), BCrypt.gensalt()));
        user.setEmail(registerUserRequestDto.getEmail());
        user.setRole(Role.valueOf(registerUserRequestDto.getRole()));
        userRepository.save(user);

    }

    @Override
    public User findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username doesn't exist");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        } else {
            User user = userOptional.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    AuthorityUtils.createAuthorityList(user.getRole().toString())
            );
        }
    }
}
