package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.aww.Domain;
import net.azisaba.vanilife.aww.WebPage;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AzireUI extends ChestUI
{
    private final Domain domain;

    private final int page;

    private final List<WebPage> pages;
    private final Map<Integer, WebPage> slots = new HashMap<>();

    public AzireUI(@NotNull Player player, @NotNull Domain domain)
    {
        this(player, domain, 0);
    }

    public AzireUI(@NotNull Player player, @NotNull Domain domain, int page)
    {
        super(player, Bukkit.createInventory(null, 54, Language.translate("ui.azire.title", player)));

        this.domain = domain;
        this.page = page;
        this.pages = this.domain.getPages();

        int i = 0;
        int[] slots = new int[] {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

        for (WebPage document : this.pages.subList(this.page * 21, Math.min((this.page + 1) * 21, this.pages.size())))
        {
            String name = document.getName();
            String[] parts = name.split("\\.");

            if (1 < parts.length)
            {
                name = String.join(".", Arrays.copyOf(parts, parts.length - 1));
            }

            ItemStack stack = new ItemStack(Material.PAPER);
            ItemMeta meta = stack.getItemMeta();
            meta.displayName(Component.text(name).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(Language.translate("ui.azire.left", this.player).decoration(TextDecoration.ITALIC, false),
                    Language.translate("ui.azire.right", this.player).decoration(TextDecoration.ITALIC, false),
                    Language.translate("ui.azire.right.shift", this.player).decoration(TextDecoration.ITALIC, false)));
            stack.setItemMeta(meta);
            this.inventory.setItem(slots[i], stack);

            this.slots.put(slots[i], document);

            i ++;
        }

        ItemStack backStack = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backStack.getItemMeta();
        backMeta.displayName(Language.translate("ui.back", this.player).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        backMeta.lore(List.of(Language.translate("ui.back.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        backStack.setItemMeta(backMeta);
        this.inventory.setItem(45, backStack);

        ItemStack returnStack = new ItemStack(Material.ARROW);
        ItemMeta returnMeta = returnStack.getItemMeta();
        returnMeta.displayName(Language.translate("ui.return", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        returnMeta.lore(List.of(Language.translate("ui.return.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        returnStack.setItemMeta(returnMeta);
        this.inventory.setItem(48, returnStack);

        ItemStack closeStack = new ItemStack(Material.ANVIL);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", this.player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(49, closeStack);

        ItemStack createStack = new ItemStack(Material.OAK_SIGN);
        ItemMeta createMeta = createStack.getItemMeta();
        createMeta.displayName(Language.translate("ui.azire.create", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        createMeta.lore(List.of(Language.translate("ui.azire.create.details.1", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.azire.create.details.2", this.player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
        createStack.setItemMeta(createMeta);
        this.inventory.setItem(50, createStack);

        ItemStack nextStack = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextStack.getItemMeta();
        nextMeta.displayName(Language.translate("ui.next", this.player).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        nextMeta.lore(List.of(Language.translate("ui.next.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        nextStack.setItemMeta(nextMeta);
        this.inventory.setItem(53, nextStack);
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);

        User user = User.getInstance(this.player);

        if (event.getSlot() == 45)
        {
            new AzireUI(this.player, this.domain, Math.max(this.page - 1, 0));
        }

        if (event.getSlot() == 48)
        {
            new DomainUI(this.player, this.domain);
        }

        if (event.getSlot() == 49)
        {
            this.player.closeInventory();
        }

        if (event.getSlot() == 50)
        {
            if (user.getMola() < Vanilife.MOLA_AZIRE)
            {
                this.player.sendMessage(Language.translate("msg.shortage", this.player, "need=" + (150 - user.getMola())).color(NamedTextColor.RED));
                this.player.playSound(this.player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            }

            new AnvilUI(this.player, Language.translate("ui.azire.create.title", this.player))
            {
                @Override
                public @NotNull String getPlaceholder()
                {
                    return "index";
                }

                @Override
                protected void onTyped(@NotNull String string)
                {
                    Domain domain = AzireUI.this.domain;

                    if (domain.getPage(string + ".yml") != null || ! Vanilife.pattern2.matcher(string).matches())
                    {
                        this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                    }
                    else
                    {
                        domain.addPage(new WebPage(domain, string + ".yml"));
                        user.setMola(user.getMola() - 150);
                    }

                    new AzireUI(this.player, domain);
                }
            };
        }

        if (event.getSlot() == 53)
        {
            new AzireUI(this.player, this.domain, Math.min(this.page + 1, this.pages.size() / 21));
        }

        WebPage page = this.slots.get(event.getSlot());

        if (page == null)
        {
            return;
        }

        if (event.isShiftClick() && event.isRightClick())
        {
            new ConfirmUI(this.player, () -> this.domain.removePage(page), () -> new AzireUI(this.player, this.domain, this.page));
            return;
        }

        if (event.isRightClick())
        {
            new AnvilUI(this.player, Language.translate("ui.azire.rename.title", this.player))
            {
                @Override
                public @NotNull String getPlaceholder()
                {
                    return Domain.parent(page.getName());
                }

                @Override
                protected void onTyped(@NotNull String string)
                {
                    if (domain.getPage(string + ".yml") != null || ! Vanilife.pattern2.matcher(string).matches())
                    {
                        this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                    }
                    else
                    {
                        page.setName(string + ".yml");
                        this.player.playSound(this.player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
                    }

                    new AzireUI(this.player, domain);
                }
            };

            return;
        }

        new BlendUI(this.player, page);
    }
}
