package net.azisaba.vanilife.command;

import net.azisaba.vanilife.ui.Language;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WikiCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        Language lang = sender instanceof Player player ? Language.getInstance(player) : Language.getInstance("ja-jp");

        sender.sendMessage(Component.text().build());
        sender.sendMessage(lang.translate("wiki.title").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD));
        sender.sendMessage(lang.translate("wiki.details").color(NamedTextColor.YELLOW));
        sender.sendMessage(Component.text().build());

        for (Wiki wiki : Wiki.values())
        {
            sender.sendMessage(Component.space()
                    .append(Component.text("*").color(NamedTextColor.RED)).appendSpace()
                    .append(wiki.getDisplayName(lang)
                            .hoverEvent(HoverEvent.showText(lang.translate("wiki.click-to-select").color(NamedTextColor.LIGHT_PURPLE)))
                            .clickEvent(ClickEvent.openUrl(wiki.getUrl()))));
        }

        sender.sendMessage(Component.text().build());
        sender.sendMessage(lang.translate("wiki.help")
                .hoverEvent(lang.translate("wiki.help.click-to-join").color(TextColor.color(88, 101, 242)))
                .clickEvent(ClickEvent.openUrl("https://discord.gg/azisaba")));
        sender.sendMessage(Component.text().build());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }

    private enum Wiki
    {
        MAIN_PAGE("main_page", "https://wiki.azisaba.net/wiki/ばにらいふ２！:メインページ"),
        MULTI("multi", "https://wiki.azisaba.net/wiki/ばにらいふ２！:メインページ#マルチ"),
        SURVIVAL("survival", "https://wiki.azisaba.net/wiki/ばにらいふ２！:メインページ#サバイバル"),
        CHAT("chat", "https://wiki.azisaba.net/wiki/ばにらいふ２！:チャット"),
        PLOT("plot", "https://wiki.azisaba.net/wiki/ばにらいふ２！:土地の保護"),
        VRVC("vrvc", "https://wiki.azisaba.net/wiki/ばにらいふ２！:VRVoiceChat"),
        HOUSING("housing", "https://wiki.azisaba.net/wiki/ばにらいふ２！:Housing"),
        WORLD("world", "https://wiki.azisaba.net/wiki/ばにらいふ２！:メインページ#ワールド");

        private final String name;
        private final String url;

        Wiki(@NotNull String name, @NotNull String url)
        {
            this.name = name;
            this.url = url;
        }

        public @NotNull String getName()
        {
            return this.name;
        }

        public @NotNull Component getDisplayName(@NotNull Language lang)
        {
            return lang.translate("wiki." + this.name).color(NamedTextColor.AQUA);
        }

        public @NotNull String getUrl()
        {
            return this.url;
        }
    }
}
