package hexlet.code.app.service;

import hexlet.code.app.dto.UserRegistrationDto;
import hexlet.code.app.model.User;

public interface UserService {
    User updateUser(Long id, UserRegistrationDto userRegistrationDto);
    User newUser(UserRegistrationDto userRegistrationDto);
}
