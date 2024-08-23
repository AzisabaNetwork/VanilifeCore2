package net.azisaba.vanilife.vwm;

import com.google.gson.JsonObject;
import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.util.FileUtility;
import net.azisaba.vanilife.util.SeasonUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class VanilifeWorld
{
    private static final ArrayList<VanilifeWorld> instances = new ArrayList<>();

    public static VanilifeWorld getInstance(String name)
    {
        ArrayList<VanilifeWorld> filteredInstances = new ArrayList<>(VanilifeWorld.instances.stream().filter(i -> i.getName().equals(name)).toList());
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static VanilifeWorld getInstance(World world)
    {
        ArrayList<VanilifeWorld> filteredInstances = new ArrayList<>(VanilifeWorld.instances.stream().filter(i -> i.contains(world)).toList());
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static VanilifeWorld getInstance(int version)
    {
        ArrayList<VanilifeWorld> filteredInstances = new ArrayList<>(VanilifeWorld.instances.stream().filter(i -> i.getVersion() == version).toList());
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static ArrayList<VanilifeWorld> getInstances()
    {
        return VanilifeWorld.instances;
    }

    private final String name;
    private final int version;
    private final SeasonUtility.Season season;
    private JsonObject properties;

    private final World overworld;
    private final World nether;
    private final World end;
    private final ArrayList<World> worlds = new ArrayList<>();
    private final ArrayList<Plot> plots = new ArrayList<>();

    private final HashMap<UUID, Location> locations = new HashMap<>();

    private final RandomTeleporter teleporter;

    protected VanilifeWorld(String name)
    {
        this.name = name;

        this.properties = VanilifeWorldManager.getWorldProperties(this);
        this.version = this.properties.get("version").getAsInt();
        this.season = SeasonUtility.Season.valueOf(this.properties.get("season").getAsString());

        JsonObject levels = this.properties.get("levels").getAsJsonObject();

        this.overworld = this.mount(levels.get("overworld").getAsString(), World.Environment.NORMAL);
        this.nether = this.mount(levels.get("nether").getAsString(), World.Environment.NETHER);
        this.end = this.mount(levels.get("end").getAsString(), World.Environment.THE_END);

        JsonObject locations = this.properties.get("locations").getAsJsonObject();

        for (String key : locations.keySet())
        {
            JsonObject jsonObject = locations.get(key).getAsJsonObject();
            Location location = new Location(Bukkit.getWorld(jsonObject.get("world").getAsString()), jsonObject.get("x").getAsInt(), jsonObject.get("y").getAsInt(), jsonObject.get("z").getAsInt(), jsonObject.get("yaw").getAsFloat(), jsonObject.get("pitch").getAsFloat());
            this.locations.put(UUID.fromString(key), location);
        }

        this.teleporter = new RandomTeleporter(this.overworld, 320, Material.LAVA, Material.WATER, Material.FIRE);
        VanilifeWorld.instances.add(this);
    }

    public String getName()
    {
        return this.name;
    }

    public int getVersion()
    {
        return this.version;
    }

    public SeasonUtility.Season getSeason()
    {
        return this.season;
    }

    public JsonObject getProperties()
    {
        return this.properties;
    }

    public void setProperties(JsonObject properties)
    {
        this.properties = properties;
        VanilifeWorldManager.setWorldProperties(this, this.properties);
    }

    public World getOverworld()
    {
        return this.overworld;
    }

    public World getNether()
    {
        return this.nether;
    }

    public World getEnd()
    {
        return this.end;
    }

    public ArrayList<World> getWorlds()
    {
        return this.worlds;
    }

    public ArrayList<Plot> getPlots()
    {
        return this.plots;
    }

    public Location getLocation(Player player)
    {
        if (! this.locations.containsKey(player.getUniqueId()))
        {
            Location location = this.teleporter.randomLocation(this.overworld);
            this.locations.put(player.getUniqueId(), location);

            JsonObject locations = this.properties.get("locations").getAsJsonObject();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("world", location.getWorld().getName());
            jsonObject.addProperty("x", location.getX());
            jsonObject.addProperty("y", location.getY());
            jsonObject.addProperty("z", location.getZ());
            jsonObject.addProperty("yaw", location.getYaw());
            jsonObject.addProperty("pitch", location.getPitch());

            locations.add(player.getUniqueId().toString(), jsonObject);
            this.properties.add("locations", locations);

            this.setProperties(this.properties);
        }

        return this.locations.get(player.getUniqueId());
    }

    public void setLocation(Player player, Location location)
    {
        if (! this.contains(location.getWorld()))
        {
            return;
        }

        this.locations.put(player.getUniqueId(), location);

        JsonObject locations = this.properties.get("locations").getAsJsonObject();

        locations.remove(player.getUniqueId().toString());

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("world", location.getWorld().getName());
        jsonObject.addProperty("x", location.getX());
        jsonObject.addProperty("y", location.getY());
        jsonObject.addProperty("z", location.getZ());
        jsonObject.addProperty("yaw", location.getYaw());
        jsonObject.addProperty("pitch", location.getPitch());

        locations.add(player.getUniqueId().toString(), jsonObject);

        this.properties.add("locations", locations);
        this.setProperties(this.properties);
    }

    public RandomTeleporter getTeleporter()
    {
        return this.teleporter;
    }

    public ArrayList<Player> getOnlinePlayers()
    {
        ArrayList<Player> onlinePlayers = new ArrayList<>();
        this.worlds.forEach(w -> onlinePlayers.addAll(w.getPlayers()));
        return onlinePlayers;
    }

    public int getOnline()
    {
        return this.getOnlinePlayers().size();
    }

    public String archive()
    {
        this.worlds.forEach(w -> w.getPlayers().forEach(p -> p.kick(Component.text(String.format("ごめんなさい！%s はアンロードされたため、プレイすることはできません", this.name)).color(NamedTextColor.RED))));
        this.worlds.forEach(w -> Bukkit.unloadWorld(w, true));
        VanilifeWorld.instances.remove(this);
        VanilifeWorldManager.setWorldProperties(this, null);

        try
        {
            File archives = new File(Vanilife.getPlugin().getDataFolder().getPath(), "/vwm/archive");

            if (! archives.exists())
            {
                archives.mkdirs();
            }

            String archiveName = String.format("{%s-%s}", this.name, Vanilife.sdf4.format(new Date()));
            Files.move(Paths.get(String.format("./%s", this.name)), Paths.get(archives.toPath() + "/" + archiveName));
            return archiveName;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String backup()
    {
        this.worlds.forEach(w -> w.getPlayers().forEach(p -> p.kick(Component.text(String.format("現在、 %s のバックアップを実行しています\nしばらくしてから再接続をお願いします", this.name)).color(NamedTextColor.RED))));
        this.worlds.forEach(w -> Bukkit.unloadWorld(w, true));
        VanilifeWorld.instances.remove(this);

        File backup = new File(Vanilife.getPlugin().getDataFolder().getPath(), "/vwm/backup");

        if (! backup.exists())
        {
            backup.mkdirs();
        }

        String backupName = String.format("{%s}", this.name);
        Path backupPath = Paths.get(backup.toPath() + "/" + backupName);

        if (Files.exists(backupPath))
        {
            File oldBackup = backupPath.toFile();
            FileUtility.rmdir(oldBackup);
        }

        FileUtility.cpdir(Paths.get(String.format("./%s", this.name)), backupPath);

        Vanilife.channel.sendMessageEmbeds(new EmbedBuilder()
                .setTitle(String.format("ワールド %s のバックアップに成功しました", this.name))
                .setFooter("以前のバックアップは削除されます")
                .setColor(new Color(85, 255, 85))
                .addField("バックアップ先", String.format("./plugins/Vanilife/vwm/backup/%s", backupName), false)
                .build()).queue();

        new VanilifeWorld(this.name);
        return backupName;
    }

    public boolean contains(World world)
    {
        return this.worlds.contains(world);
    }

    public boolean contains(Player player)
    {
        return this.getOnlinePlayers().contains(player);
    }

    private World mount(String name, World.Environment environment)
    {
        World world = new WorldCreator(String.format("%s/%s", this.name, name)).environment(environment).createWorld();
        this.worlds.add(world);
        return world;
    }

    public static class Builder
    {
        public static VanilifeWorld build(String name)
        {
            Builder.generate(name, "overworld", World.Environment.NORMAL);
            Builder.generate(name, "nether", World.Environment.NETHER);
            Builder.generate(name, "end", World.Environment.THE_END);

            JsonObject properties = new JsonObject();
            properties.addProperty("name", name);
            properties.addProperty("version", VanilifeWorldManager.getLatestVersion() + 1);
            properties.addProperty("season", SeasonUtility.getSeason().toString());

            properties.add("locations", new JsonObject());

            VanilifeWorldManager.setLatestVersion(properties.get("version").getAsInt());

            JsonObject levels = new JsonObject();
            levels.addProperty("overworld", "overworld");
            levels.addProperty("nether", "nether");
            levels.addProperty("end", "end");

            properties.add("levels", levels);

            VanilifeWorldManager.setWorldProperties(name, properties);
            return new VanilifeWorld(name);
        }

        private static void generate(String namespace, String name, World.Environment environment)
        {
            WorldCreator creator = new WorldCreator(String.format("%s/%s", namespace, name));
            creator.type(WorldType.NORMAL);
            creator.environment(environment);
            Bukkit.createWorld(creator);
        }
    }
}
