package net.azisaba.vanilife.user.mail;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.ComponentUtility;
import net.azisaba.vanilife.util.UserUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Mail
{
    private static final List<Mail> instances = new ArrayList<>();

    public static Mail getInstance(UUID id)
    {
        List<Mail> filteredInstances = new ArrayList<>(Mail.instances.stream().filter(i -> i.getId().equals(id)).toList());
        return filteredInstances.isEmpty() ? new Mail(id) : filteredInstances.getFirst();
    }

    private final UUID id;

    private User from;
    private User to;

    private String subject;
    private Date date;
    private String message;

    private boolean read;

    private Mail(UUID id)
    {
        this.id = id;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM mail WHERE id = ?");
            stmt.setString(1, this.id.toString());

            ResultSet rs = stmt.executeQuery();
            rs.next();

            this.from = User.getInstance(UUID.fromString(rs.getString("user_from")));
            this.to = User.getInstance(UUID.fromString(rs.getString("user_to")));
            this.subject = rs.getString("subject");
            this.date = Vanilife.sdf2.parse(rs.getString("date"));
            this.message = rs.getString("message");
            this.read = rs.getBoolean("readed");

            rs.close();
            stmt.close();
            con.close();

            Mail.instances.add(this);
        }
        catch (SQLException | ParseException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to get mail record: %s", e.getMessage())).color(NamedTextColor.RED));
        }
    }

    public Mail(User from, User to, String subject, String message)
    {
        this.id = UUID.randomUUID();
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.date = new Date();
        this.message = message;
        this.read = false;

        this.from.getMails().addFirst(this);
        this.to.getMails().addFirst(this);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO mail VALUES(?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, this.id.toString());
            stmt.setString(2, this.from.getId().toString());
            stmt.setString(3, this.to.getId().toString());
            stmt.setString(4, this.subject);
            stmt.setString(5, Vanilife.sdf2.format(this.date));
            stmt.setString(6, this.message);
            stmt.setBoolean(7, false);

            stmt.executeUpdate();

            stmt.close();
            con.close();

            Mail.instances.add(this);
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to insert mail record: %s", e.getMessage())).color(NamedTextColor.RED));
        }

        if (this.to.isOnline())
        {
            Player player = this.to.asPlayer();

            player.sendMessage(Component.text("✉ ").color(NamedTextColor.GRAY).append(this.from.getName().decorate(TextDecoration.BOLD)).append(Component.text(" ➡ ").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.BOLD, false).append(ComponentUtility.asChat(this.from, this.message).color(NamedTextColor.WHITE))));
            this.read();
        }


        if ((Vanilife.filter.filter(this.message) || Vanilife.filter.filter(this.subject)) && ! UserUtility.isAdmin(this.from))
        {
            Vanilife.CHANNEL_CONSOLE.sendMessageEmbeds(new EmbedBuilder()
                    .setTitle(":shield: メール")
                    .setColor(Color.RED)
                    .addField("From:", String.format("%s (%s)", this.from.getPlaneName(), this.from.getId()), false)
                    .addField("To:", String.format("%s (%s)", this.to.getPlaneName(), this.to.getId()), false)
                    .addField("Subject", this.subject, false)
                    .addField("Message", this.message, false)
                    .build()).queue();

            Vanilife.CHANNEL_CONSOLE.sendMessage(":envelope_with_arrow: " + Vanilife.ROLE_SUPPORT.getAsMention() + " このメールはチャットフィルタリングによって不適切と判断されました、ご確認をお願いします").queue();
            return;
        }

        Vanilife.CHANNEL_HISTORY.sendMessageEmbeds(new EmbedBuilder()
                .setTitle("メール")
                .addField("From:", String.format("%s (%s)", this.from.getPlaneName(), this.from.getId()), false)
                .addField("To:", String.format("%s (%s)", this.to.getPlaneName(), this.to.getId()), false)
                .addField("Subject", this.subject, false)
                .addField("Message", this.message, false)
                .build()).queue();
    }

    public UUID getId()
    {
        return this.id;
    }

    public User getFrom()
    {
        return this.from;
    }

    public User getTo()
    {
        return this.to;
    }

    public String getSubject()
    {
        return this.subject;
    }

    public Date getDate()
    {
        return this.date;
    }

    public String getMessage()
    {
        return this.message;
    }

    public boolean isRead()
    {
        return this.read;
    }

    public void read()
    {
        this.read = true;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE mail SET readed = true WHERE id = ?");
            stmt.setString(1, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to update mail record: %s", e.getMessage())).color(NamedTextColor.RED));
        }
    }
}
