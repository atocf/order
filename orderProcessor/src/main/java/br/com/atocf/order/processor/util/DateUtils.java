package br.com.atocf.order.processor.util;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateUtils {

    /**
     * Converte uma string de data para um objeto Date.
     *
     * @param dateString String no formato ISO 8601 (exemplo: "2025-02-18T01:26:07Z").
     * @return Objeto Date correspondente à string de entrada ou null se a conversão falhar.
     */
    public static Date convertStringToDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        try {
            Instant instant = Instant.parse(dateString);
            return Date.from(instant);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}