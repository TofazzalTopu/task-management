package basico.task.management;

import basico.task.management.controller.UserProfileController;
import basico.task.management.dto.Response;
import basico.task.management.dto.userprofile.ChangePasswordDto;
import basico.task.management.dto.userprofile.UserProfileDto;
import basico.task.management.exception.Messages;
import basico.task.management.service.UserService;

import basico.task.management.enums.UserStatusEnum;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class UserProfileTests {

    @Mock
    private UserService userService;

    @Mock
    private Messages messages;

    private MockMvc mockMvc;

    @InjectMocks
    UserProfileController userProfileController;

    @BeforeEach
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(userProfileController).build();
    }


    @Test
    void saveUserProfile() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setCompany("Basico");
        userProfileDto.setFirstName("Samir ");
        userProfileDto.setLastName("Gautam");
        userProfileDto.setUserName("samir@gmail.com");
        userProfileDto.setSourceEmailId((long) 1);
//        ResponseEntity<Response> responseEntity = userProfileController.save(userProfileDto);
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }


    @Test
    void changePassword() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ChangePasswordDto userProfileDto = new ChangePasswordDto();
        userProfileDto.setCofrimPassword("password");
        userProfileDto.setNewPassword("password");
        userProfileDto.setToken("token");
//        ResponseEntity<Response> responseEntity = userProfileController.changePassword(userProfileDto);
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(202);
    }

    @Test
    void updateStatus() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//        ResponseEntity<Response> responseEntity = userProfileController.updateStatus(1L, UserStatusEnum.REGISTERED.name());
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(202);
    }

    @Test
    void getSupplierMember() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//        ResponseEntity<Response> responseEntity = userProfileController.getSupplierMember(10L);
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void getAllSupplier() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//        ResponseEntity<Response> responseEntity = userProfileController.getAllSupplier();
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

}
