package basico.task.management.scheduler;
import java.util.*;
import basico.task.management.dto.MailDto;
import basico.task.management.enums.EmailEnum;
import basico.task.management.model.primary.UserProfile;
import basico.task.management.service.MailSender;
import basico.task.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final MailSender mailSender;

    private final UserService userService;

    @Value("${invite.link}")
    private String link;

    @Value("${invite.subject}")
    private String subject;

    @Value("${invite.body}")
    private String body;


    @Scheduled(cron = "0 0 3 * * *")
    public void firstMailSendingAfterThreeDays() {
        List<UserProfile> user=userService.findPendingNotRegisteredUser();
        for (UserProfile u:user) {
            MailDto mailDto = new MailDto();
            mailDto.setLink(link + u.getToken());
            mailDto.setTo( u.getEmail());
            mailDto.setSubject(subject);
            mailDto.setType(EmailEnum.INVITE.name());
            mailSender.sendFormattedMail(mailDto);
            userService.updateLastEmailSentDate(u.getEmail());

        }
    }

}
