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

    public static ArrayList<Mail> getMails(User user)
    {
        ArrayList<Mail> mails = new ArrayList<>();

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
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to get mails: %s", e.getMessage())).color(NamedTextColor.RED));
        }

        return mails;
    }

    public static Sara calculateSara(Player player)
    {
        if (player.isOp())
        {
            return Sara.ADMIN;
        }
        else if (player.hasPermission("group.nitro"))
        {
            return Sara.NITRO;
        }
        else if (player.hasPermission("group.gamingsara"))
        {
            return Sara.GAMING;
        }
        else if (player.hasPermission("group.50000yen"))
        {
            return Sara.$50000YEN;
        }
        else if (player.hasPermission("group.10000yen"))
        {
            return Sara.$10000YEN;
        }
        else if (player.hasPermission("group.5000yen"))
        {
            return Sara.$5000YEN;
        }
        else if (player.hasPermission("group.2000yen"))
        {
            return Sara.$2000YEN;
        }
        else if (player.hasPermission("group.1000yen"))
        {
            return Sara.$1000YEN;
        }
        else if (player.hasPermission("group.500yen"))
        {
            return Sara.$500YEN;
        }
        else if (player.hasPermission("group.100yen"))
        {
            return Sara.$100YEN;
        }
        else
        {
            return Sara.DEFAULT;
        }
    }

    public static boolean isModerator(User user)
    {
        return 10 <= user.getSara().level;
    }

    public static boolean isModerator(CommandSender sender)
    {
        if (sender instanceof Player player)
        {
            return UserUtility.isModerator(User.getInstance(player));
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
        catch (SQLException ignored)
        {
            return false;
        }
    }
}
