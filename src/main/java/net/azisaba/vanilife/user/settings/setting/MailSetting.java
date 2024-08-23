package net.azisaba.vanilife.user.settings.setting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MailSetting extends AbstractScopeSetting
{
    @Override
    public String getName()
    {
        return "mail";
    }

    @Override
    public ItemStack getFavicon()
    {
        ItemStack faviconStack = new ItemStack(Material.PAPER);
        ItemMeta faviconMeta = faviconStack.getItemMeta();

        faviconMeta.displayName(Component.text("Mail").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        faviconMeta.lore(this.getLore(Component.text("あなたに Mail を送信できるプレイヤーの範囲").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        faviconStack.setItemMeta(faviconMeta);

        return faviconStack;
    }
}
