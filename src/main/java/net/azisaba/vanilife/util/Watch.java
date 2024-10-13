package net.azisaba.vanilife.util;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Watch
{
    private static final List<UUID> watchers = new ArrayList<>();

    public static @NotNull List<Player> getWatchers()
    {
        return Watch.watchers.stream()
                .map(Bukkit::getPlayer)
                .collect(Collectors.toList());
    }

    public static void addWatcher(@NotNull Player player)
    {
        if (! UserUtility.isModerator(player))
        {
            return;
        }

        Bukkit.getPluginManager().callEvent(new PlayerQuitEvent(player, Component.text().build(), PlayerQuitEvent.QuitReason.DISCONNECTED));
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> ! Watch.isWatcher(p))
                .forEach(p -> p.hidePlayer(Vanilife.getPlugin(), player));

        Watch.watchers.add(player.getUniqueId());

        player.setGameMode(GameMode.SPECTATOR);
    }

    public static void removeWatcher(@NotNull Player player)
    {
        if (! Watch.isWatcher(player))
        {
            return;
        }

        Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(player, Component.text().build()));
        Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(Vanilife.getPlugin(), player));

        Watch.watchers.remove(player.getUniqueId());

        player.setGameMode(GameMode.SURVIVAL);
    }

    public static boolean isWatcher(@NotNull User user)
    {
        return Watch.watchers.contains(user.getId());
    }

    public static boolean isWatcher(@NotNull Player player)
    {
        return Watch.isWatcher(User.getInstance(player));
    }
}
