package net.azisaba.vanilife.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.subscription.Subscriptions;
import net.azisaba.vanilife.gomenne.Gomenne;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.report.ReportDataContainer;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.ui.ReportAttachUI;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.UserStatus;
import net.azisaba.vanilife.util.ComponentUtility;
import net.azisaba.vanilife.util.Typing;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.vc.VoiceChat;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.azisaba.vanilife.vwm.VanilifeWorldManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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

        int online = Bukkit.getOnlinePlayers().size() - 1;
        Vanilife.jda.getPresence().setActivity(Activity.customStatus(0 < online ? online + " 人がばにらいふ！ をプレイ中！" : "azisaba.net をプレイ中！"));

        event.quitMessage(null);
        Bukkit.getOnlinePlayers().stream().filter(p -> ! User.getInstance(p).isBlock(user)).forEach(p -> p.sendMessage(Language.translate("msg.quit", p, "name=" + ComponentUtility.getAsString(user.getName()))));

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
                    .append(Sara.$2000YEN.level < to.getOwner().getSara().level && to.getName().contains("&") ? ComponentUtility.getAsComponent(to.getName()) : Component.text(to.getName()).color(NamedTextColor.GRAY));

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

        Player player = event.getPlayer();
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

        if (! user.read("settings.chat").getAsBoolean())
        {
            player.sendMessage(Language.translate("cmd.togglechat.cant-send", player).color(NamedTextColor.RED));
            return;
        }

        String message = ((TextComponent) event.message()).content();

        Typing typing = Typing.getInstance(player);

        if (typing != null)
        {
            player.sendMessage(Component.text(" " + message).color(NamedTextColor.GRAY));
            typing.onTyped(message);
            return;
        }

        boolean gomenne = false;

        if (Language.getInstance(user).getId().equals("ja-jp")
                && message.matches("[a-zA-Z0-9\\p{Punct}]*")
                && ! message.contains(":") && user.read("settings.ime").getAsBoolean()
                && ! ((message.contains("!1") || message.contains("!2") || message.contains("!3") || message.contains("!4")) && user.getSettings().METUBOU.isValid()))
        {
            message = Gomenne.convert(Gomenne.hira(message)) + " §8(" + message + "§r§8)";
            gomenne = true;
        }

        Vanilife.filter.onChat(player, ! gomenne ? message : Gomenne.convert(Gomenne.hira(((TextComponent) event.message()).content())) + " (" + ((TextComponent) event.message()).content() + ")");

        if (user.hasSubscription(Subscriptions.NEON))
        {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }

        message = LegacyComponentSerializer.legacySection().serialize(ComponentUtility.parseChat(message, user));

        Component chat = Component.text().build().append(user.getName()).append(Component.text(": ").color(NamedTextColor.GRAY)).append(ComponentUtility.parseUrl(LegacyComponentSerializer.legacySection().deserialize(message)));

        List<Player> players = new ArrayList<>();

        if (! UserUtility.isModerator(user))
        {
            players.addAll(Bukkit.getOnlinePlayers().stream().filter(p -> !User.getInstance(p).isBlock(user) && User.getInstance(p).read("settings.chat").getAsBoolean()).toList());
        }
        else
        {
            players.addAll(Bukkit.getOnlinePlayers());
        }

        players.forEach(p -> p.sendMessage(chat));
    }
}
