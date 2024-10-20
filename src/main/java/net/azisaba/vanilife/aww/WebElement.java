package net.azisaba.vanilife.aww;

import net.azisaba.vanilife.util.ComponentUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class WebElement
{
    private final WebPage page;
    private final ConfigurationSection source;

    private ItemStack original;

    public WebElement(@NotNull WebPage page, @NotNull ConfigurationSection source)
    {
        this.page = page;
        this.source = source;
        this.original = this.source.getItemStack("original");
    }

    public @NotNull WebPage getPage()
    {
        return this.page;
    }

    public @NotNull Component getTitle()
    {
        String plane = this.source.getString("title");
        return plane != null ? Component.text().decoration(TextDecoration.ITALIC, false)
                .append(ComponentUtility.asComponent(plane)).build() : Component.text("# 未設定のタイトル").decoration(TextDecoration.ITALIC, false);
    }

    public void setTitle(String title)
    {
        this.source.set("title", title);
        this.page.export();
    }

    public @NotNull List<Component> getLore()
    {
        return this.source.getStringList("lore").stream()
                .map(row -> Component.text()
                        .color(NamedTextColor.WHITE)
                        .decoration(TextDecoration.ITALIC, false)
                        .append(ComponentUtility.asComponent(row)).build())
                .collect(Collectors.toList());
    }

    public void setLore(@NotNull List<String> lore)
    {
        this.source.set("lore", lore);
        this.page.export();
    }

    public @NotNull ItemStack getOriginal()
    {
        return this.original;
    }

    public void setOriginal(@NotNull ItemStack original)
    {
        this.original = original;
        this.source.set("original", original);
        this.page.export();
    }

    public @NotNull ConfigurationSection source()
    {
        return this.source;
    }

    public @NotNull ItemStack compile()
    {
        ItemStack stack = this.getOriginal().clone();
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(this.getTitle());
        meta.lore(this.getLore());
        stack.setItemMeta(meta);
        return stack;
    }
}
