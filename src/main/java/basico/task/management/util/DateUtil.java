package basico.task.management.util;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Calendar;

public class DateUtil {

    public static Date getPreviousTimeInDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -3);
        Date threeMinutesBack = cal.getTime();
        return threeMinutesBack;
    }

}
