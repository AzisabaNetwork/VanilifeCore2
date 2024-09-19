package net.azisaba.vanilife.report;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Report
{
    private static final ArrayList<Report> instances = new ArrayList<>();

    public static Report getInstance(UUID id)
    {
        ArrayList<Report> filteredInstances = new ArrayList<>(Report.instances.stream().filter(i -> i.getId().equals(id)).toList());
        return filteredInstances.isEmpty() ? new Report(id) : filteredInstances.getFirst();
    }

    public static Report getInstance(Message controller)
    {
         ArrayList<Report> filteredInstances = new ArrayList<>(Report.instances.stream().filter(i -> i.getController().equals(controller)).toList());
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    private final UUID id;

    private User sender;
    private String details;
    private Location location;
    private Date date;
    private Message controller;
    private boolean supported;

    public Report(User sender, String details)
    {
        this.id = UUID.randomUUID();
        this.sender = sender;
        this.details = details;
        this.date = new Date();
        this.location = sender.getAsPlayer().getLocation();

        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(sender.getPlaneName(), null, String.format("https://api.mineatar.io/face/%s", sender.getId().toString().replace("-", "")))
                .setTitle("レポートを受信しました")
                .setDescription(Vanilife.ROLE_SUPPORT.getAsMention())
                .addField("レポート内容", details, true)
                .addField("座標", String.format("%s : %s, %s, %s", this.location.getWorld().getName(), this.location.getBlockX(), this.location.getBlockY(), this.location.getBlockZ()), false)
                .setFooter("サポートを発行するにはこのメッセージに返信してください")
                .setColor(Color.YELLOW);

        Vanilife.consoleChannel.sendMessageEmbeds(builder.build()).queue(message -> {
            this.controller = message;

            try
            {
                Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
                PreparedStatement stmt = con.prepareStatement("INSERT INTO report VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, 0)");
                stmt.setString(1, this.id.toString());
                stmt.setString(2, this.sender.getId().toString());
                stmt.setString(3, this.details);
                stmt.setString(4, this.location.getWorld().getName());
                stmt.setInt(5, this.location.getBlockX());
                stmt.setInt(6, this.location.getBlockY());
                stmt.setInt(7, this.location.getBlockZ());
                stmt.setString(8, Vanilife.sdf1.format(this.date));
                stmt.setString(9, this.controller.getId());

                stmt.executeUpdate();

                stmt.close();
                con.close();
            }
            catch (SQLException e)
            {
                Vanilife.getPluginLogger().error(Component.text(String.format("Failed to insert report record: %s", e.getMessage())).color(NamedTextColor.RED));
            }
        });

        Report.instances.add(this);
    }

    public Report(User sender, String details, User to)
    {
        this(sender, String.format("To: %s\n%s", to.getPlaneName(), details));
    }

    private Report(UUID id)
    {
        this.id = id;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM report WHERE id = ?");
            stmt.setString(1, this.id.toString());

            ResultSet rs = stmt.executeQuery();
            rs.next();

            this.sender = User.getInstance(rs.getString("sender"));
            this.details = rs.getString("details");
            this.location = new Location(Bukkit.getWorld(rs.getString("world")), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));
            this.date = Vanilife.sdf1.parse(rs.getString("date"));

            Vanilife.consoleChannel.retrieveMessageById(rs.getString("controller")).queue(message -> {
                this.controller = message;
            });

            this.supported = rs.getBoolean("supported");

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException | ParseException e)
        {
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to get report record: %s", e.getMessage())).color(NamedTextColor.RED));
        }

        Report.instances.add(this);
    }

    public UUID getId()
    {
        return this.id;
    }

    public User getSender()
    {
        return this.sender;
    }

    public String getDetails()
    {
        return this.details;
    }

    public Location getLocation()
    {
        return this.location;
    }

    public Date getDate()
    {
        return this.date;
    }

    public Message getController()
    {
        return this.controller;
    }

    public void support()
    {
        this.supported = true;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE report SET supported = 1 WHERE id = ?");
            stmt.setString(1, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to update report record: %s", e.getMessage())).color(NamedTextColor.RED));
        }
    }

    public boolean isSupported()
    {
        return this.supported;
    }
}
