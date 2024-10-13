/**

 IME developed by tksimeji.

 https://twitter.com/tksimeji

 **/


package net.azisaba.vanilife.gomenne;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.ResourceUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Gomenne
{
    private static final Map<String, String> registry = new HashMap<>();

    private static final Map<String, String> dictionary = new HashMap<>();

    public static boolean isValid(@NotNull User sender, @NotNull String message)
    {
        return Language.getInstance(sender).getId().equals("ja-jp") &&
                message.matches("^[A-Za-z0-9 !?.,~()-]*$") &&
                sender.read("settings.ime").getAsBoolean() &&
                ! message.contains(":") &&
                ! message.contains("!1") &&
                ! message.contains("!2") &&
                ! message.contains("!3") &&
                ! message.contains("!4");
    }

    public static boolean isValid(@NotNull Player sender, @NotNull String message)
    {
        return Gomenne.isValid(User.getInstance(sender), message);
    }

    static
    {
        Gomenne.key("a", "あ");
        Gomenne.key("i", "い");
        Gomenne.key("u", "う");
        Gomenne.key("e", "え");
        Gomenne.key("o", "お");
        Gomenne.key("ka", "か");
        Gomenne.key("ki", "き");
        Gomenne.key("ku", "く");
        Gomenne.key("ke", "け");
        Gomenne.key("ko", "こ");
        Gomenne.key("sa", "さ");
        Gomenne.key("si", "し");
        Gomenne.key("shi", "し");
        Gomenne.key("su", "す");
        Gomenne.key("se", "せ");
        Gomenne.key("so", "そ");
        Gomenne.key("ta", "た");
        Gomenne.key("ti", "ち");
        Gomenne.key("tu", "つ");
        Gomenne.key("te", "て");
        Gomenne.key("to", "と");
        Gomenne.key("na", "な");
        Gomenne.key("ni", "に");
        Gomenne.key("nu", "ぬ");
        Gomenne.key("ne", "ね");
        Gomenne.key("no", "の");
        Gomenne.key("ha", "は");
        Gomenne.key("hi", "ひ");
        Gomenne.key("hu", "ふ");
        Gomenne.key("fu", "ふ");
        Gomenne.key("he", "へ");
        Gomenne.key("ho", "ほ");
        Gomenne.key("ma", "ま");
        Gomenne.key("mi", "み");
        Gomenne.key("mu", "む");
        Gomenne.key("me", "め");
        Gomenne.key("mo", "も");
        Gomenne.key("ya", "や");
        Gomenne.key("yu", "ゆ");
        Gomenne.key("yo", "よ");
        Gomenne.key("ra", "ら");
        Gomenne.key("ri", "り");
        Gomenne.key("ru", "る");
        Gomenne.key("re", "れ");
        Gomenne.key("ro", "ろ");
        Gomenne.key("wa", "わ");
        Gomenne.key("wo", "を");
        Gomenne.key("n", "ん");

        Gomenne.key("ga", "が");
        Gomenne.key("gi", "ぎ");
        Gomenne.key("gu", "ぐ");
        Gomenne.key("ge", "げ");
        Gomenne.key("go", "ご");
        Gomenne.key("za", "ざ");
        Gomenne.key("zi", "じ");
        Gomenne.key("ji", "じ");
        Gomenne.key("zu", "ず");
        Gomenne.key("ze", "ぜ");
        Gomenne.key("zo", "ぞ");
        Gomenne.key("da", "だ");
        Gomenne.key("di", "ぢ");
        Gomenne.key("du", "づ");
        Gomenne.key("de", "で");
        Gomenne.key("do", "ど");
        Gomenne.key("ba", "ば");
        Gomenne.key("bi", "び");
        Gomenne.key("bu", "ぶ");
        Gomenne.key("be", "べ");
        Gomenne.key("bo", "ぼ");
        Gomenne.key("pa", "ぱ");
        Gomenne.key("pi", "ぴ");
        Gomenne.key("pu", "ぷ");
        Gomenne.key("pe", "ぺ");
        Gomenne.key("po", "ぽ");

        Gomenne.key("la", "ぁ");
        Gomenne.key("li", "ぃ");
        Gomenne.key("lu", "ぅ");
        Gomenne.key("le", "ぇ");
        Gomenne.key("lo", "ぉ");
        Gomenne.key("ltu", "っ");
        Gomenne.key("lya", "ゃ");
        Gomenne.key("lyu", "ゅ");
        Gomenne.key("lyo", "ょ");

        Gomenne.key("dda", "っだ");
        Gomenne.key("ddi", "っぢ");
        Gomenne.key("ddu", "っづ");
        Gomenne.key("dde", "っで");
        Gomenne.key("ddo", "っど");
        Gomenne.key("dya", "ぢゃ");
        Gomenne.key("dyi", "ぢぃ");
        Gomenne.key("dyu", "ぢゅ");
        Gomenne.key("dye", "ぢぇ");
        Gomenne.key("dyo", "ぢょ");
        Gomenne.key("dha", "でゃ");
        Gomenne.key("dhi", "でぃ");
        Gomenne.key("dhu", "でゅ");
        Gomenne.key("dhe", "でぇ");
        Gomenne.key("dho", "でょ");
        Gomenne.key("dhy", "ぢぃ");

        Gomenne.key("fa", "ふぁ");
        Gomenne.key("fi", "ふぃ");
        Gomenne.key("fe", "ふぇ");
        Gomenne.key("fo", "ふぉ");
        Gomenne.key("fya", "ふゃ");
        Gomenne.key("fyi", "ふぃ");
        Gomenne.key("fyu", "ふゅ");
        Gomenne.key("fye", "ふぇ");
        Gomenne.key("fyo", "ふょ");

        Gomenne.key("gga", "っが");
        Gomenne.key("ggu", "っぐ");
        Gomenne.key("gge", "っげ");
        Gomenne.key("ggo", "っご");
        Gomenne.key("gya", "ぎゃ");
        Gomenne.key("gyu", "ぎゅ");
        Gomenne.key("gyo", "ぎょ");

        Gomenne.key("hha", "っは");
        Gomenne.key("hhi", "っひ");
        Gomenne.key("hhu", "っふ");
        Gomenne.key("hhe", "っへ");
        Gomenne.key("hho", "っほ");
        Gomenne.key("hya", "ひゃ");
        Gomenne.key("hyi", "ひぃ");
        Gomenne.key("hyu","ひゅ");
        Gomenne.key("hyo", "ひょ");
        Gomenne.key("hye", "ひぇ");

        Gomenne.key("ja", "じゃ");
        Gomenne.key("ju", "じゅ");
        Gomenne.key("je", "じぇ");
        Gomenne.key("jo", "じょ");
        Gomenne.key("jja", "っじゃ");
        Gomenne.key("jji", "っじ");
        Gomenne.key("jju", "っじゅ");
        Gomenne.key("jje", "っじぇ");
        Gomenne.key("jjo", "っじょ");
        Gomenne.key("jya", "じゃ");
        Gomenne.key("jyi", "じぃ");
        Gomenne.key("jyu", "じゅ");
        Gomenne.key("jye", "じぇ");
        Gomenne.key("jyo", "じょ");

        Gomenne.key("kka", "っか");
        Gomenne.key("kki", "っき");
        Gomenne.key("kku", "っく");
        Gomenne.key("kke", "っけ");
        Gomenne.key("kko", "っこ");
        Gomenne.key("kya", "きゃ");
        Gomenne.key("kyi", "きぃ");
        Gomenne.key("kyu", "きゅ");
        Gomenne.key("kye", "きぇ");
        Gomenne.key("kyo", "きょ");

        Gomenne.key("mma", "っま");
        Gomenne.key("mmi", "っみ");
        Gomenne.key("mmu", "っむ");
        Gomenne.key("mme", "っめ");
        Gomenne.key("mmo", "っも");
        Gomenne.key("mya", "みゃ");
        Gomenne.key("myi", "みぃ");
        Gomenne.key("myu", "みゅ");
        Gomenne.key("mye", "みぇ");
        Gomenne.key("myo", "みょ");

        Gomenne.key("nya", "にゃ");
        Gomenne.key("nyi", "にぃ");
        Gomenne.key("nyu", "にゅ");
        Gomenne.key("nye", "にぇ");
        Gomenne.key("nyo", "にょ");

        Gomenne.key("ppa", "っぱ");
        Gomenne.key("ppi", "っぴ");
        Gomenne.key("ppu", "っぷ");
        Gomenne.key("ppe", "っぺ");
        Gomenne.key("ppo", "っぽ");
        Gomenne.key("pya", "ぴゃ");
        Gomenne.key("pyi", "ぴぃ");
        Gomenne.key("pyu", "ぴゅ");
        Gomenne.key("pye", "ぴぇ");
        Gomenne.key("pyo", "ぴょ");

        Gomenne.key("rra", "っら");
        Gomenne.key("rri", "っり");
        Gomenne.key("rru", "っる");
        Gomenne.key("rre", "っれ");
        Gomenne.key("rro", "っろ");
        Gomenne.key("rya", "りょ");
        Gomenne.key("ryi", "りぃ");
        Gomenne.key("ryu", "りゅ");
        Gomenne.key("rye", "りぇ");
        Gomenne.key("ryo", "りょ");

        Gomenne.key("ssa", "っさ");
        Gomenne.key("ssi", "っし");
        Gomenne.key("ssu", "っす");
        Gomenne.key("sse", "っせ");
        Gomenne.key("sso", "っそ");
        Gomenne.key("sya", "しゃ");
        Gomenne.key("syi", "しぃ");
        Gomenne.key("syu", "しゅ");
        Gomenne.key("sye", "しぇ");
        Gomenne.key("syo", "しょ");
        Gomenne.key("sha", "しゃ");
        Gomenne.key("shi", "し");
        Gomenne.key("shu", "しゅ");
        Gomenne.key("she", "しぇ");
        Gomenne.key("sho", "しょ");

        Gomenne.key("tta", "った");
        Gomenne.key("tti", "っち");
        Gomenne.key("ttu", "っつ");
        Gomenne.key("tte", "って");
        Gomenne.key("tto", "っと");
        Gomenne.key("tya", "ちゃ");
        Gomenne.key("tyi", "ちぃ");
        Gomenne.key("tyu", "ちゅ");
        Gomenne.key("tye", "ちぇ");
        Gomenne.key("tyo", "ちょ");
        Gomenne.key("tha", "てゃ");
        Gomenne.key("thi", "てぃ");
        Gomenne.key("thu", "てゅ");
        Gomenne.key("the", "てぇ");
        Gomenne.key("tho", "てょ");

        Gomenne.key("va", "ヴぁ");
        Gomenne.key("vi", "ヴぃ");
        Gomenne.key("vu", "ヴ");
        Gomenne.key("ve", "ヴぇ");
        Gomenne.key("vo", "ヴぉ");
        Gomenne.key("vya", "ヴゃ");
        Gomenne.key("vyi", "ヴぃ");
        Gomenne.key("vyu", "ヴゅ");
        Gomenne.key("vye", "ヴぇ");
        Gomenne.key("vyo", "ヴょ");

        Gomenne.key("wi", "うぃ");
        Gomenne.key("we", "うぇ");
        Gomenne.key("wwa", "っわ");
        Gomenne.key("wwi", "っうぃ");
        Gomenne.key("wwu", "っう");
        Gomenne.key("wwe", "っうぇ");
        Gomenne.key("wwo", "っを");

        Gomenne.key("zya", "じゃ");
        Gomenne.key("zyi", "じぃ");
        Gomenne.key("zyu", "じゅ");
        Gomenne.key("zye", "じぇ");
        Gomenne.key("zyo", "じょ");

        Gomenne.key("!", "！");
        Gomenne.key("?", "？");
        Gomenne.key("[", "「");
        Gomenne.key("]", "」");
        Gomenne.key("(", "(");
        Gomenne.key(")", ")");
        Gomenne.key("-", "ー");
        Gomenne.key("~", "～");
        Gomenne.key(",", "、");
        Gomenne.key(".", "。");

        JsonObject dictionary = ResourceUtility.getJsonResource("gomenne.json").getAsJsonObject();
        dictionary.keySet().forEach(key -> Gomenne.dictionary.put(key, dictionary.get(key).getAsString()));
    }

    public static @NotNull Map<String, String> getRegistry()
    {
        return Gomenne.registry;
    }

    public static @NotNull Map<String, String> getDictionary()
    {
        return Gomenne.dictionary;
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
                    String suggest = Gomenne.registry.get(substring);

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

    private static void key(@NotNull String roman, @NotNull String hira)
    {
        Gomenne.registry.put(roman.toLowerCase(), hira);
    }

    public static void register(@NotNull String yomi, @NotNull String kaki)
    {
        Gomenne.dictionary.put(yomi, kaki);

        Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
            JsonObject dictionary = ResourceUtility.getJsonResource("gomenne.json").getAsJsonObject();
            dictionary.addProperty(yomi, kaki);

            Map<String, String> sortedMap = new TreeMap<>(String::compareTo);

            for (Map.Entry<String, JsonElement> entry : dictionary.entrySet())
            {
                sortedMap.put(entry.getKey(), entry.getValue().getAsString());
            }

            JsonObject sortedDictionary = new JsonObject();

            for (Map.Entry<String, String> entry : sortedMap.entrySet())
            {
                sortedDictionary.addProperty(entry.getKey(), entry.getValue());
            }

            ResourceUtility.save("gomenne.json", sortedDictionary);
        });
    }

    public static void unregister(@NotNull String yomi)
    {
        Gomenne.dictionary.remove(yomi);

        Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
            JsonObject dictionary = ResourceUtility.getJsonResource("gomenne.json").getAsJsonObject();
            dictionary.remove(yomi);
            ResourceUtility.save("gomenne.json", dictionary);
        });
    }
}
