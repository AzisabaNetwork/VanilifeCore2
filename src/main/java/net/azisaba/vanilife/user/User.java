package net.azisaba.vanilife.user;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.mail.Mail;
import net.azisaba.vanilife.user.request.IRequest;
import net.azisaba.vanilife.user.settings.Settings;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class User
{
    private static final ArrayList<User> instances = new ArrayList<>();

    public static User getInstance(UUID id)
    {
        ArrayList<User> filteredInstances = new ArrayList<>(User.instances.stream().filter(i -> i.getId().equals(id)).toList());
        return filteredInstances.isEmpty() ? UserUtility.exists(id) ? new User(id) : new User(id, null, Sara.DEFAULT, null, null, null, null, 0, UserStatus.DEFAULT) : filteredInstances.getFirst();
    }

    public static User getInstance(Player player)
    {
        return User.getInstance(player.getUniqueId());
    }

    public static User getInstance(String name)
    {
        return User.getInstance(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    public static ArrayList<User> getInstances()
    {
        return User.instances;
    }

    private final UUID id;

    private String nick;
    private Sara sara;
    private String bio;
    private Date birthday;
    private String youtube;
    private String twitter;
    private String discord;
    private int mola;
    private UserStatus status;

    private final Settings settings;

    private Date lastLogin;
    private int loginStreak;

    private final ArrayList<User> friends = new ArrayList<>();
    private final ArrayList<User> blocks = new ArrayList<>();
    private final ArrayList<Mail> mails;
    private final ArrayList<IRequest> requests = new ArrayList<>();

    private BossBar bossBar;

    private User(UUID id)
    {
        this.id = id;
        this.settings = Settings.getInstance(this);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM user WHERE id = ?");
            stmt.setString(1, this.id.toString());

            ResultSet rs = stmt.executeQuery();
            rs.next();

            this.nick = rs.getString("nick");
            this.sara = Sara.valueOf(rs.getString("sara"));
            this.bio = rs.getString("bio");
            this.birthday = (rs.getString("birthday") == null) ? null : Vanilife.sdf1.parse(rs.getString("birthday"));
            this.youtube = rs.getString("youtube");
            this.twitter = rs.getString("twitter");
            this.discord = rs.getString("discord");
            this.mola = rs.getInt("mola");
            this.status = UserStatus.valueOf(rs.getString("status"));

            rs.close();
            stmt.close();
            User.instances.add(this);

            PreparedStatement stmt2 = con.prepareStatement("SELECT * FROM friend WHERE user1 = ? OR user2 = ?");
            stmt2.setString(1, this.id.toString());
            stmt2.setString(2, this.id.toString());

            ResultSet rs2 = stmt2.executeQuery();

            while (rs2.next())
            {
                User user1 = User.getInstance(UUID.fromString(rs2.getString("user1")));
                User user2 = User.getInstance(UUID.fromString(rs2.getString("user2")));

                this.friends.add((user1 == this) ? user2 : user1);
            }

            rs2.close();
            stmt2.close();

            PreparedStatement stmt3 = con.prepareStatement("SELECT * FROM block WHERE user1 = ?");
            stmt3.setString(1, this.id.toString());

            ResultSet rs3 = stmt3.executeQuery();

            while (rs3.next())
            {
                this.blocks.add(User.getInstance(UUID.fromString(rs3.getString("user2"))));
            }

            rs3.close();
            stmt3.close();

            PreparedStatement stmt4 = con.prepareStatement("SELECT * FROM login WHERE user = ?");
            stmt4.setString(1, this.id.toString());

            ResultSet rs4 = stmt4.executeQuery();
            rs4.next();

            this.lastLogin = (rs4.getString("login") == null) ? null : Vanilife.sdf1.parse(rs4.getString("login"));
            this.loginStreak = rs4.getInt("streak");

            rs4.close();
            stmt4.close();

            con.close();
        }
        catch (SQLException | ParseException e)
        {
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to get user record: %s", e.getMessage())).color(NamedTextColor.RED));
        }

        this.mails = UserUtility.getMails(this);
    }

    private User(UUID id, String nick, Sara sara, String bio, String youtube, String twitter, String discord, int mola, UserStatus status)
    {
        this.id = id;

        this.nick = nick;
        this.sara = sara;
        this.bio = bio;
        this.youtube = youtube;
        this.twitter = twitter;
        this.discord = discord;
        this.mola = mola;
        this.lastLogin = null;
        this.status = status;

        this.settings = Settings.Builder.build(this);

        User.instances.add(this);

        this.mails = UserUtility.getMails(this);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);

            PreparedStatement stmt = con.prepareStatement("INSERT INTO user VALUES(?, ?, ?, ?, NULL, ?, ?, ?, ?, ?)");
            stmt.setString(1, this.id.toString());
            stmt.setString(2, this.nick);
            stmt.setString(3, this.sara.toString());
            stmt.setString(4, this.bio);
            stmt.setString(5, this.youtube);
            stmt.setString(6, this.twitter);
            stmt.setString(7, this.discord);
            stmt.setInt(8, this.mola);
            stmt.setString(9, this.status.toString());

            stmt.executeUpdate();

            stmt.close();

            PreparedStatement stmt2 = con.prepareStatement("INSERT INTO login VALUES(?, NULL, 0)");
            stmt2.setString(1, this.id.toString());

            stmt2.executeUpdate();

            stmt2.close();

            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to insert user record: %s", e.getMessage())).color(NamedTextColor.RED));
        }
    }

    public UUID getId()
    {
        return this.id;
    }

    public Component getName()
    {
        return this.getSara().role.append(Component.text(this.getNick(), this.getSara().color)).clickEvent(ClickEvent.runCommand(String.format("/profile %s", this.getPlaneName()))).hoverEvent(HoverEvent.showText(Component.text("クリックしてプロフィールを開きます")));
    }

    public String getPlaneName()
    {
        return Bukkit.getOfflinePlayer(this.id).getName();
    }

    public String getNick()
    {
        return (this.nick != null) ? this.nick : this.getPlaneName();
    }

    public void setNick(String nick)
    {
        this.nick = nick;
        this.upload();
    }

    public Sara getSara()
    {
        return (this.sara != null) ? this.sara : Sara.DEFAULT;
    }

    public void setSara(Sara sara)
    {
        this.sara = sara;
        this.upload();
    }

    public String getBio()
    {
        return this.bio;
    }

    public void setBio(String bio)
    {
        this.bio = bio;
        this.upload();
    }

    public Date getBirthday()
    {
        return this.birthday;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
        this.upload();
    }

    public String getYoutube()
    {
        return this.youtube;
    }

    public void setYouTube(String youtube)
    {
        this.youtube = youtube;
        this.upload();
    }

    public String getTwitter()
    {
        return this.twitter;
    }

    public void setTwitter(String twitter)
    {
        this.twitter = twitter;
        this.upload();
    }

    public String getDiscord()
    {
        return this.discord;
    }

    public void setDiscord(String discord)
    {
        this.discord = discord;
        this.upload();
    }

    public int getMola()
    {
        return this.mola;
    }

    public void setMola(int mola)
    {
        this.mola = Math.max(0, mola);
        this.upload();
    }

    public void setMola(int mola, String category)
    {
        Player player = this.getAsOfflinePlayer().getPlayer();

        if (player != null)
        {
            if (this.bossBar != null)
            {
                this.bossBar.removeViewer(player);
            }

            float progress = (float) ((this.mola + mola) % 100) / 100;
            progress = (progress == 0) ? 1.0f : progress;

            this.bossBar = BossBar.bossBar(Component.text("Mola: ").color(NamedTextColor.WHITE)
                    .append(Component.text(category + "  ").color(NamedTextColor.YELLOW))
                    .append(Component.text(String.format("+ %s ", mola - this.mola)).color(NamedTextColor.AQUA))
                    .append(Component.text(mola).color(NamedTextColor.DARK_AQUA))
                    .append(Component.text(" Mola").color(NamedTextColor.GRAY)), progress, BossBar.Color.PINK, BossBar.Overlay.PROGRESS);

            player.playSound(player, (progress == 1.0f) ? Sound.ENTITY_PLAYER_LEVELUP : Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
            player.sendMessage(Component.text(category + "! ").color(NamedTextColor.GREEN).append(Component.text(String.format("+ %s Mola", mola - this.mola)).color(NamedTextColor.GOLD)));
            this.bossBar.addViewer(player);

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    bossBar.removeViewer(player);
                }
            }.runTaskLater(Vanilife.getPlugin(), 20L * 2);
        }

        this.setMola(mola);
    }

    public Date getLastLogin()
    {
        return this.lastLogin;
    }

    public void setLastLogin(Date lastLogin)
    {
        this.lastLogin = lastLogin;
        this.upload();
    }

    public int getLoginStreak()
    {
        return this.loginStreak;
    }

    public void setLoginStreak(int loginStreak)
    {
        this.loginStreak = loginStreak;
        this.upload();
    }

    public UserStatus getStatus()
    {
        return this.status;
    }

    public void setStatus(UserStatus status)
    {
        this.status = status;
        this.upload();
    }

    public Settings getSettings()
    {
        return this.settings;
    }

    public ArrayList<User> getFriends()
    {
        return this.friends;
    }

    public ArrayList<User> getBlocks()
    {
        return this.blocks;
    }

    public ArrayList<Mail> getMails()
    {
        return this.mails;
    }

    public ArrayList<IRequest> getRequests()
    {
        return this.requests;
    }

    public OfflinePlayer getAsOfflinePlayer()
    {
        return Bukkit.getOfflinePlayer(this.id);
    }

    public boolean isOnline()
    {
        return Bukkit.getOfflinePlayer(this.id).isOnline();
    }

    public boolean isFriend(User user)
    {
        return this.friends.contains(user);
    }

    public boolean isBlock(User user)
    {
        return this.blocks.contains(user);
    }

    public void friend(User user)
    {
        if (user == this)
        {
            return;
        }

        if (! this.isFriend(user))
        {
            this.friends.add(user);
            user.friends.add(this);
            this.upload();
        }
    }

    public void unfriend(User user)
    {
        if (this.isFriend(user))
        {
            this.friends.remove(user);
            user.friends.remove(this);
            this.upload();
        }
    }

    public void block(User user)
    {
        if (! this.blocks.contains(user))
        {
            this.blocks.add(user);
            this.unfriend(user);
            this.upload();
        }
    }

    public void unblock(User user)
    {
        if (this.blocks.contains(user))
        {
            this.blocks.remove(user);
            this.upload();;
        }
    }

    private void upload()
    {
        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE user SET nick = ?, sara = ?, bio = ?, birthday = ?, youtube = ?, twitter = ?, discord = ?, mola = ?, status = ? WHERE id = ?");
            stmt.setString(1, this.nick);
            stmt.setString(2, this.getSara().toString());
            stmt.setString(3, this.bio);
            stmt.setString(4, (this.birthday == null) ? null : Vanilife.sdf1.format(this.birthday));
            stmt.setString(5, this.youtube);
            stmt.setString(6, this.twitter);
            stmt.setString(7, this.discord);
            stmt.setInt(8, this.mola);
            stmt.setString(9, this.status.toString());
            stmt.setString(10, this.id.toString());

            stmt.executeUpdate();

            stmt.close();

            PreparedStatement stmt2 = con.prepareStatement("DELETE FROM friend WHERE user1 = ? OR user2 = ?");
            stmt2.setString(1, this.id.toString());
            stmt2.setString(2, this.id.toString());
            stmt2.executeUpdate();
            stmt2.close();

            PreparedStatement stmt3 = con.prepareStatement("INSERT INTO friend VALUES(?, ?)");

            for (User friend : this.friends)
            {
                stmt3.setString(1, this.id.toString());
                stmt3.setString(2, friend.id.toString());
                stmt3.executeUpdate();
            }

            stmt3.close();

            PreparedStatement stmt4 = con.prepareStatement("DELETE FROM block WHERE user1 = ?");
            stmt4.setString(1, this.id.toString());
            stmt4.executeUpdate();
            stmt4.close();

            PreparedStatement stmt5 = con.prepareStatement("INSERT INTO block VALUES(?, ?)");

            for (User block : this.blocks)
            {
                stmt5.setString(1, this.id.toString());
                stmt5.setString(2, block.id.toString());
                stmt5.executeUpdate();
            }

            stmt5.close();

            PreparedStatement stmt6 = con.prepareStatement("UPDATE login SET login = ?, streak = ? WHERE user = ?");
            stmt6.setString(1, (this.lastLogin == null) ? null : Vanilife.sdf1.format(this.lastLogin));
            stmt6.setInt(2, this.loginStreak);
            stmt6.setString(3, this.id.toString());

            stmt6.executeUpdate();

            stmt6.close();

            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to update user record: %s", e.getMessage())).color(NamedTextColor.RED));
        }
    }
}
