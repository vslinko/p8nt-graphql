package ru.p8nt.graphql.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class LocalizationServiceTest {
    private MessageSource messageSource;
    private Locale locale;

    private LocalizationService localizationService;

    @BeforeMethod
    public void setUp() {
        LocaleContext localeContext = mock(LocaleContext.class);
        messageSource = mock(MessageSource.class);

        localizationService = new LocalizationService(localeContext, messageSource);
        locale = new Locale("zh_CN");

        when(localeContext.getLocale()).thenReturn(locale);
    }

    @Test
    public void testLocaleGetter() {
        assertEquals(localizationService.getCurrentLocale().getLanguage(), "zh_cn");
    }

    @Test
    public void testMessageGetter() {
        List<Object> objectList = Arrays.asList("a", "b");
        Object[] objects = objectList.toArray();

        when(messageSource.getMessage("message1", null, locale)).thenReturn("Message #1");
        when(messageSource.getMessage("message2", objects, locale)).thenReturn("Message #2");
        when(messageSource.getMessage("message3", objects, locale)).thenReturn("Message #3");

        assertEquals(localizationService.getMessage("message1"), "Message #1");
        assertEquals(localizationService.getMessage("message2", objectList), "Message #2");
        assertEquals(localizationService.getMessage("message3", objects), "Message #3");
    }
}
