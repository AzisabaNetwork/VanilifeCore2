package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.HeadUtility;
import net.azisaba.vanilife.util.Typing;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class YouTubeSetting extends ScopeSetting
{
    public YouTubeSetting(@NotNull User user)
    {
        super(user);
    }

    @Override
    public @NotNull String getName()
    {
        return "youtube";
    }

    @Override
    public @NotNull ItemStack getIcon()
    {
        ItemStack iconStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta iconMeta = (SkullMeta) iconStack.getItemMeta();
        iconMeta.setPlayerProfile(HeadUtility.getPlayerProfile(HeadUtility.YOUTUBE));
        iconStack.setItemMeta(iconMeta);
        return iconStack;
    }

    @Override
    public void onRightClick(InventoryClickEvent event)
    {
        new Typing((Player) event.getWhoClicked())
        {
            @Override
            public void init()
            {
                this.player.sendMessage(Language.translate("settings.youtube.pls-send-handle", this.player).color(NamedTextColor.GREEN));
                this.player.sendMessage(Language.translate("settings.youtube.pls-send-handle.details", this.player).color(NamedTextColor.YELLOW));
            }

            @Override
            public void onTyped(String string)
            {
                super.onTyped(string);

                if (string.equals(":"))
                {
                    this.player.sendMessage(Language.translate("settings.youtube.cancelled", this.player).color(NamedTextColor.RED));
                    return;
                }

                if (string.equals("!"))
                {
                    user.setYouTube(null);
                    this.player.sendMessage(Language.translate("settings.youtube.deleted", this.player).color(NamedTextColor.RED));
                    return;
                }

                string = string.startsWith("@") ? string.substring(1) : string;

                if (string.length() <= 30)
                {
                    User.getInstance(this.player).setYouTube(string);
                    this.player.sendMessage(Language.translate("settings.youtube.changed", this.player).color(NamedTextColor.GREEN));
                }
                else
                {
                    this.player.sendMessage(Language.translate("settings.youtube.invalid", this.player).color(NamedTextColor.RED));
                }
            }
        };
    }
}
