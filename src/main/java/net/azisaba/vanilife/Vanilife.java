/**

 © 2024 Azisaba All Rights Reserved.

 Developed by @tksimeji.

**/

package net.azisaba.vanilife;

import net.azisaba.vanilife.arcade.Paint;
import net.azisaba.vanilife.command.*;
import net.azisaba.vanilife.command.discord.MconsoleCommand;
import net.azisaba.vanilife.command.discord.MetubotCommand;
import net.azisaba.vanilife.command.discord.VanilifeLinkCommand;
import net.azisaba.vanilife.command.discord.VanilifeUnlinkCommand;
import net.azisaba.vanilife.command.filter.FilterCommand;
import net.azisaba.vanilife.command.gomenne.GomenneCommand;
import net.azisaba.vanilife.command.wallet.WalletCommand;
import net.azisaba.vanilife.command.service.ServiceCommand;
import net.azisaba.vanilife.entity.VanilifeEntityListener;
import net.azisaba.vanilife.gimmick.MessageGimmick;
import net.azisaba.vanilife.gimmick.SoundGimmick;
import net.azisaba.vanilife.gimmick.TitleGimmick;
import net.azisaba.vanilife.housing.Housing;
import net.azisaba.vanilife.housing.HousingAfkRunnable;
import net.azisaba.vanilife.housing.HousingListener;
import net.azisaba.vanilife.listener.*;
import net.azisaba.vanilife.nkip.NightCheckRunnable;
import net.azisaba.vanilife.runnable.*;
import net.azisaba.vanilife.service.Service;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.subscription.RichEmote;
import net.azisaba.vanilife.util.Afk;
import net.azisaba.vanilife.util.ChatFilter;
import net.azisaba.vanilife.util.SqlUtility;
import net.azisaba.vanilife.vc.VoiceChatListener;
import net.azisaba.vanilife.vc.VoiceChatRunnable;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.azisaba.vanilife.vwm.VanilifeWorldManager;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import okhttp3.OkHttpClient;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

public final class Vanilife extends JavaPlugin
{
    private static Vanilife plugin;

    private static String version;

    public static final Random random = new Random();

    public static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
    public static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd-HH:mm");
    public static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat sdf4 = new SimpleDateFormat("MM/dd");
    public static final SimpleDateFormat sdf5 = new SimpleDateFormat("yyMMddHHmmss");

    public static final Pattern pattern1 = Pattern.compile("^\\d{4}/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])$");
    public static final Pattern pattern2 = Pattern.compile("^[a-z0-9]{3,32}$");

    public static final OkHttpClient httpclient = new OkHttpClient();

    private static CoreProtectAPI coreprotect;

    public static ChatFilter filter;

    public static JDA jda;
    public static Category category;

    public static Guild SERVER_PUBLIC;
    public static Guild SERVER_PRIVATE;

    public static NewsChannel CHANNEL_ANNOUNCE;
    public static TextChannel CHANNEL_CONSOLE;
    public static TextChannel CHANNEL_HISTORY;
    public static VoiceChannel CHANNEL_VOICE;

    public static String DB_URL;
    public static String DB_USER;
    public static String DB_PASS;

    public static Role ROLE_SUPPORT;
    public static Role ROLE_MCONSOLE;
    public static Role ROLE_DEVELOPER;

    public static final int MOLA_PLOT_NEW = 150;
    public static final int MOLA_PLOT_CLAIM = 60;
    public static final int MOLA_TPR = 2;
    public static final int MOLA_RTP = 6;
    public static final int MOLA_PTP = 6;

    public static String METUBOT_SERVER;
    public static UUID METUBOT_PROVIDER;

    public static boolean publisher = false;

    public static Vanilife getPlugin()
    {
        return Vanilife.plugin;
    }

    public static String getPluginVersion()
    {
        return Vanilife.version;
    }

    public static ComponentLogger getPluginLogger()
    {
        return Vanilife.getPlugin().getComponentLogger();
    }

    public static FileConfiguration getPluginConfig()
    {
        return Vanilife.getPlugin().getConfig();
    }

    public static CoreProtectAPI getCoreProtect()
    {
        return Vanilife.coreprotect;
    }

    public static void sendExceptionReport(@NotNull Exception exception)
    {
        if (Vanilife.CHANNEL_CONSOLE == null)
        {
            return;
        }

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        String clazz = null;
        String method = null;
        int line = -1;

        if (2 < stackTrace.length)
        {
            StackTraceElement caller = stackTrace[2];
            clazz = caller.getClassName();
            method = caller.getMethodName();
            line = caller.getLineNumber();
        }

        Vanilife.CHANNEL_CONSOLE.sendMessageEmbeds(new EmbedBuilder()
                .setTitle(exception.getClass().getSimpleName())
                .setDescription(exception.getMessage())
                .addField("クラス", clazz == null ? "不明": clazz, false)
                .addField("メソッド", method == null ? "不明" : method, false)
                .addField("行", line < 0 ? "不明" : String.valueOf(line), false)
                .setColor(Color.RED)
                .build()).queue();

        Vanilife.CHANNEL_CONSOLE.sendMessage(":warning: " + Vanilife.ROLE_DEVELOPER.getAsMention() + " **予期しない例外が発生しました**").queue();
    }

    @Override
    public void onEnable()
    {
        Vanilife.plugin = this;
        Vanilife.version = this.getDescription().getVersion();

        this.getComponentLogger().info(Component.text("   "));
        this.getComponentLogger().info(Component.text("   (*'▽')/  ばにらいふ！ ").append(Component.text("v" + Vanilife.version).color(NamedTextColor.BLUE)));
        this.getComponentLogger().info(Component.text("   azisaba.net").color(NamedTextColor.DARK_GRAY));
        this.getComponentLogger().info(Component.text("   "));

        this.getServer().getPluginManager().registerEvents(new EntityListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
        this.getServer().getPluginManager().registerEvents(new HousingListener(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new RedstoneListener(), this);
        this.getServer().getPluginManager().registerEvents(new VanilifeEntityListener(), this);

        this.getServer().getPluginManager().registerEvents(new Afk(), this);
        this.getServer().getPluginManager().registerEvents(new Paint(), this);
        this.getServer().getPluginManager().registerEvents(new RichEmote(), this);

        this.getCommand("accept").setExecutor(new AcceptCommand());
        this.getCommand("alert").setExecutor(new AlertCommand());
        this.getCommand("block").setExecutor(new BlockCommand());
        this.getCommand("chat").setExecutor(new net.azisaba.vanilife.command.ChatCommand());
        this.getCommand("/chat").setExecutor(new net.azisaba.vanilife.command.chat.ChatCommand());
        this.getCommand("checkout").setExecutor(new CheckoutCommand());
        this.getCommand("domain").setExecutor(new DomainCommand());
        this.getCommand("emote").setExecutor(new EmoteCommand());
        this.getCommand("enderchest").setExecutor(new EnderChestCommand());
        this.getCommand("feedback").setExecutor(new FeedbackCommand());
        this.getCommand("filter").setExecutor(new FilterCommand());
        this.getCommand("friend").setExecutor(new FriendCommand());
        this.getCommand("friendlist").setExecutor(new FriendListCommand());
        this.getCommand("housing").setExecutor(new HousingCommand());
        this.getCommand("jail").setExecutor(new JailCommand());
        this.getCommand("ime").setExecutor(new GomenneCommand());
        this.getCommand("jnkn").setExecutor(new JnknCommand());
        this.getCommand("language").setExecutor(new LanguageCommand());
        this.getCommand("mail").setExecutor(new MailCommand());
        this.getCommand("mute").setExecutor(new MuteCommand());
        this.getCommand("nick").setExecutor(new NickCommand());
        this.getCommand("nkip").setExecutor(new NkipCommand());
        this.getCommand("osatou").setExecutor(new OsatouCommand());
        this.getCommand("plot").setExecutor(new PlotCommand());
        this.getCommand("/plot").setExecutor(new net.azisaba.vanilife.command.plot.PlotCommand());
        this.getCommand("poll").setExecutor(new PollCommand());
        this.getCommand("profile").setExecutor(new ProfileCommand());
        this.getCommand("ptp").setExecutor(new PtpCommand());
        this.getCommand("referral").setExecutor(new ReferralCommand());
        this.getCommand("realname").setExecutor(new RealNameCommand());
        this.getCommand("report").setExecutor(new ReportCommand());
        this.getCommand("review").setExecutor(new ReviewCommand());
        this.getCommand("rtp").setExecutor(new RtpCommand());
        this.getCommand("sara").setExecutor(new SaraCommand());
        this.getCommand("service").setExecutor(new ServiceCommand());
        this.getCommand("settings").setExecutor(new SettingsCommand());
        this.getCommand("skin").setExecutor(new SkinCommand());
        this.getCommand("store").setExecutor(new StoreCommand());
        this.getCommand("su").setExecutor(new SuCommand());
        this.getCommand("subscribe").setExecutor(new SubscribeCommand());
        this.getCommand("sudo").setExecutor(new SudoCommand());
        this.getCommand("togglechat").setExecutor(new ToggleChatCommand());
        this.getCommand("tpa").setExecutor(new TpaCommand());
        this.getCommand("tpr").setExecutor(new TprCommand());
        this.getCommand("trade").setExecutor(new TradeCommand());
        this.getCommand("trash").setExecutor(new TrashCommand());
        this.getCommand("unblock").setExecutor(new UnblockCommand());
        this.getCommand("unfriend").setExecutor(new UnfriendCommand());
        this.getCommand("unjail").setExecutor(new UnjailCommand());
        this.getCommand("unmute").setExecutor(new UnmuteCommand());
        this.getCommand("unsubscribe").setExecutor(new UnsubscribeCommand());
        this.getCommand("unwatch").setExecutor(new UnwatchCommand());
        this.getCommand("vote").setExecutor(new VoteCommand());
        this.getCommand("wallet").setExecutor(new WalletCommand());
        this.getCommand("warn").setExecutor(new WarnCommand());
        this.getCommand("watch").setExecutor(new WatchCommand());
        this.getCommand("web").setExecutor(new WebCommand());
        this.getCommand("wiki").setExecutor(new WikiCommand());
        this.getCommand("world").setExecutor(new WorldCommand());
        this.getCommand("worlds").setExecutor(new WorldsCommand());

        this.saveDefaultConfig();
        this.saveResource("lang/en-us.json", true);
        this.saveResource("lang/ja-jp.json", true);
        this.saveResource("tutorial/en-us.json", false);
        this.saveResource("tutorial/ja-jp.json", false);
        this.saveResource("service/checkout.yml", false);
        this.saveResource("structure/housing.nbt", true);
        this.saveResource("structure/jail.nbt", true);
        this.saveResource("vwm/vwm.json", false);
        this.saveResource("gomenne.json", false);

        SqlUtility.jdbc("org.mariadb.jdbc.Driver");

        Vanilife.DB_URL = this.getConfig().getString("database.url");
        Vanilife.DB_USER = this.getConfig().getString("database.user");
        Vanilife.DB_PASS = this.getConfig().getString("database.pass");

        SqlUtility.setup();

        Vanilife.METUBOT_SERVER = this.getConfig().getString("metubot.server");
        Vanilife.METUBOT_PROVIDER = UUID.fromString(this.getConfig().getString("metubot.provider"));

        Language.mount();
        Service.mount();

        this.getCommand("tutorial").setExecutor(new TutorialCommand());

        Vanilife.jda = JDABuilder.createDefault(this.getConfig().getString("discord.token")).setActivity(Activity.playing("azisaba.net")).build();
        Vanilife.jda.addEventListener(new DiscordListener());
        Vanilife.jda.addEventListener(new VoiceChatListener());
        Vanilife.jda.addEventListener(new MconsoleCommand());
        Vanilife.jda.addEventListener(new MetubotCommand());
        Vanilife.jda.addEventListener(new VanilifeLinkCommand());
        Vanilife.jda.addEventListener(new VanilifeUnlinkCommand());

        Vanilife.filter = new ChatFilter();

        VanilifeWorldManager.mount();

        new CacheClearRunnable().runTaskTimer(this, 0L, 20L * 3600);
        new ObjectiveRunnable().runTaskTimer(this, 0L, 10L);
        new HousingAfkRunnable().runTaskTimer(this, 0L, 5L);
        new NightCheckRunnable().runTaskTimer(this, 0L, 20L);
        new PlayerListRunnable().runTaskTimer(this, 0L, 8L);
        new PlayingRewardRunnable().runTask(this);
        new ReviewRunnable().runTaskTimer(this, 0L, 20L * 60 * 8);
        new TrustRunnable().runTaskTimer(this, 0L, 20L * 60 * 17);
        new VoiceChatRunnable().runTaskTimerAsynchronously(this, 0L, 20L * 2);

        Plugin coreprotectPlugin = Bukkit.getServer().getPluginManager().getPlugin("CoreProtect");

        if (! (coreprotectPlugin instanceof CoreProtect))
        {
            throw new RuntimeException("Failed to acquire CoreProtect.");
        }

        Vanilife.coreprotect = ((CoreProtect) coreprotectPlugin).getAPI();

        if (! Vanilife.coreprotect.isEnabled())
        {
            throw new RuntimeException("Failed to acquire CoreProtect.");
        }

        if (Vanilife.coreprotect.APIVersion() < 10)
        {
            throw new RuntimeException("Failed to acquire CoreProtect.");
        }

        ShapedRecipe elytraRecipe = new ShapedRecipe(new NamespacedKey(this, "elytra"), new ItemStack(Material.ELYTRA));
        elytraRecipe.shape("PNP", "PWP", "P P");
        elytraRecipe.setIngredient('P', Material.PHANTOM_MEMBRANE);
        elytraRecipe.setIngredient('N', Material.NETHER_STAR);
        elytraRecipe.setIngredient('W', Material.WIND_CHARGE);
        Bukkit.addRecipe(elytraRecipe);

        ShapedRecipe messageRecipe = new ShapedRecipe(new NamespacedKey(this, "gimmick.message"), MessageGimmick.getItemStack());
        messageRecipe.shape("RBR", "BNB", "RBR");
        messageRecipe.setIngredient('R', Material.REDSTONE);
        messageRecipe.setIngredient('N', Material.NETHER_STAR);
        messageRecipe.setIngredient('B', Material.WRITABLE_BOOK);
        Bukkit.addRecipe(messageRecipe);

        ShapedRecipe titleRecipe = new ShapedRecipe(new NamespacedKey(this, "gimmick.title"), TitleGimmick.getItemStack());
        titleRecipe.shape("RPR", "PNP", "RPR");
        titleRecipe.setIngredient('R', Material.REDSTONE);
        titleRecipe.setIngredient('N', Material.NETHER_STAR);
        titleRecipe.setIngredient('P', Material.PAINTING);
        Bukkit.addRecipe(titleRecipe);

        ShapedRecipe soundRecipe = new ShapedRecipe(new NamespacedKey(this, "gimmick.sound"), SoundGimmick.getItemStack());
        soundRecipe.shape("RSR", "SNS", "RSR");
        soundRecipe.setIngredient('R', Material.REDSTONE);
        soundRecipe.setIngredient('N', Material.NETHER_STAR);
        soundRecipe.setIngredient('S', Material.NOTE_BLOCK);
        Bukkit.addRecipe(soundRecipe);
    }

    @Override
    public void onDisable()
    {
        Vanilife.SERVER_PUBLIC.getAudioManager().closeAudioConnection();
        Vanilife.CHANNEL_CONSOLE.sendMessage(":file_folder: " + Vanilife.ROLE_DEVELOPER.getAsMention() + " サーバーを停止しました").queue();

        Bukkit.getOnlinePlayers().forEach(player -> player.kick(Component.text("サーバーを停止しています").color(NamedTextColor.RED)));
        VanilifeWorld.getInstances().forEach(w -> w.getWorlds().forEach(World::save));
        Housing.getWorld().save();

        if (Vanilife.jda != null)
        {
            try
            {
                Vanilife.jda.shutdownNow();
                Vanilife.jda = null;
            }
            catch (Exception ignored) {}
        }
    }
}
