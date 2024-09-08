/**

 © 2024 Azisaba All Rights Reserved.

**/

package net.azisaba.vanilife;

import net.azisaba.vanilife.command.*;
import net.azisaba.vanilife.command.discord.MconsoleCommand;
import net.azisaba.vanilife.command.discord.MetubotCommand;
import net.azisaba.vanilife.command.filter.FilterCommand;
import net.azisaba.vanilife.command.mola.MolaCommand;
import net.azisaba.vanilife.command.plot.PlotCommand;
import net.azisaba.vanilife.command.service.ServiceCommand;
import net.azisaba.vanilife.command.vwm.VwmCommand;
import net.azisaba.vanilife.listener.*;
import net.azisaba.vanilife.runnable.PlayingRewardRunnable;
import net.azisaba.vanilife.service.ServiceManager;
import net.azisaba.vanilife.util.ChatFilter;
import net.azisaba.vanilife.util.PlotUtility;
import net.azisaba.vanilife.util.SqlUtility;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.azisaba.vanilife.vwm.VanilifeWorldManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import okhttp3.OkHttpClient;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

public final class Vanilife extends JavaPlugin
{
    private static Vanilife plugin;

    public static final Random random = new Random();

    public static final Pattern pattern1 = Pattern.compile("^\\d{4}/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])$");

    public static final OkHttpClient httpClient = new OkHttpClient();

    public static JDA jda;
    public static Guild server;
    public static TextChannel channel;

    public static ChatFilter filter;

    public static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd-HH:mm");
    public static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat sdf3 = new SimpleDateFormat("MM/dd");
    public static final SimpleDateFormat sdf4 = new SimpleDateFormat("yyMMddHHmmss");

    public static String DB_URL;
    public static String DB_USER;
    public static String DB_PASS;

    public static Role ROLE_SUPPORT;
    public static Role ROLE_MCONSOLE;

    public static final int MOLA_PLOT_NEW = 50;
    public static final int MOLA_PLOT_CLAIM = 40;

    public static String METUBOT_SERVER;
    public static UUID METUBOT_PROVIDER;

    public static Vanilife getPlugin()
    {
        return Vanilife.plugin;
    }

    public static ComponentLogger getPluginLogger()
    {
        return Vanilife.getPlugin().getComponentLogger();
    }

    public static FileConfiguration getPluginConfig()
    {
        return Vanilife.getPlugin().getConfig();
    }

    @Override
    public void onEnable()
    {
        Vanilife.plugin = this;

        this.getComponentLogger().info(Component.text("   "));
        this.getComponentLogger().info(Component.text("   (*'▽')/  ばにらいふ！ ").append(Component.text("v" + this.getDescription().getVersion()).color(NamedTextColor.BLUE)));
        this.getComponentLogger().info(Component.text("   azisaba.net").color(NamedTextColor.DARK_GRAY));
        this.getComponentLogger().info(Component.text("   "));

        this.getServer().getPluginManager().registerEvents(new EntityListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        this.getCommand("block").setExecutor(new BlockCommand());
        this.getCommand("enderchest").setExecutor(new EnderChestCommand());
        this.getCommand("filter").setExecutor(new FilterCommand());
        this.getCommand("friend").setExecutor(new FriendCommand());
        this.getCommand("friendlist").setExecutor(new FriendListCommand());
        this.getCommand("jnkn").setExecutor(new JnknCommand());
        this.getCommand("mail").setExecutor(new MailCommand());
        this.getCommand("mola").setExecutor(new MolaCommand());
        this.getCommand("mute").setExecutor(new MuteCommand());
        this.getCommand("nick").setExecutor(new NickCommand());
        this.getCommand("plot").setExecutor(new PlotCommand());
        this.getCommand("poll").setExecutor(new PollCommand());
        this.getCommand("profile").setExecutor(new ProfileCommand());
        this.getCommand("report").setExecutor(new ReportCommand());
        this.getCommand("rtp").setExecutor(new RtpCommand());
        this.getCommand("sara").setExecutor(new SaraCommand());
        this.getCommand("service").setExecutor(new ServiceCommand());
        this.getCommand("settings").setExecutor(new SettingsCommand());
        this.getCommand("tpa").setExecutor(new TpaCommand());
        this.getCommand("tpr").setExecutor(new TprCommand());
        this.getCommand("trade").setExecutor(new TradeCommand());
        this.getCommand("trash").setExecutor(new TrashCommand());
        this.getCommand("unblock").setExecutor(new UnblockCommand());
        this.getCommand("unmute").setExecutor(new UnmuteCommand());
        this.getCommand("vote").setExecutor(new VoteCommand());
        this.getCommand("vwm").setExecutor(new VwmCommand());
        this.getCommand("world").setExecutor(new WorldCommand());
        this.getCommand("worlds").setExecutor(new WorldsCommand());

        this.saveDefaultConfig();
        this.saveResource("service.yml", false);
        this.saveResource("vwm/vwm.json", false);

        SqlUtility.jdbc("org.mariadb.jdbc.Driver");
        ServiceManager.mount();

        Vanilife.jda = JDABuilder.createDefault(this.getConfig().getString("discord.token")).build();
        Vanilife.jda.addEventListener(new DiscordListener());
        Vanilife.jda.addEventListener(new MconsoleCommand());
        Vanilife.jda.addEventListener(new MetubotCommand());

        Vanilife.DB_URL = this.getConfig().getString("database.url");
        Vanilife.DB_USER = this.getConfig().getString("database.user");
        Vanilife.DB_PASS = this.getConfig().getString("database.pass");

        Vanilife.METUBOT_SERVER = this.getConfig().getString("metubot.server");
        Vanilife.METUBOT_PROVIDER = UUID.fromString(this.getConfig().getString("metubot.provider"));

        Vanilife.filter = new ChatFilter();
        VanilifeWorldManager.mount();
        PlotUtility.mount();
        new PlayingRewardRunnable().runTaskLater(Vanilife.getPlugin());
    }

    @Override
    public void onDisable()
    {
        VanilifeWorld.getInstances().forEach(w -> w.getWorlds().forEach(World::save));
        Vanilife.jda.shutdown();
        Vanilife.jda = null;
    }
}
