package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ConfirmUI extends ChestUI
{
    private final Runnable accept;
    private final Runnable reject;

    public ConfirmUI(@NotNull Player player, @NotNull Runnable accept, @NotNull Runnable reject)
    {
        super(player, Bukkit.createInventory(null, 27, Language.translate("ui.confirm.title", player)));

        this.accept = accept;
        this.reject = reject;

        ItemStack acceptStack = new ItemStack(Material.GREEN_TERRACOTTA);
        ItemMeta acceptMeta = acceptStack.getItemMeta();
        acceptMeta.displayName(Language.translate("ui.confirm.accept", this.player).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
        acceptStack.setItemMeta(acceptMeta);
        this.inventory.setItem(11, acceptStack);

        ItemStack rejectStack = new ItemStack(Material.RED_TERRACOTTA);
        ItemMeta rejectMeta = rejectStack.getItemMeta();
        rejectMeta.displayName(Language.translate("ui.confirm.reject", this.player).color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
        rejectStack.setItemMeta(rejectMeta);
        this.inventory.setItem(15, rejectStack);
    }

    private boolean b = true;

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);

        if (event.getSlot() == 11)
        {
            this.b = false;
            this.player.closeInventory();
            Bukkit.getScheduler().runTask(Vanilife.getPlugin(), this.accept);
        }

        if (event.getSlot() == 15)
        {
            this.player.closeInventory();
        }
    }

    @Override
    public void onClose(@NotNull InventoryCloseEvent event)
    {
        super.onClose(event);

        if (this.b)
        {
            Bukkit.getScheduler().runTask(Vanilife.getPlugin(), this.reject);
        }
    }
}
