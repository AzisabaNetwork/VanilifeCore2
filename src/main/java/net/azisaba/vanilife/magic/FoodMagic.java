package net.azisaba.vanilife.magic;

import net.azisaba.vanilife.util.PlayerUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FoodMagic implements GiveMagic
{
    @Override
    public @NotNull ItemStack getItem()
    {
        return new ItemStack(Material.COOKED_BEEF, 24);
    }

    @Override
    public @NotNull List<String> getKeywords()
    {
        return List.of("おなか", "お腹", "ごはん", "ご飯", "しょくじ", "食事", "くうふく", "空腹", "たべもの", "食べ物", "しょくりょう", "食料", "にく", "肉", "すてーき", "ステーキ");
    }

    @Override
    public @NotNull List<String> getItemKeywords()
    {
        return List.of();
    }

    @Override
    public void perform(@NotNull Player player)
    {
        PlayerUtility.giveItemStack(player, this.getItem());
        player.setFoodLevel(20);
    }
}
