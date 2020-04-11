package numberserver.stringutils;

public class StringUtils {

    public static boolean tryParseInteger(String strInteger)
    {
        try {
            Integer.parseInt(strInteger);
            return true;
        }
        catch (NumberFormatException ex)
        {
            return false;
        }
    }
}
