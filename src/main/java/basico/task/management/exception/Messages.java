package basico.task.management.exception;

import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;


@Component
public class Messages {

    private final MessageSource messageSource;

    @Autowired
    public Messages() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setCacheSeconds(5);
        this.messageSource = messageSource;
    }

    public String get(String code,Locale locale) {
        return messageSource.getMessage(code,null,locale);
    }

    public String get(String code) {
        return messageSource.getMessage(code, null,getCurrentLocale());
    }

    public String get(String code,Object[] objects,Locale locale) {
        return messageSource.getMessage(code,objects,locale);
    }

    private Locale getCurrentLocale() {
        Locale locale = LocaleContextHolder.getLocale();

        return locale;
    }
}
