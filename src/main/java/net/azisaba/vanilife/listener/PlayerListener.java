package net.azisaba.vanilife.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.UserStatus;
import net.azisaba.vanilife.user.subscription.Subscriptions;
import net.azisaba.vanilife.util.ComponentUtility;
import net.azisaba.vanilife.util.Typing;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.vc.VoiceChat;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.azisaba.vanilife.vwm.VanilifeWorldManager;
import net.dv8tion.jda.api.entities.Activity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PlayerListener implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                User user = User.getInstance(player);
                Sara sara = UserUtility.calculateSara(player);

                if (user.getSara().level < sara.level)
                {
                    user.setSara(sara);
                }

                player.displayName(user.getName());
                player.playerListName(user.getName());
                event.joinMessage(null);

                VanilifeWorld latestWorld = VanilifeWorld.getInstance(VanilifeWorldManager.getLatestVersion());

                if (latestWorld != null && (! player.hasPlayedBefore() || VanilifeWorld.getInstance(player.getWorld()) == null))
                {
                    latestWorld.getTeleporter().teleport(player);
                    ItemStack bread = new ItemStack(Material.BREAD);
                    bread.setAmount(16);
                    player.getInventory().addItem(bread);
                }

                if (! player.hasPlayedBefore())
                {
                    Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Language.translate("msg.first-join", p, "name=" + ComponentUtility.getAsString(user.getName()))));
                }
                else
                {
                    Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Language.translate("msg.join", p, "name=" + ComponentUtility.getAsString(user.getName()))));
                }

                int unread = user.getMails().stream().filter(m -> ! m.isRead() && m.getTo() == user).toList().size();

                if (0 < unread)
                {
                    player.sendMessage(Language.translate("msg.new-mail", player, "unread=" + unread).color(NamedTextColor.GREEN).clickEvent(ClickEvent.runCommand("/mail")).hoverEvent(HoverEvent.showText(Component.text("クリックして /mail を実行"))));
                }

                Calendar now = Calendar.getInstance();
                Calendar lastLogin = Calendar.getInstance();

                if (user.getLastLogin() == null)
                {
                    lastLogin = now;
                }
                else
                {
                    lastLogin.setTime(user.getLastLogin());
                }

                if (now == lastLogin || ! (now.get(Calendar.YEAR) == lastLogin.get(Calendar.YEAR) && now.get(Calendar.MONTH) == lastLogin.get(Calendar.MONTH) && now.get(Calendar.DAY_OF_MONTH) == lastLogin.get(Calendar.DAY_OF_MONTH)))
                {
                    user.setLoginStreak((now.get(Calendar.DAY_OF_MONTH) == lastLogin.get(Calendar.DAY_OF_MONTH) + 1) ? user.getLoginStreak() + 1 : 0);

                    int streak = user.getLoginStreak() + 1;
                    int bonus = 10 * Math.min(streak, 10);

                    user.setMola(user.getMola() + bonus);
                    player.sendMessage(Language.translate("msg.login-bonus", player, "mola=" + bonus, "streak=" + streak));
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
                }

                user.setLastLogin(new Date());

                Vanilife.jda.getPresence().setActivity(Activity.customStatus(Bukkit.getOnlinePlayers().size() + " 人がばにらいふ！ をプレイ中！"));
            }
        }.runTaskAsynchronously(Vanilife.getPlugin());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        User user = User.getInstance(event.getPlayer());
        user.getRequests().clear();

        VoiceChat vc = VoiceChat.getInstance(user);

        if (vc != null)
        {
            vc.disconnect(user);

            Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
                Vanilife.publicServer.kickVoiceMember(Vanilife.publicServer.retrieveMemberById(user.getDiscord().getId()).complete()).queue();
            });
        }

        int online = Bukkit.getOnlinePlayers().size() - 1;
        Vanilife.jda.getPresence().setActivity(Activity.customStatus(0 < online ? online + " 人がばにらいふ！ をプレイ中！" : "azisaba.net をプレイ中！"));

        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Language.translate("msg.quit", p, "name=" + ComponentUtility.getAsString(user.getName()))));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        Plot from = Plot.getInstance(event.getFrom().getChunk());
        Plot to = Plot.getInstance(event.getTo().getChunk());

        if (to != null && from != to)
        {
            player.sendActionBar(Component.text("Plot: ").color(NamedTextColor.GREEN).append(Component.text(to.getName()).color(NamedTextColor.GRAY)));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Plot plot = Plot.getInstance(event.getPlayer().getChunk());

        if (plot != null)
        {
            plot.onPlayerInteract(event);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
    {
        Player player = event.getPlayer();

        if (player.isSneaking() && event.getRightClicked() instanceof Player clicked)
        {
            Bukkit.dispatchCommand(player, String.format("profile %s", clicked.getName()));
        }
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event)
    {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH)
        {
            return;
        }

        User user = User.getInstance(event.getPlayer());
        user.setMola(user.getMola() + 6, "reward.category.fishing", NamedTextColor.AQUA);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerPortal(PlayerPortalEvent event)
    {
        VanilifeWorld world = VanilifeWorld.getInstance(event.getFrom().getWorld());

        if (world == null)
        {
            return;
        }
        
        World level = switch (event.getTo().getWorld().getEnvironment())
        {
            case NETHER -> world.getNether();
            case THE_END -> world.getEnd();
            default -> world.getOverworld();
        };

        Location to = event.getTo();
        to.setWorld(level);
        event.setTo(to);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event)
    {
        if (! (event.getFrom().getEnvironment() == World.Environment.THE_END && event.getPlayer().getWorld().getEnvironment() == World.Environment.NORMAL))
        {
            return;
        }

        VanilifeWorld world = VanilifeWorld.getInstance(event.getFrom());

        if (world == null)
        {
            return;
        }

        world.getTeleporter().teleport(event.getPlayer());
    }

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event)
    {
        Advancement advancement = event.getAdvancement();

        if (! (advancement.getKey().getKey().startsWith("story/")))
        {
            return;
        }

        int difficulty = 1;

        Advancement parent = event.getAdvancement().getParent();

        while (parent != null)
        {
            parent = parent.getParent();
            difficulty ++;
        }

        Player player = event.getPlayer();
        User user = User.getInstance(player);
        int bonus = Vanilife.random.nextInt(difficulty) + 1;

        user.setMola(user.getMola() + bonus, "reward.category.story", NamedTextColor.GOLD);
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        Player player = event.getPlayer();

        if (event.getPlayer().getRespawnLocation() != null)
        {
            return;
        }

        VanilifeWorld world = VanilifeWorld.getInstance(player.getLocation().getWorld());

        if (world == null)
        {
            return;
        }

        event.setRespawnLocation(world.getOverworld().getSpawnLocation());
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase().split(" ")[0];

        if (message.equals("/tell") || message.equals("/msg") || message.equals("/w"))
        {
            player.sendMessage(Language.translate("mail.dont-tell", player).color(NamedTextColor.RED));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncChat(AsyncChatEvent event)
    {
        event.setCancelled(true);

        Player player = event.getPlayer();
        User user = User.getInstance(player);

        if (user.getStatus() != UserStatus.DEFAULT)
        {
            player.sendMessage(Language.translate("msg.muted", player).color(NamedTextColor.RED));
            return;
        }

        String content = ((TextComponent) event.message()).content();

        if (user.hasSubscription(Subscriptions.NEON))
        {
            content = ChatColor.translateAlternateColorCodes('&', content);
        }

        Typing typing = Typing.getInstance(player);

        if (typing != null)
        {
            player.sendMessage(Component.text(" " + content).color(NamedTextColor.GRAY));
            typing.onTyped(content);
            return;
        }

        Vanilife.filter.onAsyncChat(event);

        Component msg = Component.text("").append(user.getName()).append(Component.text(": ").color(NamedTextColor.GRAY)).append(Component.text(user.getSettings().METUBOU.isValid() ? "(*'▽') " : "").color(NamedTextColor.WHITE).append(Component.text(content)).color(NamedTextColor.WHITE));
        Bukkit.getOnlinePlayers().stream().filter(p -> ! User.getInstance(p).isBlock(user)).toList().forEach(p -> p.sendMessage(msg));
    }
}
