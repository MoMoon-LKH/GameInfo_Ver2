package com.gmi.gameInfo.common.date;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonDateFunction {

    public Date getCalDate(int day) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);

        return formatter.parse(formatter.format(cal.getTime()));
    }
}
