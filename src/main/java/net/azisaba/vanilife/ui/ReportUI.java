package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.report.Report;
import net.azisaba.vanilife.report.ReportDataContainer;
import net.kyori.adventure.text.Component;
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

public class ReportUI extends InventoryUI
{
    private final ReportDataContainer container;

    public ReportUI(@NotNull Player player, @NotNull ReportDataContainer container)
    {
        super(player, Bukkit.createInventory(null, 27, Language.translate("ui.report.title", player)));

        this.container = container;

        ItemStack yesStack = new ItemStack(Material.GREEN_TERRACOTTA);
        ItemMeta yesMeta = yesStack.getItemMeta();
        yesMeta.displayName(Language.translate("ui.report.yes", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        yesMeta.lore(List.of(Language.translate("ui.report.yes.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        yesStack.setItemMeta(yesMeta);
        this.inventory.setItem(11, yesStack);
        
        ItemStack attachStack = new ItemStack(Material.OAK_SIGN);
        ItemMeta attachMeta = attachStack.getItemMeta();
        attachMeta.displayName(Language.translate("ui.report.attach", this.player).color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        attachMeta.lore(List.of(Language.translate("ui.report.attach.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        attachStack.setItemMeta(attachMeta);
        this.inventory.setItem(13, attachStack);

        ItemStack noStack = new ItemStack(Material.RED_TERRACOTTA);
        ItemMeta noMeta = noStack.getItemMeta();
        noMeta.displayName(Language.translate("ui.report.no", this.player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        noMeta.lore(List.of(Language.translate("ui.report.no.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        noStack.setItemMeta(noMeta);
        this.inventory.setItem(15, noStack);
    }

    private boolean cancel = true;

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);

        if (event.getCurrentItem() == null)
        {
            return;
        }

        if (event.getSlot() == 11)
        {
            new Report(this.container);
            this.inventory.close();
            this.player.sendMessage(Component.text().build());
            this.player.sendMessage(Language.translate("ui.report.reported", this.player).color(NamedTextColor.GREEN));
            this.player.sendMessage(Component.text().build());
            this.player.playSound(this.player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
        }

        if (event.getSlot() == 13)
        {
            this.cancel = false;
            this.player.closeInventory();
            this.player.sendMessage(Language.translate("ui.report.attach.help", this.player).color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD));
        }

        if (event.getSlot() == 15)
        {
            this.container.cancel();
            this.inventory.close();
            this.player.sendMessage(Language.translate("ui.report.cancelled", this.player).color(NamedTextColor.RED));
        }
    }

    @Override
    public void onInventoryClose(@NotNull InventoryCloseEvent event)
    {
        super.onInventoryClose(event);

        if (this.cancel)
        {
            this.container.cancel();
            this.player.sendMessage(Language.translate("ui.report.cancelled", this.player).color(NamedTextColor.RED));
        }
    }
}
