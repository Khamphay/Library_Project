package com.mycompany.library_project.ControllerDAOModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

public class DateFormat {

    String stringFormater = "dd/MM/yyyy";

    public DateFormat() {
    }

    public DateFormat(String stringFormater) {
        this.stringFormater = stringFormater;
    }

    public DatePicker formateDatePicker(DatePicker datePicker) {
        datePicker.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter formater = DateTimeFormatter.ofPattern(stringFormater);

            @Override
            public String toString(LocalDate date) {
                if (date == null)
                    return "null";
                else
                    return formater.format(date);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, formater);
            }
        });

        return datePicker;
    }
}
