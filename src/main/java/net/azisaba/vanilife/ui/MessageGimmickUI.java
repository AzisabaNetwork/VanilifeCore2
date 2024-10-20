package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.gimmick.MessageGimmick;
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

public class MessageGimmickUI extends ChestUI
{
    private final MessageGimmick gimmick;

    public MessageGimmickUI(@NotNull Player player, @NotNull MessageGimmick gimmick)
    {
        super(player, Bukkit.createInventory(null, 45, Language.translate("ui.gimmick.message.title", player)));

        this.gimmick = gimmick;

        ItemStack writeStack = new ItemStack(Material.PAPER);
        ItemMeta writeMeta = writeStack.getItemMeta();
        writeMeta.displayName(Language.translate("ui.gimmick.message.write", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        writeMeta.lore(List.of(Language.translate("ui.gimmick.message.write.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC ,false)));
        writeStack.setItemMeta(writeMeta);
        this.inventory.setItem(22, writeStack);

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

        if (event.getSlot() == 22)
        {
            new AnvilUI(this.player, Language.translate("ui.gimmick.message.write.title", this.player))
            {
                @Override
                public @NotNull String getPlaceholder()
                {
                    return MessageGimmickUI.this.gimmick.getMessage();
                }

                @Override
                protected void onTyped(@NotNull String string)
                {
                    MessageGimmickUI.this.gimmick.setMessage(string);
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
