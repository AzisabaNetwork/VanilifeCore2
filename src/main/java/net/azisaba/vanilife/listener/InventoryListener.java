package net.azisaba.vanilife.listener;

import net.azisaba.vanilife.ui.InventoryUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class InventoryListener implements Listener
{
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = player.getOpenInventory().getTopInventory();

        if (InventoryUI.getInstances().containsValue(inventory))
        {
            Map.Entry<InventoryUI, Inventory> entry = InventoryUI.getInstances().entrySet().stream().filter(i -> i.getValue() == inventory).toList().getFirst();
            entry.getKey().onInventoryClick(event);

            if (InventoryUI.getInstances().containsValue(event.getClickedInventory()))
            {
                entry.getKey().onUiClick(event);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = player.getOpenInventory().getTopInventory();

        if (InventoryUI.getInstances().containsValue(inventory))
        {
            Map.Entry<InventoryUI, Inventory> entry = InventoryUI.getInstances().entrySet().stream().filter(i -> i.getValue() == inventory).toList().getFirst();
            entry.getKey().onInventoryDrag(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        Inventory inventory = event.getInventory();

        if (InventoryUI.getInstances().containsValue(inventory))
        {
            Map.Entry<InventoryUI, Inventory> entry = InventoryUI.getInstances().entrySet().stream().filter(i -> i.getValue() == inventory).toList().getFirst();
            entry.getKey().onInventoryClose(event);
        }
    }
}
