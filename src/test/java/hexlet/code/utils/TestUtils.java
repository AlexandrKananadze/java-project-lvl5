package hexlet.code.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.dto.LabelDto;
import hexlet.code.dto.StatusDto;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.UserRegistrationDto;
import hexlet.code.model.Label;
import hexlet.code.model.Status;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.StatusRepository;
import hexlet.code.repository.TaskRepository;
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

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.controller.StatusController.STATUS_CONTROLLER_PATH;
import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
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

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private LabelRepository labelRepository;

    public static final String BASE_URL = "/api";
    public static final String TEMPLATES_PATH = "src/test/resources/templates/";

    private final UserRegistrationDto userRegDto = new UserRegistrationDto("test@mail.ru",
            "firstname",
            "lastName",
            "password");

    private final StatusDto statusRegDto = new StatusDto("New");

    private final TaskDto taskRegDto = new TaskDto(
            "TaskName",
            "TaskDescription",
            55L,
            20L
    );

    private final LabelDto labelRegDto = new LabelDto("NewLabel");

    public Label regDefaultLabel() throws Exception {
        User user = userRepository.findAll().get(0);

        MockHttpServletResponse req = perform(
                post(BASE_URL + LABEL_CONTROLLER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(labelRegDto)),
                user.getEmail()
        ).andReturn().getResponse();

        return labelRepository.findAll().get(0);
    }

    public Task regDefaultTask() throws Exception {
        User user = userRepository.findAll().get(0);

        MockHttpServletResponse req = perform(
                post(BASE_URL + TASK_CONTROLLER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(taskRegDto)),
                user.getEmail()
        ).andReturn().getResponse();

        return taskRepository.findAll().get(0);
    }

    public User regDefaultUser() throws Exception {
        User user = userRepository.findAll().get(0);

        MockHttpServletResponse req = perform(
                post(BASE_URL + USER_CONTROLLER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(userRegDto)),
                user.getEmail()
        ).andReturn().getResponse();

        return userRepository.findAll().get(1);
    }

    public Status regDefaultStatus() throws Exception {
        User user = userRepository.findAll().get(0);

        MockHttpServletResponse req = perform(
                post(BASE_URL + STATUS_CONTROLLER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(statusRegDto)),
                user.getEmail()
        ).andReturn().getResponse();

        return statusRepository.findAll().get(0);
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
