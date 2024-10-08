package net.azisaba.vanilife.housing;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.housing.pack.HousingPacks;
import net.azisaba.vanilife.housing.pack.IHousingPack;
import net.azisaba.vanilife.housing.world.VoidChunkGenerator;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.LevelUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Housing
{
    private static final List<Housing> instances = new ArrayList<>();

    private static World world;

    public static Housing getInstance(@NotNull UUID id)
    {
        List<Housing> filteredInstances = Housing.instances.stream().filter(i -> i.getId().equals(id)).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static Housing getInstance(Location location)
    {
        List<Housing> filteredInstances = Housing.instances.stream().filter(i -> i.contains(location)).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static Housing getInstance(User user)
    {
        List<Housing> filteredInstances = Housing.instances.stream().filter(i -> i.getUser() == user).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static Housing getInstance(Player player)
    {
        return Housing.getInstance(User.getInstance(player));
    }

    public static List<Housing> getInstances()
    {
        return Housing.instances;
    }

    public static World getWorld()
    {
        return Housing.world;
    }

    public static void mount()
    {
        Housing.world = Bukkit.getWorld("housing");

        if (Housing.world != null)
        {
            return;
        }

        new HousingRunnable().runTaskTimer(Vanilife.getPlugin(), 0L, 10L);

        WorldCreator creator = new WorldCreator("housing");
        creator.generator(new VoidChunkGenerator());
        Housing.world = creator.createWorld();

        if (Housing.world == null)
        {
            return;
        }

        Housing.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        Housing.world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        Housing.world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        Housing.world.setTime(HousingTime.MIDNIGHT.getTime());

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT id FROM housing");
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                new Housing(UUID.fromString(rs.getString("id")));
            }

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to select housing table: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    private final UUID id;

    private final User user;

    private Location pos1;
    private Location pos2;
    private Location spawn;

    private final List<IHousingPack> packs = new ArrayList<>();

    protected Housing(@NotNull UUID id)
    {
        this.id = id;
        this.user = User.getInstance(this.id);

        this.user.setHousing(this);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM housing WHERE id = ?");
            stmt.setString(1, id.toString());

            ResultSet rs = stmt.executeQuery();
            rs.next();

            this.pos1 = new Location(Housing.getWorld(), rs.getInt("pos1_x"), rs.getInt("pos1_y"), rs.getInt("pos1_z"));
            this.pos2 = new Location(Housing.getWorld(), rs.getInt("pos2_x"), rs.getInt("pos2_y"), rs.getInt("pos2_z"));
            this.spawn = new Location(Housing.getWorld(), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to get housing record: " + e.getMessage()).color(NamedTextColor.RED));
        }

        for (JsonElement pack : this.user.getStorage().getAsJsonArray("housing.packs"))
        {
            IHousingPack p = HousingPacks.valueOf(pack.getAsString());

            if (p != null)
            {
                this.packs.add(p);
            }
        }

        Housing.instances.add(this);
    }

    public Housing(@NotNull User user)
    {
        if (Housing.getInstance(user) != null)
        {
            throw new IllegalArgumentException(user.getId() + " already has an instance.");
        }

        this.id = user.getId();
        this.user = user;

        user.write("settings.housing.afk", false);
        user.write("settings.housing.time", HousingTime.DAY.toString());
        user.write("settings.housing.activity", true);
        user.write("settings.housing.scope", HousingScope.PRIVATE.toString());

        user.getStorage().add("housing.packs", new JsonArray());
        user.saveStorage();

        Location l = null;

        for (int x = -100000; x <= 100000; x += 1000)
        {
            for (int z = -100000; z <= 100000; z += 1000)
            {
                l = new Location(Housing.getWorld(), x, 82, z);

                if (Housing.getInstance(l) == null)
                {
                    break;
                }
            }

            if (Housing.getInstance(l) == null){
                break;
            }
        }

        final Location location = l;

        Bukkit.getScheduler().runTask(Vanilife.getPlugin(), () -> {
            LevelUtility.generate(location, "housing.nbt");

            this.pos1 = location.clone().add(-20, -20, -20);
            this.pos2 = location.clone().add(20, 20, 20);
            this.spawn = new Location(Housing.world, location.getBlockX(), Housing.world.getHighestBlockYAt(location.getBlockX(), location.getBlockZ()) + 1, location.getZ());

            try
            {
                Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
                PreparedStatement stmt = con.prepareStatement("INSERT INTO housing VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                stmt.setString(1, this.user.getId().toString());
                stmt.setInt(2, this.pos1.getBlockX());
                stmt.setInt(3, this.pos1.getBlockY());
                stmt.setInt(4, this.pos1.getBlockZ());
                stmt.setInt(5, this.pos2.getBlockX());
                stmt.setInt(6, this.pos2.getBlockY());
                stmt.setInt(7, this.pos2.getBlockZ());
                stmt.setInt(8, this.spawn.getBlockX());
                stmt.setInt(9, this.spawn.getBlockY());
                stmt.setInt(10, this.spawn.getBlockZ());

                stmt.executeUpdate();

                stmt.close();
                con.close();
            }
            catch (SQLException e)
            {
                Vanilife.sendExceptionReport(e);
                Vanilife.getPluginLogger().warn(Component.text("Failed to insert housing record: " + e.getMessage()).color(NamedTextColor.RED));
            }
        });

        Housing.instances.add(this);
    }

    public @NotNull UUID getId()
    {
        return this.id;
    }

    public @NotNull User getUser()
    {
        return this.user;
    }

    public @NotNull Location getPos1()
    {
        return this.pos1;
    }

    public void setPos1(@NotNull Location pos1)
    {
        this.pos1 = pos1;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_USER);
            PreparedStatement stmt = con.prepareStatement("UPDATE housing SET pos1_x = ?, pos1_y = ?, pos1_z = ? WHERE id = ?");
            stmt.setInt(1, this.pos1.getBlockX());
            stmt.setInt(2, this.pos1.getBlockY());
            stmt.setInt(3, this.pos1.getBlockZ());
            stmt.setString(4, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text("Failed to update housing record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public @NotNull Location getPos2()
    {
        return this.pos2;
    }

    public void setPos2(@NotNull Location pos2)
    {
        this.pos2 = pos2;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_USER);
            PreparedStatement stmt = con.prepareStatement("UPDATE housing SET pos2_x = ?, pos2_y = ?, pos2_z = ? WHERE id = ?");
            stmt.setInt(1, this.pos2.getBlockX());
            stmt.setInt(2, this.pos2.getBlockY());
            stmt.setInt(3, this.pos2.getBlockZ());
            stmt.setString(4, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text("Failed to update housing record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public @NotNull Location getSpawn()
    {
        return this.spawn;
    }

    public void setSpawn(@NotNull Location spawn)
    {
        this.spawn = spawn;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE housing SET x = ?, y = ?, z = ?");
            stmt.setInt(1, spawn.getBlockX());
            stmt.setInt(2, spawn.getBlockY());
            stmt.setInt(3, spawn.getBlockZ());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to update housing record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public @NotNull HousingScope getScope()
    {
        return HousingScope.valueOf(this.user.read("settings.housing.scope").getAsString());
    }

    public void setScope(@NotNull HousingScope scope)
    {
        this.user.write("settings.housing.scope", scope.toString());
    }

    public @NotNull HousingTime getTime()
    {
        return HousingTime.valueOf(this.user.read("settings.housing.time").getAsString());
    }

    public void setTime(@NotNull HousingTime time)
    {
        this.user.write("settings.housing.time", time.toString());
    }

    public List<IHousingPack> getPacks()
    {
        return this.packs;
    }

    public void addPack(@NotNull IHousingPack pack)
    {
        if (this.packs.contains(pack))
        {
            return;
        }

        this.packs.add(pack);
        this.user.getStorage().getAsJsonArray("housing.packs").add(pack.getName());
        this.user.saveStorage();
    }

    public void removePack(@NotNull IHousingPack pack)
    {
        if (! this.packs.contains(pack))
        {
            return;
        }

        this.packs.add(pack);

        JsonArray packs = new JsonArray();

        for (JsonElement p : this.user.getStorage().getAsJsonArray("housing.packs"))
        {
            if (! p.getAsString().equals(pack.getName()))
            {
                packs.add(p);
            }
        }

        this.user.getStorage().add("housing.packs", packs);
        this.user.saveStorage();
    }

    public boolean withInScope(User user)
    {
        return this.user == user ||
                (this.getScope() == HousingScope.PUBLIC) ||
                (this.getScope() == HousingScope.FRIEND && this.user.isFriend(user)) ||
                (this.getScope() == HousingScope.OSATOU && this.user.getOsatou() == user);
    }

    public boolean has(IHousingPack pack)
    {
        return this.packs.contains(pack);
    }

    public boolean contains(Location location)
    {
        if (location == null)
        {
            return false;
        }

        int minX = Math.min(this.pos1.getBlockX(), this.pos2.getBlockX());
        int maxX = Math.max(this.pos1.getBlockX(), this.pos2.getBlockX());

        int minY = Math.min(this.pos1.getBlockY(), this.pos2.getBlockY());
        int maxY = Math.max(this.pos1.getBlockY(), this.pos2.getBlockY());

        int minZ = Math.min(this.pos1.getBlockZ(), this.pos2.getBlockZ());
        int maxZ = Math.max(this.pos1.getBlockZ(), this.pos2.getBlockZ());

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return (x >= minX && x <= maxX) && (y >= minY && y <= maxY) && (z >= minZ && z <= maxZ);
    }
}
