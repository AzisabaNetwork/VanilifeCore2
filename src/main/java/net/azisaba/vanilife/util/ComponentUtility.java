package net.azisaba.vanilife.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@Utility
public class ComponentUtility
{
    public static Component getAsGaming(String src)
    {
        Component gaming = Component.text("");
        NamedTextColor[] colors = {NamedTextColor.RED, NamedTextColor.YELLOW, NamedTextColor.GREEN, NamedTextColor.AQUA, NamedTextColor.BLUE, NamedTextColor.DARK_PURPLE, NamedTextColor.LIGHT_PURPLE};

        for (int i = 0; i < src.length(); i ++)
        {
            gaming = gaming.append(Component.text(src.charAt(i)).color(colors[i % colors.length]));
        }

        return gaming;
    }
}
