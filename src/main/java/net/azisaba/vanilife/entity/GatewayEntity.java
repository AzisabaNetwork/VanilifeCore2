package net.azisaba.vanilife.entity;

import net.azisaba.vanilife.Vanilife;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class GatewayEntity extends VanilifeEntity
{
    public GatewayEntity(@NotNull Entity entity)
    {
        super(entity);

        Bukkit.getScheduler().runTaskLater(Vanilife.getPlugin(), () -> {
            entity.remove();
            VanilifeEntity.instances.remove(this);
        }, 20L);
    }

    public GatewayEntity(@NotNull Location location)
    {
        super(location);
    }

    @Override
    protected void init()
    {
        super.init();

        ItemDisplay entity = (ItemDisplay) this.entity;
        entity.setCustomNameVisible(false);
        entity.setItemStack(new ItemStack(Material.SLIME_BLOCK));
        entity.setTicksLived(Integer.MAX_VALUE);
        entity.setBillboard(Display.Billboard.FIXED);
        entity.setTransformation(new Transformation(new Vector3f(0f, 0f, 0f), new Quaternionf(), new Vector3f(0.7f, 0.7f, 0.7f), new Quaternionf()));
    }

    @Override
    public @NotNull String getName()
    {
        return "gateway";
    }

    @Override
    public @NotNull EntityType getType()
    {
        return EntityType.ITEM_DISPLAY;
    }

    @Override
    public void tick()
    {
        Location location = this.entity.getLocation().add(0, 0.5, 0);
        location.getWorld().spawnParticle(Particle.PORTAL, location, 56, 0.8, 0.8, 0.8);
    }
}
