package basico.task.management;

import basico.task.management.controller.TaskController;
import basico.task.management.dto.Response;
import basico.task.management.dto.TaskDto;
import basico.task.management.exception.Messages;
import basico.task.management.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TaskTest {

    @Mock
    private TaskService taskService;
    @Mock
    private Messages messages;

    private MockMvc mockMvc;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }


//    @Test
//    void findAll() throws Exception {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//        ResponseEntity<Response> responseEntity = taskController.findAll();
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
//    }

    @Test
    void findOne() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//        ResponseEntity<Response> responseEntity = taskController.findById((1L));
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void save() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        TaskDto taskDto = new TaskDto();
        taskDto.setReferenceId(1L);
        taskDto.setType("type");
//        ResponseEntity<Response> responseEntity = taskController.save(taskDto);
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    void update() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        TaskDto taskDto = new TaskDto();
        taskDto.setReferenceId(1L);
        taskDto.setType("type");
//        ResponseEntity<Response> responseEntity = taskController.update(1L, taskDto);
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(202);
    }

}
