package net.azisaba.vanilife.listener;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityListener implements Listener
{
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();

        if (! (damager instanceof Player player) || ! (entity instanceof Player))
        {
            return;
        }

        Plot plot = Plot.getInstance(damager.getChunk());

        if (plot != null)
        {
            plot.onEntityDamageByEntity(event);
        }
        else if (! UserUtility.isModerator(player))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityAddToWorld(EntityAddToWorldEvent event)
    {

    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event)
    {
        Plot plot = Plot.getInstance(event.getLocation().getChunk());
        event.setCancelled(plot != null || event.isCancelled());
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        LivingEntity entity = event.getEntity();

        if (! (entity instanceof Monster))
        {
            return;
        }

        Player player = entity.getKiller();

        if (player != null && Vanilife.random.nextDouble() < 0.05)
        {
            User user = User.getInstance(player);
            user.setMola(user.getMola() + 3, "reward.category.kill", NamedTextColor.LIGHT_PURPLE);
        }
    }
}
