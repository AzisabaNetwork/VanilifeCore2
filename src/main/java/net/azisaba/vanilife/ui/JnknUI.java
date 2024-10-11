package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.arcade.Jnkn;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JnknUI extends ChestUI
{
    private final Jnkn jnkn;

    public JnknUI(@NotNull Player player, @NotNull Jnkn jnkn)
    {
        super(player, Bukkit.createInventory(null, 27, Language.translate("ui.jnkn.title", player)));
        this.jnkn = jnkn;

        ItemStack gStack = new ItemStack(Material.COBBLESTONE);
        ItemMeta gMeta = gStack.getItemMeta();
        gMeta.displayName(Language.translate("ui.jnkn.rock", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        gStack.setItemMeta(gMeta);
        this.inventory.setItem(9, gStack);

        ItemStack cStack = new ItemStack(Material.SHEARS);
        ItemMeta cMeta = cStack.getItemMeta();
        cMeta.displayName(Language.translate("ui.jnkn.scissors", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        cStack.setItemMeta(cMeta);
        this.inventory.setItem(10, cStack);

        ItemStack pStack = new ItemStack(Material.PAPER);
        ItemMeta pMeta = pStack.getItemMeta();
        pMeta.displayName(Language.translate("ui.jnkn.paper", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        pStack.setItemMeta(pMeta);
        this.inventory.setItem(11, pStack);
        
        ItemStack hoiStack = new ItemStack(Material.OAK_DOOR);
        ItemMeta hoiMeta = hoiStack.getItemMeta();
        hoiMeta.displayName(Language.translate("ui.jnkn.cancel", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.ITALIC, false));
        hoiMeta.lore(List.of(Language.translate("ui.jnkn.cancel.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        hoiStack.setItemMeta(hoiMeta);
        this.inventory.setItem(17, hoiStack);
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event)
    {
        super.onClick(event);

        if (event.getCurrentItem() == null)
        {
            return;
        }

        if (event.getSlot() != 17 && event.getClickedInventory() == this.inventory)
        {
            this.player.playSound(this.player, Sound.UI_BUTTON_CLICK, 1.0F, 1.2F);

            ItemStack lockStack = new ItemStack(Material.BARRIER);
            ItemMeta lockMeta = lockStack.getItemMeta();
            lockMeta.displayName(Language.translate("ui.jnkn.waiting", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
            lockMeta.lore(List.of(Language.translate("ui.jnkn.waiting.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
            lockStack.setItemMeta(lockMeta);

            for (int i = 9; i < 12; i ++)
            {
                this.inventory.setItem(i, lockStack);
            }
        }

        if (event.getSlot() == 17)
        {
            this.player.closeInventory();
        }

        if (this.jnkn.getHand(this.player) != Jnkn.Hand.UNKNOWN)
        {
            return;
        }

        if (event.getSlot() == 9)
        {
            this.jnkn.setHand(this.player, Jnkn.Hand.G);
        }

        if (event.getSlot() == 10)
        {
            this.jnkn.setHand(this.player, Jnkn.Hand.C);
        }

        if (event.getSlot() == 11)
        {
            this.jnkn.setHand(this.player, Jnkn.Hand.P);
        }
    }

    @Override
    public void onClose(@NotNull InventoryCloseEvent event)
    {
        super.onClose(event);

        this.jnkn.cancel();
    }
}
