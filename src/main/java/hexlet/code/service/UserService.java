package hexlet.code.service;

import hexlet.code.dto.UserRegistrationDto;
import hexlet.code.model.User;

public interface UserService {
    User updateUser(Long id, UserRegistrationDto userRegistrationDto);
    User newUser(UserRegistrationDto userRegistrationDto);
}
