package ru.p8nt.graphql.i18n;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleContextFacade implements LocaleContext {
    @Override
    public Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }
}
