package net.azisaba.vanilife.listener;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.entity.DragonEntity;
import net.azisaba.vanilife.vwm.VanilifeWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootTable;

public class UnderworldListener implements Listener
{
    @EventHandler
    public void onEntitySpawn(EntityAddToWorldEvent event)
    {
        Location location = event.getEntity().getLocation();

        if (event.getEntityType() == EntityType.ENDER_DRAGON)
        {
            return;
        }

        if (! location.getWorld().equals(VanilifeWorldManager.getUnderworld()))
        {
            return;
        }

        if (0.05 < Vanilife.random.nextDouble())
        {
            return;
        }

        Bukkit.getScheduler().runTaskLater(Vanilife.getPlugin(), () -> new DragonEntity(location.add(0, 56, 0)), 20L * 4);
    }

    @EventHandler
    public void onLootTable(InventoryOpenEvent event)
    {
        Inventory inventory = event.getInventory();

        if (inventory.getType() != InventoryType.CHEST)
        {
            return;
        }

        if (! (inventory.getHolder() instanceof Chest chest))
        {
            return;
        }

        LootTable table = chest.getLootTable();

        if (table == null || ! table.getKey().getNamespace().equals(Vanilife.getPlugin().getName()))
        {
            return;
        }
    }
}
