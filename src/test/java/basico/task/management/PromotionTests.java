package basico.task.management;

import basico.task.management.controller.PromotionController;
import basico.task.management.dto.Response;
import basico.task.management.dto.promotion.PromotionDto;
import basico.task.management.exception.Messages;
import basico.task.management.service.PromotionService;
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
public class PromotionTests {

    @Mock
    private PromotionService promotionService;
    @Mock
    private Messages messages;
    private MockMvc mockMvc;

    @InjectMocks
    PromotionController promotionController;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(promotionController).build();
    }

    @Test
    void savePromotion() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        PromotionDto promotionDto = new PromotionDto();
        promotionDto.setCarteraId((long) 1);
        promotionDto.setPortfolioId((long) 1);
//        ResponseEntity<Response> responseEntity = promotionController.save(promotionDto);
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }
}
