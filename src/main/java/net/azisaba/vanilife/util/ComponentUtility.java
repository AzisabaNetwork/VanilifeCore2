package net.azisaba.vanilife.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

@Utility
public class ComponentUtility
{
    public static @NotNull Component getAsGaming(@NotNull String src)
    {
        Component gaming = Component.text("");
        NamedTextColor[] colors = {NamedTextColor.RED, NamedTextColor.YELLOW, NamedTextColor.GREEN, NamedTextColor.AQUA, NamedTextColor.BLUE, NamedTextColor.DARK_PURPLE, NamedTextColor.LIGHT_PURPLE};

        for (int i = 0; i < src.length(); i ++)
        {
            gaming = gaming.append(Component.text(src.charAt(i)).color(colors[i % colors.length]));
        }

        return gaming;
    }

    public static @NotNull String getAsString(@NotNull Component component)
    {
        return LegacyComponentSerializer.legacySection().serialize(component).replace('§', '&');
    }

    public static @NotNull Component getAsComponent(@NotNull String src)
    {
        return LegacyComponentSerializer.legacySection().deserialize(src.replace('&', '§'));
    }
}
