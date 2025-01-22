package edu.tcu.cs.hogwarts_artifacts_online.hogwartsuser.converter;

import edu.tcu.cs.hogwarts_artifacts_online.hogwartsuser.HogwartsUser;
import edu.tcu.cs.hogwarts_artifacts_online.hogwartsuser.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter {
    public UserDto convert(HogwartsUser source){
        UserDto userDto = new UserDto(source.getId(),source.getUsername(), source.getEnabled(),source.getRoles());
        return userDto;
    }
}
