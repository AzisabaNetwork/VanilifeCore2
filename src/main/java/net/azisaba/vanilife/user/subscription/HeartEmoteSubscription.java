package net.azisaba.vanilife.user.subscription;

import net.azisaba.vanilife.ui.Language;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Product
@SingletonSubscription
public class HeartEmoteSubscription implements IEmoteSubscription
{
    @Override
    public @NotNull String getName()
    {
        return "heart_emote";
    }

    @Override
    public @NotNull String getEmoteName()
    {
        return "heart";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.APPLE;
    }

    @Override
    public @NotNull List<Component> getDetails(@NotNull Language lang)
    {
        return List.of(lang.translate("subscription.heart_emote.details.1"), Component.text(this.getCost() + " Mola").color(NamedTextColor.GREEN));
    }

    @Override
    public int getCost()
    {
        return 200;
    }

    @Override
    public void use(@NotNull Location location)
    {
        double yaw = Math.toRadians(location.getYaw());

        for (double t = 0; t <= Math.PI * 2; t += 0.1)
        {
            double x = 16 * Math.pow(Math.sin(t), 3);
            double y = 13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t);

            Location particleLocation = location.clone().add(x / 15 * Math.cos(yaw), y / 15, x / 15 * Math.sin(yaw));
            location.getWorld().spawnParticle(Particle.HEART, particleLocation, 1);
        }
    }
}
