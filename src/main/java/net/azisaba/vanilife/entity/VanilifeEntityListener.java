package net.azisaba.vanilife.entity;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import net.azisaba.vanilife.Vanilife;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.InvocationTargetException;

public class VanilifeEntityListener implements Listener
{
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        VanilifeEntity<?> entity = VanilifeEntity.getInstance(event.getEntity());

        if (entity == null)
        {
            return;
        }

        event.getDrops().clear();
        entity.onDeath(event);
    }

    @EventHandler
    public void onEntityAddToWorld(EntityAddToWorldEvent event) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException
    {
        Entity entity = event.getEntity();
        NamespacedKey key = new NamespacedKey(Vanilife.getPlugin(), "name");

        if (! entity.getPersistentDataContainer().has(key))
        {
            return;
        }

        Class<? extends VanilifeEntity<?>> clazz = VanilifeEntities.registry.get(entity.getPersistentDataContainer().get(key, PersistentDataType.STRING));

        if (clazz == null)
        {
            entity.remove();
        }

        clazz.getConstructor(entity.getClass()).newInstance(entity);
    }
}
