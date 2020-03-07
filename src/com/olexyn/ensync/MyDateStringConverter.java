package com.olexyn.ensync;

import javafx.util.converter.DateStringConverter;

import java.util.Date;

public class MyDateStringConverter extends DateStringConverter {

    public MyDateStringConverter(final String pattern) {

        super(pattern);

    }

    @Override

    public Date fromString(String value) {

        // catches the RuntimeException thrown by

        // DateStringConverter.fromString()

        try {

            return super.fromString(value);

        } catch (RuntimeException ex) {

            return null;

        }

    }

}
