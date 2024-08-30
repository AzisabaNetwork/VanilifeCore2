package net.azisaba.vanilife.util;

import java.util.UUID;

public class UuidUtility
{
    public static boolean isUuid(String s)
    {
        try
        {
            UUID.fromString(s);
            return true;
        }
        catch (IllegalArgumentException e)
        {
            return false;
        }
    }
}
