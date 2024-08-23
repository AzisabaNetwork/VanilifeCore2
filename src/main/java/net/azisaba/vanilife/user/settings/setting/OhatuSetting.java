package net.azisaba.vanilife.user.settings.setting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OhatuSetting extends AbstractToggleSetting
{
    @Override
    public String getName()
    {
        return "ohatu";
    }

    @Override
    public ItemStack getFavicon()
    {
        ItemStack faviconStack = new ItemStack(Material.COOKIE);
        ItemMeta faviconMeta = faviconStack.getItemMeta();

        faviconMeta.displayName(Component.text("お初").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        faviconMeta.lore(this.getLore(Component.text("はじめてのプレイヤーに自動的にお初します").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text("メッセージはランダムに選択されます！").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false)));

        faviconStack.setItemMeta(faviconMeta);
        return faviconStack;
    }
}
