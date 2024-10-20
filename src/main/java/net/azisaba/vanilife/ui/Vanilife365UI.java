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

public class Vanilife365UI extends ChestUI
{
    private static final List<ISubscription> products = Subscriptions.products();

    private final int page;

    public Vanilife365UI(@NotNull Player player)
    {
        this(player, 0);
    }

    public Vanilife365UI(@NotNull Player player, int page)
    {
        super(player, Bukkit.createInventory(null, 54, Language.translate("ui.365.title", player)));

        this.page = page;

        User user = User.getInstance(this.player);

        ItemStack backStack = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backStack.getItemMeta();
        backMeta.displayName(Language.translate("ui.back", this.player).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        backMeta.lore(List.of(Language.translate("ui.back.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        backStack.setItemMeta(backMeta);
        this.inventory.setItem(45, backStack);

        ItemStack nextStack = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextStack.getItemMeta();
        nextMeta.displayName(Language.translate("ui.next", this.player).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        nextMeta.lore(List.of(Language.translate("ui.next.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        nextStack.setItemMeta(nextMeta);
        this.inventory.setItem(53, nextStack);

        ItemStack returnStack = new ItemStack(Material.ARROW);
        ItemMeta returnMeta = returnStack.getItemMeta();
        returnMeta.displayName(Language.translate("ui.return", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        returnMeta.lore(List.of(Language.translate("ui.return.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        returnStack.setItemMeta(returnMeta);
        this.registerListener(48, returnStack, "vanilife:store", ExecutionType.CLIENT);

        ItemStack closeStack = new ItemStack(Material.ANVIL);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", this.player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(49, closeStack);

        ItemStack walletStack = new ItemStack(Material.EMERALD);
        ItemMeta walletMeta = walletStack.getItemMeta();
        walletMeta.displayName(Language.translate("ui.365.wallet", this.player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        walletMeta.lore(List.of(Component.text().build(),
                Language.translate("ui.365.wallet.mola", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text(user.getMola() + " Mola").color(NamedTextColor.GOLD))));
        walletStack.setItemMeta(walletMeta);
        this.inventory.setItem(50, walletStack);

        int i = 0;
        int[] slots = new int[] {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

        for (ISubscription product : Vanilife365UI.products.subList(this.page * 21, Math.min((this.page + 1) * 21, Vanilife365UI.products.size())))
        {
            ItemStack stack = new ItemStack(product.getIcon());

            ItemMeta meta = stack.getItemMeta();

            final String translation = "subscription." + product.getName();

            meta.displayName(Language.translate(translation + ".name", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));

            List<Component> lore = new ArrayList<>();

            if (Language.has(translation + ".description.1", this.player))
            {
                int j = 1;

                while (Language.has(translation + ".description." + j, this.player))
                {
                    lore.add(Language.translate(translation + ".description." + j, this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));

                    j ++;
                }

                lore.add(Component.text().build());
            }

            if (! user.hasSubscription(product) && SubscriptionUtility.getProgress() != 0.0)
            {
                double rest = 1.0d - SubscriptionUtility.getProgress();
                int cost = (int) (product.getCost() * rest);

                lore.add(Component.text().build());
                lore.add(Language.translate("ui.365.monthly-amount", this.player).color(NamedTextColor.GRAY).decorate(TextDecoration.STRIKETHROUGH).decoration(TextDecoration.ITALIC, false).append(Component.text(product.getCost() + " Mola").color(NamedTextColor.GREEN)));
                lore.add(Language.translate("ui.365.discount", this.player, "rate=" + (int) (SubscriptionUtility.getProgress() * 100), "cost=" + cost).color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
            }
            else
            {
                lore.add(Language.translate("ui.365.monthly-amount", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text(product.getCost() + " Mola").color(NamedTextColor.GREEN)));
            }

            if (user.hasSubscription(product))
            {
                lore.add(Component.text().build());
                lore.add(Language.translate("ui.365.already-bought", this.player).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
                lore.add(Language.translate("ui.365.unsubscribe", this.player).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
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

        if (event.getSlot() == 45)
        {
            new Vanilife365UI(this.player, Math.max(this.page - 1, 0));
        }

        if (event.getSlot() == 49)
        {
            this.player.closeInventory();
        }

        if (event.getSlot() == 53)
        {
            new Vanilife365UI(this.player, Math.min(this.page + 1, Vanilife365UI.products.size() / 21));
        }
    }
}
