package hexlet.code.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.dto.UserRegistrationDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.impl.JWTTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
@DBRider
@DataSet("dataset/data-set.yml")
public class TestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    public static final String BASE_URL = "/api";
    public static final String TEMPLATES_PATH = "src/test/resources/templates/";

    private final UserRegistrationDto regDto = new UserRegistrationDto("test@mail.ru",
            "firstname",
            "lastName",
            "password");

    public User regDefaultUser() throws Exception {
        User user = userRepository.findAll().get(0);

        MockHttpServletResponse req = perform(
                post(BASE_URL + USER_CONTROLLER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(regDto)),
                user.getEmail()
        ).andReturn().getResponse();

        return userRepository.findAll().get(1);
    }

    public ResultActions perform(MockHttpServletRequestBuilder req) throws Exception {
        return mockMvc.perform(req);
    }

    public ResultActions perform(MockHttpServletRequestBuilder req, String user) throws Exception {
        String token = jwtTokenService.expiring(Map.of("username", user));
        req.header(HttpHeaders.AUTHORIZATION, token);

        return perform(req);
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    public String readFile(String pathToFile) throws IOException {
        return Files.readString(Paths.get(pathToFile).toAbsolutePath().normalize());
    }
}