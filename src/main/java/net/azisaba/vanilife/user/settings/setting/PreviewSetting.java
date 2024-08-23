package net.azisaba.vanilife.user.settings.setting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PreviewSetting extends AbstractToggleSetting
{
    public String getName()
    {
        return "preview";
    }

    @Override
    public ItemStack getFavicon()
    {
        ItemStack faviconStack = new ItemStack(Material.COMPARATOR);
        ItemMeta faviconMeta = faviconStack.getItemMeta();

        faviconMeta.displayName(Component.text("実験的機能").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        faviconMeta.lore(this.getLore(Component.text("実験的な機能を利用しますか？").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text("実験的機能は不安定で、削除される可能性があります").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));

        faviconStack.setItemMeta(faviconMeta);
        return faviconStack;
    }
}
