package com.mycompany.library_project.ControllerDAOModel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

public class MyDate {

    String stringFormater = "dd/MM/yyyy";

    public MyDate() {
    }

    public MyDate(String stringFormater) {
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

    public int cancalarDate(LocalDate date) {
        int outdate = 0;
        if (LocalDate.now().compareTo(date) > 0) {
            long day = ChronoUnit.DAYS.between(date, LocalDate.now());
            outdate = (int) day;
            DayOfWeek day_ow = date.getDayOfWeek();

            for (int i = 1; i <= day; i++) {
                if (day_ow.getDisplayName(TextStyle.FULL, Locale.getDefault()).equals("Sunday")
                        || day_ow.getDisplayName(TextStyle.FULL, Locale.getDefault()).equals("Saturday")) {
                    outdate = outdate - 1;
                }
                day_ow = day_ow.plus(1);
            }
        }
        return outdate;
    }
}
