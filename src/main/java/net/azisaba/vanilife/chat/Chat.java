package net.azisaba.vanilife.chat;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.ComponentUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class Chat
{
    private static final List<Chat> instances = new ArrayList<>();

    public static final Pattern namepattern = Pattern.compile("^[a-zA-Z0-9ぁ-んァ-ン一-龠]+$");

    public static Chat getInstance(@NotNull String name)
    {
        List<Chat> filteredInstances = Chat.instances.stream().filter(i -> i.getName().equals(name)).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static Chat getInstance(@NotNull User user)
    {
        List<Chat> filteredInstances = Chat.instances.stream().filter(i -> i.isFocus(user)).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static Chat getInstance(@NotNull Player player)
    {
        return Chat.getInstance(User.getInstance(player));
    }

    public static @NotNull List<Chat> getInstances()
    {
        return Chat.instances;
    }

    public static void mount()
    {
        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT id FROM chat");
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                new Chat(UUID.fromString(rs.getString("id")));
            }

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to mount chats: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    private final UUID id;

    private String name;
    private User owner;
    private ChatScope scope;
    private TextColor color;

    private final boolean direct;

    private final List<User> members = new ArrayList<>();
    private final List<User> focuses = new ArrayList<>();

    private Chat(@NotNull UUID id)
    {
        this.id = id;
        this.direct = false;

        Chat.instances.add(this);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM chat WHERE id = ?");
            stmt.setString(1, this.id.toString());
            ResultSet rs = stmt.executeQuery();

            rs.next();

            this.name = rs.getString("name");
            this.owner = User.getInstance(UUID.fromString(rs.getString("owner")));
            this.scope = ChatScope.valueOf(rs.getString("scope"));
            this.color = TextColor.color(rs.getInt("color"));

            rs.close();
            stmt.close();

            PreparedStatement stmt2 = con.prepareStatement("SELECT user FROM `chat-member` WHERE chat = ?");
            stmt2.setString(1, this.id.toString());
            ResultSet rs2 = stmt2.executeQuery();

            while (rs2.next())
            {
                this.members.add((User.getInstance(UUID.fromString(rs2.getString("user")))));
            }

            rs2.close();
            stmt2.close();

            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to get chat record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public Chat(@NotNull String name, @NotNull User owner)
    {
        this.id = UUID.randomUUID();
        this.name = name;
        this.owner = owner;
        this.scope = ChatScope.PRIVATE;
        this.color = NamedTextColor.BLUE;

        this.direct = false;

        Chat.instances.add(this);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO chat VALUES(?, ?, ?, ?, ?)");
            stmt.setString(1, this.id.toString());
            stmt.setString(2, this.name);
            stmt.setString(3, this.owner.getId().toString());
            stmt.setString(4, this.scope.toString());
            stmt.setInt(5, this.color.value());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed tot insert chat record: " + e.getMessage()).color(NamedTextColor.RED));
        }

        this.addMember(owner);
    }

    public Chat(@NotNull User user1, @NotNull User user2)
    {
        this.id = UUID.randomUUID();
        this.name = "@" + this.id;
        this.color = NamedTextColor.BLUE;

        this.members.add(user1);
        this.members.add(user2);

        this.direct = true;

        Chat.instances.add(this);
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
            PreparedStatement stmt = con.prepareStatement("UPDATE chat SET name = ? WHERE id = ?");
            stmt.setString(1, this.name);
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to update chat record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public @NotNull User getOwner()
    {
        return this.owner;
    }

    public @NotNull ChatScope getScope()
    {
        return this.scope;
    }

    public void setScope(@NotNull ChatScope scope)
    {
        this.scope = scope;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE chat SET scope = ? WHERE id = ?");
            stmt.setString(1, this.scope.toString());
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to update chat record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public @NotNull TextColor getColor()
    {
        return this.color;
    }

    public void setColor(@NotNull TextColor color)
    {
        if (this.color.value() == color.value())
        {
            return;
        }

        this.color = color;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE chat SET color = ? WHERE id = ?");
            stmt.setInt(1, this.color.value());
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to update chat record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public @NotNull List<User> getMembers()
    {
        return this.members;
    }

    public @NotNull List<User> getOnline()
    {
        return this.getMembers().stream().filter(User::isOnline).toList();
    }

    public void addMember(@NotNull User user)
    {
        if (this.isMember(user))
        {
            return;
        }

        this.members.add(user);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO `chat-member` VALUES(?, ?)");
            stmt.setString(1, user.getId().toString());
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to insert `chat-member` record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public void removeMember(@NotNull User user)
    {
        this.members.remove(user);

        if (Chat.getInstance(user) == this)
        {
            this.unfocus(user);
        }

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("DELETE FROM `chat-member` WHERE user = ? AND chat = ?");
            stmt.setString(1, user.getId().toString());
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to delete `chat-member` record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public boolean isMember(User user)
    {
        return this.members.contains(user);
    }

    public boolean isMember(Player player)
    {
        if (player == null)
        {
            return false;
        }

        return this.isMember(User.getInstance(player));
    }

    public boolean isDirect()
    {
        return this.direct;
    }

    public boolean isFocus(User user)
    {
        return this.focuses.contains(user);
    }

    public boolean isFocus(Player player)
    {
        if (player == null)
        {
            return false;
        }

        return this.isFocus(User.getInstance(player));
    }

    public boolean inScope(@NotNull User user)
    {
        return (this.scope == ChatScope.PUBLIC) ||
                (this.scope == ChatScope.FRIEND && (user.isFriend(this.owner) || this.isMember(user))) ||
                (this.scope == ChatScope.OSATOU && (user.hasOsatou() && user.getOsatou() == this.owner || this.isMember(user))) ||
                (this.scope == ChatScope.PRIVATE && this.isMember(user));
    }

    public void focus(@NotNull User user)
    {
        if (! this.isMember(user))
        {
            return;
        }

        Chat old = Chat.getInstance(user);

        if (old != null)
        {
            old.unfocus(user);
        }

        this.focuses.add(user);
    }

    public void focus(@NotNull Player player)
    {
        this.focus(User.getInstance(player));
    }

    public void unfocus(@NotNull User user)
    {
        this.focuses.remove(user);
    }

    public void unfocus(@NotNull Player player)
    {
        this.unfocus(User.getInstance(player));
    }

    public void chat(@NotNull User sender, @NotNull String message)
    {
        if (! this.isMember(sender) || ! sender.isOnline())
        {
            return;
        }

        final Component body = ComponentUtility.asChat(sender.asPlayer(), message);

        Vanilife.filter.onChat(sender.asPlayer(), ((TextComponent) body).content());

        this.members.stream()
                .filter(member -> member.isOnline() && ! member.isBlock(sender) && member.read("settings.chat").getAsBoolean())
                .forEach(listener -> listener.asPlayer().sendMessage(Component.text().build()
                        .append(((! this.direct ? Component.text(this.name) : Language.translate("msg.chat.direct", listener)).append(Component.text(" > "))).color(this.color))
                        .append(sender.getName(listener))
                        .append(Component.text(": ").color(NamedTextColor.GRAY))
                        .append(body)));
    }

    public void chat(@NotNull Player sender, @NotNull String message)
    {
        this.chat(User.getInstance(sender), message);
    }

    public void delete()
    {
        Chat.instances.remove(this);

        this.members.forEach(this::unfocus);
        this.members.clear();

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);

            PreparedStatement stmt = con.prepareStatement("DELETE FROM chat WHERE id = ?");
            stmt.setString(1, this.id.toString());
            stmt.executeUpdate();

            stmt.close();

            PreparedStatement stmt2 = con.prepareStatement("DELETE FROM `chat-member` WHERE chat = ?");
            stmt2.setString(1, this.id.toString());
            stmt2.executeUpdate();

            stmt2.close();

            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to delete chat: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }
}
