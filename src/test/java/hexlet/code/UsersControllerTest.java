package hexlet.code;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.model.User;
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

import static hexlet.code.controller.UserController.ID;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.BASE_URL;
import static hexlet.code.utils.TestUtils.TEMPLATES_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DBRider
@SpringBootTest
@DataSet("dataset/data-set.yml")
@AutoConfigureMockMvc
public class UsersControllerTest {

    private static String userChangeData;
    private static String userRegistrationIncorrectData;

    @Autowired
    private TestUtils testUtils;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void beforeAll() throws IOException {
        userChangeData = testUtils.readFile(TEMPLATES_PATH + "change-data-user.json");
        userRegistrationIncorrectData = testUtils.readFile(TEMPLATES_PATH + "invalid-user-data.json");
    }

    @Test
    void testUserCreate() throws Exception {
        assertThat(userRepository.count()).isEqualTo(1);
        testUtils.regDefaultUser();
        assertThat(userRepository.count()).isEqualTo(2);
    }

    @Test
    void testGetUserById() throws Exception {
        User dbUser = userRepository.findAll().get(0);

        MockHttpServletResponse req = testUtils.perform(
                get(BASE_URL + USER_CONTROLLER_PATH + ID, dbUser.getId()),
                dbUser.getEmail()
        ).andReturn().getResponse();

        assertThat(req.getStatus()).isEqualTo(200);
        assertThat(req.getContentAsString()).contains(dbUser.getFirstName());
    }

    @Test
    void testGetAllUsers() throws Exception {
        MockHttpServletResponse req = testUtils.perform(
                get(BASE_URL + USER_CONTROLLER_PATH)
        ).andReturn().getResponse();

        assertThat(req.getStatus()).isEqualTo(200);
        assertThat(req.getContentAsString()).contains("FirstName");
    }

    @Test
    void testChangeUser() throws Exception {
        User userToChange = userRepository.findAll().get(0);

        MockHttpServletResponse req = testUtils.perform(
                put(BASE_URL + USER_CONTROLLER_PATH + ID, userToChange.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userChangeData),
                userToChange.getEmail()
        ).andReturn().getResponse();

        assertThat(req.getStatus()).isEqualTo(200);
        assertThat(req.getContentAsString()).contains("newFirstName");
    }

    @Test
    void testDeleteUser() throws Exception {
        User userToDelete = userRepository.findAll().get(0);

        assertThat(userRepository.count()).isEqualTo(1);

        MockHttpServletResponse req = testUtils.perform(
                delete(BASE_URL + USER_CONTROLLER_PATH + ID, userToDelete.getId()),
                userToDelete.getEmail()
        ).andReturn().getResponse();

        assertThat(req.getStatus()).isEqualTo(200);
        assertThat(userRepository.count()).isEqualTo(0);
    }

    @Test
    void testRegistrationWithIncorrectData() throws Exception {
        MockHttpServletResponse req = testUtils.perform(
                post(BASE_URL + USER_CONTROLLER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRegistrationIncorrectData)
        ).andReturn().getResponse();

        String requestBody = req.getContentAsString();

        assertThat(req.getStatus()).isEqualTo(422);
        assertThat(requestBody).contains("Email invalid");
        assertThat(requestBody).contains("Firstname must be at least 1 symbol");
        assertThat(requestBody).contains("Lastname must be at least 1 symbol");
        assertThat(requestBody).contains("Password must be at least 3 symbols");
    }
}
