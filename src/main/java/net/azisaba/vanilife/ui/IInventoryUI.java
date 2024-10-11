package net.azisaba.vanilife.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public interface IInventoryUI
{
    Inventory getInventory();

    @NotNull Player getPlayer();

    void onClick(@NotNull InventoryClickEvent event);

    void onUiClick(@NotNull InventoryClickEvent event);

    void onDrag(@NotNull InventoryDragEvent event);

    void onClose(@NotNull InventoryCloseEvent event);
}
