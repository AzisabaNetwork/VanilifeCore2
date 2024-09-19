package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.subscription.ISubscription;
import net.azisaba.vanilife.user.subscription.Subscriptions;
import net.azisaba.vanilife.util.SubscriptionUtility;
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

import java.util.ArrayList;
import java.util.List;

public class StoreUI extends InventoryUI
{
    private static final List<ISubscription> products = Subscriptions.products();

    private final int page;

    public StoreUI(@NotNull Player player)
    {
        this(player, 0);
    }

    public StoreUI(@NotNull Player player, int page)
    {
        super(player, Bukkit.createInventory(null, 45, Language.translate("ui.store.title", player)));

        this.page = page;

        ItemStack wallStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta wallMeta = wallStack.getItemMeta();
        wallMeta.displayName(Component.text(""));
        wallMeta.setHideTooltip(true);
        wallStack.setItemMeta(wallMeta);

        for (int i : List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 36, 37, 38, 39, 40, 41, 42, 43))
        {
            this.inventory.setItem(i, wallStack);
        }

        ItemStack backStack = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backStack.getItemMeta();
        backMeta.displayName(Language.translate("ui.back", player).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        backMeta.lore(List.of(Language.translate("ui.back.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        backStack.setItemMeta(backMeta);
        this.inventory.setItem(18, backStack);

        ItemStack nextStack = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextStack.getItemMeta();
        nextMeta.displayName(Language.translate("ui.next", player).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        nextMeta.lore(List.of(Language.translate("ui.next.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        nextStack.setItemMeta(nextMeta);
        this.inventory.setItem(26, nextStack);

        ItemStack closeStack = new ItemStack(Material.OAK_DOOR);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(44, closeStack);

        User user = User.getInstance(this.player);

        int i = 0;
        int[] slots = new int[] {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

        for (ISubscription product : StoreUI.products.subList(this.page * 9, Math.min((this.page + 1) * 9, StoreUI.products.size())))
        {
            ItemStack stack = new ItemStack(product.getIcon());

            ItemMeta meta = stack.getItemMeta();

            final String translation = "subscription." + product.getName();

            meta.displayName(Language.translate(translation + ".name", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));

            List<Component> lore = new ArrayList<>();

            if (Language.has(translation + ".description.1", player))
            {
                int j = 1;

                while (Language.has(translation + ".description." + j, player))
                {
                    lore.add(Language.translate(translation + ".description." + j, player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));

                    j ++;
                }

                lore.add(Component.text().build());
            }

            if (! user.hasSubscription(product) && SubscriptionUtility.getProgress() != 0.0)
            {
                double rest = 1.0d - SubscriptionUtility.getProgress();
                int cost = (int) (product.getCost() * rest);

                lore.add(Component.text().build());
                lore.add(Language.translate("ui.store.monthly-amount", player).color(NamedTextColor.GRAY).decorate(TextDecoration.STRIKETHROUGH).decoration(TextDecoration.ITALIC, false).append(Component.text(product.getCost() + " Mola").color(NamedTextColor.GREEN)));
                lore.add(Language.translate("ui.store.discount", player, "rate=" + (int) (SubscriptionUtility.getProgress() * 100), "cost=" + cost).color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
            }
            else
            {
                lore.add(Language.translate("ui.store.monthly-amount", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text(product.getCost() + " Mola").color(NamedTextColor.GREEN)));
            }

            if (user.hasSubscription(product))
            {
                lore.add(Component.text().build());
                lore.add(Language.translate("ui.store.already-bought", player).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
                lore.add(Language.translate("ui.store.unsubscribe", player).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            }

            meta.lore(lore);

            stack.setItemMeta(meta);

            this.registerListener(slots[i], stack, (user.hasSubscription(product) ? "vanilife:unsubscribe " : "vanilife:subscribe ") + product.getName(), ExecutionType.CLIENT);

            i ++;
        }
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);
        event.setCancelled(true);

        if (event.getSlot() == 18)
        {
            new StoreUI(this.player, Math.max(this.page - 1, 0));
        }

        if (event.getSlot() == 26)
        {
            new StoreUI(this.player, Math.min(this.page + 1, StoreUI.products.size() / 21));
        }

        if (event.getSlot() == 44)
        {
            this.player.closeInventory();
        }
    }
}
