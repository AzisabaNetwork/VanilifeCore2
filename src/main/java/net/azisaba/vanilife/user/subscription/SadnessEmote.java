package net.azisaba.vanilife.user.subscription;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Product
@SingletonSubscription
public class SadnessEmote implements Emote
{
    @Override
    public @NotNull String getName()
    {
        return "sadness_emote";
    }

    @Override
    public @NotNull String getEmoteName()
    {
        return "sadness";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.LAPIS_LAZULI;
    }

    @Override
    public @NotNull List<Component> getDetails(@NotNull Language lang)
    {
        return List.of(lang.translate("subscription.rich_emote.details.1"), Component.text(this.getCost() + " Mola").color(NamedTextColor.GREEN));
    }

    @Override
    public int getCost()
    {
        return 90;
    }

    @Override
    public void use(@NotNull Player player)
    {
        new BukkitRunnable()
        {
            private int i;

            @Override
            public void run()
            {
                if (this.i >= 10)
                {
                    this.cancel();
                    return;
                }

                Location playerLocation = player.getLocation();
                double yaw = Math.toRadians(playerLocation.getYaw());

                double offsetX = -Math.sin(yaw) * 0.5 + Math.cos(yaw) * 0.2;
                double offsetZ = Math.cos(yaw) * 0.5 + Math.sin(yaw) * 0.2;

                Location location = playerLocation.add(offsetX, 1.5, offsetZ);

                location.getWorld().spawnParticle(Particle.FALLING_WATER, location, 0);

                this.i ++;
            }
        }.runTaskTimer(Vanilife.getPlugin(), 0, 5L);
    }
}
