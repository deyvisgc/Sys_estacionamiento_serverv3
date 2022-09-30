package pe.edu.galaxy.training.parqueaderov1.utils;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Constantes {
    public static final String TIME = "HH:mm";
    public static final String FORMATDATETIME = "dd-MM-yyyy HH:mm";
    public static final String FORMATDATE = "yyyy-MM-dd";
    public static final String FORMATDATEYEARMONTH = "yyyy-MM";
    public static final Integer MEDIAHORA = 30;
    public static final Integer TOTALMINUTOSXHORA = 60;
    public static final Integer TOTALSEGUNDOSXHORA = 3600;
    public static final String URLAPIPERU = "https://apiperu.dev/api/dni/";
    public static final String TYPETOKEN = "Bearer";
    public static final String TOKEN = "8732963ea67346f79362e90a43a9dec045dff8714cb67c4b5173ee4f9fd8916d";
    public static String formatTime(LocalTime localTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME);
        return localTime.format(formatter);
    }
    public static String formatDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATDATETIME);
        return localDateTime.format(formatter);
    }


    public static String formatDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATDATE);
        return localDate.format(formatter);
    }
    public static String formatDateYearMonth(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATDATEYEARMONTH);
        return localDate.format(formatter);
    }
    public static List<String> meses() {
        List<String> meses = new ArrayList<>();
        /*
        meses.add("2022-01");
        meses.add("2022-02");
        meses.add("2022-03");
        meses.add("2022-04");
        meses.add("2022-05");

         */
        meses.add("2022-06");
        meses.add("2022-07");
        meses.add("2022-08");
        meses.add("2022-09");
        meses.add("2022-10");
        meses.add("2022-11");
        meses.add("2022-12");
        return meses;
    }
}
