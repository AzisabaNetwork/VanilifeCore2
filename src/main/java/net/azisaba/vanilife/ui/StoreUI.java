package net.azisaba.vanilife.ui;

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

    private int page;

    public StoreUI(@NotNull Player player)
    {
        super(player, Bukkit.createInventory(null, 45, Component.text("Store")));

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
        backMeta.displayName(Component.text("戻る").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        backMeta.lore(List.of(Component.text("前のページに戻ります").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        backStack.setItemMeta(backMeta);
        this.inventory.setItem(18, backStack);

        ItemStack nextStack = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextStack.getItemMeta();
        nextMeta.displayName(Component.text("次へ").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        nextMeta.lore(List.of(Component.text("次のページに進みます").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        nextStack.setItemMeta(nextMeta);
        this.inventory.setItem(26, nextStack);

        ItemStack closeStack = new ItemStack(Material.OAK_DOOR);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Component.text("閉じる").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Component.text("この画面を閉じます").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(44, closeStack);

        User user = User.getInstance(this.player);

        int i = 0;
        int[] slots = new int[] {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

        for (ISubscription product : StoreUI.products.subList(this.page * 9, Math.min((this.page + 1) * 9, StoreUI.products.size())))
        {
            ItemStack stack = new ItemStack(product.getFavicon());

            ItemMeta meta = stack.getItemMeta();
            meta.displayName(Component.text(product.getDisplayName()).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));

            List<Component> lore = new ArrayList<>();

            for (String row : product.getDescription())
            {
                lore.add(Component.text(row).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
            }

            lore.add(Component.text().build());

            if (! user.hasSubscription(product) && SubscriptionUtility.getProgress() != 0.0)
            {
                double rest = 1.0d - SubscriptionUtility.getProgress();
                int cost = (int) (product.getCost() * rest);

                lore.add(Component.text().build());
                lore.add(Component.text("月額: ").color(NamedTextColor.GRAY).decorate(TextDecoration.STRIKETHROUGH).decoration(TextDecoration.ITALIC, false).append(Component.text(product.getCost() + " Mola").color(NamedTextColor.GREEN)));
                lore.add(Component.text(String.format("今月: %s%% OFF (%s Mola)", (int) (SubscriptionUtility.getProgress() * 100), cost)).color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
            }
            else
            {
                lore.add(Component.text("月額: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text(product.getCost() + " Mola").color(NamedTextColor.GREEN)));
            }

            if (user.hasSubscription(product))
            {
                lore.add(Component.text().build());
                lore.add(Component.text("購入済み").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
                lore.add(Component.text("クリックしてサブスクリプションを解約").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            }

            meta.lore(lore);

            stack.setItemMeta(meta);

            this.inventory.setItem(slots[i], stack);

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
            this.page = Math.max(this.page - 1, 0);
        }

        if (event.getSlot() == 26)
        {
            this.page = Math.min(this.page + 1, StoreUI.products.size() - 1);
        }

        if (event.getSlot() == 44)
        {
            this.player.closeInventory();
        }
    }
}
