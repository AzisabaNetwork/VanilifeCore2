package net.azisaba.vanilife.nkip;

import net.azisaba.vanilife.vwm.VanilifeWorld;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class NightCheckRunnable extends BukkitRunnable
{
    private long cache = -1;

    @Override
    public void run()
    {
        VanilifeWorld.getInstances().forEach(vw -> {
            World world = vw.getOverworld();
            long time = world.getTime();

            if (time >= 13000 && this.cache < 13000)
            {
                if (NkipVote.getInstance(world) == null)
                {
                    new NkipVote(world);
                }
            }

            this.cache = time;
        });
    }
}
