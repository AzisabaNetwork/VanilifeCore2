package net.azisaba.vanilife.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.housing.Housing;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.mail.Mail;
import net.azisaba.vanilife.user.request.IRequest;
import net.azisaba.vanilife.user.settings.Settings;
import net.azisaba.vanilife.user.subscription.ISubscription;
import net.azisaba.vanilife.user.subscription.SingletonSubscription;
import net.azisaba.vanilife.user.subscription.Subscriptions;
import net.azisaba.vanilife.util.Afk;
import net.azisaba.vanilife.util.ComponentUtility;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class User
{
    private static final ArrayList<User> instances = new ArrayList<>();

    public static User getInstance(@NotNull UUID id)
    {
        List<User> filteredInstances = User.instances.stream().filter(i -> i.getId().equals(id)).toList();
        return filteredInstances.isEmpty() ? UserUtility.exists(id) ? new User(id) : new User(id, true) : filteredInstances.getFirst();
    }

    public static User getInstance(Player player)
    {
        return User.getInstance(player.getUniqueId());
    }

    public static User getInstance(String name)
    {
        return User.getInstance(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    public static User getInstance(net.dv8tion.jda.api.entities.User discord)
    {
        if (discord == null)
        {
            return null;
        }

        List<User> filteredInstances = User.instances.stream().filter(i -> i.getDiscord() != null && i.getDiscord().getId().equals(discord.getId())).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static ArrayList<User> getInstances()
    {
        return User.instances;
    }

    public static void mount()
    {
        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);

            PreparedStatement stmt = con.prepareStatement("SELECT id FROM user");
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                UUID id = UUID.fromString(rs.getString("id"));
                User.getInstance(id);
            }

            rs.close();
            stmt.close();

            PreparedStatement stmt2 = con.prepareStatement("SELECT * FROM friend");
            ResultSet rs2 = stmt2.executeQuery();

            while (rs2.next())
            {
                User user1 = User.getInstance(UUID.fromString(rs2.getString("user1")));
                User user2 = User.getInstance(UUID.fromString(rs2.getString("user2")));

                user1.friends.add(user2);
                user2.friends.add(user1);
            }

            rs2.close();
            stmt2.close();

            PreparedStatement stmt3 = con.prepareStatement("SELECT * FROM block");
            ResultSet rs3 = stmt3.executeQuery();

            while (rs3.next())
            {
                User user1 = User.getInstance(UUID.fromString(rs3.getString("user1")));
                User user2 = User.getInstance(UUID.fromString(rs3.getString("user2")));

                user1.blocks.add(user2);
                user2.blocks.add(user1);
            }

            rs3.close();
            stmt3.close();

            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to mount user: %s", e.getMessage())).color(NamedTextColor.RED));
        }
    }

    private final UUID id;

    private String nick;
    private String bio;
    private Date birthday;
    private String youtube;
    private String twitter;
    private net.dv8tion.jda.api.entities.User discord;
    private int mola;
    private Sara sara;
    private UserStatus status;
    private Skin skin;
    private User osatou;
    private JsonObject storage;

    private Housing housing;

    private final Settings settings;

    private Date lastLogin;
    private int loginStreak;

    private final List<User> friends = new ArrayList<>();
    private final List<User> blocks = new ArrayList<>();
    private final List<Mail> mails;
    private final List<Skin> skins = new ArrayList<>();
    private final List<ISubscription> subscriptions = new ArrayList<>();
    private final List<IRequest> requests = new ArrayList<>();

    private BossBar bossBar;

    private User(@NotNull UUID id)
    {
        this.id = id;

        User.instances.add(this);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM user WHERE id = ?");
            stmt.setString(1, this.id.toString());

            ResultSet rs = stmt.executeQuery();
            rs.next();

            this.nick = rs.getString("nick");
            this.bio = rs.getString("bio");
            this.birthday = (rs.getString("birthday") == null) ? null : Vanilife.sdf2.parse(rs.getString("birthday"));
            this.youtube = rs.getString("youtube");
            this.twitter = rs.getString("twitter");
            this.discord = rs.getString("discord") == null ? null : Vanilife.jda.retrieveUserById(rs.getString("discord")).complete();
            this.mola = rs.getInt("mola");
            this.sara = Sara.valueOf(rs.getString("sara"));
            this.status = UserStatus.valueOf(rs.getString("status"));
            this.osatou = rs.getString("osatou") == null ? null : User.getInstance(UUID.fromString(rs.getString("osatou")));
            this.storage = JsonParser.parseString(rs.getString("storage")).getAsJsonObject();

            rs.close();
            stmt.close();

            PreparedStatement stmt2 = con.prepareStatement("SELECT * FROM login WHERE user = ?");
            stmt2.setString(1, this.id.toString());

            ResultSet rs2 = stmt2.executeQuery();
            rs2.next();

            this.lastLogin = (rs2.getString("login") == null) ? null : Vanilife.sdf2.parse(rs2.getString("login"));
            this.loginStreak = rs2.getInt("streak");

            rs2.close();
            stmt2.close();

            PreparedStatement stmt3 = con.prepareStatement("SELECT subscription FROM subscription WHERE user = ?");
            stmt3.setString(1, this.id.toString());
            ResultSet rs3 = stmt3.executeQuery();

            while (rs3.next())
            {
                ISubscription subscription = Subscriptions.valueOf(rs3.getString("subscription"));

                if (subscription != null)
                {
                    this.subscriptions.add(subscription);
                }
            }

            con.close();
        }
        catch (SQLException | ParseException e)
        {
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to get user record: %s", e.getMessage())).color(NamedTextColor.RED));
        }

        this.settings = Settings.getInstance(this);
        this.mails = UserUtility.getMails(this);
    }

    private User(@NotNull UUID id, boolean write)
    {
        this.id = id;

        this.nick = null;
        this.sara = Sara.DEFAULT;
        this.bio = null;
        this.youtube = null;
        this.twitter = null;
        this.discord = null;
        this.mola = 0;
        this.lastLogin = null;
        this.status = UserStatus.DEFAULT;
        this.storage = new JsonObject();

        User.instances.add(this);

        if (write)
        {
            try
            {
                Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);

                PreparedStatement stmt = con.prepareStatement("INSERT INTO user VALUES(?, NULL, NULL, NULL, NULL, NULL, NULL, 0, ?, ?, NULL, NULL, '{}')");
                stmt.setString(1, this.id.toString());
                stmt.setString(2, this.sara.toString());
                stmt.setString(3, this.status.toString());

                stmt.executeUpdate();

                stmt.close();

                PreparedStatement stmt2 = con.prepareStatement("INSERT INTO login VALUES(?, NULL, 0)");
                stmt2.setString(1, this.id.toString());

                stmt2.executeUpdate();

                stmt2.close();

                con.close();

                this.write("settings.ime", true);
                this.write("settings.chat", true);
            }
            catch (SQLException e)
            {
                Vanilife.getPluginLogger().error(Component.text(String.format("Failed to insert user record: %s", e.getMessage())).color(NamedTextColor.RED));
            }
        }

        this.housing = null;
        this.settings = Settings.getInstance(this);
        this.mails = UserUtility.getMails(this);
    }

    public @NotNull UUID getId()
    {
        return this.id;
    }

    public @NotNull Component getName()
    {
        return this.getSara().role.append(Sara.$50000YEN.level < this.getSara().level && this.getNick().contains("&") ?
                ComponentUtility.getAsComponent(this.getNick())
                : Component.text(this.getNick(), this.getSara().getColor())).clickEvent(ClickEvent.runCommand(String.format("/profile %s", this.getPlaneName()))).hoverEvent(HoverEvent.showText(Component.text("クリックしてプロフィールを開きます")));
    }

    public @NotNull String getPlaneName()
    {
        if (this.id.equals(UserUtility.UUID_AZISABA))
        {
            return "azisaba";
        }

        return Bukkit.getOfflinePlayer(this.id).getName();
    }

    public String getNick()
    {
        return (this.nick != null) ? this.nick : this.getPlaneName();
    }

    public void setNick(String nick)
    {
        this.nick = nick;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE user SET nick = ? WHERE id = ?");
            stmt.setString(1, this.nick);
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to update user record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public String getBio()
    {
        return this.bio;
    }

    public void setBio(String bio)
    {
        this.bio = bio;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE user SET bio = ? WHERE id = ?");
            stmt.setString(1, this.bio);
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to update user record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public Date getBirthday()
    {
        return this.birthday;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE user SET birthday = ? WHERE id = ?");
            stmt.setString(1, Vanilife.sdf2.format(this.birthday));
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to update user record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public String getYoutube()
    {
        return this.youtube;
    }

    public void setYouTube(String youtube)
    {
        this.youtube = youtube;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE user SET youtube = ? WHERE id = ?");
            stmt.setString(1, this.youtube);
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to update user record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public String getTwitter()
    {
        return this.twitter;
    }

    public void setTwitter(String twitter)
    {
        this.twitter = twitter;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE user SET twitter = ? WHERE id = ?");
            stmt.setString(1, this.twitter);
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to update user record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public net.dv8tion.jda.api.entities.User getDiscord()
    {
        return this.discord;
    }

    public void setDiscord(net.dv8tion.jda.api.entities.User discord)
    {
        this.discord = discord;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE user SET discord = ? WHERE id = ?");
            stmt.setString(1, this.discord == null ? null : this.discord.getId());
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to update user record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public void setDiscord(String discord)
    {
        this.setDiscord(Vanilife.jda.getUserById(discord));
    }

    public int getMola()
    {
        return this.mola;
    }

    public void setMola(int mola)
    {
        this.mola = Math.max(0, mola);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE user SET mola = ? WHERE id = ?");
            stmt.setInt(1, this.mola);
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to update user record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public void setMola(int mola, @NotNull String category, @NotNull TextColor color)
    {
        Player player = this.getAsPlayer();

        if (player != null)
        {
            if (this.bossBar != null)
            {
                this.bossBar.removeViewer(player);
            }

            float progress = (float) ((this.mola + mola) % 100) / 100;
            progress = (progress == 0) ? 1.0f : progress;

            this.bossBar = BossBar.bossBar(Component.text("Mola: ").color(NamedTextColor.WHITE)
                    .append(Language.translate(category, player).color(NamedTextColor.YELLOW).append(Component.text(CLI.getSpaces(2))))
                    .append(Component.text(String.format("+%s ", mola - this.mola)).color(NamedTextColor.AQUA))
                    .append(Component.text(mola).color(NamedTextColor.DARK_AQUA))
                    .append(Component.text(" Mola").color(NamedTextColor.GRAY)), progress, BossBar.Color.PINK, BossBar.Overlay.PROGRESS);

            if (! Afk.isAfk(player))
            {
                player.playSound(player, (progress == 1.0f) ? Sound.ENTITY_PLAYER_LEVELUP : Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
            }

            player.sendMessage(Component.text(String.format("+ %s Mola! (", (mola - this.mola))).color(color).append(Language.translate(category, player)).append(Component.text(")")));
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

    public @NotNull Sara getSara()
    {
        return (this.sara != null) ? this.sara : Sara.DEFAULT;
    }

    public void setSara(@NotNull Sara sara)
    {
        this.sara = sara;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE user SET sara = ? WHERE id = ?");
            stmt.setString(1, this.sara.toString());
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to update user record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    @NotNull
    public UserStatus getStatus()
    {
        return this.status;
    }

    public void setStatus(@NotNull UserStatus status)
    {
        this.status = status;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE user SET status = ? WHERE id = ?");
            stmt.setString(1, this.status.toString());
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to update user record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public Skin getSkin()
    {
        return this.skin;
    }

    public void setSkin(Skin skin)
    {
        this.skin = skin;

        if (this.isOnline() && this.skin != null)
        {
            this.skin.use(this.getAsPlayer());
        }

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE user SET skin = ? WHERE id = ?");
            stmt.setString(1, this.skin == null ? null : this.skin.getName());
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to update user record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public void addSkin(@NotNull Skin skin)
    {
        if (this.skins.contains(skin))
        {
            return;
        }

        this.skins.add(skin);
    }

    public User getOsatou()
    {
        return this.osatou;
    }

    public void setOsatou(User osatou)
    {
        if (this.osatou == osatou)
        {
            return;
        }

        if (osatou == this)
        {
            return;
        }

        final User former = this.osatou;

        this.write("settings.osatou.open", false);
        this.osatou = osatou;

        if (former != null)
        {
            former.setOsatou(null);
        }

        if (this.osatou != null && this.osatou.getOsatou() != this)
        {
            osatou.setOsatou(this);
        }

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE user SET osatou = ? WHERE id = ?");
            stmt.setString(1, this.osatou != null ? this.osatou.getId().toString() : null);
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to update user record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    @NotNull
    public JsonObject getStorage()
    {
        return this.storage;
    }

    public void saveStorage()
    {
        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE user SET storage = ? WHERE id = ?");
            stmt.setString(1, this.storage.toString());
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to update user record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public Date getLastLogin()
    {
        return this.lastLogin;
    }

    public void setLastLogin(@NotNull Date lastLogin)
    {
        this.lastLogin = lastLogin;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);

            PreparedStatement stmt = con.prepareStatement("UPDATE login SET login = ? WHERE user = ?");
            stmt.setString(1, (this.lastLogin == null) ? null : Vanilife.sdf2.format(this.lastLogin));
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to update login record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public int getLoginStreak()
    {
        return this.loginStreak;
    }

    public void setLoginStreak(int loginStreak)
    {
        this.loginStreak = loginStreak;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("UPDATE login SET streak = ? WHERE user = ?");
            stmt.setInt(1, this.loginStreak);
            stmt.setString(2, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to update login record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public Housing getHousing()
    {
        return this.housing;
    }

    public void setHousing(Housing housing)
    {
        this.housing = housing;
    }

    public @NotNull Settings getSettings()
    {
        return this.settings;
    }

    public @NotNull List<User> getFriends()
    {
        return this.friends;
    }

    public @NotNull List<User> getBlocks()
    {
        return this.blocks;
    }

    public @NotNull List<Mail> getMails()
    {
        return this.mails;
    }

    public @NotNull List<Skin> getSkins()
    {
        return this.skins;
    }

    public @NotNull List<ISubscription> getSubscriptions()
    {
        return this.subscriptions;
    }

    public @NotNull List<IRequest> getRequests()
    {
        return this.requests;
    }

    public Player getAsPlayer()
    {
        return Bukkit.getPlayer(this.id);
    }

    public @NotNull OfflinePlayer getAsOfflinePlayer()
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

    public boolean inHousing()
    {
        return this.isOnline() && this.getAsPlayer().getWorld().equals(Housing.getWorld());
    }

    public boolean hasOsatou()
    {
        return this.osatou != null;
    }

    public boolean hasHousing()
    {
        return this.housing != null;
    }

    public boolean hasSubscription(ISubscription subscription)
    {
        return this.subscriptions.stream().anyMatch(s -> s == subscription);
    }

    public void sendMail(User user, String subject, String message)
    {
        new Mail(user, this, subject, message);
    }

    public void sendNotice(String subject, String message)
    {
        this.sendMail(User.getInstance("azisaba"), subject, message);
    }

    public void friend(@NotNull User user)
    {
        if (user == this)
        {
            return;
        }

        if (this.isFriend(user))
        {
            return;
        }

        this.friends.add(user);
        user.friends.add(this);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO friend VALUES(?, ?)");
            stmt.setString(1, this.id.toString());
            stmt.setString(2, user.getId().toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to insert friend record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public void unfriend(@NotNull User user)
    {
        if (! this.isFriend(user))
        {
            return;
        }

        this.friends.remove(user);
        user.friends.remove(this);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("DELETE FROM friend WHERE (user1 = ? AND user2 = ?) OR (user1 = ? AND user2 = ?)");
            stmt.setString(1, this.id.toString());
            stmt.setString(2, user.getId().toString());
            stmt.setString(3, user.getId().toString());
            stmt.setString(4, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to delete friend record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public void block(@NotNull User user)
    {
        if (this.blocks.contains(user))
        {
            return;
        }

        this.blocks.add(user);
        this.unfriend(user);

        if (this.osatou == user)
        {
            this.setOsatou(null);
        }

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO block VALUES(?, ?)");
            stmt.setString(1, this.id.toString());
            stmt.setString(2, user.getId().toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to insert block record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public void unblock(@NotNull User user)
    {
        if (! this.blocks.contains(user))
        {
            return;
        }

        this.blocks.remove(user);

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("DELETE FROM block WHERE (user1 = ? AND user2 = ?) OR (user1 = ? AND user2 = ?)");
            stmt.setString(1, this.id.toString());
            stmt.setString(2, user.getId().toString());
            stmt.setString(3, user.getId().toString());
            stmt.setString(4, this.id.toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().warn(Component.text("Failed to delete block record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public void subscribe(@NotNull ISubscription subscription)
    {
        if (this.hasSubscription(subscription))
        {
            return;
        }

        this.subscriptions.add(subscription);

        if (! subscription.getClass().isAnnotationPresent(SingletonSubscription.class))
        {
            return;
        }

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);

            PreparedStatement stmt = con.prepareStatement("INSERT INTO subscription VALUES(?, ?)");
            stmt.setString(1, this.id.toString());
            stmt.setString(2, subscription.getName());

            stmt.executeUpdate();

            stmt.close();

            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().error(Component.text("Failed to insert subscription record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public void unsubscribe(@NotNull ISubscription subscription)
    {
        this.subscriptions.removeIf(s -> s == subscription);

        if (! subscription.getClass().isAnnotationPresent(SingletonSubscription.class))
        {
            return;
        }

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement("DELETE FROM subscription WHERE user = ? AND subscription = ?");
            stmt.setString(1, this.id.toString());
            stmt.setString(2, subscription.getName());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().error(Component.text("Failed to delete subscription record: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    public void write(@NotNull String key, @NotNull String value)
    {
        this.storage.addProperty(key, value);
        this.saveStorage();
    }

    public void write(@NotNull String key, @NotNull Number value)
    {
        this.storage.addProperty(key, value);
        this.saveStorage();
    }

    public void write(@NotNull String key, @NotNull Boolean value)
    {
        this.storage.addProperty(key, value);
        this.saveStorage();
    }

    public void write(@NotNull String key, @NotNull Character value)
    {
        this.storage.addProperty(key, value);
        this.saveStorage();
    }

    public JsonElement read(@NotNull String key)
    {
        return this.storage.get(key);
    }
}
