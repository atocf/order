package br.com.atocf.order.processor.util;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class DateUtilsTest {

    @Test
    void testConvertStringToDateWithValidISO8601() {
        String validDateString = "2025-02-18T01:26:07Z";

        Date date = DateUtils.convertStringToDate(validDateString);

        assertNotNull(date, "A data não deveria ser nula para uma string válida");
        assertEquals("Mon Feb 17 22:26:07 BRT 2025", date.toString());
    }

    @Test
    void testConvertStringToDateWithNull() {
        String nullString = null;

        Date date = DateUtils.convertStringToDate(nullString);

        assertNull(date, "O retorno deveria ser nulo para uma string nula");
    }

    @Test
    void testConvertStringToDateWithEmptyString() {
        String emptyString = "";

        Date date = DateUtils.convertStringToDate(emptyString);

        assertNull(date, "O retorno deveria ser nulo para uma string vazia");
    }

    @Test
    void testConvertStringToDateWithInvalidFormat() {
        String invalidDateString = "invalid-date";

        Date date = DateUtils.convertStringToDate(invalidDateString);

        assertNull(date, "O retorno deveria ser nulo para uma string de formato inválido");
    }
}