package net.azisaba.vanilife.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.chat.IChat;
import net.azisaba.vanilife.housing.Housing;
import net.azisaba.vanilife.objective.Objectives;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.report.ReportDataContainer;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.ui.ReportAttachUI;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.UserStatus;
import net.azisaba.vanilife.util.ComponentUtility;
import net.azisaba.vanilife.util.Typing;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.util.Watch;
import net.azisaba.vanilife.vc.VoiceChat;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.azisaba.vanilife.vwm.VanilifeWorldManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener
{
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        User user = User.getInstance(player);

        user.getRequests().clear();

        VoiceChat vc = VoiceChat.getInstance(user);

        if (vc != null)
        {
            vc.disconnect(user);

            Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
                Vanilife.SERVER_PUBLIC.kickVoiceMember(Vanilife.SERVER_PUBLIC.retrieveMemberById(user.getDiscord().getId()).complete()).queue();
            });
        }

        ReportDataContainer report = ReportDataContainer.getInstance(user);

        if (report != null)
        {
            report.cancel();
        }

        int online = Bukkit.getOnlinePlayers().stream().filter(p -> ! Watch.isWatcher(p)).toList().size() - 1;
        Vanilife.jda.getPresence().setActivity(Activity.customStatus(0 < online ? online + " 人がばにらいふ！ をプレイ中！" : "azisaba.net をプレイ中！"));

        event.quitMessage(null);

        if (UserStatus.MUTED.level() <= user.getStatus().level() || Watch.isWatcher(player))
        {
            return;
        }

        Bukkit.getOnlinePlayers().stream()
                .filter(p -> ! User.getInstance(p).isBlock(user))
                .forEach(p -> p.sendMessage(Component.text("- ").color(NamedTextColor.RED).append(user.getName(p)).appendSpace().append(Language.translate("msg.quit", p).color(NamedTextColor.GRAY))));

        Vanilife.CHANNEL_HISTORY.sendMessageEmbeds(new EmbedBuilder()
                .setAuthor(player.getName() + " (" + player.getUniqueId() + ")", null, String.format("https://api.mineatar.io/face/%s", player.getUniqueId().toString().replace("-", "")))
                .setDescription(player.getName() + " が切断しました")
                .setColor(Color.RED).build()).queue();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        Plot from = Plot.getInstance(event.getFrom().getChunk());
        Plot to = Plot.getInstance(event.getTo().getChunk());

        if (to != null && from != to)
        {
            Component info = Component.text("Plot: ").color(NamedTextColor.GREEN)
                    .append(Sara.$2000YEN.level < to.getOwner().getSara().level && to.getName().contains("&") ? ComponentUtility.asComponent(to.getName()) : Component.text(to.getName()).color(NamedTextColor.GRAY));

            if (to.canPvP() && to.isMember(player ))
            {
                info = info.append(Component.text(" - ").color(NamedTextColor.DARK_GRAY)).append(Component.text("PvP").color(NamedTextColor.RED));
            }

            player.sendActionBar(info);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        Plot plot = Plot.getInstance(player.getChunk());
        ReportDataContainer report = ReportDataContainer.getInstance(player);

        if (plot != null && report == null)
        {
            plot.onPlayerInteract(event);
            return;
        }

        if (report == null)
        {
            return;
        }

        event.setCancelled(true);

        Block block = event.getClickedBlock();

        if (block == null)
        {
            return;
        }

        new ReportAttachUI(player, report, block.getLocation());
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
        Bukkit.getPluginManager().callEvent(new EntityPortalEvent(event.getPlayer(), event.getFrom(), event.getTo(), event.getSearchRadius(), event.getCanCreatePortal(), event.getCreationRadius(), switch (event.getCause())
        {
            case PlayerTeleportEvent.TeleportCause.NETHER_PORTAL -> PortalType.NETHER;
            case PlayerTeleportEvent.TeleportCause.END_PORTAL -> PortalType.ENDER;
            case PlayerTeleportEvent.TeleportCause.END_GATEWAY -> PortalType.END_GATEWAY;
            default -> PortalType.CUSTOM;
        }));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityPortal(EntityPortalEvent event)
    {
        VanilifeWorld world = VanilifeWorld.getInstance(event.getFrom().getWorld());

        if (world == null)
        {
            return;
        }

        Location to = event.getTo();

        if (to == null)
        {
            return;
        }

        World level = switch (to.getWorld().getEnvironment())
        {
            case World.Environment.NETHER -> world.getNether();
            case World.Environment.THE_END -> world.getEnd();
            default -> world.getOverworld();
        };

        to.setWorld(level);
        event.setTo(to);
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event)
    {
        Player player = event.getPlayer();

        if (! UserUtility.isAdmin(player))
        {
            event.setCancelled(true);
        }

        if (Watch.isWatcher(player) && event.getNewGameMode() != GameMode.SPECTATOR)
        {
            player.sendMessage(Component.text("Watch モードではスペクテイターモードのみが利用可能です").color(NamedTextColor.RED));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event)
    {
        Player player = event.getPlayer();

        if (User.getInstance(player).isJailed())
        {
            player.teleport(VanilifeWorldManager.getJail().getSpawnLocation());
            return;
        }

        if (! Housing.getWorld().equals(player))
        {
            Housing.getInstances().stream()
                    .filter(housing -> housing.isGuest(player))
                    .forEach(housing -> housing.removeGuest(player));
        }

        if (! (event.getFrom().getEnvironment() == World.Environment.THE_END && event.getPlayer().getWorld().getEnvironment() == World.Environment.NORMAL))
        {
            return;
        }

        VanilifeWorld world = VanilifeWorld.getInstance(event.getFrom());

        if (world == null)
        {
            return;
        }

        if (player.getWorld().equals(Housing.getWorld()))
        {
            return;
        }

        if (player.getWorld().equals(VanilifeWorldManager.getUnderworld()))
        {
            return;
        }

        Location respawn = player.getRespawnLocation();

        if (respawn != null)
        {
            player.teleport(respawn);
            return;
        }

        world.getTeleporter().teleport(player);
    }

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event)
    {
        if (event.message() == null)
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

        if (Vanilife.random.nextDouble() < 0.5)
        {
            user.setTrust(user.getTrust() + 1);
        }

        int bonus = Vanilife.random.nextInt(difficulty) + 1;

        user.setMola(user.getMola() + bonus, "reward.category.story", NamedTextColor.GOLD);
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        Player player = event.getPlayer();

        Location respawn = event.getRespawnLocation();

        if (player.getRespawnLocation() != null
                && VanilifeWorld.getInstance(player.getRespawnLocation().getWorld()) != null
                && VanilifeWorld.getInstance(respawn.getWorld()) != null)
        {
            return;
        }

        VanilifeWorld world = VanilifeWorld.getInstance(player.getLocation().getWorld());

        if (world == null)
        {
            world = VanilifeWorld.getInstance(VanilifeWorldManager.getLatestVersion());
        }

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
        String command = event.getMessage().toLowerCase().split(" ")[0];

        if (! command.contains(":"))
        {
            command = "/minecraft:" + command.substring(1);
        }

        if (command.equalsIgnoreCase("/minecraft:tell") || command.equalsIgnoreCase("/minecraft:msg") || command.equalsIgnoreCase("/minecraft:w"))
        {
            player.sendMessage(Language.translate("mail.dont-tell", player).color(NamedTextColor.RED));
            event.setCancelled(true);
        }

        if (command.equalsIgnoreCase("/minecraft:help"))
        {
            if (player.getGameMode() != GameMode.SURVIVAL && UserUtility.isAdmin(player))
            {
                return;
            }

            event.setMessage("/vanilife:wiki");
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event)
    {
        if (! (event.getEntity() instanceof Player player))
        {
            return;
        }

        event.setCancelled((User.getInstance(player).getStatus() == UserStatus.JAILED && player.getWorld().equals(VanilifeWorldManager.getJail())) || event.isCancelled());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncChat(AsyncChatEvent event)
    {
        event.setCancelled(true);

        Player player = event.getPlayer();

        User user = User.getInstance(player);

        if (UserStatus.MUTED.level() <= user.getStatus().level())
        {
            player.sendMessage(Language.translate("msg.muted", player).color(NamedTextColor.RED));
            return;
        }

        if (! user.read("settings.chat").getAsBoolean())
        {
            player.sendMessage(Language.translate("cmd.togglechat.cant-send", player).color(NamedTextColor.RED));
            return;
        }

        String message = ((TextComponent) event.message()).content();

        Typing typing = Typing.getInstance(player);

        if (typing != null)
        {
            player.sendMessage(Component.space().append(Component.text(message).color(NamedTextColor.GRAY)));
            typing.onTyped(message);
            return;
        }

        if (! user.isAchieved(Objectives.SEND_MESSAGE))
        {
            user.achieve(Objectives.SEND_MESSAGE);
        }

        IChat chat = user.getChat();

        if (Watch.isWatcher(player) && chat == null)
        {
            player.sendMessage(Component.text("Watch モードでは全体チャットは利用できません").color(NamedTextColor.RED));
            return;
        }

        if (chat != null)
        {
            chat.send(user, message);
            return;
        }
        
        List<Player> listeners = new ArrayList<>();

        if (! UserUtility.isAdmin(user))
        {
            listeners.addAll(Bukkit.getOnlinePlayers().stream().filter(p -> ! User.getInstance(p).isBlock(user) && User.getInstance(p).read("settings.chat").getAsBoolean()).toList());
        }
        else
        {
            listeners.addAll(Bukkit.getOnlinePlayers());
        }

        if (user.getTrust() < 15 && Vanilife.random.nextDouble() < 0.3)
        {
            user.setTrust(user.getTrust() + 1);
        }
        else if (Vanilife.random.nextDouble() < 0.05)
        {
            user.setTrust(user.getTrust() + 1);
        }

        Component body = ComponentUtility.asChat(player, message);
        Vanilife.filter.onChat(player, message);

        ComponentUtility.getMentions(message).stream()
                .filter(mention -> ! mention.isBlock(user) && mention.isOnline() && mention.read("settings.chat").getAsBoolean())
                .forEach(mention -> {
                    mention.asPlayer().playSound(mention.asPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
                    mention.asPlayer().sendActionBar(Language.translate("msg.mentioned", player, "name=" + ComponentUtility.asString(user.getName())));
                });

        listeners.forEach(listener -> listener.sendMessage(Component.text().build()
                .append(Component.text("[" + user.getTrustRank().getName().charAt(0) + "] ").color(user.getTrustRank().getColor()).hoverEvent(Component.text(user.getTrustRank().getName()).color(user.getTrustRank().getColor())))
                .append(user.getName(listener))
                .append(Component.text(": ").color(NamedTextColor.GRAY))
                .append(body)));
    }
}
