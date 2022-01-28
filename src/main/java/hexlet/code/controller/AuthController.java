package hexlet.code.controller;

import hexlet.code.dto.UserLoginDto;
import hexlet.code.service.UserAuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static hexlet.code.controller.AuthController.LOGIN_CONTROLLER_PATH;

@RestController
@RequestMapping("${base-url}" + LOGIN_CONTROLLER_PATH)
public class AuthController {

    private final UserAuthenticationService userAuthenticationService;

    public static final String LOGIN_CONTROLLER_PATH = "/login";

    public AuthController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @PostMapping
    public String login(@RequestBody UserLoginDto userLoginDto) {
        return userAuthenticationService.login(userLoginDto.getEmail(), userLoginDto.getPassword());
    }
}
