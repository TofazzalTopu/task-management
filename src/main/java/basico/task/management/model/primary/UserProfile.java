package basico.task.management.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "users")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserProfile_SEQ")
    @SequenceGenerator(name = "UserProfile_SEQ", sequenceName = "users_profile", allocationSize = 1)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "EMAIL")
    private String email;

    @Column(length = 50, name = "LAST_NAME")
    private String lastName;

    @Column(length = 50, name = "FIRST_NAME")
    private String firstName;

    @Column(length = 50, name = "COMPANY_NAME", unique = true)
    private String company;

    @Column(length = 10, name = "COMPANY_ID")
    private Long companyId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INVITE_DATE")
    private Date inviteDate;

    @Column(name = "IS_SUPPLIER")
    private boolean supplier;

    @Column(name = "SOURCE_EMAIL_ID")
    private Long sourceEmailId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false, foreignKey = @ForeignKey(name = "user_status_fk"))
    private Status status;

    @Column(length = 20, name = "PASSWORD")
    private String password;

    @Column(length = 50, name = "NIF")
    private String nif;

    @Column(length = 500, name = "ADDRESS")
    private String address;

    @Column(length = 15, name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(length = 50, name = "POSITION")
    private String position;

    @Column(length = 1, name = "SUPPLIER_INFO_IS_UPDATED")
    private boolean supplierInfoIsUpdated = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_LOGIN_TIME")
    private Date lastLoginTime;

    @Column(length = 100)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID", nullable = false, foreignKey = @ForeignKey(name = "ID"))
    private Role role;

    @Column(name = "EMAIL_COUNTER")
    private  Long emailCounter;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_EMAIL_SEND_DATE")
    private Date lastEmailSendDate;

}
