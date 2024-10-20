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
public class ThinkingEmote implements Emote
{
    @Override
    public @NotNull String getName()
    {
        return "thinking_emote";
    }

    @Override
    public @NotNull String getEmoteName()
    {
        return "thinking";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.KNOWLEDGE_BOOK;
    }

    @Override
    public @NotNull List<Component> getDetails(@NotNull Language lang)
    {
        return List.of(lang.translate("subscription.thinking_emote.details.1"), Component.text(this.getCost() + " Mola").color(NamedTextColor.GREEN));
    }

    @Override
    public int getCost()
    {
        return 150;
    }

    @Override
    public void use(@NotNull Player player)
    {
        new BukkitRunnable()
        {
            private final int particle = 40;
            private int i = 0;

            @Override
            public void run()
            {
                if (10 < this.i ++)
                {
                    this.cancel();
                    return;
                }

                Location location = player.getLocation();

                for (int j = 0; j < 360; j += 360 / this.particle)
                {
                    double angle = Math.toRadians(j);
                    double radius = 0.8;
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    location.add(x, 0, z);

                    player.getWorld().spawnParticle(Particle.ENCHANT, location, 1);
                    location.subtract(x, 0, z);
                }
            }
        }.runTaskTimer(Vanilife.getPlugin(), 0L, 20L);
    }
}
