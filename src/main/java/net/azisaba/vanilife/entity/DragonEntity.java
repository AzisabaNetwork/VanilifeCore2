package net.azisaba.vanilife.entity;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class DragonEntity extends VanilifeEntity<EnderDragon>
{
    public DragonEntity(@NotNull EnderDragon entity)
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

        Player target = Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getWorld().equals(this.entity.getWorld()) && player.getGameMode() == GameMode.SURVIVAL && player.getLocation().distance(this.entity.getLocation()) <= 64)
                .min(Comparator.comparingDouble((Player p) -> p.getLocation().distance(this.entity.getLocation())))
                .orElse(null);

        if (target == null)
        {
            this.entity.setPhase(EnderDragon.Phase.SEARCH_FOR_BREATH_ATTACK_TARGET);
            return;
        }

        this.entity.setTarget(target);

        if (this.entity.getTarget() == null)
        {
            return;
        }

        this.entity.setPhase(EnderDragon.Phase.STRAFING);
    }
}
