/**

 IME developed by tksimeji.

 https://twitter.com/tksimeji

 **/


package net.azisaba.vanilife.util;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TkmizIme
{
    private static final Map<String, String> registry = new HashMap<>();

    private static final Map<String, String> dictionary = new HashMap<>();

    static
    {
        TkmizIme.register("a", "あ");
        TkmizIme.register("i", "い");
        TkmizIme.register("u", "う");
        TkmizIme.register("e", "え");
        TkmizIme.register("o", "お");
        TkmizIme.register("ka", "か");
        TkmizIme.register("ki", "き");
        TkmizIme.register("ku", "く");
        TkmizIme.register("ke", "け");
        TkmizIme.register("ko", "こ");
        TkmizIme.register("sa", "さ");
        TkmizIme.register("si", "し");
        TkmizIme.register("shi", "し");
        TkmizIme.register("su", "す");
        TkmizIme.register("se", "せ");
        TkmizIme.register("so", "そ");
        TkmizIme.register("ta", "た");
        TkmizIme.register("ti", "ち");
        TkmizIme.register("tu", "つ");
        TkmizIme.register("te", "て");
        TkmizIme.register("to", "と");
        TkmizIme.register("na", "な");
        TkmizIme.register("ni", "に");
        TkmizIme.register("nu", "ぬ");
        TkmizIme.register("ne", "ね");
        TkmizIme.register("no", "の");
        TkmizIme.register("ha", "は");
        TkmizIme.register("hi", "ひ");
        TkmizIme.register("hu", "ふ");
        TkmizIme.register("he", "へ");
        TkmizIme.register("ho", "ほ");
        TkmizIme.register("ma", "ま");
        TkmizIme.register("mi", "み");
        TkmizIme.register("mu", "む");
        TkmizIme.register("me", "め");
        TkmizIme.register("mo", "も");
        TkmizIme.register("ya", "や");
        TkmizIme.register("yu", "ゆ");
        TkmizIme.register("yo", "よ");
        TkmizIme.register("ra", "ら");
        TkmizIme.register("ri", "り");
        TkmizIme.register("ru", "る");
        TkmizIme.register("re", "れ");
        TkmizIme.register("ro", "ろ");
        TkmizIme.register("wa", "わ");
        TkmizIme.register("wo", "を");
        TkmizIme.register("n", "ん");

        TkmizIme.register("ga", "が");
        TkmizIme.register("gi", "ぎ");
        TkmizIme.register("gu", "ぐ");
        TkmizIme.register("ge", "げ");
        TkmizIme.register("go", "ご");
        TkmizIme.register("za", "ざ");
        TkmizIme.register("zi", "じ");
        TkmizIme.register("ji", "じ");
        TkmizIme.register("zu", "ず");
        TkmizIme.register("ze", "ぜ");
        TkmizIme.register("zo", "ぞ");
        TkmizIme.register("da", "だ");
        TkmizIme.register("di", "ぢ");
        TkmizIme.register("du", "づ");
        TkmizIme.register("de", "で");
        TkmizIme.register("do", "ど");
        TkmizIme.register("ba", "ば");
        TkmizIme.register("bi", "び");
        TkmizIme.register("bu", "ぶ");
        TkmizIme.register("be", "べ");
        TkmizIme.register("bo", "ぼ");
        TkmizIme.register("pa", "ぱ");
        TkmizIme.register("pi", "ぴ");
        TkmizIme.register("pu", "ぷ");
        TkmizIme.register("pe", "ぺ");
        TkmizIme.register("po", "ぽ");

        TkmizIme.register("la", "ぁ");
        TkmizIme.register("li", "ぃ");
        TkmizIme.register("lu", "ぅ");
        TkmizIme.register("le", "ぇ");
        TkmizIme.register("lo", "ぉ");
        TkmizIme.register("ltu", "っ");
        TkmizIme.register("lya", "ゃ");
        TkmizIme.register("lyu", "ゅ");
        TkmizIme.register("lyo", "ょ");

        TkmizIme.register("dda", "っだ");
        TkmizIme.register("ddi", "っぢ");
        TkmizIme.register("ddu", "っづ");
        TkmizIme.register("dde", "っで");
        TkmizIme.register("ddo", "っど");
        TkmizIme.register("dya", "ぢゃ");
        TkmizIme.register("dyi", "ぢぃ");
        TkmizIme.register("dyu", "ぢゅ");
        TkmizIme.register("dye", "ぢぇ");
        TkmizIme.register("dyo", "ぢょ");
        TkmizIme.register("dha", "でゃ");
        TkmizIme.register("dhi", "でぃ");
        TkmizIme.register("dhu", "でゅ");
        TkmizIme.register("dhe", "でぇ");
        TkmizIme.register("dho", "でょ");
        TkmizIme.register("dhy", "ぢぃ");

        TkmizIme.register("fa", "ふぁ");
        TkmizIme.register("fi", "ふぃ");
        TkmizIme.register("fe", "ふぇ");
        TkmizIme.register("fo", "ふぉ");
        TkmizIme.register("fya", "ふゃ");
        TkmizIme.register("fyi", "ふぃ");
        TkmizIme.register("fyu", "ふゅ");
        TkmizIme.register("fye", "ふぇ");
        TkmizIme.register("fyo", "ふょ");

        TkmizIme.register("gga", "っが");
        TkmizIme.register("ggu", "っぐ");
        TkmizIme.register("gge", "っげ");
        TkmizIme.register("ggo", "っご");
        TkmizIme.register("gya", "ぎゃ");
        TkmizIme.register("gyu", "ぎゅ");
        TkmizIme.register("gyo", "ぎょ");

        TkmizIme.register("hha", "っは");
        TkmizIme.register("hhi", "っひ");
        TkmizIme.register("hhu", "っふ");
        TkmizIme.register("hhe", "っへ");
        TkmizIme.register("hho", "っほ");
        TkmizIme.register("hya", "ひゃ");
        TkmizIme.register("hyi", "ひぃ");
        TkmizIme.register("hyu","ひゅ");
        TkmizIme.register("hyo", "ひょ");
        TkmizIme.register("hye", "ひぇ");

        TkmizIme.register("ja", "じゃ");
        TkmizIme.register("ju", "じゅ");
        TkmizIme.register("je", "じぇ");
        TkmizIme.register("jo", "じょ");
        TkmizIme.register("jja", "っじゃ");
        TkmizIme.register("jji", "っじ");
        TkmizIme.register("jju", "っじゅ");
        TkmizIme.register("jje", "っじぇ");
        TkmizIme.register("jjo", "っじょ");
        TkmizIme.register("jya", "じゃ");
        TkmizIme.register("jyi", "じぃ");
        TkmizIme.register("jyu", "じゅ");
        TkmizIme.register("jye", "じぇ");
        TkmizIme.register("jyo", "じょ");

        TkmizIme.register("kka", "っか");
        TkmizIme.register("kki", "っき");
        TkmizIme.register("kku", "っく");
        TkmizIme.register("kke", "っけ");
        TkmizIme.register("kko", "っこ");
        TkmizIme.register("kya", "きゃ");
        TkmizIme.register("kyi", "きぃ");
        TkmizIme.register("kyu", "きゅ");
        TkmizIme.register("kye", "きぇ");
        TkmizIme.register("kyo", "きょ");

        TkmizIme.register("mma", "っま");
        TkmizIme.register("mmi", "っみ");
        TkmizIme.register("mmu", "っむ");
        TkmizIme.register("mme", "っめ");
        TkmizIme.register("mmo", "っも");
        TkmizIme.register("mya", "みゃ");
        TkmizIme.register("myi", "みぃ");
        TkmizIme.register("myu", "みゅ");
        TkmizIme.register("mye", "みぇ");
        TkmizIme.register("myo", "みょ");

        TkmizIme.register("nya", "にゃ");
        TkmizIme.register("nyi", "にぃ");
        TkmizIme.register("nyu", "にゅ");
        TkmizIme.register("nye", "にぇ");
        TkmizIme.register("nyo", "にょ");

        TkmizIme.register("ppa", "っぱ");
        TkmizIme.register("ppi", "っぴ");
        TkmizIme.register("ppu", "っぷ");
        TkmizIme.register("ppe", "っぺ");
        TkmizIme.register("ppo", "っぽ");
        TkmizIme.register("pya", "ぴゃ");
        TkmizIme.register("pyi", "ぴぃ");
        TkmizIme.register("pyu", "ぴゅ");
        TkmizIme.register("pye", "ぴぇ");
        TkmizIme.register("pyo", "ぴょ");

        TkmizIme.register("rra", "っら");
        TkmizIme.register("rri", "っり");
        TkmizIme.register("rru", "っる");
        TkmizIme.register("rre", "っれ");
        TkmizIme.register("rro", "っろ");
        TkmizIme.register("rya", "りょ");
        TkmizIme.register("ryi", "りぃ");
        TkmizIme.register("ryu", "りゅ");
        TkmizIme.register("rye", "りぇ");
        TkmizIme.register("ryo", "りょ");

        TkmizIme.register("ssa", "っさ");
        TkmizIme.register("ssi", "っし");
        TkmizIme.register("ssu", "っす");
        TkmizIme.register("sse", "っせ");
        TkmizIme.register("sso", "っそ");
        TkmizIme.register("sya", "しゃ");
        TkmizIme.register("syi", "しぃ");
        TkmizIme.register("syu", "しゅ");
        TkmizIme.register("sye", "しぇ");
        TkmizIme.register("syo", "しょ");
        TkmizIme.register("sha", "しゃ");
        TkmizIme.register("shi", "し");
        TkmizIme.register("shu", "しゅ");
        TkmizIme.register("she", "しぇ");
        TkmizIme.register("sho", "しょ");

        TkmizIme.register("tta", "った");
        TkmizIme.register("tti", "っち");
        TkmizIme.register("ttu", "っつ");
        TkmizIme.register("tte", "って");
        TkmizIme.register("tto", "っと");
        TkmizIme.register("tya", "ちゃ");
        TkmizIme.register("tyi", "ちぃ");
        TkmizIme.register("tyu", "ちゅ");
        TkmizIme.register("tye", "ちぇ");
        TkmizIme.register("tyo", "ちょ");
        TkmizIme.register("tha", "てゃ");
        TkmizIme.register("thi", "てぃ");
        TkmizIme.register("thu", "てゅ");
        TkmizIme.register("the", "てぇ");
        TkmizIme.register("tho", "てょ");

        TkmizIme.register("va", "ヴぁ");
        TkmizIme.register("vi", "ヴぃ");
        TkmizIme.register("vu", "ヴ");
        TkmizIme.register("ve", "ヴぇ");
        TkmizIme.register("vo", "ヴぉ");
        TkmizIme.register("vya", "ヴゃ");
        TkmizIme.register("vyi", "ヴぃ");
        TkmizIme.register("vyu", "ヴゅ");
        TkmizIme.register("vye", "ヴぇ");
        TkmizIme.register("vyo", "ヴょ");

        TkmizIme.register("wi", "うぃ");
        TkmizIme.register("we", "うぇ");
        TkmizIme.register("wwa", "っわ");
        TkmizIme.register("wwi", "っうぃ");
        TkmizIme.register("wwu", "っう");
        TkmizIme.register("wwe", "っうぇ");
        TkmizIme.register("wwo", "っを");

        TkmizIme.register("!", "！");
        TkmizIme.register("?", "？");
        TkmizIme.register("[", "「");
        TkmizIme.register("]", "」");
        TkmizIme.register("(", "（");
        TkmizIme.register(")", "）");
        TkmizIme.register("-", "ー");
        TkmizIme.register("~", "～");
        TkmizIme.register(",", "、");
        TkmizIme.register(".", "。");

        JsonObject dictionary = ResourceUtility.getJsonResource("dictionary.json").getAsJsonObject();
        dictionary.keySet().forEach(key -> TkmizIme.dictionary.put(key, dictionary.get(key).getAsString()));
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
                    String suggest = TkmizIme.registry.get(substring);

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

    private static void register(@NotNull String roman, @NotNull String hiragana)
    {
        TkmizIme.registry.put(roman.toLowerCase(), hiragana);
    }
}
