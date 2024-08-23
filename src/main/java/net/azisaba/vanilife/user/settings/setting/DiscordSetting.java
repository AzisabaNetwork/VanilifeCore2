package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.HeadUtility;
import net.azisaba.vanilife.util.Typing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class DiscordSetting extends AbstractScopeSetting
{
    @Override
    public String getName()
    {
        return "discord";
    }

    @Override
    public ItemStack getFavicon()
    {
        ItemStack faviconStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta faviconMeta = (SkullMeta) faviconStack.getItemMeta();

        faviconMeta.displayName(Component.text("Discord").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        faviconMeta.lore(this.getLore(Component.text("左クリック: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text("プロフィールでの公開範囲を変更").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)),
                Component.text("右クリック: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text("Discord ハンドルを変更").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))));
        faviconMeta.setPlayerProfile(HeadUtility.getPlayerProfile(HeadUtility.DISCORD));
        faviconStack.setItemMeta(faviconMeta);

        return faviconStack;
    }

    @Override
    public void onRightClick(InventoryClickEvent event)
    {
        new Typing((Player) event.getWhoClicked())
        {
            @Override
            public void init()
            {
                this.player.sendMessage(Component.text("Discord ハンドルを送信してください:").color(NamedTextColor.GREEN));
                this.player.sendMessage(Component.text("「:」を送信してキャンセル、「!」を入力して削除します").color(NamedTextColor.YELLOW));
            }

            @Override
            public void onTyped(String string)
            {
                super.onTyped(string);

                if (string.equals(":"))
                {
                    this.player.sendMessage(Component.text("操作をキャンセルしました").color(NamedTextColor.RED));
                    return;
                }

                if (string.equals("!"))
                {
                    user.setDiscord(null);
                    this.player.sendMessage(Component.text("Discord を Profile から削除しました").color(NamedTextColor.RED));
                    return;
                }


                string = string.startsWith("@") ? string.substring(1) : string;

                if (string.length() <= 30)
                {
                    User.getInstance(this.player).setDiscord(string);
                    this.player.sendMessage(Component.text("Discord ハンドルを設定しました").color(NamedTextColor.GREEN));
                }
                else
                {
                    this.player.sendMessage(Component.text(String.format("%s は無効な Discord ハンドルです", string)).color(NamedTextColor.RED));
                }
            }
        };
    }
}
