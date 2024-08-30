package net.azisaba.vanilife.ime;

import java.util.HashMap;
import java.util.Map;

public class AzisabaIME
{
    private static final Map<String, String> registry = new HashMap<>();

    static
    {
        AzisabaIME.register("a", "あ");
        AzisabaIME.register("i", "い");
        AzisabaIME.register("u", "う");
        AzisabaIME.register("e", "え");
        AzisabaIME.register("o", "お");
        AzisabaIME.register("ka", "か");
        AzisabaIME.register("ki", "き");
        AzisabaIME.register("ku", "く");
        AzisabaIME.register("ke", "け");
        AzisabaIME.register("ko", "こ");
        AzisabaIME.register("sa", "さ");
        AzisabaIME.register("si", "し");
        AzisabaIME.register("su", "す");
        AzisabaIME.register("se", "せ");
        AzisabaIME.register("so", "そ");
        AzisabaIME.register("ta", "た");
        AzisabaIME.register("ti", "ち");
        AzisabaIME.register("tu", "つ");
        AzisabaIME.register("te", "て");
        AzisabaIME.register("to", "と");
        AzisabaIME.register("na", "な");
        AzisabaIME.register("ni", "に");
        AzisabaIME.register("nu", "ぬ");
        AzisabaIME.register("ne", "ね");
        AzisabaIME.register("no", "の");
        AzisabaIME.register("ha", "は");
        AzisabaIME.register("hi", "ひ");
        AzisabaIME.register("hu", "ふ");
        AzisabaIME.register("he", "へ");
        AzisabaIME.register("ho", "ほ");
        AzisabaIME.register("ma", "ま");
        AzisabaIME.register("mi", "み");
        AzisabaIME.register("mu", "む");
        AzisabaIME.register("me", "め");
        AzisabaIME.register("mo", "も");
        AzisabaIME.register("ya", "や");
        AzisabaIME.register("yu", "ゆ");
        AzisabaIME.register("yo", "よ");
        AzisabaIME.register("ra", "ら");
        AzisabaIME.register("ri", "り");
        AzisabaIME.register("ru", "る");
        AzisabaIME.register("re", "れ");
        AzisabaIME.register("ro", "ろ");
        AzisabaIME.register("wa", "わ");
        AzisabaIME.register("wo", "を");
        AzisabaIME.register("nn", "ん");
        AzisabaIME.register("n", "ん");
        AzisabaIME.register("ga", "が");
        AzisabaIME.register("gi", "ぎ");
        AzisabaIME.register("gu", "ぐ");
        AzisabaIME.register("ge", "げ");
        AzisabaIME.register("go", "ご");
        AzisabaIME.register("za", "ざ");
        AzisabaIME.register("zi", "じ");
        AzisabaIME.register("ji", "じ");
        AzisabaIME.register("zu", "ず");
        AzisabaIME.register("ze", "ぜ");
        AzisabaIME.register("zo", "ぞ");
        AzisabaIME.register("da", "だ");
        AzisabaIME.register("di", "ぢ");
        AzisabaIME.register("du", "づ");
        AzisabaIME.register("de", "で");
        AzisabaIME.register("do", "ど");
        AzisabaIME.register("ba", "ば");
        AzisabaIME.register("bi", "び");
        AzisabaIME.register("bu", "ぶ");
        AzisabaIME.register("be", "べ");
        AzisabaIME.register("bo", "ぼ");
        AzisabaIME.register("pa", "ぱ");
        AzisabaIME.register("pi", "ぴ");
        AzisabaIME.register("pu", "ぷ");
        AzisabaIME.register("pe", "ぺ");
        AzisabaIME.register("po", "ぽ");
    }

    public static String convert(String src)
    {
        src = src.toLowerCase();
        StringBuilder hiragana = new StringBuilder();

        int i = 0;

        while (i < src.length())
        {
            boolean found = false;

            for (int length = 3; length > 0; length --)
            {
                if (i + length <= src.length())
                {
                    String substring = src.substring(i, i + length);
                    String suggest = AzisabaIME.registry.get(substring);

                    if (suggest != null)
                    {
                        hiragana.append(suggest);
                        i += length;
                        found = true;
                        break;
                    }
                }
            }

            if (! found)
            {
                hiragana.append(src.charAt(i));
                i ++;
            }
        }

        return hiragana.toString();
    }

    private static void register(String roman, String hiragana)
    {
        AzisabaIME.registry.put(roman.toLowerCase(), hiragana);
    }
}
