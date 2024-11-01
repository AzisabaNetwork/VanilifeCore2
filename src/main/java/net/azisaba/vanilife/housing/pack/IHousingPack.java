package net.azisaba.vanilife.housing.pack;

import net.azisaba.vanilife.ui.Language;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface IHousingPack
{
    @NotNull String getName();

    @NotNull default Component getDefaultName(@NotNull Language lang)
    {
        return lang.translate("housing.pack." + this.getName() + ".name");
    }

    @NotNull Material getIcon();

    @NotNull default List<Component> getDetails(@NotNull Language lang)
    {
        List<Component> details = new ArrayList<>();

        final String translation = "housing.pack." + this.getName();

        if (lang.has(translation + ".details.1"))
        {
            int i = 1;

            while (lang.has(translation + ".details." + i))
            {
                details.add(lang.translate(translation + ".details." + i).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
                i ++;
            }

            details.add(Component.text().build());
        }

        details.add(lang.translate("housing.pack.cost").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text(this.getCost() + " Mola").color(NamedTextColor.YELLOW)));
        details.add(Component.text().build());

        details.add(lang.translate("housing.pack.include").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));

        int i = 0;

        for (Material material : this.getMaterials())
        {
            if (10 < i)
            {
                details.add(Component.text().build());
                details.add(lang.translate("housing.pack.and-more").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
                break;
            }

            details.add(Component.translatable("block.minecraft." + material.name().toLowerCase()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));

            i ++;
        }

        return details;
    }

    int getCost();

    @NotNull List<Material> getMaterials();

    default @NotNull List<Tag<Material>> getTags()
    {
        return List.of();
    }

    default boolean include(Material material)
    {
        return this.getMaterials().contains(material) || this.getTags().stream().anyMatch(tag -> tag.isTagged(material));
    }

    default boolean include(Block block)
    {
        return this.include(block.getType());
    }
}
