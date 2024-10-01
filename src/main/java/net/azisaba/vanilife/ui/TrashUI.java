package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.util.Typing;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class TrashUI extends InventoryUI
{
    public TrashUI(@NotNull Player player)
    {
        super(player, Bukkit.createInventory(null, 54, Language.translate("ui.trash.title", player)));
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {}

    @Override
    public void onClose(@NotNull InventoryCloseEvent event)
    {
        super.onClose(event);

        ArrayList<ItemStack> stacks = new ArrayList<>();

        this.inventory.forEach(stack -> {
            if (stack != null && 0 < stack.getAmount())
            {
                stacks.add(stack);
            }
        });

        if (stacks.isEmpty())
        {
            return;
        }

        new Typing(this.player)
        {
            @Override
            public void init()
            {
                this.player.sendMessage(Language.translate("ui.trash.check", this.player).color(NamedTextColor.GREEN));
                this.player.sendMessage(Language.translate("ui.trash.check.details", this.player).color(NamedTextColor.YELLOW));
            }

            @Override
            public void onTyped(String string)
            {
                super.onTyped(string);

                if (string.equalsIgnoreCase("y"))
                {
                    this.player.sendMessage(Language.translate("ui.trash.deleted", this.player).color(NamedTextColor.GREEN));
                    return;
                }

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        Inventory inventory = player.getInventory();
                        ArrayList<ItemStack> remainingStacks = new ArrayList<>();

                        stacks.forEach(stack -> {
                            if (stack != null && 0 < stack.getAmount())
                            {
                                ItemStack[] leftover = inventory.addItem(stack).values().toArray(new ItemStack[0]);
                                remainingStacks.addAll(Arrays.asList(leftover));
                            }
                        });

                        remainingStacks.forEach(stack -> {
                            if (stack != null && 0 < stack.getAmount())
                            {
                                player.getWorld().dropItem(player.getLocation(), stack);
                            }
                        });

                        player.sendMessage(Language.translate("ui.trash.canceled", player).color(NamedTextColor.GREEN));
                    }
                }.runTask(Vanilife.getPlugin());
            }
        };
    }
}
