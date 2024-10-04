package net.azisaba.vanilife.plot;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.subscription.PlotSubscription;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class Plot
{
    private static final ArrayList<Plot> instances = new ArrayList<>();

    public static Plot getInstance(String name)
    {
        List<Plot> filteredInstances = new ArrayList<>(Plot.instances.stream().filter(i -> i.getName().equals(name)).toList());
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static Plot getInstance(Chunk chunk)
    {
        List<Plot> filteredInstances = new ArrayList<>(Plot.instances.stream().filter(i -> i.contains(chunk)).toList());
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static List<Plot> getInstances()
    {
        return Plot.instances;
    }

    public static void mount()
    {
        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT id FROM plot");
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                new Plot(UUID.fromString(rs.getString("id")));
            }

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to mount plots: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    private final UUID id;

    private String name;
    private User owner;
    private String readme;

    private PlotScope scope = PlotScope.PRIVATE;
    private boolean edit;
    private boolean chest;
    private boolean pvp;

    private World world;
    private Location spawn;

    private final List<User> members = new ArrayList<>();
    private final List<Chunk> chunks = new ArrayList<>();

    private VanilifeWorld vanilifeWorld;

    private Plot(@NotNull UUID id)
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
            this.readme = rs.getString("readme");
            this.scope = PlotScope.valueOf(rs.getString("scope"));
            this.edit = rs.getBoolean("edit");
            this.chest = rs.getBoolean("chest");
            this.pvp = rs.getBoolean("pvp");
            this.world = Bukkit.getWorld(rs.getString("world"));
            this.vanilifeWorld = VanilifeWorld.getInstance(this.world);
            this.spawn = new Location(this.world, rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));

            this.vanilifeWorld.getPlots().add(this);

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
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to get plot record: %s", e.getMessage())).color(NamedTextColor.RED));
        }

        Plot.instances.add(this);
    }

    public Plot(@NotNull String name, @NotNull Chunk chunk, Chunk... chunks)
    {
        this.id = UUID.randomUUID();
        this.name = name;
        this.world = chunk.getWorld();
        this.vanilifeWorld = VanilifeWorld.getInstance(this.world);

        final int x = chunk.getX() * 16 + 8;
        final int z = chunk.getZ() * 16 + 8;

        this.spawn = new Location(this.world, x, this.world.getHighestBlockYAt(x, z) + 1, z);

        this.claim(chunk);
        Stream.of(chunks).forEach(this::claim);
        this.vanilifeWorld.getPlots().add(this);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO plot VALUES(?, ?, NULL, NULL, ?, 0, 0, 0, ?, ?, ?, ?)");
            stmt.setString(1, this.id.toString());
            stmt.setString(2, this.name);
            stmt.setString(3, this.scope.toString());
            stmt.setString(4, this.world.getName());
            stmt.setInt(5, this.spawn.getBlockX());
            stmt.setInt(6, this.spawn.getBlockY());
            stmt.setInt(7, this.spawn.getBlockZ());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to insert plot record: %s", e.getMessage())).color(NamedTextColor.RED));
        }

        Plot.instances.add(this);
    }

    public @NotNull String getName()
    {
        return this.name;
    }

    public void setName(@NotNull String name)
    {
        this.name = name;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE plot SET name = ? WHERE id = ?");
            stmt.setString(1, this.name);
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to update plot record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public @NotNull User getOwner()
    {
        return this.owner;
    }

    public void setOwner(@NotNull User owner)
    {
        this.owner = owner;
        this.addMember(owner);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE plot SET owner = ? WHERE id = ?");
            stmt.setString(1, this.owner.getId().toString());
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to update plot record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public String getReadme()
    {
        return this.readme;
    }

    public void setReadme(String readme)
    {
        this.readme = readme;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE plot SET readme = ? WHERE id = ?");
            stmt.setString(1, this.readme);
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to update plot record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public @NotNull PlotScope getScope()
    {
        return this.scope;
    }

    public void setScope(@NotNull PlotScope scope)
    {
        this.scope = scope;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE plot SET scope = ? WHERE id = ?");
            stmt.setString(1, this.scope.toString());
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to update plot record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public @NotNull World getWorld()
    {
        return this.world;
    }

    public @NotNull Location getSpawn()
    {
        return this.spawn;
    }

    public void setSpawn(@NotNull Location spawn)
    {
        if (Plot.getInstance(spawn.getChunk()) != this)
        {
            return;
        }

        this.spawn = spawn;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE plot SET world = ?, x = ?, y = ?, z = ? WHERE id = ?");
            stmt.setString(1, this.world.getName());
            stmt.setInt(2, this.spawn.getBlockX());
            stmt.setInt(3, this.spawn.getBlockY());
            stmt.setInt(4, this.spawn.getBlockZ());
            stmt.setString(5, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to update plot record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public @NotNull List<User> getMembers()
    {
        return this.members;
    }

    public @NotNull List<Chunk> getChunks()
    {
        return this.chunks;
    }

    public void setEdit(boolean edit)
    {
        this.edit = edit;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE plot SET edit = ? WHERE id = ?");
            stmt.setBoolean(1, this.edit);
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to update plot record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public void setChest(boolean chest)
    {
        this.chest = chest;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE plot SET chest = ? WHERE id = ?");
            stmt.setBoolean(1, this.chest);
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to update plot record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public void setPvP(boolean pvp)
    {
        this.pvp = pvp;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE plot SET pvp = ? WHERE id = ?");
            stmt.setBoolean(1, this.pvp);
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to update plot record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public void addMember(@NotNull User user)
    {
        if (this.members.contains(user))
        {
            return;
        }

        this.members.add(user);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO member VALUES(?, ?)");
            stmt.setString(1, user.getId().toString());
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to insert member record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public void removeMember(@NotNull User user)
    {
        if (user == this.owner)
        {
            return;
        }

        this.members.remove(user);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("DELETE FROM member WHERE plot = ?");
            stmt.setString(1, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed delete member record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public boolean canEdit()
    {
        return this.edit;
    }

    public boolean canChest()
    {
        return this.chest;
    }

    public boolean canPvP()
    {
        return this.pvp;
    }

    public boolean isMember(User user)
    {
        return UserUtility.isModerator(user)
                || (this.scope == PlotScope.PUBLIC)
                || (this.scope == PlotScope.FRIEND && (user.isFriend(this.owner) || this.members.contains(user)))
                || (this.scope == PlotScope.OSATOU) && (this.owner.getOsatou() == user || this.members.contains(user))
                || (this.scope == PlotScope.PRIVATE && this.members.contains(user));
    }

    public boolean isMember(Player player)
    {
        return this.isMember(User.getInstance(player));
    }

    public void onBlockBreak(@NotNull BlockBreakEvent event)
    {
        Player player = event.getPlayer();

        if (UserUtility.isModerator(player))
        {
            return;
        }

        if (User.getInstance(player) == this.owner)
        {
            return;
        }

        if (this.isMember(player) && this.edit)
        {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(Language.translate("plot.cant.break", player).color(NamedTextColor.RED));
    }

    public void onBlockPlace(@NotNull BlockPlaceEvent event)
    {
        Player player = event.getPlayer();

        if (UserUtility.isModerator(player))
        {
            return;
        }

        if (User.getInstance(player) == this.owner)
        {
            return;
        }

        if (this.isMember(player) && this.edit)
        {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(Language.translate("plot.cant.place", player).color(NamedTextColor.RED));
    }

    private static final EnumSet<Material> chests = EnumSet.of(Material.CHEST, Material.DISPENSER, Material.DROPPER, Material.FURNACE, Material.BREWING_STAND, Material.SMITHING_TABLE, Material.BEACON, Material.HOPPER, Material.SHULKER_BOX, Material.BARREL, Material.SMOKER, Material.LOOM, Material.CHISELED_BOOKSHELF, Material.DECORATED_POT, Material.CRAFTER);

    public void onPlayerInteract(@NotNull PlayerInteractEvent event)
    {
        Block click = event.getClickedBlock();

        if (click == null)
        {
            return;
        }

        if (! Plot.chests.contains(click.getType()))
        {
            return;
        }

        Player player = event.getPlayer();

        if (UserUtility.isModerator(player))
        {
            return;
        }

        if (User.getInstance(player) == this.owner)
        {
            return;
        }

        if (this.isMember(player) && this.edit)
        {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(Language.translate("plot.cant.chest", player).color(NamedTextColor.RED));
    }

    public void onEntityDamageByEntity(@NotNull EntityDamageByEntityEvent event)
    {
        Player damager = (Player) event.getDamager();
        Player player = (Player) event.getEntity();

        if (UserUtility.isModerator(damager))
        {
            return;
        }

        if (this.isMember(damager) && this.isMember(player) && this.pvp)
        {
            return;
        }

        event.setCancelled(true);
        damager.sendMessage(Language.translate("plot.cant.pvp", damager).color(NamedTextColor.RED));
    }

    public boolean contains(Chunk chunk)
    {
        return this.chunks.stream().anyMatch(c -> c.equals(chunk));
    }

    public void claim(@NotNull Chunk chunk)
    {
        if (this.contains(chunk))
        {
            return;
        }

        this.chunks.add(chunk);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO chunk VALUES(?, ?, ?)");
            stmt.setString(1, this.id.toString());
            stmt.setInt(2, chunk.getX());
            stmt.setInt(3, chunk.getZ());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to insert chunk record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public void unclaim(@NotNull Chunk chunk)
    {
        if (this.contains(chunk))
        {
            this.chunks.remove(chunk);

            try
            {
                Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_USER);
                PreparedStatement stmt = con.prepareStatement("DELETE FROM chunk WHERE plot = ?");
                stmt.setString(1, this.id.toString());

                stmt.executeUpdate();

                stmt.close();
                con.close();
            }
            catch (SQLException e)
            {
                Vanilife.sendExceptionReport(e);
                Vanilife.getPluginLogger().warn(Component.text("Failed to delete chunk record: " + e.getMessage()).color(NamedTextColor.RED));
            }

            if (this.chunks.isEmpty())
            {
                this.delete();
            }
        }
    }

    public void delete()
    {
        Plot.getInstances().remove(this);
        this.vanilifeWorld.getPlots().remove(this);
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
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to delete plot record: %s", e.getMessage())).color(NamedTextColor.RED));
        }
    }
}
