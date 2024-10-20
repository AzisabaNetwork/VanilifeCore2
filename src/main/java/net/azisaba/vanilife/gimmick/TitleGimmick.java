package net.azisaba.vanilife.gimmick;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.ui.TitleGimmickUI;
import net.azisaba.vanilife.util.ComponentUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class TitleGimmick extends VanilifeGimmick
{
    private String title;
    private String subtitle;

    public static @NotNull ItemStack getItemStack()
    {
        ItemStack messageStack = new ItemStack(Material.COMMAND_BLOCK);

        ItemMeta messageMeta = messageStack.getItemMeta();
        messageMeta.displayName(Component.text("ギミックブロック").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
                .appendSpace()
                .append(Component.text("(タイトル)").color(NamedTextColor.GRAY)));
        messageMeta.getPersistentDataContainer().set(new NamespacedKey(Vanilife.getPlugin(), "gimmick.type"), PersistentDataType.STRING, "title");

        messageStack.setItemMeta(messageMeta);
        return messageStack;
    }

    public TitleGimmick(@NotNull Block block)
    {
        super(block);

        this.title = this.readString("title");

        if (this.title == null)
        {
            this.title = "タイトルが設定されていません";
        }

        this.subtitle = this.readString("subtitle");

        if (this.subtitle == null)
        {
            this.subtitle = "サブタイトルが設定されていません";
        }
    }

    @Override
    public @NotNull String getType()
    {
        return "title";
    }

    @Override
    public @NotNull ItemStack getDrop()
    {
        return TitleGimmick.getItemStack();
    }

    public @NotNull String getTitle()
    {
        return this.title;
    }

    public void setTitle(@NotNull String title)
    {
        this.title = title;
        this.write("title", this.title);
    }

    public @NotNull String getSubtitle()
    {
        return this.subtitle;
    }

    public void setSubtitle(@NotNull String subtitle)
    {
        this.subtitle = subtitle;
        this.write("subtitle", this.subtitle);
    }

    @Override
    public void use(@NotNull PlayerInteractEvent event)
    {
        new TitleGimmickUI(event.getPlayer(), this);
    }

    @Override
    public void run(@NotNull Player player)
    {
        player.showTitle(Title.title(ComponentUtility.asComponent(this.title), ComponentUtility.asComponent(this.subtitle)));
        player.sendActionBar(Component.text().append(Component.text("[G]").color(TextColor.color(17, 148, 240))
                .appendSpace()
                .append(Language.translate("gimmick.title.attention", player).color(NamedTextColor.WHITE))));
    }
}
