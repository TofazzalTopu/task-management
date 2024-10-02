package basico.task.management;

import basico.task.management.controller.CarteraController;
import basico.task.management.dto.Response;
import basico.task.management.exception.Messages;
import basico.task.management.model.primary.Cartera;
import basico.task.management.service.CarteraService;
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

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CarteraTest {

    @Mock
    private CarteraService carteraService;

    @Mock
    private Messages messages;

    private MockMvc mockMvc;

    @InjectMocks
    CarteraController carteraController;


    @BeforeEach
    public void setup() throws Exception{
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(carteraController).build();
    }


    @Test
    void saveCartera() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Cartera cartera=new Cartera();
        cartera.setName("Cartera");
        cartera.setManagementType("Mangement");
        cartera.setCif("cif");
        cartera.setCompany("Basico");
        cartera.setLogo("logo");
        cartera.setAddress("Mumbai");
        cartera.setWebsite("www.gmail.com");
        cartera.setManager("Manager");
        cartera.setNoOfAssets(2000);
        cartera.setStartDate(new Date());
//        ResponseEntity<Response> responseEntity = carteraController.save(cartera);
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    void findAll() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//        ResponseEntity<Response> responseEntity = carteraController.findAll();
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void findById() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//        ResponseEntity<Response> responseEntity = carteraController.findById(1L);
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }
}
