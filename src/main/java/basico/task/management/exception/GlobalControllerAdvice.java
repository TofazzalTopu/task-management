package basico.task.management.exception;

import basico.task.management.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {


    @Autowired
    private Messages messages;


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ResponseEntity<Object> showCustomMessage(Exception ex) {
        return new ResponseEntity<>(new Response(HttpStatus.CONFLICT.value(), ex.getMessage(), new String[0]), HttpStatus.METHOD_NOT_ALLOWED);
    }


    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new Response(HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getMessage(), new String[0]), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ex.getHttpInputMessage()), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(), messages.get("validation.failed"), null), HttpStatus.BAD_REQUEST);

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<Map<String, Object>> details = new ArrayList<>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            Map<String, Object> errorsMap = new HashMap<>();
            errorsMap.put("fieldName", error.getField());
            errorsMap.put("errorMsg", error.getDefaultMessage());
            details.add(errorsMap);
        }
        return new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(), details.get(0).get("fieldName").toString() + " " + details.get(0).get("errorMsg").toString(), details), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handelInternalServerError(Exception ex, WebRequest req) {
        ex.printStackTrace();
        return new ResponseEntity<>(new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), new String[0]), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleMethodNotFound(Exception ex, WebRequest req) {
        return new ResponseEntity<>(new Response(HttpStatus.NOT_FOUND.value(), ex.getMessage(), new String[0]), HttpStatus.NOT_FOUND);
    }


    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new Response((HttpStatus.BAD_REQUEST.value()), ex.getMessage(), new String[0]), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), new String[0]), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({DataIntegrityViolationException.class})
    protected ResponseEntity<Object> customConstraintVoilated(RuntimeException ex, WebRequest request) {

        return new ResponseEntity<>(new Response(HttpStatus.CONFLICT.value(), ex.getMessage(), new String[0]), HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new Response(HttpStatus.NOT_FOUND.value(), ex.getMessage(), new String[0]), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new Response(HttpStatus.NOT_FOUND.value(), ex.getMessage(), new String[0]), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<Map<String, Object>> details = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            Map<String, Object> errorsMap = new HashMap<>();
            errorsMap.put("fieldName", error.getField());
            errorsMap.put("errorMsg", error.getDefaultMessage());
            details.add(errorsMap);
        }
        return new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(), details.get(0).get("fieldName").toString() + " " + details.get(0).get("errorMsg").toString(), details), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    public ResponseEntity<Object> handleBindException(BindException exception, HttpServletRequest request) {
        List<Map<String, Object>> details = new ArrayList<>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            Map<String, Object> errorsMap = new HashMap<>();
            errorsMap.put("fieldName", error.getField());
            errorsMap.put("errorMsg", error.getDefaultMessage());
            details.add(errorsMap);
        }
        return new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(), details.get(0).get("fieldName").toString() + " " + details.get(0).get("errorMsg").toString(), details), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<Object> customException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new Response(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage(), new String[0]), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler({AlreadyExistException.class})
    protected ResponseEntity<Object> alreadyExistException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new Response(HttpStatus.CONFLICT.value(), ex.getMessage(), new String[0]), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({NotFoundException.class})
    protected ResponseEntity<Object> notFoundException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new Response(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage(), new String[0]), HttpStatus.NOT_ACCEPTABLE);
    }
    //
    //    @ExceptionHandler(value = {InternalAuthenticationServiceException.class})
    //    public ResponseEntity<Object> handleLdapCommunicationException(Exception ex, WebRequest req) {
    //        return new ResponseEntity<>(new Response(HttpStatus.UNAUTHORIZED.value(), messages.get("ldap.connection.timeout"), new String[0]), HttpStatus.INTERNAL_SERVER_ERROR);
    //
    //    }
    //
    //    @ExceptionHandler(value = {UncategorizedLdapException.class})
    //    public ResponseEntity<Object> handleLdapNamingException(Exception ex, WebRequest req) {
    //        return new ResponseEntity<>(new Response(HttpStatus.UNAUTHORIZED.value(), messages.get("ldap.authentication.failure"), new String[0]), HttpStatus.UNAUTHORIZED);
    //    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<Object> handleLdapAuthenticationFailure(Exception ex, WebRequest req) {
        return new ResponseEntity<>(new Response(HttpStatus.UNAUTHORIZED.value(), messages.get("login.failure"), new String[0]), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {NotAllowedException.class})
    public ResponseEntity<Object> handleForgotPasswordForLdapUser(Exception ex, WebRequest req) {
        return new ResponseEntity<>(new Response(HttpStatus.NOT_ACCEPTABLE.value(), messages.get("reset.password.not.allowed"), new String[0]), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = {UnAuthorizedException.class})
    public ResponseEntity<Object> unAuthorizedException(Exception ex, WebRequest req) {
        return new ResponseEntity<>(new Response(HttpStatus.UNAUTHORIZED.value(), messages.get("you.are.not.authorized"), new String[0]), HttpStatus.NOT_ACCEPTABLE);
    }

}
