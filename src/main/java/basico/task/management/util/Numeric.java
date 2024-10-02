package basico.task.management.util;

import java.util.Objects;
import java.util.regex.Pattern;

public class Numeric {

    public  static boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (Objects.isNull(strNum)) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

}
