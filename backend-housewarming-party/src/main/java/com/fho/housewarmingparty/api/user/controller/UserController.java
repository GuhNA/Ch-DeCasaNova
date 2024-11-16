package com.fho.housewarmingparty.api.user.controller;


import com.fho.housewarmingparty.api.user.dto.UserDTO;
import com.fho.housewarmingparty.api.user.entity.User;
import com.fho.housewarmingparty.api.user.mapper.UserMapper;
import com.fho.housewarmingparty.api.user.service.UserService;
import com.fho.housewarmingparty.security.authentication.LoggedUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController implements UserApi {

    private final UserService service;
    private final UserMapper mapper;

    @Override
    public ResponseEntity<?> create(@Valid @RequestBody UserDTO dto) {
        log.info("Creating user. Payload: {}.", dto.getEmail());
        service.create(dto);
        return ResponseEntity.ok("User created successfully.");
    }

    @Override
    public void update(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
        log.info("Updating user {}. Payload: {}.", id, dto);
        service.update(id, mapper.toEntity(dto));
    }

    @Override
    public UserDTO findById(@PathVariable Long id) {
        log.info("Getting user with id: {}.", id);
        User user = service.findById(id);
        return mapper.toDto(user);
    }

    @Override
    public String getLoggedUser() {
        log.info("Getting logged user username.");
        String username = LoggedUser.getLoggedInUserDetails().getUsername();
        return "Logged-in User: " + username;
    }

    @Override
    public void delete(@PathVariable Long id) {
        log.info("Deleting the user with id: {}.", id);
        service.deleteById(id);
    }
}
