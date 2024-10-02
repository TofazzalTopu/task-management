package basico.task.management.service.impl.ldap;

import basico.task.management.constant.AppConstants;
import basico.task.management.service.LDAPUserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Service
public class LDAPUserServiceImpl implements LDAPUserService {

    @Override
    public String checkLdapGroup(String userName, String password) throws Exception {
        InitialDirContext context = getContext();
        SearchControls ctrls = new SearchControls();
        ctrls.setReturningAttributes(new String[]{"givenName", "sn", "memberOf"});
        ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<String> roleList = new ArrayList<>();
        NamingEnumeration<SearchResult> answers = context.search("CN=Users,DC=basico,DC=es", "(sAMAccountName=" + userName + ")", ctrls);
        if (answers.hasMore()) {
            javax.naming.directory.SearchResult result = answers.nextElement();
            try {
                Attribute memberOf = result.getAttributes().get("memberOf");
                NamingEnumeration<String> resultGroup = (NamingEnumeration<String>) memberOf.getAll();
                while (resultGroup.hasMore()) {
                    String role = resultGroup.next();
                    role = role.substring(3, role.indexOf(','));
                    if (Objects.nonNull(role) && !role.equals(""))
                        roleList.add(role);
                }

                if (roleList.contains(AppConstants.LDAP_GROUP_TECHNOLOGIA)) {
                    return AppConstants.LDAP_GROUP_TECHNOLOGIA;
                } else if (roleList.contains(AppConstants.LDAP_GROUP_PORTFOLIO_MNG)) {
                    return AppConstants.LDAP_GROUP_PORTFOLIO_MNG;
                } else if (roleList.contains(AppConstants.LDAP_GROUP_TECNICOS)) {
                    return AppConstants.LDAP_GROUP_TECNICOS;
                } else if (userName.equalsIgnoreCase(AppConstants.ROLE_NAME_ADMIN)) {
                    return null;
                } else {
                    throw new BadCredentialsException(AppConstants.LDAP_GROUP_NOT_ALLOWED);
                }
            } catch (AuthenticationException e) {
                return AppConstants.LDAP_GROUP_NOT_FOUND;
            }
        } else {
            return null;
        }
    }

    private static InitialDirContext getContext() throws NamingException {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(Context.SECURITY_AUTHENTICATION, "simple");
        props.put(Context.PROVIDER_URL, "ldap://ldap.basico.es:389");
        props.put(Context.SECURITY_PRINCIPAL, "CN=usrldap,CN=Users,DC=basico,DC=es");//adminuser - User with special priviledge, dn user
        props.put(Context.SECURITY_CREDENTIALS, "Basico3349");//dn user password
        return new InitialDirContext(props);
    }

}
