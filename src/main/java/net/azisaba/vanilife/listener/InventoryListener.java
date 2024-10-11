package net.azisaba.vanilife.listener;

import net.azisaba.vanilife.ui.InventoryUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

public class InventoryListener implements Listener
{
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        InventoryUI ui = InventoryUI.getInstance((Player) event.getWhoClicked());

        if (ui == null)
        {
            return;
        }

        ui.onClick(event);

        if (event.getClickedInventory() == ui.getInventory())
        {
            ui.onUiClick(event);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event)
    {
        InventoryUI ui = InventoryUI.getInstance((Player) event.getWhoClicked());

        if (ui == null)
        {
            return;
        }

        ui.onDrag(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        InventoryUI ui = InventoryUI.getInstance((Player) event.getPlayer());

        if (ui == null || event.getInventory() != ui.getInventory())
        {
            return;
        }

        ui.onClose(event);
        ui.getPlayer().updateInventory();
    }
}
