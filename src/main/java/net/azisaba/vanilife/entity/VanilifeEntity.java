package net.azisaba.vanilife.entity;

import net.azisaba.vanilife.Vanilife;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
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
    private static final List<VanilifeEntity> instances = new ArrayList<>();

    public static VanilifeEntity getInstance(UUID id)
    {
        List<VanilifeEntity> filteredInstances = VanilifeEntity.instances.stream().filter(i -> i.asEntity().getUniqueId().equals(id)).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static VanilifeEntity getInstance(LivingEntity entity)
    {
        return VanilifeEntity.getInstance(entity.getUniqueId());
    }

    protected final LivingEntity entity;
    protected final Random random = Vanilife.random;

    public VanilifeEntity(@NotNull LivingEntity entity)
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
        this.entity = (LivingEntity) location.getWorld().spawnEntity(location, this.getType());

        PersistentDataContainer container = this.entity.getPersistentDataContainer();
        container.set(new NamespacedKey(Vanilife.getPlugin(), "name"), PersistentDataType.STRING, this.getName());

        this.init();
    }

    @Override
    public void setScale(double scale)
    {
        AttributeInstance attribute = this.entity.getAttribute(Attribute.GENERIC_SCALE);

        if (attribute == null)
        {
            return;
        }

        attribute.setBaseValue(scale);
    }

    protected void onDeath(@NotNull EntityDeathEvent event) {}

    public @NotNull LivingEntity asEntity()
    {
        return this.entity;
    }

    protected void init()
    {
        VanilifeEntity.instances.add(this);

        this.entity.customName(this.getDisplayName());
        this.entity.setCustomNameVisible(true);
        this.entity.setSilent(true);

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

                VanilifeEntity.this.tick();
            }
        }.runTaskTimer(Vanilife.getPlugin(), 0L, this.getPeriod());
    }

    @Override
    public void tick()
    {
        EntityEquipment equipment = this.entity.getEquipment();

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
