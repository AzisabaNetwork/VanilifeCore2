package net.azisaba.vanilife.entity;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class DragonEntity extends VanilifeEntity
{
    public DragonEntity(@NotNull Entity entity)
    {
        super(entity);
    }

    public DragonEntity(@NotNull Location location)
    {
        super(location);
    }

    @Override
    public @NotNull String getName()
    {
        return "dragon";
    }

    @Override
    public @NotNull EntityType getType()
    {
        return EntityType.ENDER_DRAGON;
    }

    @Override
    public void tick()
    {
        super.tick();

        EnderDragon dragon = (EnderDragon) this.entity;

        Player target = Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getWorld().equals(dragon.getWorld()) && player.getGameMode() == GameMode.SURVIVAL && player.getLocation().distance(dragon.getLocation()) <= 64)
                .min(Comparator.comparingDouble((Player p) -> p.getLocation().distance(dragon.getLocation())))
                .orElse(null);

        if (target == null)
        {
            dragon.setPhase(EnderDragon.Phase.SEARCH_FOR_BREATH_ATTACK_TARGET);
            return;
        }

        dragon.setTarget(target);

        if (dragon.getTarget() == null)
        {
            return;
        }

        dragon.setPhase(EnderDragon.Phase.STRAFING);
    }
}
