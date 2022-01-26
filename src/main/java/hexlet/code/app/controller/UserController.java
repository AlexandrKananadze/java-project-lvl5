package hexlet.code.app.controller;

import hexlet.code.app.dto.UserRegistrationDto;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("${base-url}" + "/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public static final String ID = "/{id}";

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping(ID)
    public User getUserById(@PathVariable("id") Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @PostMapping
    public User newUser(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        return userService.newUser(userRegistrationDto);
    }

    @PutMapping(ID)
    public User updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        return userService.updateUser(id, userRegistrationDto);
    }

    @DeleteMapping(ID)
    public void deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
    }

}
