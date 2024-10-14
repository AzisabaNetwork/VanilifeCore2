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
import net.kyori.adventure.text.format.TextColor;
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

    public static @NotNull Component asChat(@NotNull Player sender, @NotNull final String src)
    {
        final User user = User.getInstance(sender);
        String body = src;

        if (Gomenne.isValid(user, src))
        {
            body = Gomenne.convert(Gomenne.hira(src)) + " §8(" + src + "§r§8)";
        }

        if (user.hasSubscription(Subscriptions.NEON))
        {
            body = ChatColor.translateAlternateColorCodes('&', body);
        }

        body = LegacyComponentSerializer.legacySection().serialize(ComponentUtility.parseChat(body, user));

        return ComponentUtility.parseUrl(LegacyComponentSerializer.legacySection().deserialize(body));
    }

    public static @NotNull Component parseChat(@NotNull String src, @NotNull User user)
    {
        if (user.hasSubscription(Subscriptions.NEON))
        {
            src = ChatColor.translateAlternateColorCodes('&', src);
        }

        if (user.getSettings().METUBOU.isValid())
        {
            src = src.replace("!1", "(*'▽')");
            src = src.replace("!2", "(/・ω・)/");
            src = src.replace("!3", "(^^♪");
            src = src.replace("!4", "( 一一)");
        }

        Component component = Component.text(src).color(NamedTextColor.WHITE);

        for (ChatCommand command : ChatCommand.values())
        {
            if (user.getSara().level < command.level.level)
            {
                continue;
            }

            component = component.replaceText(builder -> builder.matchLiteral(command.name).replacement(command.component));
        }

        TextReplacementConfig config = TextReplacementConfig.builder()
                .match(ComponentUtility.MENTION_PATTERN)
                .replacement((matchResult, builder) -> {
                    final String mention = matchResult.group();
                    final String name = mention.substring(1);

                    boolean exists = User.getInstances().stream().anyMatch(i -> i.getPlaneName().equals(name));

                    if (! exists)
                    {
                        return builder;
                    }

                    return Component.text(mention).color(TextColor.color(114, 137, 218)).decorate(TextDecoration.BOLD);
                }).build();

        component = component.replaceText(config);

        return component;
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
