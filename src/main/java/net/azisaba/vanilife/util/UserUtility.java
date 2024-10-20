package net.azisaba.vanilife.util;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.mail.Mail;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserUtility
{
    public static final UUID UUID_AZISABA = UUID.fromString("8052db20-6fa8-4d26-ac59-30fd04c34f5e");

    public static Sara getSara(Player player)
    {
        return User.getInstance(player).getSara();
    }

    public static Sara getSara(CommandSender sender)
    {
        if (sender instanceof Player player)
        {
            return User.getInstance(player).getSara();
        }

        return (sender.isOp()) ? Sara.ADMIN : Sara.DEFAULT;
    }

    public static List<Mail> getMails(User user)
    {
        List<Mail> mails = new ArrayList<>();

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT id FROM mail WHERE user_from = ? OR user_to = ?");
            stmt.setString(1, user.getId().toString());
            stmt.setString(2, user.getId().toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                mails.addFirst(Mail.getInstance(UUID.fromString(rs.getString("id"))));
            }

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to get mails: %s", e.getMessage())).color(NamedTextColor.RED));
        }

        return mails;
    }

    public static Sara calculateSara(@NotNull Player player, boolean rankOnly)
    {
        boolean op = player.isOp();

        if (rankOnly && op)
        {
            player.setOp(false);
        }

        Sara sara;

        if (player.isOp() && ! rankOnly)
        {
            sara = Sara.ADMIN;
        }
        else if (player.hasPermission("group.nitro"))
        {
            sara = Sara.NITRO;
        }
        else if (player.hasPermission("group.gamingsara"))
        {
            sara = Sara.GAMING;
        }
        else if (player.hasPermission("group.50000yen"))
        {
            sara = Sara.$50000YEN;
        }
        else if (player.hasPermission("group.10000yen"))
        {
            sara = Sara.$10000YEN;
        }
        else if (player.hasPermission("group.5000yen"))
        {
            sara = Sara.$5000YEN;
        }
        else if (player.hasPermission("group.2000yen"))
        {
            sara = Sara.$2000YEN;
        }
        else if (player.hasPermission("group.1000yen"))
        {
            sara = Sara.$1000YEN;
        }
        else if (player.hasPermission("group.500yen"))
        {
            sara = Sara.$500YEN;
        }
        else if (player.hasPermission("group.100yen"))
        {
            sara = Sara.$100YEN;
        }
        else
        {
            sara = Sara.DEFAULT;
        }

        if (rankOnly && op)
        {
            player.setOp(true);
        }

        return sara;
    }

    @Deprecated(since = "2.1.0", forRemoval = true)
    public static boolean isModerator(CommandSender sender)
    {
        if (sender instanceof Player player)
        {
            return UserUtility.isAdmin(User.getInstance(player));
        }

        return sender.isOp();
    }

    public static boolean isAdmin(User user)
    {
        return 11 <= user.getSara().level;
    }

    public static boolean isAdmin(CommandSender sender)
    {
        if (sender instanceof Player player)
        {
            return UserUtility.isAdmin(User.getInstance(player));
        }

        return sender.isOp();
    }

    public static boolean exists(@NotNull UUID id)
    {
        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT id FROM user WHERE id = ?");
            stmt.setString(1, id.toString());

            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next();

            rs.close();
            stmt.close();
            con.close();

            return exists;
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            return false;
        }
    }
}
