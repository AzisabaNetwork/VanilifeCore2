package net.azisaba.vanilife.user.subscription;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Product
@SingletonSubscription
public class CreeperEmote implements Emote
{
    @Override
    public @NotNull String getName()
    {
        return "creeper_emote";
    }

    @Override
    public @NotNull String getEmoteName()
    {
        return "creeper";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.CREEPER_HEAD;
    }

    @Override
    public @NotNull List<Component> getDetails(@NotNull Language lang)
    {
        return List.of(lang.translate("subscription.creeper_emote.details.1"), Component.text(this.getCost() + " Mola").color(NamedTextColor.GREEN));
    }

    @Override
    public int getCost()
    {
        return 140;
    }

    @Override
    public void use(@NotNull Player player)
    {
        World world = player.getWorld();
        world.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);

        Bukkit.getScheduler().runTaskLater(Vanilife.getPlugin(), () -> {
            world.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
            world.spawnParticle(Particle.EXPLOSION_EMITTER, player.getLocation(), 1);
        }, 30L);
    }
}
