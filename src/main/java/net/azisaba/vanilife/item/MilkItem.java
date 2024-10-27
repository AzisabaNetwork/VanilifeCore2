package net.azisaba.vanilife.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MilkItem extends VanilifeItem
{
    @Override
    public @NotNull String getName()
    {
        return "hw24.milk";
    }

    @Override
    public Component getDisplayName()
    {
        return Component.text("上質なミルク").decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public List<Component> getLore()
    {
        return List.of(Component.text("おいしい").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
    }

    @Override
    public @NotNull Material getTexture()
    {
        return Material.MILK_BUCKET;
    }

    @Override
    public boolean hasEnchantment()
    {
        return true;
    }
}
