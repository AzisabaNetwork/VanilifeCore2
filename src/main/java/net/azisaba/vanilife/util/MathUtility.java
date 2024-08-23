package net.azisaba.vanilife.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Utility
public class MathUtility
{
    public static boolean isInt(String string)
    {
        Pattern pattern = Pattern.compile("^[0-9]+$");
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}
