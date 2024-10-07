package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.util.PlayerUtility;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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

        new ConfirmUI(this.player, () -> {
            this.player.sendMessage(Language.translate("ui.trash.deleted", this.player).color(NamedTextColor.GREEN));
        }, () -> {
            PlayerUtility.giveItemStack(this.player, stacks);
            player.sendMessage(Language.translate("ui.trash.cancelled", player).color(NamedTextColor.GREEN));
        });
    }
}
