package net.azisaba.vanilife.util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class StringUtility
{
    public static boolean isHiragana(@NotNull String src)
    {

        for (char c : src.toCharArray())
        {
            if (! StringUtility.isHiragana(c) && ! StringUtility.isFullWidthSymbol(c))
            {
                return false;
            }
        }

        return true;
    }

    public static boolean isHiragana(char c)
    {
        return c >= 'ぁ' && c <= 'ゖ';
    }

    public static boolean isFullWidthSymbol(char c)
    {
        return c >= '！' && c <= '～' || c == 'ー';
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
