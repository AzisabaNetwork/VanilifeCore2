package net.azisaba.vanilife.util;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MetubouIME
{
    private static final Map<String, String> registry = new HashMap<>();

    private static final Map<String, String> dictionary = new HashMap<>();

    static
    {
        MetubouIME.register("a", "あ");
        MetubouIME.register("i", "い");
        MetubouIME.register("u", "う");
        MetubouIME.register("e", "え");
        MetubouIME.register("o", "お");
        MetubouIME.register("ka", "か");
        MetubouIME.register("ki", "き");
        MetubouIME.register("ku", "く");
        MetubouIME.register("ke", "け");
        MetubouIME.register("ko", "こ");
        MetubouIME.register("sa", "さ");
        MetubouIME.register("si", "し");
        MetubouIME.register("su", "す");
        MetubouIME.register("se", "せ");
        MetubouIME.register("so", "そ");
        MetubouIME.register("ta", "た");
        MetubouIME.register("ti", "ち");
        MetubouIME.register("tu", "つ");
        MetubouIME.register("te", "て");
        MetubouIME.register("to", "と");
        MetubouIME.register("na", "な");
        MetubouIME.register("ni", "に");
        MetubouIME.register("nu", "ぬ");
        MetubouIME.register("ne", "ね");
        MetubouIME.register("no", "の");
        MetubouIME.register("ha", "は");
        MetubouIME.register("hi", "ひ");
        MetubouIME.register("hu", "ふ");
        MetubouIME.register("he", "へ");
        MetubouIME.register("ho", "ほ");
        MetubouIME.register("ma", "ま");
        MetubouIME.register("mi", "み");
        MetubouIME.register("mu", "む");
        MetubouIME.register("me", "め");
        MetubouIME.register("mo", "も");
        MetubouIME.register("ya", "や");
        MetubouIME.register("yu", "ゆ");
        MetubouIME.register("yo", "よ");
        MetubouIME.register("ra", "ら");
        MetubouIME.register("ri", "り");
        MetubouIME.register("ru", "る");
        MetubouIME.register("re", "れ");
        MetubouIME.register("ro", "ろ");
        MetubouIME.register("wa", "わ");
        MetubouIME.register("wo", "を");
        MetubouIME.register("n", "ん");
        MetubouIME.register("ga", "が");
        MetubouIME.register("gi", "ぎ");
        MetubouIME.register("gu", "ぐ");
        MetubouIME.register("ge", "げ");
        MetubouIME.register("go", "ご");
        MetubouIME.register("za", "ざ");
        MetubouIME.register("zi", "じ");
        MetubouIME.register("ji", "じ");
        MetubouIME.register("zu", "ず");
        MetubouIME.register("ze", "ぜ");
        MetubouIME.register("zo", "ぞ");
        MetubouIME.register("da", "だ");
        MetubouIME.register("di", "ぢ");
        MetubouIME.register("du", "づ");
        MetubouIME.register("de", "で");
        MetubouIME.register("do", "ど");
        MetubouIME.register("ba", "ば");
        MetubouIME.register("bi", "び");
        MetubouIME.register("bu", "ぶ");
        MetubouIME.register("be", "べ");
        MetubouIME.register("bo", "ぼ");
        MetubouIME.register("pa", "ぱ");
        MetubouIME.register("pi", "ぴ");
        MetubouIME.register("pu", "ぷ");
        MetubouIME.register("pe", "ぺ");
        MetubouIME.register("po", "ぽ");
        MetubouIME.register("la", "ぁ");
        MetubouIME.register("li", "ぃ");
        MetubouIME.register("lu", "ぅ");
        MetubouIME.register("le", "ぇ");
        MetubouIME.register("lo", "ぉ");
        MetubouIME.register("ltu", "っ");
        MetubouIME.register("lya", "ゃ");
        MetubouIME.register("lyu", "ゅ");
        MetubouIME.register("lyo", "ょ");
        MetubouIME.register("tya", "ちゃ");
        MetubouIME.register("tyu", "ちゅ");
        MetubouIME.register("tyo", "ちょ");
        MetubouIME.register("tta", "った");
        MetubouIME.register("tto", "っと");
        MetubouIME.register("ttu", "っつ");
        MetubouIME.register("tte", "って");
        MetubouIME.register("wi", "うぃ");
        MetubouIME.register("ryu", "りゅ");
        MetubouIME.register("shu", "しゅ");
        MetubouIME.register("syu", "しゅ");
        MetubouIME.register("sha", "しゃ");
        MetubouIME.register("sya", "しゃ");
        MetubouIME.register("kyu", "きゅ");
        MetubouIME.register("kyo", "きょ");
        MetubouIME.register("!", "！");
        MetubouIME.register("?", "？");
        MetubouIME.register("[", "「");
        MetubouIME.register("]", "」");
        MetubouIME.register("(", "（");
        MetubouIME.register(")", "）");
        MetubouIME.register("-", "ー");
        MetubouIME.register(",", "、");
        MetubouIME.register(".", "。");

        JsonObject dictionary = ResourceUtility.getJsonResource("dictionary.json").getAsJsonObject();
        dictionary.keySet().forEach(key -> MetubouIME.dictionary.put(key, dictionary.get(key).getAsString()));
    }

    public static @NotNull String convert(@NotNull String src)
    {
        StringBuilder builder = new StringBuilder();
        int i = 0;

        while (i < src.length())
        {
            String longestMatch = null;
            int longestMatchLength = 0;

            for (int j = 1; j <= src.length() - i; j++)
            {
                String substring = src.substring(i, i + j);

                if (dictionary.containsKey(substring) && j > longestMatchLength)
                {
                    longestMatch = substring;
                    longestMatchLength = j;
                }
            }

            if (longestMatch != null)
            {
                builder.append(dictionary.get(longestMatch));
                i += longestMatchLength;
            }
            else
            {
                builder.append(src.charAt(i));
                i ++;
            }
        }

        return builder.toString();
    }

    public static @NotNull String hira(@NotNull String src)
    {
        src = src.toLowerCase();
        StringBuilder builder = new StringBuilder();

        int i = 0;

        while (i < src.length())
        {
            boolean found = false;

            for (int length = 3; length > 0; length --)
            {
                if (i + length <= src.length())
                {
                    String substring = src.substring(i, i + length);
                    String suggest = MetubouIME.registry.get(substring);

                    if (suggest != null)
                    {
                        builder.append(suggest);
                        i += length;
                        found = true;
                        break;
                    }
                }
            }

            if (! found)
            {
                builder.append(src.charAt(i));
                i ++;
            }
        }

        return builder.toString();
    }

    private static void register(String roman, String hiragana)
    {
        MetubouIME.registry.put(roman.toLowerCase(), hiragana);
    }
}
