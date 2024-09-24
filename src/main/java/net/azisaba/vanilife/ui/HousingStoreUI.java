package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.housing.Housing;
import net.azisaba.vanilife.housing.pack.HousingPacks;
import net.azisaba.vanilife.housing.pack.IHousingPack;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousingStoreUI extends InventoryUI
{
    private static final List<IHousingPack> products = HousingPacks.registry();

    private final int page;

    private final Map<Integer, IHousingPack> slots = new HashMap<>();

    public HousingStoreUI(@NotNull Player player)
    {
        this(player, 0);
    }

    public HousingStoreUI(@NotNull Player player, int page)
    {
        super(player, Bukkit.createInventory(null, 54, Language.translate("ui.housing-store.title", player)));

        this.page = page;

        final User user = User.getInstance(player);
        final Housing housing = user.getHousing();

        ItemStack backStack = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backStack.getItemMeta();
        backMeta.displayName(Language.translate("ui.back", player).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        backMeta.lore(List.of(Language.translate("ui.back.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        backStack.setItemMeta(backMeta);
        this.inventory.setItem(45, backStack);

        ItemStack nextStack = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextStack.getItemMeta();
        nextMeta.displayName(Language.translate("ui.next", player).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        nextMeta.lore(List.of(Language.translate("ui.next.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        nextStack.setItemMeta(nextMeta);
        this.inventory.setItem(53, nextStack);

        ItemStack returnStack = new ItemStack(Material.ARROW);
        ItemMeta returnMeta = returnStack.getItemMeta();
        returnMeta.displayName(Language.translate("ui.return", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        returnMeta.lore(List.of(Language.translate("ui.return.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        returnStack.setItemMeta(returnMeta);
        this.registerListener(48, returnStack, "vanilife:store", ExecutionType.CLIENT);

        ItemStack closeStack = new ItemStack(Material.ANVIL);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(49, closeStack);

        int i = 0;
        int[] slots = new int[] {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

        for (IHousingPack pack : HousingStoreUI.products.subList(this.page * 21, Math.min((this.page + 1) * 21, HousingStoreUI.products.size())))
        {
            ItemStack stack = new ItemStack(pack.getIcon());

            ItemMeta meta = stack.getItemMeta();

            final String translation = "housing.pack." + pack.getName();

            meta.displayName(Language.translate(translation + ".name", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));

            List<Component> lore = pack.getDetails(Language.getInstance(player));

            lore.add(Component.text().build());

            if (housing.has(pack))
            {
                meta.addEnchant(Enchantment.INFINITY, 1, false);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                lore.add(Language.translate("ui.housing-store.already", player).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
            }

            meta.lore(lore);
            stack.setItemMeta(meta);
            this.inventory.setItem(slots[i], stack);

            this.slots.put(slots[i], pack);

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
            new HousingStoreUI(this.player, Math.max(this.page - 1, 0));
        }

        if (event.getSlot() == 49)
        {
            this.player.closeInventory();
        }

        if (event.getSlot() == 53)
        {
            new HousingStoreUI(this.player, Math.min(this.page + 1, HousingStoreUI.products.size() / 21));
        }

        IHousingPack pack = this.slots.get(event.getSlot());

        if (pack == null)
        {
            return;
        }

        Housing housing = Housing.getInstance(this.player);

        if (housing.has(pack))
        {
            this.player.sendMessage(Language.translate("housing.pack.already", this.player).color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(this.player);

        if (user.getMola() < pack.getCost())
        {
            this.player.sendMessage(Language.translate("msg.shortage", this.player, "need=" + (pack.getCost() - user.getMola())).color(NamedTextColor.RED));
            this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
            return;
        }

        this.player.closeInventory();

        this.player.sendMessage(Language.translate("housing.pack.bought", this.player, "pack=" + pack.getName()));
        this.player.playSound(this.player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);

        housing.addPack(pack);
        user.setMola(user.getMola() - pack.getCost());

        new HousingStoreUI(this.player);
    }
}
