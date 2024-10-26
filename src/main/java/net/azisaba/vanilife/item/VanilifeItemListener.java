package net.azisaba.vanilife.item;

import net.azisaba.vanilife.Vanilife;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class VanilifeItemListener implements Listener
{
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        ItemStack item = event.getItem();

        if (item == null)
        {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Vanilife.getPlugin(), "name");

        if (! container.has(key))
        {
            return;
        }

        VanilifeItem vi = VanilifeItems.registry.get(container.get(key, PersistentDataType.STRING));

        if (vi == null)
        {
            container.remove(key);
            return;
        }

        vi.use(event.getPlayer(), new ItemStorage(item));
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event)
    {
        ItemStack item = event.getItem();
        ItemMeta meta = item.getItemMeta();

        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Vanilife.getPlugin(), "name");

        if (! container.has(key))
        {
            return;
        }

        VanilifeItem vi = VanilifeItems.registry.get(container.get(key, PersistentDataType.STRING));

        if (! (vi instanceof FoodItem food))
        {
            container.remove(key);
            return;
        }

        food.consume(event.getPlayer());
    }
}
