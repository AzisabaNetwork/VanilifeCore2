package net.azisaba.vanilife.runnable;

import net.azisaba.vanilife.util.Watch;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.scheduler.BukkitRunnable;

public class WatcherRunnable extends BukkitRunnable
{
    @Override
    public void run()
    {
        Watch.getWatchers().forEach(watcher -> {
            watcher.sendActionBar(Component.text("Watch モードが有効です！").color(NamedTextColor.RED).decorate(TextDecoration.BOLD));
        });
    }
}
