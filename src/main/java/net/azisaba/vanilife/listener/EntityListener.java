package net.azisaba.vanilife.listener;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;

public class EntityListener implements Listener
{
    private final ArrayList<Entity> monsters = new ArrayList<>();

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();

        if (damager instanceof Player p && entity instanceof Player)
        {
            Plot plot = Plot.getInstance(damager.getChunk());

            if (plot != null)
            {
                plot.onEntityDamageByEntity(event);
            }
            else if (! UserUtility.isModerator(p))
            {
                event.setCancelled(true);
            }

            return;
        }

        if (! (entity instanceof Monster monster))
        {
            return;
        }

        if (! (event.getDamager() instanceof Player))
        {
            return;
        }

        this.monsters.add(entity);

        double ratio = monster.getHealth() / monster.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        NamedTextColor color;

        if (0.7 <= ratio)
        {
            color = NamedTextColor.GREEN;
        }
        else if (0.3 <= ratio)
        {
            color = NamedTextColor.YELLOW;
        }
        else
        {
            color = NamedTextColor.RED;
        }

        entity.customName(Component.text("HP ").color(NamedTextColor.GRAY)
                .append(Component.text((int) monster.getHealth()).color(color))
                .append(Component.text("/").color(NamedTextColor.WHITE))
                .append(Component.text((int) monster.getMaxHealth()).color(NamedTextColor.GREEN))
                .append(Component.text("❤").color(NamedTextColor.RED)));

        entity.setCustomNameVisible(true);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (Collections.frequency(monsters, entity) == 1)
                {
                    entity.setCustomNameVisible(false);
                    entity.customName(null);
                }

                monsters.remove(entity);
            }
        }.runTaskLater(Vanilife.getPlugin(), 20L * 2);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        LivingEntity entity = event.getEntity();

        if (! (entity instanceof Monster))
        {
            return;
        }

        entity.setCustomNameVisible(false);
        entity.customName(null);

        Player player = entity.getKiller();

        if (player != null && Vanilife.random.nextDouble() < 0.05)
        {
            User user = User.getInstance(player);
            user.setMola(user.getMola() + 3, "reward.category.kill", NamedTextColor.LIGHT_PURPLE);
        }
    }
}
