package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.Typing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BioSetting implements ISetting
{
    private User user;

    @Override
    public void init(User user)
    {
        this.user = user;
    }

    @Override
    public String getName()
    {
        return "bio";
    }

    @Override
    public ItemStack getFavicon()
    {
        ItemStack faviconStack = new ItemStack(Material.BOOK);
        ItemMeta faviconMeta = faviconStack.getItemMeta();
        faviconMeta.displayName(Component.text("bio").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        faviconMeta.lore(List.of(Component.text("プロフィールに表示する bio を設定する").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        faviconStack.setItemMeta(faviconMeta);
        return faviconStack;
    }

    @Override
    public void onClick(InventoryClickEvent event)
    {
        new Typing((Player) event.getWhoClicked())
        {
            @Override
            public void init()
            {
                this.player.sendMessage(Component.text("bio を送信してください:").color(NamedTextColor.GREEN));
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
                    user.setBio(null);
                    this.player.sendMessage(Component.text("bio を Profile から削除しました").color(NamedTextColor.RED));
                    return;
                }

                if (120 < string.length())
                {
                    this.player.sendMessage(Component.text("bio は120文字以内で設定してください").color(NamedTextColor.RED));
                    return;
                }

                user.setBio(string);
                this.player.sendMessage(Component.text("bio を更新しました").color(NamedTextColor.GREEN));
            }
        };
    }

    @Override
    public void save()
    {

    }
}
