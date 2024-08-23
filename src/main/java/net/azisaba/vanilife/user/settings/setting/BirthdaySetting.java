package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.Vanilife;
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

import java.text.ParseException;
import java.util.Date;

public class BirthdaySetting extends AbstractScopeSetting
{
    @Override
    public String getName()
    {
        return "birthday";
    }

    @Override
    public ItemStack getFavicon()
    {
        ItemStack faviconStack = new ItemStack(Material.CAKE);
        ItemMeta faviconMeta = faviconStack.getItemMeta();

        faviconMeta.displayName(Component.text("お誕生日").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        faviconMeta.lore(this.getLore(Component.text("左クリック: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text("プロフィールでの公開範囲を変更").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)),
                Component.text("右クリック: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text("お誕生日を変更").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)),
                Component.text("プロフィールには西暦は表示されません！").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false)));

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
                this.player.sendMessage(Component.text("お誕生日を「yyyy/MM/dd」の形式で送信してください:").color(NamedTextColor.GREEN));
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
                    user.setBirthday(null);
                    this.player.sendMessage(Component.text("お誕生日を Profile から削除しました").color(NamedTextColor.RED));
                    return;
                }

                if (Vanilife.pattern1.matcher(string).matches())
                {
                    try
                    {
                        Date birthday = Vanilife.sdf2.parse(string);

                        if (birthday.after(new Date()))
                        {
                            this.player.sendMessage(Component.text(String.format("%s は無効な日付です", string)).color(NamedTextColor.RED));
                            return;
                        }

                        User.getInstance(this.player).setBirthday(birthday);
                    }
                    catch (ParseException e)
                    {
                        throw new RuntimeException(e);
                    }

                    this.player.sendMessage(Component.text("お誕生日を設定しました").color(NamedTextColor.GREEN));
                }
                else
                {
                    this.player.sendMessage(Component.text("yyyy/MM/dd の形式で入力してください").color(NamedTextColor.RED));
                }

            }
        };
    }
}
