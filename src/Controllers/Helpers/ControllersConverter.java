package Controllers.Helpers;

import java.math.BigInteger;

public final class ControllersConverter
{
    // Metody dzialaja wylacznie dla prawidlowych danych

    public static byte[] binStringToByteArray(String value)
    {
        int valueLength = value.length();
        if(valueLength % 8 == 0)
        {
            BigInteger binValue = new BigInteger(value, 2);
            byte[] byteArray = binValue.toByteArray();
            if (byteArray[0] == 0)
            {
                byte[] tmp = new byte[byteArray.length - 1];
                System.arraycopy(byteArray, 1, tmp, 0, tmp.length);
                byteArray = tmp;
            }
            return byteArray;
        }
        else
            return null;
    }

    public static byte[] hexStringToByteArray(String value)
    {
        int valueLength = value.length();
        if(valueLength % 2 == 0)
        {
            BigInteger hexValue = new BigInteger(value, 16);
            byte[] byteArray = hexValue.toByteArray();
            if (byteArray[0] == 0)
            {
                byte[] tmp = new byte[byteArray.length - 1];
                System.arraycopy(byteArray, 1, tmp, 0, tmp.length);
                byteArray = tmp;
            }
            return byteArray;
        }
        else
            return null;
    }

}
