package hexlet.code.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class WelcomeController {

    @GetMapping("/welcome")
    public String welcomePage() {
        return "Welcome to Spring";
    }
}
