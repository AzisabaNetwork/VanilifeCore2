package net.azisaba.vanilife.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class PumpkinItem extends VanilifeItem
{
    @Override
    public @NotNull String getName()
    {
        return "hw24.pumpkin";
    }

    @Override
    public Component getDisplayName()
    {
        return Component.text("カボチャ").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public @NotNull Material getTexture()
    {
        return Material.JACK_O_LANTERN;
    }

    @Override
    public boolean hasEnchantment()
    {
        return true;
    }
}
