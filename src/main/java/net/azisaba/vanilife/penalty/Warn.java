package net.azisaba.vanilife.penalty;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Warn
{
    private static final List<Warn> instances = new ArrayList<>();

    public static @NotNull List<Warn> getInstances(@NotNull User user)
    {
        return Warn.instances.stream().filter(i -> i.getTarget() == user).toList();
    }

    public static @NotNull List<Warn> getInstances(@NotNull Player player)
    {
        return Warn.getInstances(User.getInstance(player));
    }

    public static @NotNull List<Warn> getInstances()
    {
        return new ArrayList<>(Warn.instances);
    }

    public static void mount()
    {
        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT id FROM warn");
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                new Warn(UUID.fromString(rs.getString("id")));
            }

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to mount warns: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    private final UUID id;
    private User target;
    private String reason;

    private Warn(@NotNull UUID id)
    {
        this.id = id;

        Warn.instances.add(this);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM warn WHERE id = ?");
            stmt.setString(1, this.id.toString());
            ResultSet rs = stmt.executeQuery();

            rs.next();

            this.target = User.getInstance(UUID.fromString(rs.getString("target")));
            this.reason = rs.getString("reason");

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to get warn record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public Warn(@NotNull User target, @NotNull String reason)
    {
        this.id = UUID.randomUUID();
        this.target = target;
        this.reason = reason;

        this.target.setTrust(this.target.getTrust() - 20);

        Warn.instances.add(this);

        if (this.target.isOnline())
        {
            this.send();
        }
    }

    public User getTarget()
    {
        return this.target;
    }

    public String getReason()
    {
        return this.reason;
    }

    public void send()
    {
        if (! this.target.isOnline())
        {
            return;
        }

        final Player listener = this.target.asPlayer();

        listener.sendMessage(Component.text().build());
        listener.sendMessage(Component.text().append(Component.text("[").color(NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD).append(Language.translate("msg.warn", target)).append(Component.text("]"))));
        listener.sendMessage(Component.text(this.reason));
        listener.sendMessage(Component.text().build());

        listener.playSound(listener, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.1f);

        Warn.instances.remove(this);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("DELETE FROM warn WHERE id = ?");
            stmt.setString(1, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to delete warn record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }
}
