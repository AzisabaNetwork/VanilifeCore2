package net.azisaba.vanilife.arcade;

import net.azisaba.vanilife.Vanilife;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Paint implements Listener
{
    private final List<Location> particles = new ArrayList<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        Location location = player.getLocation().add(player.getLocation().getDirection().multiply(5)).add(0, 1, 0);

        if (event.getAction().isLeftClick())
        {
            new ArrayList<>(this.particles).stream().filter(particle -> particle.distance(player.getLocation()) < 1.3).forEach(this.particles::remove);
            new ArrayList<>(this.particles).stream().filter(particle -> particle.distance(location) < 1.5).forEach(this.particles::remove);
            return;
        }

        if (event.getClickedBlock() != null)
        {
            return;
        }

        List<Brush> brush = Arrays.stream(Brush.values()).filter(b -> b.material() == event.getMaterial()).toList();

        if (brush.isEmpty())
        {
            return;
        }

        this.spawnParticle(location, brush.getFirst().color());
    }

    private void spawnParticle(@NotNull Location location, @NotNull Color color)
    {
        this.particles.add(location);

        Bukkit.getScheduler().runTaskLater(Vanilife.getPlugin(), () -> this.particles.remove(location), 20L * 60 * 3);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (! particles.contains(location))
                {
                    this.cancel();
                    return;
                }

                location.getWorld().spawnParticle(Particle.DUST, location, 2, new Particle.DustOptions(color, 1));
            }
        }.runTaskTimer(Vanilife.getPlugin(), 0, 1L);
    }

    private enum Brush
    {
        WHITE(Material.WHITE_DYE, Color.WHITE),
        GRAY(Material.GRAY_DYE, Color.GRAY),
        BLACK(Material.BLACK_DYE, Color.BLACK),
        BROWN(Material.BROWN_DYE, Color.fromRGB(166, 73, 36)),
        RED(Material.RED_DYE, Color.RED),
        YELLOW(Material.YELLOW_DYE, Color.YELLOW),
        LIME(Material.LIME_DYE, Color.LIME),
        GREEN(Material.GREEN_DYE, Color.GREEN),
        AQUA(Material.LIGHT_BLUE_DYE, Color.AQUA),
        PURPLE(Material.PURPLE_DYE, Color.PURPLE),
        PINK(Material.PINK_DYE, Color.fromRGB(255, 163, 194));

        private final Material material;
        private final Color color;

        Brush(@NotNull Material material, @NotNull Color color)
        {
            this.material = material;
            this.color = color;
        }

        public @NotNull Material material()
        {
            return this.material;
        }

        public @NotNull Color color()
        {
            return this.color;
        }
    }
}
