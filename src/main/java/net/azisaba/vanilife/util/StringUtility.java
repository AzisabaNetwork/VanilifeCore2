package net.azisaba.vanilife.util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Utility
public class StringUtility
{
    public static boolean isHiragana(@NotNull String src)
    {
        for (char ch : src.toCharArray())
        {
            if (!((ch >= '\u3040' && ch <= '\u309F') || (ch >= '\u3000' && ch <= '\u303F') || (ch >= '\uFF00' && ch <= '\uFFEF')))
            {
                return false;
            }
        }

        return true;
    }

    public static @NotNull Map<String, String> parameters(@NotNull String src)
    {
        Map<String, String> parameters = new HashMap<>();
        String[] parts = src.split("&");

        for (String part : parts)
        {
            String[] set = part.split("=", 2);

            if (set.length == 2)
            {
                parameters.put(set[0], set[1]);
            }
        }

        return parameters;
    }
}
