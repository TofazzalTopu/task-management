package basico.task.management.constant;

import org.springframework.stereotype.Component;

/**
 * @author Tofazzal
 */

@Component
public class AppConstants {
    public static final String API_VERSION = "v1";

    public static final String JWT_SECRET = "SECUREOFFNOSECURITYSECUREOFFNOSECURITYSECUREOFFNOSECURITYSECUREOFFNOSECURITYSECUREOFFNOSECURITYSECUREOFFNOSECURITY";
    public static final String HEADER = "Authorization";
    public static final String PREFIX = "Bearer ";
    public static final String LOGIN_PATH = "/user/login";
    public static final String REGISTRATION_PATH = "/users";
    public static final String[] AUTH_WHITELIST = {
            "/",
            "/v2/api-docs",
            "/v2/api-docs",
            "/configuration/ui",
            "/configuration/security",
            LOGIN_PATH,
            REGISTRATION_PATH
    };

    public static final String[] AUTH_LIST_SWAGGER = {"**/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**"};

    public static final String[] PERMIT_ALL = {"/resources/**", "/webjars/**", "/assets/**", "/static/assets/**", "/js/**", "/css/**", "/img/**"};

    public static final String USER_REGISTERED_SUCCESS = "User saved successfully.";
    public static final String USER_NOT_EXIST = "User not exist";
    public static final String USER_NAME_ALREADY_EXIST = "User already exist with the username: ";
    public static final String USER_EMAIL_ALREADY_EXIST = "User already exist with the email: ";
    public static final String USER_FETCH_SUCCESS = "User fetch successfully.";
    public static final String LOGOUT_SUCCESS = "You have been logged out successfully.";
    public static final String ALREADY_EXIST = "Already exist!";

    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60;
    public static final String SIGNING_KEY = "devglan123r";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHORITIES_KEY = "scopes";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String SWAGGER_USERNAME = "task";
    public static final String SWAGGER_PASSWORD = "$2a$10$GgSe6rKhvuCJwehSII1YcO3ng3CxZkOkAm0CZxMC2ANVj.t8R/yOe";
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "$2a$10$GgSe6rKhvuCJwehSII1YcO3ng3CxZkOkAm0CZxMC2ANVj.t8R/yOe";
    public static final String ADMIN_ROLE = "ROLE_ADMIN";
    public static final String ROLE_NAME_ADMIN = "admin";
    public static final String ROLE_LABEL_ADMIN = "ADMIN";
    public static final String ROLE_LABEL_PORTFOLIO_MANAGER = "PORTFOLIO_MANAGER";
    public static final String ROLE_LABEL_TECHNICAL_USER = "TECHNICAL_USER";
    public static final String ROLE_LABEL_TECHNICAL_MANAGER = "TECHNICAL_MANGER";
    public static final String ROLE_NAME_SUPPLIER = "Suppiler";
    public static final String ROLE_LABEL_SUPPLIER = "SUPPLIER";

    public static final String LDAP_GROUP_TECHNOLOGIA = "Tecnologia";
    public static final String LDAP_GROUP_TECNICOS = "Tecnicos";
    public static final String LDAP_GROUP_PORTFOLIO_MNG = "Portfolio MNG";
    public static final String LDAP_GROUP_NOT_FOUND = "NOT_FOUND";
    public static final String LDAP_GROUP_NOT_ALLOWED = "You are not allowed to login. Contact with admin.";
    public static final String TASK_MAIL_SUBJECT = "You have a new task";
    public static final String TASK_MAIL_SUBJECT_ACCEPTED = "Task accepted successfully";
    public static final String TASK_MAIL_SUBJECT_REJECTED = "Task is rejected";
    public static final String TASK_MAIL_SUBJECT_BUDGET_CREATED = "Budget created successfully";
    public static final String TASK_MAIL_SUBJECT_HOLD = "Task hold successfully";

    public static String ACCEPT_TASK_BY_SUPPLIER = "Accept Task By Supplier";
    public static String ACCEPT_TASK_BY_SUPPLIER_TEMPLATE = "acceptTaskBySupplier.html";

    public static String APPROVE_BUDGET = "Approve Budget";
    public static String APPROVE_BUDGET_TEMPLATE = "approveBudget.html";

    public static String APPROVE_TASK = "Approve Task";
    public static String APPROVE_TASK_TEMPLATE = "approveTask.html";

    public static String ASSIGN_TO_SUPPLIER = "assign To Supplier";
    public static String ASSIGN_TO_SUPPLIER_TEMPLATE = "assignToSupplier.html";

    public static String ASSIGN_TASK_TO_TECHNICAL_USER = "Assign To Technical User";
    public static String ASSIGN_TASK_TO_TECHNICAL_USER_TEMPLATE = "assignToTechnicalUser.html";

    public static String COMPLETE_TASK_BY_SUPPLIER = "Complete Task By Supplier";
    public static String COMPLETE_TASK_BY_SUPPLIER_TEMPLATE = "completeTaskBySupplier.html";

    public static String FORGOT_PASSWORD = "Forgot Password";
    public static String FORGOT_PASSWORD_TEMPLATE = "forgotPassword.html";

    public static String HOLD_TASK = "Hold Task";
    public static String HOLD_TASK_TEMPLATE = "holdTask.html";

    public static String REJECT_BUDGET_BY_TECHNICAL_USER = "Reject Budget By Technical User";
    public static String REJECT_BUDGET_BY_TECHNICAL_USER_TEMPLATE = "rejectBudgetByTechnicalUser.html";

    public static String REJECT_BY_SUPPLIER = "Reject By Supplier";
    public static String REJECT_BY_SUPPLIER_TEMPLATE = "rejectBySupplier.html";

    public static String REJECT_TASK_BY_SUPPLIER = "Reject Task By Supplier";
    public static String REJECT_TASK_BY_SUPPLIER_TEMPLATE = "rejectTaskByTechnicalUser.html";

    public static String SEND_INVITATION = "Send Invitation";
    public static String SEND_INVITATION_TEMPLATE = "sendInvitation.html";

    public static String SUBMIT_BUDGET = "Submit Budget";
    public static String SUBMIT_BUDGET_TEMPLATE = "submitBudget.html";

}
