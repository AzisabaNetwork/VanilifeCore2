package net.azisaba.vanilife.util;

public class SqlUtility
{
    public static boolean jdbc(String clazz)
    {
        try
        {
            Class.forName(clazz);
            return true;
        }
        catch (ClassNotFoundException e)
        {
            return false;
        }
    }
}
