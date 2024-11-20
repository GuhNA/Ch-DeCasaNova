package com.fho.housewarmingparty.api.user.controller;

import com.fho.housewarmingparty.api.user.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "User API", description = "API for managing users")
public interface UserApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new user", description = "Creates a new user with the provided data")
    ResponseEntity<?> create(@Valid @RequestBody UserDTO dto);

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update an existing user", description = "Updates the details of an existing user by its ID")
    void update(@Parameter(description = "ID of the user to be updated") @PathVariable Long id,
                @Valid @RequestBody UserDTO dto);

    @GetMapping("{id}")
    @Operation(summary = "Get user by ID", description = "Returns the details of a user by its ID")
    UserDTO findById(@Parameter(description = "ID of the user to be fetched") @PathVariable Long id);

    @GetMapping("/logged-user")
    @Operation(summary = "Get logged user", description = "Returns the logged user username")
    Long getLoggedUser();

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a user", description = "Deletes a user by its ID")
    void delete(@Parameter(description = "ID of the user to be deleted") @PathVariable Long id);
}
