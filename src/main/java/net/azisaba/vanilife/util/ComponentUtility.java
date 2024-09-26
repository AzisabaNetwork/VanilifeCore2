package net.azisaba.vanilife.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final Pattern URL_PATTERN = Pattern.compile("(https?://\\S+)");

    public static @NotNull Component toLink(@NotNull Component src)
    {
        String plane = ((TextComponent) src).content();
        Matcher matcher = ComponentUtility.URL_PATTERN.matcher(plane);

        TextComponent.Builder builder = Component.text();

        int lastEnd = 0;

        while (matcher.find())
        {
            String url = matcher.group(1);

            builder.append(Component.text(plane.substring(lastEnd, matcher.start())).color(src.color()));
            builder.append(Component.text(url).color(NamedTextColor.BLUE).decorate(TextDecoration.UNDERLINED).hoverEvent(HoverEvent.showText(Component.text("Click to open url."))).clickEvent(ClickEvent.openUrl(url)));

            lastEnd = matcher.end();
        }

        builder.append(Component.text(plane.substring(lastEnd)).color(src.color()));
        return builder.build();
    }
}
