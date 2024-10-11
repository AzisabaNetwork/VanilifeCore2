package net.azisaba.vanilife.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class InventoryUI implements IInventoryUI
{
    private static final List<InventoryUI> instances = new ArrayList<>();

    public static InventoryUI getInstance(@NotNull Player player)
    {
        List<InventoryUI> filteredInstances = InventoryUI.instances.stream().filter(i -> i.getPlayer().equals(player)).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static List<InventoryUI> getInstances()
    {
        return InventoryUI.instances;
    }

    protected final Player player;

    public InventoryUI(@NotNull Player player)
    {
        this.player = player;

        InventoryUI old = InventoryUI.getInstance(this.player);

        if (old != null)
        {
            old.onClose(new InventoryCloseEvent(this.player.getOpenInventory(), InventoryCloseEvent.Reason.PLUGIN));
            InventoryUI.instances.remove(old);
            this.player.updateInventory();
        }

        InventoryUI.instances.add(this);
    }

    @Override
    public @NotNull Player getPlayer()
    {
        return this.player;
    }

    @Override
    public void onClose(@NotNull InventoryCloseEvent event)
    {
        InventoryUI.instances.remove(this);
    }
}
