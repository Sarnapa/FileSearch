package Controllers.Helpers;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ControllersConverter
{
    // Metody dzialaja wylacznie dla prawidlowych danych

    public static byte[] binStringToByteArray(String value)
    {
        int valueLength = value.length();
        if(valueLength == 0)
            return new byte[0];
        if(valueLength % 8 == 0)
        {
            BigInteger binValue = new BigInteger(value, 2);
            byte[] byteArray = binValue.toByteArray();
            byte[] tmp = new byte[valueLength / 8];
            if (byteArray[0] == 0 && binValue.longValue() != 0)
                System.arraycopy(byteArray, 1, tmp, tmp.length - byteArray.length + 1, byteArray.length - 1);
            else
                System.arraycopy(byteArray, 0, tmp, tmp.length - byteArray.length, byteArray.length);
            byteArray = tmp;
            return byteArray;
        }
        else
            return null;
    }

    public static byte[] hexStringToByteArray(String value)
    {
        int valueLength = value.length();
        if(valueLength == 0)
            return new byte[0];
        if(valueLength % 2 == 0)
        {
            BigInteger hexValue = new BigInteger(value, 16);
            byte[] byteArray = hexValue.toByteArray();
            byte[] tmp = new byte[valueLength / 2];
            if (byteArray[0] == 0 && hexValue.longValue() != 0)
                System.arraycopy(byteArray, 1, tmp, tmp.length - byteArray.length + 1, byteArray.length - 1);
            else
                System.arraycopy(byteArray, 0, tmp, tmp.length - byteArray.length, byteArray.length);
            byteArray = tmp;
            return byteArray;
        }
        else
            return null;
    }

    // Metody dla ResultConverter

    public static String getDateFormat(LocalDateTime time)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS");
        return time.format(formatter);
    }

}
