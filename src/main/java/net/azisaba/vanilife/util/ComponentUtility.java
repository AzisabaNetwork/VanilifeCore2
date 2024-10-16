package net.azisaba.vanilife.util;

import net.azisaba.vanilife.gomenne.Gomenne;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.subscription.Subscriptions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentUtility
{
    private static final Pattern URL_PATTERN = Pattern.compile("(https?://\\S+)");

    private static final Pattern MENTION_PATTERN = Pattern.compile("@[a-zA-Z0-9_]{3,16}");

    public static @NotNull List<User> getMentions(@NotNull String src)
    {
        List<User> mentions = new ArrayList<>();
        Matcher matcher = ComponentUtility.MENTION_PATTERN.matcher(src);

        while (matcher.find())
        {
            final String mention = matcher.group();
            boolean exists = User.getInstances().stream().anyMatch(i -> i.getPlaneName().equals(mention.substring(1)));

            if (! exists)
            {
                continue;
            }

            mentions.add(User.getInstance(mention.substring(1)));
        }

        return mentions;
    }

    private static Pattern placeholder;

    public static @NotNull Pattern getPlaceholder()
    {
        if (ComponentUtility.placeholder != null)
        {
            return ComponentUtility.placeholder;
        }

        StringBuilder builder = new StringBuilder("(![1-4o]|&[1-9a-g]|" + ComponentUtility.MENTION_PATTERN.pattern() + "|");

        for (int i = 0; i < ChatCommand.values().length; i ++)
        {
            if (0 < i)
            {
                builder.append('|');
            }

            builder.append(ChatCommand.values()[i].name);
        }

        builder.append(')');

        ComponentUtility.placeholder = Pattern.compile(builder.toString());
        return ComponentUtility.placeholder;
    }

    public static @NotNull Component asGaming(@NotNull String src)
    {
        Component gaming = Component.text("");
        NamedTextColor[] colors = {NamedTextColor.RED, NamedTextColor.YELLOW, NamedTextColor.GREEN, NamedTextColor.AQUA, NamedTextColor.BLUE, NamedTextColor.DARK_PURPLE, NamedTextColor.LIGHT_PURPLE};

        for (int i = 0; i < src.length(); i ++)
        {
            gaming = gaming.append(Component.text(src.charAt(i)).color(colors[i % colors.length]));
        }

        return gaming;
    }

    public static @NotNull String asString(@NotNull Component component)
    {
        return LegacyComponentSerializer.legacySection().serialize(component).replace('§', '&');
    }

    public static @NotNull Component asComponent(@NotNull String src)
    {
        return LegacyComponentSerializer.legacySection().deserialize(src.replace('&', '§'));
    }

    public static @NotNull Component asChat(@NotNull User sender, @NotNull final String src)
    {
        Matcher matcher = ComponentUtility.getPlaceholder().matcher(src);
        StringBuilder builder = new StringBuilder();

        int lastMatchEnd = 0;
        boolean gomenne = false;

        while (matcher.find())
        {
            String part = src.substring(lastMatchEnd, matcher.start());

            if (Gomenne.isValid(sender, part))
            {
                builder.append(Gomenne.convert(Gomenne.hira(part)));
                gomenne = true;
            }
            else
            {
                builder.append(part);
            }

            String placeholder = matcher.group();

            if (placeholder.startsWith("@"))
            {
                String name = placeholder.substring(1);

                boolean exists = User.getInstances().stream().anyMatch(i -> i.getPlaneName().equals(name));

                if (! exists)
                {
                    builder.append(matcher.group());
                }
                else
                {
                    builder.append("§9§l").append(placeholder).append("§r");
                }
            }
            else
            {
                builder.append(matcher.group());
            }

            lastMatchEnd = matcher.end();
        }

        if (lastMatchEnd < src.length())
        {
            String remaining = src.substring(lastMatchEnd);

            if (Gomenne.isValid(sender, remaining))
            {
                builder.append(Gomenne.convert(Gomenne.hira(remaining)));
                gomenne = true;
            }
            else
            {
                builder.append(remaining);
            }
        }

        String body = builder.toString();

        if (sender.getSettings().METUBOU.isValid())
        {
            body = body.replace("!1", "(*'▽')");
            body = body.replace("!2", "(/・ω・)/");
            body = body.replace("!3", "(^^♪");
            body = body.replace("!4", "( 一一)");
            body = body.replace("!o", "082");
        }

        for (ChatCommand command : ChatCommand.values())
        {
            if (sender.getSara().level < command.level.level)
            {
                continue;
            }

            body = body.replace(command.name, ComponentUtility.asString(command.component) + "&r");
        }

        if (sender.hasSubscription(Subscriptions.NEON))
        {
            body = ChatColor.translateAlternateColorCodes('&', body);
        }

        if (gomenne)
        {
            body += " §8(" + src + "§r§8)";
        }

        return ComponentUtility.parseUrl(LegacyComponentSerializer.legacySection().deserialize(body));
    }

    public static @NotNull Component asChat(@NotNull Player sender, @NotNull final String src)
    {
        return ComponentUtility.asChat(User.getInstance(sender), src);
    }

    public static @NotNull Component parseUrl(@NotNull Component src)
    {
        TextReplacementConfig config = TextReplacementConfig.builder()
                .match(ComponentUtility.URL_PATTERN)
                .replacement((matchResult, builder) -> {
                    String url = URLDecoder.decode(matchResult.group(), StandardCharsets.UTF_8);

                    return Component.text(url)
                            .color(NamedTextColor.BLUE)
                            .decorate(TextDecoration.UNDERLINED)
                            .decoration(TextDecoration.ITALIC, false)
                            .decoration(TextDecoration.BOLD, false)
                            .decoration(TextDecoration.STRIKETHROUGH, false)
                            .decoration(TextDecoration.OBFUSCATED, false)
                            .hoverEvent(HoverEvent.showText(Component.text("Click to open url")))
                            .clickEvent(ClickEvent.openUrl(url));
                }).build();

        return src.replaceText(config);
    }

    private enum ChatCommand
    {
        YES(":yes:", Component.text("✔").color(NamedTextColor.GREEN), Sara.$500YEN),
        NO(":no:", Component.text("✖").color(NamedTextColor.RED), Sara.$500YEN),
        STAR(":star:", Component.text("✮").color(NamedTextColor.GOLD), Sara.$1000YEN),
        PEACE(":peace:", Component.text("✌").color(NamedTextColor.GREEN), Sara.$1000YEN),
        HEART(":heart:", Component.text("♥").color(NamedTextColor.RED), Sara.$2000YEN),
        CUTE(":cute:", Component.text("(").color(NamedTextColor.YELLOW).append(Component.text("✿").color(NamedTextColor.GREEN)).append(Component.text("ᴖ‿ᴖ)").color(NamedTextColor.YELLOW)), Sara.$2000YEN),
        SHRUG(":shrug:", Component.text("¯\\_(ツ)_/¯").color(NamedTextColor.YELLOW), Sara.$5000YEN),
        TABLE_FLIP(":tableflip:", Component.text("(╯°□°）╯").color(NamedTextColor.RED).append(Component.text("︵ ┻━┻").color(NamedTextColor.GRAY)), Sara.$5000YEN),
        YEY(":yey:", Component.text("ヽ (◕◡◕) ﾉ").color(NamedTextColor.GREEN), Sara.$10000YEN),
        THINKING(":thinking:", Component.text("(").color(NamedTextColor.GOLD)
                .append(Component.text("0").color(NamedTextColor.GREEN))
                .append(Component.text(".").color(NamedTextColor.GOLD))
                .append(Component.text("o").color(NamedTextColor.GREEN))
                .append(Component.text("?").color(NamedTextColor.RED))
                .append(Component.text(")").color(NamedTextColor.GOLD)), Sara.$10000YEN),
        GIMME(":gimme:", Component.text("༼つ ◕_◕ ༽つ").color(NamedTextColor.AQUA), Sara.$50000YEN),
        WIZARD(":wizard:", Component.text("(").color(NamedTextColor.YELLOW)
                .append(Component.text("'").color(NamedTextColor.AQUA))
                .append(Component.text("-").color(NamedTextColor.YELLOW))
                .append(Component.text("'").color(NamedTextColor.AQUA))
                .append(Component.text(")⊃").color(NamedTextColor.YELLOW))
                .append(Component.text("━").color(NamedTextColor.RED))
                .append(Component.text("☆ﾟ.*･｡ﾟ").color(NamedTextColor.LIGHT_PURPLE)), Sara.$50000YEN);

        private final String name;
        private final Component component;
        private final Sara level;

        ChatCommand(@NotNull String name, @NotNull Component component)
        {
            this(name, component, Sara.DEFAULT);
        }

        ChatCommand(@NotNull String name, @NotNull Component component, @NotNull Sara level)
        {
            this.name = name;
            this.component = component;
            this.level = level;
        }
    }
}
