package hexlet.code;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.model.Label;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
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

import static hexlet.code.controller.LabelController.ID;
import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
@DBRider
@DataSet("dataset/data-set.yml")
@AutoConfigureMockMvc
public class LabelControllerTest {

    private static String labelChangeJson;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TestUtils testUtils;

    @BeforeAll
    void beforeAll() throws IOException {
        labelChangeJson = testUtils.readFile(TestUtils.TEMPLATES_PATH + "change-data-label.json");
    }

    @Test
    void testNewLabel() throws Exception {
        assertThat(labelRepository.count()).isEqualTo(0);
        testUtils.regDefaultLabel();
        assertThat(labelRepository.count()).isEqualTo(1);
    }

    @Test
    void getLabelById() throws Exception {
        Label label = testUtils.regDefaultLabel();
        User user = userRepository.findAll().get(0);

        MockHttpServletResponse req = testUtils.perform(
                get(BASE_URL + LABEL_CONTROLLER_PATH + ID, label.getId()),
                user.getEmail()
        ).andReturn().getResponse();

        assertThat(req.getStatus()).isEqualTo(200);
        assertThat(req.getContentAsString()).contains("NewLabel");
    }

    @Test
    void testGetAllLabels() throws Exception {
        User user = userRepository.findAll().get(0);

        MockHttpServletResponse req = testUtils.perform(
                get(BASE_URL + LABEL_CONTROLLER_PATH),
                user.getEmail()
        ).andReturn().getResponse();

        assertThat(req.getStatus()).isEqualTo(200);
    }

    @Test
    void testDeleteLabelById() throws Exception {
        User user = userRepository.findAll().get(0);
        Label label = testUtils.regDefaultLabel();

        assertThat(labelRepository.count()).isEqualTo(1);

        MockHttpServletResponse req = testUtils.perform(
                delete(BASE_URL + LABEL_CONTROLLER_PATH + ID, label.getId()),
                user.getEmail()
        ).andReturn().getResponse();

        assertThat(req.getStatus()).isEqualTo(200);
        assertThat(labelRepository.count()).isEqualTo(0);
    }

    @Test
    void testChangeLabel() throws Exception {
        User user = userRepository.findAll().get(0);
        Label label = testUtils.regDefaultLabel();

        MockHttpServletResponse req = testUtils.perform(
                put(BASE_URL + LABEL_CONTROLLER_PATH + ID, label.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(labelChangeJson),
                user.getEmail()
        ).andReturn().getResponse();

        assertThat(req.getStatus()).isEqualTo(200);
        assertThat(req.getContentAsString()).contains("In work");
    }

}
