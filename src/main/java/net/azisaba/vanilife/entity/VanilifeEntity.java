package net.azisaba.vanilife.entity;

import net.azisaba.vanilife.Vanilife;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class VanilifeEntity implements IVanilifeEntity
{
    protected static final List<VanilifeEntity> instances = new ArrayList<>();

    public static VanilifeEntity getInstance(UUID id)
    {
        List<VanilifeEntity> filteredInstances = VanilifeEntity.instances.stream().filter(i -> i.asEntity().getUniqueId().equals(id)).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static VanilifeEntity getInstance(Entity entity)
    {
        return VanilifeEntity.getInstance(entity.getUniqueId());
    }

    protected final Entity entity;
    protected final Random random = Vanilife.random;

    public VanilifeEntity(@NotNull Entity entity)
    {
        if (entity.getType() != this.getType())
        {
            throw new RuntimeException();
        }

        this.entity = entity;

        this.init();
    }

    public VanilifeEntity(@NotNull Location location)
    {
        this.entity = location.getWorld().spawnEntity(location, this.getType());

        PersistentDataContainer container = this.entity.getPersistentDataContainer();
        container.set(new NamespacedKey(Vanilife.getPlugin(), "name"), PersistentDataType.STRING, this.getName());

        this.init();
    }

    @Override
    public void setScale(double scale)
    {
        if (! (this.entity instanceof LivingEntity living))
        {
            return;
        }

        AttributeInstance attribute = living.getAttribute(Attribute.GENERIC_SCALE);

        if (attribute == null)
        {
            return;
        }

        attribute.setBaseValue(scale);
    }

    protected void onDeath(@NotNull EntityDeathEvent event) {}

    public @NotNull Entity asEntity()
    {
        return this.entity;
    }

    public LivingEntity asLivingEntity()
    {
        if (this.entity instanceof LivingEntity living)
        {
            return living;
        }

        return null;
    }

    protected void init()
    {
        VanilifeEntity.instances.add(this);

        this.entity.customName(this.getDisplayName());
        this.entity.setCustomNameVisible(true);

        this.setScale(this.getScale());

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (VanilifeEntity.this.entity.isDead())
                {
                    this.cancel();
                    return;
                }

                if (! VanilifeEntity.this.entity.isInvisible())
                {
                    VanilifeEntity.this.tick();
                }
            }
        }.runTaskTimer(Vanilife.getPlugin(), 0L, this.getPeriod());
    }

    @Override
    public void tick()
    {
        if (! (this.entity instanceof LivingEntity living))
        {
            return;
        }

        EntityEquipment equipment = living.getEquipment();

        if (equipment == null)
        {
            return;
        }

        equipment.setHelmet(this.getHelmet());
        equipment.setChestplate(this.getChestplate());
        equipment.setLeggings(this.getLeggings());
        equipment.setBoots(this.getBoots());

        equipment.setItemInMainHand(this.getMainHand());
        equipment.setItemInOffHand(this.getOffHand());
    }
}
