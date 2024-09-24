package net.azisaba.vanilife.runnable;

import net.azisaba.vanilife.util.MojangAPI;
import org.bukkit.scheduler.BukkitRunnable;

public class CacheClearRunnable extends BukkitRunnable
{
    @Override
    public void run()
    {
        MojangAPI.CACHE_ID.clear();
        MojangAPI.CACHE_PROFILE.clear();
    }
}
