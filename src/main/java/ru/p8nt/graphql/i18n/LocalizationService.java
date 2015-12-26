package ru.p8nt.graphql.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class LocalizationService {
    private LocaleContext localeContext;
    private MessageSource messageSource;

    @Autowired
    public LocalizationService(LocaleContext localeContext, MessageSource messageSource) {
        this.localeContext = localeContext;
        this.messageSource = messageSource;
    }

    public Locale getLocale() {
        return localeContext.getLocale();
    }

    public String getMessage(String key) {
        return messageSource.getMessage(key, null, getLocale());
    }

    public String getMessage(String key, List<Object> objectList) {
        return getMessage(key, objectList.toArray());
    }

    public String getMessage(String key, Object[] objects) {
        return messageSource.getMessage(key, objects, getLocale());
    }
}
