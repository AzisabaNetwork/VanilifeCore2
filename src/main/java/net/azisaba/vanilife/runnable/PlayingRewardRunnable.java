package net.azisaba.vanilife.runnable;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayingRewardRunnable extends BukkitRunnable
{
    private static final long minLaterTicks = 20L * 60 * 10;
    private static final long maxLaterTicks = 20L * 60 * 20;

    private static long getLaterTicks()
    {
        return PlayingRewardRunnable.minLaterTicks + (long) (Vanilife.random.nextDouble() * (PlayingRewardRunnable.maxLaterTicks - PlayingRewardRunnable.minLaterTicks));
    }

    @Override
    public void run()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            User user = User.getInstance(player);
            user.setMola(user.getMola() + 5, "Playing");
        }

        new PlayingRewardRunnable().runTaskLater(Vanilife.getPlugin(), PlayingRewardRunnable.getLaterTicks());
    }

    public void runTaskLater(Plugin plugin)
    {
        super.runTaskLater(plugin, PlayingRewardRunnable.getLaterTicks());
    }
}
