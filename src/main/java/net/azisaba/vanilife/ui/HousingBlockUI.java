package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.housing.Housing;
import net.azisaba.vanilife.user.User;
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
import java.util.Comparator;
import java.util.List;

public class HousingBlockUI extends ChestUI
{
    private final int page;

    private final List<Material> blocks = new ArrayList<>();

    public HousingBlockUI(@NotNull Player player)
    {
        this(player, 0);
    }

    public HousingBlockUI(@NotNull Player player, int page)
    {
        super(player, Bukkit.createInventory(null, 54, Language.translate("ui.housing-block.title", player)));

        this.page = page;

        final User user = User.getInstance(player);
        final Housing housing = user.getHousing();

        housing.getPacks().forEach(pack -> this.blocks.addAll(pack.getMaterials().stream().filter(Material::isItem).toList()));
        this.blocks.sort(Comparator.comparing(Material::name));

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
        this.registerListener(48, returnStack, "vanilife:housing", ExecutionType.CLIENT);

        ItemStack closeStack = new ItemStack(Material.ANVIL);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(49, closeStack);

        ItemStack storeStack = new ItemStack(Material.CHEST);
        ItemMeta storeMeta = storeStack.getItemMeta();
        storeMeta.displayName(Language.translate("ui.housing-block.store", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        storeMeta.lore(List.of(Language.translate("ui.housing-block.store.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        storeStack.setItemMeta(storeMeta);
        this.registerListener(50, storeStack, "vanilife:store", ExecutionType.CLIENT);

        int i = 0;
        int[] slots = new int[] {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

        for (Material block : this.blocks.stream().toList().subList(this.page * 21, Math.min((this.page + 1) * 21, this.blocks.size())))
        {
            ItemStack stack = new ItemStack(block);

            ItemMeta meta = stack.getItemMeta();
            meta.displayName(Component.translatable("block.minecraft." + block.name().toLowerCase()).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
            stack.setItemMeta(meta);
            this.inventory.setItem(slots[i], stack);

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
            new HousingBlockUI(this.player, Math.max(this.page - 1, 0));
        }

        if (event.getSlot() == 49)
        {
            this.player.closeInventory();
        }

        if (event.getSlot() == 53)
        {
            new HousingBlockUI(this.player, Math.min(this.page + 1, this.blocks.size() / 21));
        }
    }
}
