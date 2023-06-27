package ga.justreddy.wiki.tnttag.util;

import java.util.concurrent.ThreadLocalRandom;

public class NumberUtil {

    public static boolean percentChance(int percent) {
        if (!(percent > 100.0) && !(percent < 0.0)) {
            if (percent == 0.0) {
                return false;
            } else {
                int result = ThreadLocalRandom.current().nextInt() * 100;
                return result <= percent;
            }
        } else {
            throw new IllegalArgumentException("Percentage cannot be greater than 100 or less than 0!");
        }
    }

    public static String toFormat(int seconds) {
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        return twoDigitString(minutes) + ":" + twoDigitString(seconds);
    }

    private static String twoDigitString(int number) {
        if (number == 0) {
            return "00";
        }
        if (number / 10 == 0) {
            return "0" + number;
        }
        return String.valueOf(number);
    }

}
