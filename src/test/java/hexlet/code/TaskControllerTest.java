package hexlet.code;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
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

import static hexlet.code.controller.TaskController.ID;
import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.BASE_URL;
import static hexlet.code.utils.TestUtils.TEMPLATES_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@Transactional
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DBRider
@DataSet("dataset/data-set.yml")
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    private static String taskChangeJson;

    @BeforeAll
    void beforeAll() throws IOException {
        taskChangeJson = testUtils.readFile(TEMPLATES_PATH + "change-data-task.json");
    }

    @Test
    void testNewTask() throws Exception {
        assertThat(taskRepository.count()).isEqualTo(0);
        testUtils.regDefaultTask();
        assertThat(taskRepository.count()).isEqualTo(1);
    }

    @Test
    void testGetAll() throws Exception {
        MockHttpServletResponse req = testUtils.perform(
                get(BASE_URL + TASK_CONTROLLER_PATH)
        ).andReturn().getResponse();

        assertThat(req.getStatus()).isEqualTo(200);
    }

    @Test
    void testDeleteTask() throws Exception {
        User user = userRepository.findById(55L).get();
        Task task = testUtils.regDefaultTask();
        assertThat(taskRepository.count()).isEqualTo(1);

        MockHttpServletResponse req = testUtils.perform(
                delete(BASE_URL + TASK_CONTROLLER_PATH + ID, task.getId()),
                user.getEmail()
        ).andReturn().getResponse();

        assertThat(req.getStatus()).isEqualTo(200);
        assertThat(taskRepository.count()).isEqualTo(0);
    }

    @Test
    void testGetTask() throws Exception {
        User user = userRepository.findAll().get(0);
        Task task = testUtils.regDefaultTask();

        MockHttpServletResponse req = testUtils.perform(
                get(BASE_URL + TASK_CONTROLLER_PATH + ID, task.getId()),
                user.getEmail()
        ).andReturn().getResponse();

        assertThat(req.getStatus()).isEqualTo(200);
        assertThat(req.getContentAsString()).contains(task.getName());
    }

    @Test
    void testChangeTask() throws Exception {
        User user = userRepository.findAll().get(0);
        Task task = testUtils.regDefaultTask();

        MockHttpServletResponse req = testUtils.perform(
                put(BASE_URL + TASK_CONTROLLER_PATH + ID, task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskChangeJson),
                user.getEmail()
        ).andReturn().getResponse();

        assertThat(req.getStatus()).isEqualTo(200);
        assertThat(req.getContentAsString()).contains("NewName");
    }
}
