package net.azisaba.vanilife.util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtility
{
    private static final Pattern hiragana = Pattern.compile("^[\\p{InHiragana}\\p{InCJKSymbolsAndPunctuation}\\p{InGeneralPunctuation}\\p{InHalfwidthAndFullwidthForms}]+$");

    public static boolean isHiragana(@NotNull String src)
    {
        Matcher matcher = StringUtility.hiragana.matcher(src);
        return matcher.matches();
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
