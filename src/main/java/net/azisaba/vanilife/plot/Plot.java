package net.azisaba.vanilife.plot;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.subscription.PlotSubscription;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Plot
{
    private static final ArrayList<Plot> instances = new ArrayList<>();

    public static Plot getInstance(String name)
    {
        ArrayList<Plot> filteredInstances = new ArrayList<>(Plot.instances.stream().filter(i -> i.getName().equals(name)).toList());
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static Plot getInstance(Chunk chunk)
    {
        ArrayList<Plot> filteredInstances = new ArrayList<>(Plot.instances.stream().filter(i -> i.contains(chunk)).toList());
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static ArrayList<Plot> getInstances()
    {
        return Plot.instances;
    }

    private final UUID id;

    private String name;
    private User owner;
    private PlotScope scope = PlotScope.PRIVATE;
    private World world;
    private Location spawn;
    private final ArrayList<User> members = new ArrayList<>();
    private final ArrayList<Chunk> chunks = new ArrayList<>();

    private VanilifeWorld vw;

    public Plot(UUID id)
    {
        this.id = id;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM plot WHERE id = ?");
            stmt.setString(1, this.id.toString());

            ResultSet rs = stmt.executeQuery();
            rs.next();

            this.name = rs.getString("name");
            this.owner = User.getInstance(UUID.fromString(rs.getString("owner")));
            this.scope = PlotScope.valueOf(rs.getString("scope"));
            this.world = Bukkit.getWorld(rs.getString("world"));
            this.vw = VanilifeWorld.getInstance(this.world);
            this.spawn = new Location(this.world, rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));

            this.vw.getPlots().add(this);

            rs.close();
            stmt.close();

            PreparedStatement stmt2 = con.prepareStatement("SELECT * FROM chunk WHERE plot = ?");
            stmt2.setString(1, this.id.toString());

            ResultSet rs2 = stmt2.executeQuery();

            while (rs2.next())
            {
                int x = rs2.getInt("x");
                int z = rs2.getInt("z");
                this.chunks.add(this.world.getChunkAt(x, z));
            }

            rs2.close();
            stmt2.close();

            PreparedStatement stmt3 = con.prepareStatement("SELECT user FROM member WHERE plot = ?");
            stmt3.setString(1, this.id.toString());

            ResultSet rs3 = stmt3.executeQuery();

            while (rs3.next())
            {
                this.members.add(User.getInstance(UUID.fromString(rs3.getString("user"))));
            }

            rs3.close();
            stmt3.close();

            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to get plot record: %s", e.getMessage())).color(NamedTextColor.RED));
        }

        Plot.instances.add(this);
    }

    public Plot(String name, Chunk... chunks)
    {
        this.id = UUID.randomUUID();
        this.name = name;

        Chunk chunk = chunks[0];

        this.world = chunk.getWorld();
        this.vw = VanilifeWorld.getInstance(this.world);
        this.spawn = new Location(this.world, chunk.getX() * 16 + 8, this.world.getHighestBlockYAt(chunk.getX() * 16 + 8, chunk.getZ() * 16 + 8) + 1, chunk.getZ() * 16 + 8);
        this.chunks.addAll(List.of(chunks));

        this.vw.getPlots().add(this);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO plot VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, this.id.toString());
            stmt.setString(2, this.name);
            stmt.setString(3, null);
            stmt.setString(4, this.scope.toString());
            stmt.setString(5, this.world.getName());
            stmt.setInt(6, this.spawn.getBlockX());
            stmt.setInt(7, this.spawn.getBlockY());
            stmt.setInt(8, this.spawn.getBlockZ());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to insert plot record: %s", e.getMessage())).color(NamedTextColor.RED));
        }

        this.upload();
        Plot.instances.add(this);
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
        this.upload();
    }

    public User getOwner()
    {
        return this.owner;
    }

    public void setOwner(@NotNull User owner)
    {
        this.owner = owner;
        this.addMember(owner);
    }

    public PlotScope getScope()
    {
        return this.scope;
    }

    public void setScope(PlotScope scope)
    {
        this.scope = scope;
        this.upload();
    }

    public World getWorld()
    {
        return this.world;
    }

    public Location getSpawn()
    {
        return this.spawn;
    }

    public void setSpawn(Location spawn)
    {
        this.spawn = spawn;
        this.upload();
    }

    public ArrayList<User> getMembers()
    {
        return this.members;
    }

    public void addMember(User user)
    {
        if (! this.members.contains(user))
        {
            this.members.add(user);
            this.upload();
        }
    }

    public void removeMember(User user)
    {
        if (user == this.owner)
        {
            return;
        }

        this.members.remove(user);
        this.upload();
    }

    public boolean isMember(User user)
    {
        return UserUtility.isModerator(user)
                || (this.scope == PlotScope.PUBLIC)
                || (this.scope == PlotScope.FRIEND && (user.isFriend(this.owner) || this.members.contains(user)))
                || (this.scope == PlotScope.PRIVATE && this.members.contains(user));
    }

    public boolean isMember(Player player)
    {
        return this.isMember(User.getInstance(player));
    }

    public ArrayList<Chunk> getChunks()
    {
        return this.chunks;
    }

    public boolean isAdjacent(Chunk chunk)
    {
        int x = chunk.getX();
        int z = chunk.getZ();
        return this.chunks.stream().anyMatch(c -> (Math.abs(c.getX() - x) == 1 && chunk.getZ() == z) || (Math.abs(c.getZ() - z) == 1 && chunk.getX() == x));
    }

    public void claim(Chunk chunk)
    {
        if (! this.isAdjacent(chunk))
        {
            return;
        }

        if (! this.contains(chunk))
        {
            this.chunks.add(chunk);
            this.upload();
        }
    }

    public void unclaim(Chunk chunk)
    {
        if (this.contains(chunk))
        {
            this.chunks.remove(chunk);
            this.upload();

            if (this.chunks.isEmpty())
            {
                this.delete();
            }
        }
    }

    public boolean contains(Chunk chunk)
    {
        return this.chunks.stream().anyMatch(c -> c.equals(chunk));
    }

    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();

        if (! this.isMember(player) && ! UserUtility.isModerator(player))
        {
            event.setCancelled(true);
            player.sendMessage(Component.text("この Plot でブロックを破壊する権限がありません").color(NamedTextColor.RED));
        }
    }

    public void onBlockPlace(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();

        if (! this.isMember(player) && ! UserUtility.isModerator(player))
        {
            event.setCancelled(true);
            player.sendMessage(Component.text("この Plot でブロックを設置する権限がありません").color(NamedTextColor.RED));
        }
    }

    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (! this.isMember(event.getPlayer()))
        {
            event.setCancelled(true);
        }
    }

    public void delete()
    {
        Plot.getInstances().remove(this);
        this.vw.getPlots().remove(this);
        this.owner.getSubscriptions().removeIf(subscription -> (subscription instanceof PlotSubscription s) && s.getPlot() == this);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);

            PreparedStatement stmt = con.prepareStatement("DELETE FROM plot WHERE id = ?");
            stmt.setString(1, this.id.toString());
            stmt.executeUpdate();
            stmt.close();

            PreparedStatement stmt2 = con.prepareStatement("DELETE FROM chunk WHERE plot = ?");
            stmt2.setString(1, this.id.toString());
            stmt2.executeUpdate();
            stmt2.close();

            PreparedStatement stmt3 = con.prepareStatement("DELETE FROM member WHERE plot = ?");
            stmt3.setString(1, this.id.toString());
            stmt3.executeUpdate();
            stmt3.close();

            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to delete plot record: %s", e.getMessage())).color(NamedTextColor.RED));
        }
    }

    public void upload()
    {
        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);

            PreparedStatement stmt = con.prepareStatement("UPDATE plot SET name = ?, owner = ?, scope = ?, world = ?, x = ?, y = ?, z = ?");
            stmt.setString(1, this.name);
            stmt.setString(2, (this.owner == null) ? null : this.owner.getId().toString());
            stmt.setString(3, this.scope.toString());
            stmt.setString(4, this.world.getName());
            stmt.setInt(5, this.spawn.getBlockX());
            stmt.setInt(6, this.spawn.getBlockY());
            stmt.setInt(7, this.spawn.getBlockZ());

            stmt.executeUpdate();
            stmt.close();

            PreparedStatement stmt2 = con.prepareStatement("DELETE FROM chunk WHERE plot = ?");
            stmt2.setString(1, this.id.toString());
            stmt2.executeUpdate();
            stmt2.close();

            PreparedStatement stmt3 = con.prepareStatement("INSERT INTO chunk VALUES(?, ?, ?)");

            for (Chunk chunk : this.chunks)
            {
                stmt3.setString(1, this.id.toString());
                stmt3.setInt(2, chunk.getX());
                stmt3.setInt(3, chunk.getZ());

                stmt3.executeUpdate();
            }

            stmt3.close();

            PreparedStatement stmt4 = con.prepareStatement("DELETE FROM member WHERE plot = ?");
            stmt4.setString(1, this.id.toString());
            stmt4.executeUpdate();
            stmt4.close();

            PreparedStatement stmt5 = con.prepareStatement("INSERT INTO member VALUES(?, ?)");

            for (User member : this.members)
            {
                stmt5.setString(1, member.getId().toString());
                stmt5.setString(2, this.id.toString());

                stmt5.executeUpdate();
            }

            stmt5.close();

            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to update plot record: %s", e.getMessage())).color(NamedTextColor.RED));
        }
    }
}
