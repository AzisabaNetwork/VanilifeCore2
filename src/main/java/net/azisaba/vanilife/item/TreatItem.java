package net.azisaba.vanilife.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TreatItem extends VanilifeItem implements FoodItem
{
    @Override
    public @NotNull String getName()
    {
        return "hw24.treat";
    }

    @Override
    public Component getDisplayName()
    {
        return Component.text("ハロウィンのお菓子🎃").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public @NotNull Material getTexture()
    {
        return Material.PUMPKIN_PIE;
    }

    @Override
    public boolean hasEnchantment()
    {
        return true;
    }

    @Override
    public void consume(@NotNull Player player)
    {
        FoodItem.super.consume(player);
    }
}
