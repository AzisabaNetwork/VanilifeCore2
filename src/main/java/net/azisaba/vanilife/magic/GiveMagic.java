package net.azisaba.vanilife.magic;

import net.azisaba.vanilife.util.PlayerUtility;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface GiveMagic extends Magic
{
    @NotNull ItemStack getItem();

    @Override
    default @NotNull List<String> getKeywords()
    {
        List<String> keywords = new ArrayList<>(this.getItemKeywords());
        keywords.addAll(List.of("ほしい", "欲しい", "ください", "くれ", "よこせ", "わたせ", "ちょうだい"));
        return keywords;
    }

    @NotNull List<String> getItemKeywords();

    @Override
    default void perform(@NotNull Player player)
    {
        PlayerUtility.giveItemStack(player, this.getItem());
    }
}
