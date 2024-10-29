package net.azisaba.vanilife.entity;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEnderDragon;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class DragonEntity extends VanilifeEntity<EnderDragon>
{
    public DragonEntity(@NotNull CraftEnderDragon entity)
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
    public long getPeriod()
    {
        return 20L * 10;
    }

    @Override
    protected void init()
    {
        super.init();
        this.entity.setCustomNameVisible(false);
    }

    @Override
    public void tick()
    {
        super.tick();

        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getWorld().equals(this.entity.getWorld()) && player.getGameMode() == GameMode.SURVIVAL && player.getLocation().distance(this.entity.getLocation()) <= 64)
                .min(Comparator.comparingDouble((Player p) -> p.getLocation().distance(this.entity.getLocation()))).ifPresent(this::attack);

    }

    private void attack(@NotNull Player player)
    {
        if (! this.entity.getWorld().equals(player.getWorld()))
        {
            return;
        }

        this.entity.lookAt(player);
        this.entity.setVelocity(player.getLocation().subtract(this.entity.getLocation()).toVector().normalize().multiply(1.5));

        Fireball fireball = (Fireball) this.entity.getWorld().spawnEntity(this.entity.getLocation().add(0, 1, 0), EntityType.FIREBALL);
        fireball.setDirection(player.getLocation().subtract(this.entity.getLocation()).toVector().normalize());
        fireball.setYield(0);
    }
}
