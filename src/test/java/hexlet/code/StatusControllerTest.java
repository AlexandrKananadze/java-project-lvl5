package hexlet.code;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.model.Status;
import hexlet.code.model.User;
import hexlet.code.repository.StatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.transaction.Transactional;

import java.io.IOException;

import static hexlet.code.controller.StatusController.STATUS_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.BASE_URL;
import static hexlet.code.controller.StatusController.ID;
import static hexlet.code.utils.TestUtils.TEMPLATES_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@DBRider
@DataSet("dataset/data-set.yml")
public class StatusControllerTest {

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private UserRepository userRepository;

    private static String changeStatusJson;
    private static String incorrectStatusJson;

    @BeforeAll
    void beforeAll() throws IOException {
        changeStatusJson = testUtils.readFile(TEMPLATES_PATH + "change-data-status.json");
        incorrectStatusJson = testUtils.readFile(TEMPLATES_PATH + "invalid-status-data.json");
    }

    @Test
    void testCreateStatus() throws Exception {
        assertThat(statusRepository.count()).isEqualTo(1);
        testUtils.regDefaultStatus();
        assertThat(statusRepository.count()).isEqualTo(2);
    }

    @Test
    void testGetAllStatuses() throws Exception {
        MockHttpServletResponse req = testUtils.perform(
                get(BASE_URL + STATUS_CONTROLLER_PATH)
        ).andReturn().getResponse();

        assertThat(req.getStatus()).isEqualTo(200);
    }

    @Test
    void testGetStatusById() throws Exception {
        MockHttpServletResponse req = testUtils.perform(
                get(BASE_URL + STATUS_CONTROLLER_PATH + ID, 20)
        ).andReturn().getResponse();

        assertThat(req.getStatus()).isEqualTo(200);
        assertThat(req.getContentAsString()).contains("To Do");
    }

    @Test
    void testDeleteStatus() throws Exception {
        User user = userRepository.findAll().get(0);
        testUtils.regDefaultStatus();
        Status status = statusRepository.findAll().get(1);

        assertThat(statusRepository.count()).isEqualTo(2);

        MockHttpServletResponse req = testUtils.perform(
                delete(BASE_URL + STATUS_CONTROLLER_PATH + ID, status.getId()),
                user.getEmail()
        ).andReturn().getResponse();

        assertThat(req.getStatus()).isEqualTo(200);
        assertThat(statusRepository.count()).isEqualTo(1);
    }

    @Test
    void testChangeStatusData() throws Exception {
        User user = userRepository.findAll().get(0);

        MockHttpServletResponse req = testUtils.perform(
                put(BASE_URL + STATUS_CONTROLLER_PATH + ID, 20)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changeStatusJson),
                user.getEmail()
        ).andReturn().getResponse();

        assertThat(req.getStatus()).isEqualTo(200);
        assertThat(req.getContentAsString()).contains("New Name");
    }

    @Test
    void testCreateIncorrectStatus() throws Exception {
        User user = userRepository.findAll().get(0);

        MockHttpServletResponse req = testUtils.perform(
                post(BASE_URL + STATUS_CONTROLLER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incorrectStatusJson),
                user.getEmail()
        ).andReturn().getResponse();

        assertThat(req.getStatus()).isEqualTo(422);
        assertThat(req.getContentAsString()).contains("Name must be at least 1 symbol");
    }
}
