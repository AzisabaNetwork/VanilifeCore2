package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.util.SeasonUtility;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class WorldsUI extends InventoryUI
{
    private int page = 0;

    public WorldsUI(Player player)
    {
        super(player, Bukkit.createInventory(null, 36, Component.text("ワールド")));

        this.render();

        ItemStack backStack = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backStack.getItemMeta();
        backMeta.displayName(Component.text("戻る").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        backStack.setItemMeta(backMeta);
        this.inventory.setItem(27, backStack);

        ItemStack nextStack = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextStack.getItemMeta();
        nextMeta.displayName(Component.text("次へ").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        nextStack.setItemMeta(nextMeta);
        this.inventory.setItem(35, nextStack);
    }

    public void render()
    {
        int i = 0;

        for (VanilifeWorld world : VanilifeWorld.getInstances().subList(this.page * 8, Math.min((this.page + 1) * 8, VanilifeWorld.getInstances().size())))
        {
            ItemStack worldStack = new ItemStack(SeasonUtility.getSeasonMaterial(world.getSeason()));
            ItemMeta worldMeta = worldStack.getItemMeta();
            worldMeta.displayName(Component.text(world.getName()).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
            worldMeta.lore(List.of(Component.text("バージョン: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text(String.format("v%s", world.getVersion())).color(NamedTextColor.GREEN)).decoration(TextDecoration.ITALIC, false),
                    Component.text("プレイヤー: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text(String.format("%s 人", world.getOnline())).color(NamedTextColor.GREEN)).decoration(TextDecoration.ITALIC, false)));

            if (world.contains(this.player))
            {
                List<Component> lore = (worldMeta.hasLore()) ? worldMeta.lore() : null;
                lore.addAll(List.of(Component.text(""), Component.text("プレイ中！").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));
                worldMeta.lore(lore);
            }

            worldStack.setItemMeta(worldMeta);
            this.registerListener((i < 5) ? 11 + i : 20 + (i - 5), worldStack, String.format("world %s", world.getName()), ExecutionType.CLIENT);

            i ++;
        }
    }

    @Override
    public void onUiClick(InventoryClickEvent event)
    {
        super.onUiClick(event);

        ArrayList<VanilifeWorld> worlds = new ArrayList<>(VanilifeWorld.getInstances().subList(this.page * 8, Math.min((this.page + 1) * 8, VanilifeWorld.getInstances().size())).stream().toList());

        if (event.getSlot() == 27)
        {
            this.page = Math.max(0, this.page - 1);
            this.render();
        }

        if (event.getSlot() == 35)
        {
            this.page = Math.min(worlds.size() / 10, this.page + 1);
        }
    }
}
