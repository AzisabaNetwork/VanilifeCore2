package net.azisaba.vanilife.vwm;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.util.ResourceUtility;
import net.azisaba.vanilife.util.SeasonUtility;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class VanilifeWorldManager
{
    private static JsonArray config = ResourceUtility.getJsonResource("vwm/vwm.json").getAsJsonArray();

    public static boolean running;

    private static int latestVersion;

    public static int getLatestVersion()
    {
        return VanilifeWorldManager.latestVersion;
    }

    public static void setLatestVersion(int latestVersion)
    {
        VanilifeWorldManager.latestVersion = latestVersion;
    }

    public static JsonArray getConfig()
    {
        return VanilifeWorldManager.config;
    }

    public static void setConfig(JsonArray config)
    {
        VanilifeWorldManager.config = config;
        ResourceUtility.save("vwm/vwm.json", VanilifeWorldManager.config);
    }

    public static JsonObject getWorldProperties(String name)
    {
        for (JsonElement properties : VanilifeWorldManager.config)
        {
            if (! (properties instanceof JsonObject))
            {
                continue;
            }

            if (properties.getAsJsonObject().get("name").getAsString().equals(name))
            {
                return properties.getAsJsonObject();
            }
        }

        return null;
    }

    public static JsonObject getWorldProperties(VanilifeWorld world)
    {
        return VanilifeWorldManager.getWorldProperties(world.getName());
    }

    public static void setWorldProperties(String name, JsonObject properties)
    {
        VanilifeWorldManager.config.remove(VanilifeWorldManager.getWorldProperties(name));

        if (properties != null)
        {
            VanilifeWorldManager.config.add(properties);
        }

        VanilifeWorldManager.setConfig(VanilifeWorldManager.config);
    }

    public static void setWorldProperties(VanilifeWorld world, JsonObject properties)
    {
        VanilifeWorldManager.setWorldProperties(world.getName(), properties);
    }

    public static boolean hasUpdate()
    {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        SeasonUtility.Season season = SeasonUtility.getSeason();
        return VanilifeWorld.getInstance(year + "-" + season.name().toLowerCase()) == null;
    }

    public static void backup()
    {
        new ArrayList<>(VanilifeWorld.getInstances()).forEach(VanilifeWorld::backup);
    }

    public static void update()
    {
        if (! VanilifeWorldManager.hasUpdate())
        {
            return;
        }

        long start = System.nanoTime();

        Calendar now = Calendar.getInstance();
        String name = now.get(Calendar.YEAR) + "-" + SeasonUtility.getSeason().name().toLowerCase();

        VanilifeWorld newWorld = VanilifeWorld.Builder.build(name);
        VanilifeWorld oldWorld = VanilifeWorld.getInstance(VanilifeWorldManager.getLatestVersion() - 2);

        if (oldWorld != null)
        {
            oldWorld.archive();
        }

        long end = System.nanoTime();
        double seconds = (end - start) / 1_000_000_000.0;
        DecimalFormat df = new DecimalFormat("#.##");

        Vanilife.CHANNEL_CONSOLE.sendMessageEmbeds(new EmbedBuilder()
                .setTitle(":recycle: ワールド更新が完了しました")
                .setColor(Color.GREEN)
                .setDescription("以下の変更がありました：")
                .addField("(+) 生成", newWorld.getName(), false)
                .addField("(-) 削除", oldWorld == null ? "なし" : oldWorld.getName(), false)
                .addField("経過", df.format(seconds) + "秒", false)
                .build()).queue();

        Vanilife.CHANNEL_CONSOLE.sendMessage(":envelope_with_arrow: " + Vanilife.ROLE_DEVELOPER.getAsMention() + " ワールドの更新を実行しました").queue();
    }

    public static void mount()
    {
        for (JsonElement world : VanilifeWorldManager.getConfig())
        {
            JsonObject properties = world.getAsJsonObject();
            VanilifeWorld vw = new VanilifeWorld(properties.get("name").getAsString());
            VanilifeWorldManager.setLatestVersion(Math.max(vw.getVersion(), VanilifeWorldManager.getLatestVersion()));
        }
    }
}
