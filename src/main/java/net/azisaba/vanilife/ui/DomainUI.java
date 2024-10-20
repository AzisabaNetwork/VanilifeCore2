package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.aww.Domain;
import net.azisaba.vanilife.util.Typing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DomainUI extends ChestUI
{
    private final Domain domain;

    public DomainUI(@NotNull Player player, @NotNull Domain domain)
    {
        super(player, Bukkit.createInventory(null, 45, Component.text(domain.getUrl()).decorate(TextDecoration.BOLD)));

        this.domain = domain;

        ItemStack writeStack = new ItemStack(Material.OAK_SIGN);
        ItemMeta writeMeta = writeStack.getItemMeta();
        writeMeta.displayName(Language.translate("ui.domain.transfer", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        writeMeta.lore(List.of(Language.translate("ui.domain.transfer.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC ,false)));
        writeStack.setItemMeta(writeMeta);
        this.inventory.setItem(20, writeStack);

        ItemStack serverStack = new ItemStack(Material.COD);
        ItemMeta serverMeta = serverStack.getItemMeta();
        serverMeta.displayName(Language.translate("ui.domain.server", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        serverMeta.lore(List.of(Language.translate("ui.domain.server.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC ,false)));
        serverStack.setItemMeta(serverMeta);
        this.inventory.setItem(22, serverStack);

        ItemStack deleteStack = new ItemStack(Material.TNT_MINECART);
        ItemMeta deleteMeta = deleteStack.getItemMeta();
        deleteMeta.displayName(Language.translate("ui.domain.delete", player).color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
        deleteMeta.lore(List.of(Language.translate("ui.domain.delete.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC ,false)));
        deleteStack.setItemMeta(deleteMeta);
        this.inventory.setItem(24, deleteStack);

        ItemStack closeStack = new ItemStack(Material.ARROW);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(40, closeStack);
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);

        if (event.getSlot() == 20)
        {
            new DomainTransferUI(this.player, this.domain);
        }

        if (event.getSlot() == 22)
        {
            new AzireUI(this.player, this.domain);
        }

        if (event.getSlot() == 24)
        {
            new Typing(this.player)
            {
                private String code;

                @Override
                public void init()
                {
                    super.init();

                    this.code = this.getConfirmCode(6);

                    this.player.sendMessage(Language.translate("ui.domain.delete.check", this.player, "code=" + this.code).color(NamedTextColor.GREEN));
                }

                @Override
                public void onTyped(String string)
                {
                    super.onTyped(string);

                    if (! string.equals(this.code))
                    {
                        this.player.sendMessage(Language.translate("ui.domain.delete.cancelled", this.player).color(NamedTextColor.RED));
                        return;
                    }

                    DomainUI.this.domain.delete();

                    this.player.sendMessage(Language.translate("ui.domain.delete.deleted", this.player).color(NamedTextColor.GREEN));
                }
            };
        }

        if (event.getSlot() == 40)
        {
            this.player.closeInventory();
        }
    }
}
