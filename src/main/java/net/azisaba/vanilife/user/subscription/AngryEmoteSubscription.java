package net.azisaba.vanilife.user.subscription;

import net.azisaba.vanilife.ui.Language;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Product
@SingletonSubscription
public class AngryEmoteSubscription implements IEmoteSubscription
{
    @Override
    public @NotNull String getName()
    {
        return "angry_emote";
    }

    @Override
    public @NotNull String getEmoteName()
    {
        return "angry";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.WITHER_ROSE;
    }

    @Override
    public @NotNull List<Component> getDetails(@NotNull Language lang)
    {
        return List.of(lang.translate("subscription.angry_emote.details.1"), Component.text(this.getCost() + " Mola").color(NamedTextColor.GREEN));
    }

    @Override
    public int getCost()
    {
        return 60;
    }

    @Override
    public void use(@NotNull Player player)
    {
        Location location = player.getLocation().add(0, 2, 0);
        location.getWorld().playSound(location, Sound.ENTITY_PILLAGER_AMBIENT, 1.0f, 0.8f);
        location.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, location, 20);
    }
}
