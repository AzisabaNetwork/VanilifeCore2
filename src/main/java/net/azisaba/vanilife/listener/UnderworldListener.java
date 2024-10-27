package net.azisaba.vanilife.listener;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.entity.DragonEntity;
import net.azisaba.vanilife.vwm.VanilifeWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UnderworldListener implements Listener
{
    @EventHandler
    public void onEntitySpawn(EntityAddToWorldEvent event)
    {
        Location location = event.getEntity().getLocation();

        if (event.getEntityType() == EntityType.ENDER_DRAGON)
        {
            return;
        }

        if (! location.getWorld().equals(VanilifeWorldManager.getUnderworld()))
        {
            return;
        }

        if (location.getWorld().getEntities().stream()
                .anyMatch(entity -> entity.getType() == EntityType.ENDER_DRAGON && entity.getLocation().distance(location) < 500))
        {
            return;
        }

        Bukkit.getScheduler().runTaskLater(Vanilife.getPlugin(), () -> new DragonEntity(location.add(0, 56, 0)), 20L * 4);
    }
}
