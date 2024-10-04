package net.azisaba.vanilife.listener;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.gomenne.ConvertRequest;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.Skin;
import net.azisaba.vanilife.util.ComponentUtility;
import net.azisaba.vanilife.util.PlayerUtility;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.azisaba.vanilife.vwm.VanilifeWorldManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.*;

public class PlayerJoinListener implements Listener
{
    @EventHandler(priority = EventPriority.LOW)
    public void setName(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        User user = User.getInstance(player);
        Sara sara = UserUtility.calculateSara(player);

        if (user.getSara().level < sara.level)
        {
            user.setSara(sara);
        }

        player.displayName(user.getName());
        player.playerListName(user.getName());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void setLanguage(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        if (player.hasPlayedBefore())
        {
            return;
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                User user = User.getInstance(player);

                Language lang = switch (PlayerUtility.getCountry(player))
                {
                    case "JP" -> Language.getInstance("ja-jp");
                    default -> Language.getInstance("en-us");
                };

                user.getSettings().LANGUAGE.setLanguage(lang);
            }
        }.runTaskAsynchronously(Vanilife.getPlugin());
    }

    public static final Map<Player, ProfileProperty> texturesMap = new HashMap<>();

    @EventHandler
    public void setSkin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        PlayerProfile profile = player.getPlayerProfile();
        ProfileProperty textures = profile.getProperties().stream().filter(property -> property.getName().equals("textures")).toList().getFirst();
        PlayerJoinListener.texturesMap.put(player, new ProfileProperty(textures.getName(), textures.getValue(), textures.getSignature()));

        User user = User.getInstance(player);
        Skin skin = user.getSkin();

        if (skin == null)
        {
            String name = player.getName();

            if (8 < name.length())
            {
                name = name.substring(0, 8);
            }

            skin = new Skin(name, user, textures.getValue(), textures.getSignature());
        }

        skin.use(player);
    }

    @EventHandler
    public void setOnlinePlayers(PlayerJoinEvent event)
    {
        Vanilife.jda.getPresence().setActivity(Activity.customStatus(Bukkit.getOnlinePlayers().size() + " 人がばにらいふ！ をプレイ中！"));
    }

    @EventHandler
    public void giveLoginBonus(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        User user = User.getInstance(player);

        Calendar now = Calendar.getInstance();
        Calendar last = Calendar.getInstance();

        if (user.getLastLogin() != null)
        {
            last.setTime(user.getLastLogin());
        }

        if (now == last || ! (now.get(Calendar.YEAR) == last.get(Calendar.YEAR) && now.get(Calendar.MONTH) == last.get(Calendar.MONTH) && now.get(Calendar.DAY_OF_MONTH) == last.get(Calendar.DAY_OF_MONTH)))
        {
            user.setLoginStreak((now.get(Calendar.DAY_OF_MONTH) == last.get(Calendar.DAY_OF_MONTH) + 1) ? user.getLoginStreak() + 1 : 0);

            int streak = user.getLoginStreak() + 1;
            int bonus = 10 * Math.min(streak, 10);

            user.setMola(user.getMola() + bonus);

            player.sendMessage(Language.translate("msg.login-bonus", player, "mola=" + bonus, "streak=" + streak));
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
        }

        user.setLastLogin(new Date());
    }

    @EventHandler
    public void giveStarterKit(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        if (player.hasPlayedBefore())
        {
            return;
        }

        ItemStack bread = new ItemStack(Material.BREAD);
        bread.setAmount(16);
        PlayerUtility.giveItemStack(player, bread);
    }

    @EventHandler
    public void sendJoinMessage(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        User user = User.getInstance(player);

        event.joinMessage(null);
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Language.translate(player.hasPlayedBefore() ? (user.getSara().isRank() ? "msg.ranker-join" : "msg.join") : "msg.first-join", p, "name=" + ComponentUtility.getAsString(user.getName()))));

        int unread = user.getMails().stream().filter(m -> ! m.isRead()).toList().size();

        if (0 < unread)
        {
            player.sendMessage(Language.translate("msg.new-mail", player, "unread=" + unread));
        }
    }

    @EventHandler
    public void sendJoinHistory(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        Vanilife.CHANNEL_HISTORY.sendMessageEmbeds(new EmbedBuilder()
                .setAuthor(player.getName() + " (" + player.getUniqueId() + ")", null, String.format("https://api.mineatar.io/face/%s", player.getUniqueId().toString().replace("-", "")))
                .setDescription(player.getName() + " が参加しました")
                .setColor(Color.GREEN).build()).queue();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void sendUpdate(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        User user = User.getInstance(player);

        final String version = Vanilife.getPluginVersion();

        if (user.getStorage().has("version"))
        {
            String userVersion = user.read("version").getAsString();

            if (! userVersion.equals(version))
            {
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);

                player.sendMessage(Component.text().build());
                player.sendMessage(Component.text(version.toUpperCase() + " RELEASED!").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD));
                player.sendMessage(Component.text().build());
            }
        }

        user.write("version", version);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void sendImeRequests(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        if (! UserUtility.isModerator(player))
        {
            return;
        }

        int unconfirmed = ConvertRequest.getInstances().size();

        if (unconfirmed < 1)
        {
            return;
        }

        player.sendMessage(Component.text("通知: 未処理の変換リクエストが " + unconfirmed + " 件届いています！").color(NamedTextColor.GREEN)
                .append(Component.text("こちらをクリックして確認").color(NamedTextColor.GOLD).hoverEvent(HoverEvent.showText(Component.text("クリックして /gomenne requests を実行"))).clickEvent(ClickEvent.runCommand("/vanilife:gomenne requests"))));
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void checkVwm(PlayerLoginEvent event)
    {
        if (Vanilife.publisher)
        {
            return;
        }

        event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        event.kickMessage(Component.text("Server does not allow connections…").color(NamedTextColor.RED));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void loadInstance(PlayerJoinEvent event)
    {
        User.getInstance(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void teleportPlayer(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        VanilifeWorld world = VanilifeWorld.getInstance(VanilifeWorldManager.getLatestVersion());

        if (world == null)
        {
            return;
        }

        if (player.hasPlayedBefore() && (VanilifeWorld.getInstance(player.getWorld()) != null || User.getInstance(player).inHousing()))
        {
            return;
        }

        world.getTeleporter().teleport(player);
    }
}
