package net.azisaba.vanilife.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Afk implements Listener
{
    private static final Map<Player, Long> record = new HashMap<>();

    private static final long AFK_TIME_LIMIT = 1000 * 60 * 6;

    public static boolean isAfk(@NotNull Player player)
    {
        long now = System.currentTimeMillis();
        long last = Afk.record.getOrDefault(player, now);

        return now - last > Afk.AFK_TIME_LIMIT;
    }

    private static void onActivity(@NotNull Player player)
    {
        Afk.record.put(player, System.currentTimeMillis());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        Block below = player.getLocation().getBlock();

        if (below.isLiquid())
        {
            return;
        }

        Afk.onActivity(player);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Afk.onActivity(event.getPlayer());
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        Afk.onActivity(event.getPlayer());
    }
}
