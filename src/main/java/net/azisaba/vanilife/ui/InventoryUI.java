package net.azisaba.vanilife.ui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class InventoryUI
{
    protected static final Map<InventoryUI, Inventory> instances = new HashMap<>();

    public static Map<InventoryUI, Inventory> getInstances()
    {
        return InventoryUI.instances;
    }

    protected final Player player;
    protected final Inventory inventory;
    protected final Map<Integer, String> clientListeners = new HashMap<>();
    protected final Map<Integer, String> serverListeners = new HashMap<>();

    public InventoryUI(@NotNull Player player, @NotNull Inventory inventory)
    {
        this.player = player;
        this.inventory = inventory;

        Inventory currentInv = this.player.getOpenInventory().getTopInventory();

        if (InventoryUI.getInstances().containsValue(currentInv))
        {
            InventoryUI.getInstances().entrySet().stream().filter(i -> i.getValue() == currentInv).toList().getFirst().getKey().onInventoryClose(new InventoryCloseEvent(player.getOpenInventory()));
        }

        this.player.openInventory(this.inventory);
        InventoryUI.instances.put(this, this.inventory);
    }

    @NotNull
    public Inventory getInventory()
    {
        return this.inventory;
    }

    protected void registerListener(int index, @NotNull ItemStack stack, @NotNull String command, @NotNull ExecutionType type)
    {
        switch (type)
        {
            case CLIENT -> this.clientListeners.put(index, command);
            case SERVER -> this.serverListeners.put(index, command);
        }

        this.inventory.setItem(index, stack);
    }

    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        if (event.getCurrentItem() == null)
        {
            return;
        }

        if (this.serverListeners.containsKey(event.getSlot()))
        {
            event.getWhoClicked().closeInventory();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.serverListeners.get(event.getSlot()));
        }

        if (this.clientListeners.containsKey(event.getSlot()))
        {
            event.getWhoClicked().closeInventory();
            Bukkit.dispatchCommand(event.getWhoClicked(), this.clientListeners.get(event.getSlot()));
        }
    }

    public void onInventoryClick(@NotNull InventoryClickEvent event)
    {
        event.setCancelled(true);
    }

    public void onInventoryDrag(@NotNull InventoryDragEvent event)
    {
        event.setCancelled(true);
    }

    public void onInventoryClose(@NotNull InventoryCloseEvent event)
    {
        InventoryUI.instances.remove(this);
    }

    enum ExecutionType
    {
        CLIENT,
        SERVER
    }
}
