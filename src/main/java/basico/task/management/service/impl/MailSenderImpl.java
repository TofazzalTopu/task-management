package basico.task.management.service.impl;

import basico.task.management.dto.EmailDTO;
import basico.task.management.dto.MailDto;
import basico.task.management.model.primary.Task;
import basico.task.management.service.MailSender;
import basico.task.management.enums.EmailEnum;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;

import static basico.task.management.constant.AppConstants.TASK_MAIL_SUBJECT;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.mail.username}")
    private String emailSentFrom;

    private final Configuration configuration;
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender emailSender;

    @Override
    public void sendFormattedMail(MailDto mailDto) {
        logger.info("email sending process to {}", mailDto.getTo());
        MimeMessage mimeMessage = null;
        try {

            mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            configuration.setClassForTemplateLoading(this.getClass(),
                    "/templates");
            Template te=null;
            if (mailDto.getType().equals(EmailEnum.INVITE.name())) {
                 te = configuration.getTemplate("send-invite.ftl");
            } else {
                 te = configuration.getTemplate("forgot-password.ftl");
            }
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(te, mailDto);
            helper.setTo(mailDto.getTo());
            helper.setSubject(mailDto.getSubject());
            helper.setFrom(emailSentFrom);
            helper.setText(html, true);
            emailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("email successfully sent");
    }

    @Override
    public void sendTaskMail(Task task, String toEmail, String subject) {
        MimeMessage mimeMessage = null;
        try {

            mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            configuration.setClassForTemplateLoading(this.getClass(),
                    "/templates");
            Template te= configuration.getTemplate("task-email.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(te, task);
            helper.setTo(toEmail);
            helper.setSubject(TASK_MAIL_SUBJECT);
            helper.setFrom(emailSentFrom);
            helper.setText(html, true);
            emailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("email successfully sent");
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {

    }

    @Override
    public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) {

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(emailSentFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("name of file", file);
            emailSender.send(message);
        } catch (Exception e) {

        }
    }

    public void sendEmail(EmailDTO email) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(email.getProperties());
        helper.setFrom(email.getFrom());
        helper.setTo(email.getTo());
        helper.setSubject(email.getSubject());
        String html = templateEngine.process(email.getTemplate(), context);
        helper.setText(html, true);
        emailSender.send(message);
    }

}
