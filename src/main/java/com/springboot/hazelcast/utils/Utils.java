package com.springboot.hazelcast.utils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static LocalDate formatDate(LocalDate date) throws ParseException {

        String dateFormat = "MM/dd/yyyy";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);

        String formattedString = date.format(formatter);

        LocalDate localDate = LocalDate.parse(formattedString, formatter);

        return localDate;
    }
}
