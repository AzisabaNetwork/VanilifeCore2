package net.azisaba.vanilife.user;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.listener.PlayerJoinListener;
import net.azisaba.vanilife.util.MojangAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Skin
{
    private static final List<Skin> instances = new ArrayList<>();

    public static Skin getInstance(UUID id)
    {
        List<Skin> filteredInstances = Skin.instances.stream().filter(i -> i.getId().equals(id)).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static List<Skin> getInstances()
    {
        return Skin.instances;
    }

    public static boolean match(@NotNull Player player)
    {
        ProfileProperty textures = PlayerJoinListener.texturesMap.get(player);
        List<Skin> filteredInstances = Skin.instances.stream().filter(i -> MojangAPI.getSkin(i.getTexture()).equals(MojangAPI.getSkin(textures.getValue()))).toList();
        return ! filteredInstances.isEmpty();
    }

    public static void mount()
    {
        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT id FROM skin");
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                new Skin(UUID.fromString(rs.getString("id")));
            }

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text("Failed to mount skins: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    private final UUID id;
    private String name;

    private User owner;

    private String value;
    private String signature;

    private Skin(@NotNull UUID id)
    {
        this.id = id;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM skin WHERE id = ?");
            stmt.setString(1, this.id.toString());
            ResultSet rs = stmt.executeQuery();

            rs.next();

            this.name = rs.getString("name");
            this.owner = User.getInstance(UUID.fromString(rs.getString("owner")));
            this.value = rs.getString("value");
            this.signature = rs.getString("signature");

            this.owner.addSkin(this);

            rs.close();
            stmt.close();

            PreparedStatement stmt2 = con.prepareStatement("SELECT id FROM user WHERE skin = ?");
            stmt2.setString(1, this.name);
            ResultSet rs2 = stmt2.executeQuery();

            while (rs2.next())
            {
                User.getInstance(UUID.fromString(rs2.getString("id"))).setSkin(this);
            }

            rs2.close();
            stmt2.close();

            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text("Failed to get skin record: " + e.getMessage()).color(NamedTextColor.RED));
        }

        Skin.instances.add(this);
    }

    public Skin(@NotNull String name, @NotNull User owner, @NotNull String value, @NotNull String signature)
    {
        this.id = UUID.randomUUID();
        this.name = name;
        this.owner = owner;
        this.value = value;
        this.signature = signature;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO skin VALUES(?, ?, ?, ?, ?)");
            stmt.setString(1, this.id.toString());
            stmt.setString(2, this.name);
            stmt.setString(3, this.owner.getId().toString());
            stmt.setString(4, this.value);
            stmt.setString(5, this.signature);

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text("Failed to insert skin record: " + e.getMessage()).color(NamedTextColor.RED));
        }

        Skin.instances.add(this);
    }

    public @NotNull UUID getId()
    {
        return this.id;
    }

    public @NotNull String getName()
    {
        return this.name;
    }

    public String getTexture()
    {
        return this.value;
    }

    public void use(@NotNull Player player)
    {
        PlayerProfile profile = player.getPlayerProfile();
        profile.setProperty(new ProfileProperty("textures", this.value, this.signature));
        player.setPlayerProfile(profile);
    }

    public void delete()
    {
        Skin.instances.remove(this);

        User.getInstances().stream().filter(user -> user.getSkin() == this).forEach(user -> user.setSkin(null));
        User.getInstances().stream().filter(user -> user.getSkins().contains(this)).forEach(user -> user.getSkins().remove(this));

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("DELETE FROM skin WHERE id = ?");
            stmt.setString(1, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text("Failed to delete a skin record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }
}
