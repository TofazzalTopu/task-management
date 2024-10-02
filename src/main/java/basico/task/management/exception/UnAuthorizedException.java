package basico.task.management.exception;

import org.hibernate.service.spi.ServiceException;

public class UnAuthorizedException extends ServiceException {
    public UnAuthorizedException(String message) {
        super(message);
    }
}
