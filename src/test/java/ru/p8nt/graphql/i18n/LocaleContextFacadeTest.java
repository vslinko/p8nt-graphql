package ru.p8nt.graphql.i18n;

import org.springframework.context.i18n.LocaleContextHolder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Locale;

import static org.testng.Assert.assertEquals;

public class LocaleContextFacadeTest {
    private LocaleContextFacade localeContext;

    @BeforeMethod
    public void setUp() {
        localeContext = new LocaleContextFacade();
    }

    @Test
    public void testLocaleGetter() {
        LocaleContextHolder.setLocale(new Locale("zh_CN"));

        assertEquals(localeContext.getLocale().getLanguage(), "zh_cn");
    }
}
