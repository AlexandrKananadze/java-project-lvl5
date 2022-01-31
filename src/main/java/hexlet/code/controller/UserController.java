package hexlet.code.controller;

import hexlet.code.dto.UserRegistrationDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public static final String ID = "/{id}";
    public static final String USER_CONTROLLER_PATH = "/users";
    private static final String ONLY_OWNER = """
                @userRepository.findById(#id).get().getEmail() == authentication.getName()
            """;


    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get user information"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping(ID)
    public User getUserById(@PathVariable("id") Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "Get list of all users")
    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }


    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "422", description = "User invalid/Invalid input")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User newUser(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        return userService.newUser(userRegistrationDto);
    }

    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "422", description = "Invalid input")
    })
    @PutMapping(ID)
    @PreAuthorize(ONLY_OWNER)
    public User updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        return userService.updateUser(id, userRegistrationDto);
    }

    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping(ID)
    @PreAuthorize(ONLY_OWNER)
    public void deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
    }

}
