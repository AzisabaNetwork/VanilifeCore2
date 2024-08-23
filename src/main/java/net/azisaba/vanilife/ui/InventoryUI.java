package net.azisaba.vanilife.ui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public abstract class InventoryUI
{
    protected static final HashMap<InventoryUI, Inventory> instances = new HashMap<>();

    public static HashMap<InventoryUI, Inventory> getInstances()
    {
        return InventoryUI.instances;
    }

    protected final Player player;
    protected final Inventory inventory;
    protected final HashMap<Integer, String> clientListeners = new HashMap<>();
    protected final HashMap<Integer, String> serverListeners = new HashMap<>();

    public InventoryUI(Player player, Inventory inventory)
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

    public Inventory getInventory()
    {
        return this.inventory;
    }

    protected void registerListener(int index, ItemStack stack, String command, ExecutionType type)
    {
        switch (type)
        {
            case CLIENT -> this.clientListeners.put(index, command);
            case SERVER -> this.serverListeners.put(index, command);
        }

        this.inventory.setItem(index, stack);
    }

    public void onUiClick(InventoryClickEvent event)
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

    public void onInventoryClick(InventoryClickEvent event)
    {
        event.setCancelled(true);
    }

    public void onInventoryDrag(InventoryDragEvent event)
    {
        event.setCancelled(true);
    }

    public void onInventoryClose(InventoryCloseEvent event)
    {
        InventoryUI.instances.remove(this);
    }

    enum ExecutionType
    {
        CLIENT,
        SERVER
    }
}
