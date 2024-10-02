package basico.task.management;

import basico.task.management.controller.AcdcTaskController;
import basico.task.management.dto.Response;
import basico.task.management.exception.Messages;
import basico.task.management.service.AcdcTaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class AcdcTaskTest {

    @Mock
    private AcdcTaskService acdcTaskService;
    @Mock
    private Messages messages;
    private MockMvc mockMvc;

    @InjectMocks
    private AcdcTaskController acdcTaskController;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(acdcTaskController).build();
    }

    @Test
    void findAll() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//        ResponseEntity<Response> responseEntity = acdcTaskController.findAllACDC(0,2);
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void findOne() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//        ResponseEntity<Response> responseEntity = acdcTaskController.findById((long) 1);
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }


}
