package edu.tcu.cs.hogwarts_artifacts_online.hogwartsuser;

import jakarta.validation.constraints.NotEmpty;

public record UserDto(Integer id,
                      @NotEmpty(message = "username is required.")
                      String username,
                      boolean enabled,
                      @NotEmpty(message = "role is required.")
                      String roles) {
}
