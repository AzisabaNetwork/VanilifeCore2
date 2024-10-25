package net.azisaba.vanilife.vwm;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.housing.HousingTime;
import net.azisaba.vanilife.housing.world.VoidChunkGenerator;
import net.azisaba.vanilife.util.LevelUtility;
import net.azisaba.vanilife.util.ResourceUtility;
import net.azisaba.vanilife.util.SeasonUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.*;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class VanilifeWorldManager
{
    private static JsonArray config = ResourceUtility.getJsonResource("vwm/vwm.json").getAsJsonArray();

    private static World jail;

    private static int latestVersion;

    public static int getLatestVersion()
    {
        return VanilifeWorldManager.latestVersion;
    }

    public static void setLatestVersion(int latestVersion)
    {
        VanilifeWorldManager.latestVersion = latestVersion;
    }

    public static @NotNull JsonArray getConfig()
    {
        return VanilifeWorldManager.config;
    }

    public static void setConfig(@NotNull JsonArray config)
    {
        VanilifeWorldManager.config = config;
        ResourceUtility.save("vwm/vwm.json", VanilifeWorldManager.config);
    }

    public static @NotNull World getJail()
    {
        return VanilifeWorldManager.jail;
    }

    public static World getUnderworld()
    {
        return Bukkit.getWorld(new NamespacedKey(Vanilife.getPlugin(), "underworld"));
    }

    public static boolean hasUpdate()
    {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        SeasonUtility.Season season = SeasonUtility.getSeason();
        return VanilifeWorld.getInstance(year + "-" + season.name().toLowerCase()) == null;
    }

    public static JsonObject read(@NotNull String name)
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

    public static JsonObject read(@NotNull VanilifeWorld world)
    {
        return VanilifeWorldManager.read(world.getName());
    }

    public static void write(@NotNull String name, JsonObject properties)
    {
        VanilifeWorldManager.config.remove(VanilifeWorldManager.read(name));

        if (properties != null)
        {
            VanilifeWorldManager.config.add(properties);
        }

        VanilifeWorldManager.setConfig(VanilifeWorldManager.config);
    }

    public static void write(@NotNull VanilifeWorld world, JsonObject properties)
    {
        VanilifeWorldManager.write(world.getName(), properties);
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

        VanilifeWorldManager.jail = Bukkit.getWorld("jail");

        if (VanilifeWorldManager.jail != null)
        {
            return;
        }

        WorldCreator creator = new WorldCreator("jail");
        creator.generator(new VoidChunkGenerator());
        VanilifeWorldManager.jail = creator.createWorld();

        if (VanilifeWorldManager.jail == null)
        {
            return;
        }

        VanilifeWorldManager.jail.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        VanilifeWorldManager.jail.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        VanilifeWorldManager.jail.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        VanilifeWorldManager.jail.setTime(HousingTime.MIDNIGHT.getTime());
        VanilifeWorldManager.jail.setSpawnLocation(new Location(VanilifeWorldManager.jail, 0.5, 0, 0.5, 90f, 0f));

        LevelUtility.generate(new Location(VanilifeWorldManager.jail, 0, 9, -1), "jail.nbt");
    }
}
