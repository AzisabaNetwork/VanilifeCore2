package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.gimmick.TitleGimmick;
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

import java.util.List;

public class TitleGimmickUI extends ChestUI
{
    private final TitleGimmick gimmick;

    public TitleGimmickUI(@NotNull Player player, @NotNull TitleGimmick gimmick)
    {
        super(player, Bukkit.createInventory(null, 45, Language.translate("ui.gimmick.message.title", player)));

        this.gimmick = gimmick;

        ItemStack titleStack = new ItemStack(Material.PAPER);
        ItemMeta titleMeta = titleStack.getItemMeta();
        titleMeta.displayName(Language.translate("ui.gimmick.title.subject", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        titleMeta.lore(List.of(Language.translate("ui.gimmick.title.subject.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC ,false)));
        titleStack.setItemMeta(titleMeta);
        this.inventory.setItem(20, titleStack);

        ItemStack subtitleStack = new ItemStack(Material.PAPER);
        ItemMeta subtitleMeta = subtitleStack.getItemMeta();
        subtitleMeta.displayName(Language.translate("ui.gimmick.title.subtitle", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        subtitleMeta.lore(List.of(Language.translate("ui.gimmick.title.subtitle.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC ,false)));
        subtitleStack.setItemMeta(subtitleMeta);
        this.inventory.setItem(24, subtitleStack);

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
            new AnvilUI(this.player, Language.translate("ui.gimmick.title.subject.title", this.player))
            {
                @Override
                public @NotNull String getPlaceholder()
                {
                    return TitleGimmickUI.this.gimmick.getTitle();
                }

                @Override
                protected void onTyped(@NotNull String string)
                {
                    TitleGimmickUI.this.gimmick.setTitle(string);
                }
            };
        }

        if (event.getSlot() == 24)
        {
            new AnvilUI(this.player, Language.translate("ui.gimmick.title.subtitle.title", this.player))
            {
                @Override
                public @NotNull String getPlaceholder()
                {
                    return TitleGimmickUI.this.gimmick.getSubtitle();
                }

                @Override
                protected void onTyped(@NotNull String string)
                {
                    TitleGimmickUI.this.gimmick.setSubtitle(string);
                }
            };
        }

        if (event.getSlot() == 40)
        {
            this.player.closeInventory();
        }
    }

    @Override
    public void onClose(@NotNull InventoryCloseEvent event)
    {
        super.onClose(event);
    }
}
