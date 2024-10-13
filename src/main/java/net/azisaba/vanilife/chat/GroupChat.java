package net.azisaba.vanilife.chat;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GroupChat extends Chat
{
    public static final Pattern namepattern = Pattern.compile("^[a-zA-Z0-9ぁ-んァ-ン一-龠]+$");

    public static GroupChat getInstance(@NotNull String name)
    {
        return GroupChat.getInstances().stream().filter(i -> i.getName().equals(name)).findFirst().orElse(null);
    }

    public static List<GroupChat> getInstances()
    {
        return Chat.instances.stream()
                .filter(i -> i instanceof GroupChat)
                .map(chat -> (GroupChat) chat)
                .collect(Collectors.toList());
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
                new GroupChat(UUID.fromString(rs.getString("id")));
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

    private final List<User> members = new ArrayList<>();

    private String name;
    private User owner;
    private ChatScope scope;
    private TextColor color;

    public GroupChat(@NotNull UUID id)
    {
        super(id);

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

    public GroupChat(@NotNull String name, @NotNull User owner)
    {
        super(UUID.randomUUID());

        this.name = name;
        this.owner = owner;
        this.scope = ChatScope.PRIVATE;
        this.color = NamedTextColor.BLUE;

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

        this.addMember(this.owner);
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

    public @NotNull Component getPrefix(@NotNull Language lang)
    {
        return Component.text().color(this.color)
                .append(Component.text(this.name))
                .append(Component.text(" > "))
                .build();
    }

    @Override
    public @NotNull List<User> getMembers()
    {
        return this.members;
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

        if (user.getChat() == this)
        {
            user.setChat(null);
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

    @Override
    public boolean isMember(Player player)
    {
        return false;
    }

    @Override
    public @NotNull Component format(@NotNull User sender, @NotNull User listener, @NotNull Component body)
    {
        return this.getPrefix(Language.getInstance(listener))
            .append(sender.getName(listener))
            .append(Component.text(": ").color(NamedTextColor.GRAY))
            .append(body);
    }

    public boolean inScope(@NotNull User user)
    {
        return user == this.owner ||
                this.isMember(user) ||
                (this.scope == ChatScope.PUBLIC) ||
                (this.scope == ChatScope.FRIEND && user.isFriend(this.owner)) ||
                (this.scope == ChatScope.OSATOU && user.hasOsatou() && user.getOsatou() == this.owner);
    }

    public void delete()
    {
        Chat.instances.remove(this);

        this.members.forEach(member -> {
            if (member.getChat() == this)
            {
                member.setChat(null);
            }
        });

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
