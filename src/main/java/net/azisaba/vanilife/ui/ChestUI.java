package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class ChestUI extends InventoryUI
{
    protected final Inventory inventory;

    protected final Map<Integer, String> clientListeners = new HashMap<>();
    protected final Map<Integer, String> serverListeners = new HashMap<>();

    public ChestUI(@NotNull Player player, @NotNull Inventory inventory)
    {
        super(player);

        this.inventory = inventory;
        Bukkit.getScheduler().runTask(Vanilife.getPlugin(), () -> this.player.openInventory(this.inventory));
    }

    public @NotNull Inventory getInventory()
    {
        return this.inventory;
    }

    protected void registerListener(int index, @NotNull ItemStack stack, @NotNull String command, @NotNull ExecutionType type)
    {
        if (! command.contains(":"))
        {
            command = "vanilife:" + command;
        }

        switch (type)
        {
            case CLIENT -> this.clientListeners.put(index, command);
            case SERVER -> this.serverListeners.put(index, command);
        }

        this.inventory.setItem(index, stack);
    }

    public void onClick(@NotNull InventoryClickEvent event)
    {
        event.setCancelled(true);
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

    public void onDrag(@NotNull InventoryDragEvent event)
    {
        event.setCancelled(true);
    }

    enum ExecutionType
    {
        CLIENT,
        SERVER
    }
}
