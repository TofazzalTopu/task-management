package basico.task.management.config.security;

import basico.task.management.dto.Response;
import basico.task.management.exception.Messages;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	@Autowired
    private  Messages messages;

    public JwtAuthenticationEntryPoint(Messages messages) {
        this.messages = messages;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        Response response1=   new Response(HttpServletResponse.SC_UNAUTHORIZED,messages.get("login.failure"),null);
        String json = new ObjectMapper().writeValueAsString(response1);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(json);
        response.flushBuffer();

    }
}