package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.aww.Domain;
import net.azisaba.vanilife.aww.WebElement;
import net.azisaba.vanilife.aww.WebPage;
import net.azisaba.vanilife.util.Typing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlendUI extends ChestUI
{
    private final WebPage page;
    private final Mode mode;

    private final boolean export;

    public BlendUI(@NotNull Player player, @NotNull WebPage page)
    {
        this(player, page, Mode.BIND);
    }

    private BlendUI(@NotNull Player player, @NotNull WebPage page, @NotNull Mode mode)
    {
        super(player, Bukkit.createInventory(null, 45, Component.text(Domain.parent(page.getName()))));

        this.page = page;
        this.mode = mode;

        this.export = this.mode == Mode.DESIGN;

        ItemStack returnStack = new ItemStack(Material.ARROW);
        ItemMeta returnMeta = returnStack.getItemMeta();
        returnMeta.displayName(Language.translate("ui.return", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        returnMeta.lore(List.of(Language.translate("ui.return.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        returnStack.setItemMeta(returnMeta);
        this.inventory.setItem(39, returnStack);

        ItemStack closeStack = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(40, closeStack);

        ItemStack modeStack = new ItemStack(this.mode == Mode.BIND ? Material.COMPARATOR : Material.PAINTING);
        ItemMeta modeMeta = modeStack.getItemMeta();
        modeMeta.displayName(Language.translate("ui.blend.mode", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        modeMeta.lore(List.of(Language.translate("ui.blend.mode.bind", this.getPlayer()).color(this.mode == Mode.BIND ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.blend.mode.design", this.getPlayer()).color(this.mode == Mode.DESIGN ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)));
        modeStack.setItemMeta(modeMeta);
        this.inventory.setItem(41, modeStack);

        for (int i = 0; i < this.inventory.getSize() - 9; i ++)
        {
            WebElement element = this.page.getElement(i);

            if (element == null)
            {
                continue;
            }

            this.inventory.setItem(i, this.mode == Mode.BIND ? element.compile() : element.getOriginal());
        }
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event)
    {
        event.setCancelled(36 <= event.getSlot() || event.isCancelled() || this.mode != Mode.DESIGN);

        WebElement element = this.page.getElement(event.getSlot());

        if (this.mode == Mode.BIND && element != null)
        {
            new StudioUI(this.player, element);
            return;
        }

        if (event.getSlot() == 39)
        {
            new AzireUI(this.player, this.page.getDomain());
        }

        if (event.getSlot() == 40)
        {
            this.player.closeInventory();
        }

        if (event.getSlot() == 41)
        {
            this.player.playSound(this.player, Sound.UI_BUTTON_CLICK, 1.0f, 1.2f);

            new BlendUI(this.player, this.page, switch (this.mode)
            {
                case Mode.BIND -> Mode.DESIGN;
                case Mode.DESIGN -> Mode.BIND;
            });
        }
    }

    @Override
    public void onClose(@NotNull InventoryCloseEvent event)
    {
        super.onClose(event);

        if (! this.export)
        {
            return;
        }

        for (int i = 0; i < this.inventory.getSize() - 9; i ++)
        {
            WebElement element = this.page.getElement(i);
            ItemStack stack = this.inventory.getItem(i);

            if (stack == null)
            {
                this.page.removeElement(i);
                continue;
            }

            if (element == null)
            {
                element = new WebElement(this.page, new YamlConfiguration());
                element.setOriginal(stack);
                this.page.addElement(i, element);
            }
        }
    }

    private enum Mode
    {
        BIND,
        DESIGN;
    }

    private static class StudioUI extends ChestUI
    {
        private final WebElement element;

        public StudioUI(@NotNull Player player, @NotNull WebElement element)
        {
            super(player, Bukkit.createInventory(null, 45, Component.text(Domain.parent(element.getPage().getName()))));
            this.element = element;

            ItemStack previewStack = this.element.compile();
            this.inventory.setItem(4, previewStack);

            ItemStack titleStack = new ItemStack(Material.NAME_TAG);
            ItemMeta titleMeta = titleStack.getItemMeta();
            titleMeta.displayName(Language.translate("ui.blend.studio.title", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
            titleMeta.lore(List.of(Language.translate("ui.blend.studio.title.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
            titleStack.setItemMeta(titleMeta);
            this.inventory.setItem(20, titleStack);

            ItemStack loreStack = new ItemStack(Material.PAPER);
            ItemMeta loreMeta = loreStack.getItemMeta();
            loreMeta.displayName(Language.translate("ui.blend.studio.lore", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
            loreMeta.lore(List.of(Language.translate("ui.blend.studio.lore.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
            loreStack.setItemMeta(loreMeta);
            this.inventory.setItem(22, loreStack);

            ItemStack closeStack = new ItemStack(Material.ARROW);
            ItemMeta closeMeta = closeStack.getItemMeta();
            closeMeta.displayName(Language.translate("ui.return", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
            closeMeta.lore(List.of(Language.translate("ui.return.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
            closeStack.setItemMeta(closeMeta);
            this.inventory.setItem(24, closeStack);
        }

        @Override
        public void onUiClick(@NotNull InventoryClickEvent event)
        {
            super.onUiClick(event);

            if (event.getSlot() == 20)
            {
                new AnvilUI(this.player, Language.translate("ui.blend.studio.title.title", this.player))
                {
                    @Override
                    public @NotNull String getPlaceholder()
                    {
                        return ((TextComponent) StudioUI.this.element.getTitle()).content();
                    }

                    @Override
                    protected void onTyped(@NotNull String string)
                    {
                        WebElement element = StudioUI.this.element;
                        element.setTitle(string);
                        Bukkit.getScheduler().runTaskLater(Vanilife.getPlugin(), () -> new StudioUI(this.player, element), 3L);
                    }
                };
            }

            if (event.getSlot() == 22)
            {
                this.player.sendMessage(Language.translate("ui.blend.studio.lore.description", this.player).color(NamedTextColor.GREEN));
                this.player.sendMessage(Component.text().build());
                new LoreEditor(this.player, this.element, new ArrayList<>());
            }

            if (event.getSlot() == 24)
            {
                new BlendUI(this.player, this.element.getPage());
            }
        }

        private static class LoreEditor extends Typing
        {
            private final WebElement element;
            private final List<String> lore;

            public LoreEditor(@NotNull Player player, @NotNull WebElement element, @NotNull List<String> lore)
            {
                super(player);
                this.element = element;
                this.lore = lore;
            }

            @Override
            public void onTyped(String string)
            {
                super.onTyped(string);

                if (string.equals("!"))
                {
                    this.element.setLore(this.lore);
                    new StudioUI(this.player, this.element);
                    return;
                }

                this.lore.add(string);
                new LoreEditor(this.player, this.element, this.lore);
            }
        }
    }
}
