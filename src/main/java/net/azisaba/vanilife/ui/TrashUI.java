package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.util.Typing;
import net.kyori.adventure.text.Component;
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
        super(player, Bukkit.createInventory(null, 54, Component.text("ゴミ箱")));
    }

    @Override
    public void onInventoryClick(@NotNull InventoryClickEvent event) {}

    @Override
    public void onInventoryClose(@NotNull InventoryCloseEvent event)
    {
        super.onInventoryClose(event);

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
                this.player.sendMessage(Component.text(String.format("確認: %s 個 のアイテムを削除しますか？", stacks.size())).color(NamedTextColor.GREEN));
                this.player.sendMessage(Component.text("(y)es または (n)o でを送信して確定します:").color(NamedTextColor.YELLOW));
            }

            @Override
            public void onTyped(String string)
            {
                super.onTyped(string);

                if (string.equalsIgnoreCase("y"))
                {
                    this.player.sendMessage(Component.text(String.format("%s 個のアイテムが削除されました", stacks.size())).color(NamedTextColor.GREEN));
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

                        player.sendMessage(Component.text("アイテムの削除はキャンセルされました").color(NamedTextColor.GREEN));
                    }
                }.runTask(Vanilife.getPlugin());
            }
        };
    }
}
