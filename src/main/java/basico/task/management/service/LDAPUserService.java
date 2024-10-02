package basico.task.management.service;

public interface LDAPUserService {
    String checkLdapGroup(String userName, String password) throws Exception;
}
