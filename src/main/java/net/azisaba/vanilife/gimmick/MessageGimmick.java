package net.azisaba.vanilife.gimmick;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.ui.MessageGimmickUI;
import net.azisaba.vanilife.util.ComponentUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class MessageGimmick extends VanilifeGimmick
{
    public static @NotNull ItemStack getItemStack()
    {
        ItemStack messageStack = new ItemStack(Material.COMMAND_BLOCK);

        ItemMeta messageMeta = messageStack.getItemMeta();
        messageMeta.displayName(Component.text("ギミックブロック").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false)
                .appendSpace()
                .append(Component.text("(メッセージ)").color(NamedTextColor.GRAY)));
        messageMeta.getPersistentDataContainer().set(new NamespacedKey(Vanilife.getPlugin(), "gimmick.type"), PersistentDataType.STRING, "message");

        messageStack.setItemMeta(messageMeta);
        return messageStack;
    }

    private String message;

    public MessageGimmick(@NotNull Block block)
    {
        super(block);

        this.message = this.readString("message");

        if (this.message == null)
        {
            this.message = "メッセージが設定されていません！";
        }
    }

    @Override
    public @NotNull String getType()
    {
        return "message";
    }

    @Override
    public ItemStack getDrop()
    {
        return MessageGimmick.getItemStack();
    }

    public @NotNull String getMessage()
    {
        return this.message;
    }

    public void setMessage(@NotNull String message)
    {
        this.message = message;
        this.write("message", this.message);
    }

    @Override
    public void use(@NotNull PlayerInteractEvent event)
    {
        new MessageGimmickUI(event.getPlayer(), this);
    }

    @Override
    public void run(@NotNull Player player)
    {
        player.sendMessage(Component.text().append(Component.text("[G]").color(TextColor.color(17, 148, 240)).hoverEvent(HoverEvent.showText(Language.translate("gimmick.message.attention", player))))
                .appendSpace()
                .resetStyle()
                .append(ComponentUtility.asComponent(this.message))
                .build());
    }
}
