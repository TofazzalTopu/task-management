package basico.task.management.service;

import basico.task.management.dto.LoginDto;
import basico.task.management.dto.LoginResponse;

public interface LoginService {

    LoginResponse login(LoginDto loginDto) throws Exception;
}
