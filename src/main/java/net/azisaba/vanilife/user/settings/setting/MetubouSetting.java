package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.util.HeadUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MetubouSetting extends AbstractToggleSetting
{
    @Override
    public String getName()
    {
        return "metubou";
    }

    @Override
    public ItemStack getFavicon()
    {
        ItemStack faviconStack = HeadUtility.getPlayerHead("metubou");
        ItemMeta faviconMeta = faviconStack.getItemMeta();

        faviconMeta.displayName(Component.text("(*'▽')").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        faviconMeta.lore(this.getLore(Component.text("チャットの先頭に「(*'▽')」を挿入するよ！").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text("(*'▽') metubouさんはばにらいふ！のアイドルなのですっ！").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false)));

        faviconStack.setItemMeta(faviconMeta);
        return faviconStack;
    }
}
