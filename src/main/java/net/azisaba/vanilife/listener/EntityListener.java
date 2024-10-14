package net.azisaba.vanilife.listener;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.UserStatus;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.azisaba.vanilife.vwm.VanilifeWorldManager;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

public class EntityListener implements Listener
{
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if (! (event.getEntity() instanceof Player player))
        {
            return;
        }

        User user = User.getInstance(player);
        event.setCancelled((user.getStatus() == UserStatus.JAILED && player.getWorld().equals(VanilifeWorldManager.getJail())) || event.isCancelled());
    }

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

    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent event)
    {
        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null || event.getFrom().getWorld().equals(event.getTo().getWorld()))
        {
            return;
        }

        if (from.getWorld().getEnvironment() != World.Environment.THE_END || to.getWorld().getEnvironment() != World.Environment.NORMAL)
        {
            return;
        }

        VanilifeWorld world = VanilifeWorld.getInstance(from.getWorld());

        if (world == null)
        {
            return;
        }

        event.setTo(world.getOverworld().getSpawnLocation());
    }
}
