package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.report.ReportDataContainer;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReportAttachUI extends ChestUI
{
    private final ReportDataContainer container;
    private final Location location;

    public ReportAttachUI(@NotNull Player player, @NotNull ReportDataContainer container, @NotNull Location location)
    {
        super(player, Bukkit.createInventory(null, 27, Language.translate("ui.report-attach.title", player)));

        this.container = container;
        this.location = location;

        ItemStack yesStack = new ItemStack(Material.PAINTING);
        ItemMeta yesMeta = yesStack.getItemMeta();
        yesMeta.displayName(Language.translate(this.container.contains(this.location) ? "ui.report-attach.remove" : "ui.report-attach.add", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        yesMeta.lore(List.of(Language.translate(this.container.contains(this.location) ? "ui.report-attach.remove.details" : "ui.report-attach.add.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        yesStack.setItemMeta(yesMeta);
        this.inventory.setItem(11, yesStack);

        ItemStack cancelStack = new ItemStack(Material.STRING);
        ItemMeta cancelMeta = cancelStack.getItemMeta();
        cancelMeta.displayName(Language.translate("ui.report-attach.cancel", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        cancelMeta.lore(List.of(Language.translate("ui.report-attach.cancel.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        cancelStack.setItemMeta(cancelMeta);
        this.inventory.setItem(13, cancelStack);

        ItemStack submitStack = new ItemStack(Material.OAK_SIGN);
        ItemMeta submitMeta = submitStack.getItemMeta();
        submitMeta.displayName(Language.translate("ui.report-attach.submit", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        submitMeta.lore(List.of(Language.translate("ui.report-attach.submit.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        submitStack.setItemMeta(submitMeta);
        this.inventory.setItem(15, submitStack);

        ItemStack closeStack = new ItemStack(Material.ARROW);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", this.player).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(22, closeStack);
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);

        if (event.getSlot() == 11)
        {
            if (this.container.contains(this.location))
            {
                this.container.removeLocation(this.location);
                this.player.sendMessage(Language.translate("ui.report-attach.removed", this.player).color(NamedTextColor.RED).decorate(TextDecoration.BOLD));
            }
            else
            {
                if (10 <= this.container.getLocations().size() + 1)
                {
                    this.player.closeInventory();
                    this.player.sendMessage(Language.translate("ui.report.attach.cant", this.player).color(NamedTextColor.RED));
                    this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                    return;
                }

                this.container.addLocation(this.location);
                this.player.sendMessage(Language.translate("ui.report-attach.added", this.player).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD));
            }

            new ReportAttachUI(this.player, this.container, this.location);
        }

        if (event.getSlot() == 13)
        {
            this.container.cancel();
            this.player.closeInventory();
            this.player.sendMessage(Language.translate("ui.report.cancelled", this.player).color(NamedTextColor.RED));
        }

        if (event.getSlot() == 15)
        {
            new ReportUI(this.player, this.container);
        }

        if (event.getSlot() == 22)
        {
            this.player.closeInventory();
        }
    }
}
