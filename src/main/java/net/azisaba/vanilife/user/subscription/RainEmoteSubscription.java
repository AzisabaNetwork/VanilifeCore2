package net.azisaba.vanilife.user.subscription;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Product
@SingletonSubscription
public class RainEmoteSubscription implements IEmoteSubscription
{
    @Override
    public @NotNull String getName()
    {
        return "rain_emote";
    }

    @Override
    public @NotNull String getEmoteName()
    {
        return "rain";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.WATER_BUCKET;
    }

    @Override
    public @NotNull List<Component> getDetails(@NotNull Language lang)
    {
        return List.of(lang.translate("subscription.rain_emote.details.1"), Component.text(this.getCost() + " Mola").color(NamedTextColor.GREEN));
    }

    @Override
    public int getCost()
    {
        return 70;
    }

    @Override
    public void use(@NotNull Player player)
    {
        new BukkitRunnable()
        {
            final double radius = 2.25;
            final int particles = 200;
            final int height = 5;

            private int i;

            @Override
            public void run()
            {
                if (7 < this.i ++)
                {
                    this.cancel();
                    return;
                }

                for (int i = 0; i < this.particles; i ++)
                {
                    double angle = Math.random() * Math.PI * 2;
                    double xOffset = Math.cos(angle) * this.radius * Math.random();
                    double zOffset = Math.sin(angle) * this.radius * Math.random();

                    player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation().clone().add(xOffset, this.height, zOffset), 0);

                    if (Vanilife.random.nextDouble() < 0.5)
                    {
                        player.getWorld().spawnParticle(Particle.FALLING_WATER, player.getLocation().clone().add(xOffset, this.height, zOffset), 0);
                    }
                }
            }
        }.runTaskTimer(Vanilife.getPlugin(), 0L, 10L);
    }
}
