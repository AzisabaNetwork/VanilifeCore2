package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.aww.Domain;
import net.azisaba.vanilife.aww.WebElement;
import net.azisaba.vanilife.aww.WebPage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class BrowserUI extends ChestUI
{
    private final String url;

    public BrowserUI(@NotNull Player player, @NotNull String url)
    {
        super(player, Bukkit.createInventory(null, 54, Component.text(url)));

        this.url = url + ".yml";

        ItemStack addressStack = new ItemStack(Material.OAK_SIGN);
        ItemMeta addressMeta = addressStack.getItemMeta();
        addressMeta.displayName(Component.text(url).decoration(TextDecoration.ITALIC, false));
        addressMeta.lore(List.of(Language.translate("ui.browser.address", this.getPlayer()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        addressStack.setItemMeta(addressMeta);
        this.inventory.setItem(0, addressStack);

        ItemStack closeStack = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.browser.close", this.getPlayer()).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.browser.close.details", this.getPlayer()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(8, closeStack);

        ItemStack separatorStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta separatorMeta = separatorStack.getItemMeta();
        separatorMeta.setHideTooltip(true);
        separatorStack.setItemMeta(separatorMeta);
        this.inventory.setItem(9, separatorStack);
        this.inventory.setItem(10, separatorStack);
        this.inventory.setItem(11, separatorStack);
        this.inventory.setItem(12, separatorStack);
        this.inventory.setItem(13, separatorStack);
        this.inventory.setItem(14, separatorStack);
        this.inventory.setItem(15, separatorStack);
        this.inventory.setItem(16, separatorStack);
        this.inventory.setItem(17, separatorStack);

        String[] parts = url.split("/");

        Domain domain = Domain.getInstance(Domain.reverse(parts[0]));
        String name = parts.length == 1 ? "index.yml" : parts[1] + ".yml";

        WebPage page;

        if (domain == null || (page = domain.getPage(name)) == null)
        {
            ItemStack notFoundStack = new ItemStack(Material.STRING);
            ItemMeta notFoundMeta = notFoundStack.getItemMeta();
            notFoundMeta.displayName(Component.text("404 Not Found").color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
            notFoundStack.setItemMeta(notFoundMeta);
            this.inventory.setItem(31, notFoundStack);
            return;
        }

        for (int i = 18; i < this.inventory.getSize(); i ++)
        {
            WebElement element = page.getElement(i - 9 * 2);

            if (element == null)
            {
                continue;
            }

            this.inventory.setItem(i, element.compile());
        }
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);

        if (event.getSlot() == 0)
        {
            new AnvilUI(this.player, Language.translate("ui.browser.address.title", this.player))
            {
                @Override
                public @NotNull String getPlaceholder()
                {
                    String[] parts = BrowserUI.this.url.split("\\.");
                    return parts.length == 1 ? parts[0] : String.join(".", Arrays.copyOf(parts, parts.length - 1));
                }

                @Override
                protected void onTyped(@NotNull String string)
                {
                    new BrowserUI(this.player, string);
                }
            };
        }

        if (event.getSlot() == 8)
        {
            this.player.closeInventory();
        }
    }
}
