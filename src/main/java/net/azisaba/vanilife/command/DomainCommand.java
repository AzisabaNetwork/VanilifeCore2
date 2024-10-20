package net.azisaba.vanilife.command;

import net.azisaba.vanilife.aww.Domain;
import net.azisaba.vanilife.ui.DomainRegisterUI;
import net.azisaba.vanilife.ui.DomainUI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DomainCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 0)
        {
            new DomainRegisterUI(player);
            return true;
        }

        User user = User.getInstance(player);

        Domain domain = Domain.getInstance(Domain.reverse(args[0]));

        if (domain == null || domain.getRegistrant() != user)
        {
            sender.sendMessage(Language.translate("cmd.domain.access-error", player).color(NamedTextColor.RED));
            return true;
        }

        new DomainUI(player, domain);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player) || args.length != 1)
        {
            return List.of();
        }

        User user = User.getInstance(player);

        List<String> suggest = new ArrayList<>();
        user.getDomains().stream()
                .filter(domain -> domain.getUrl().startsWith(args[0]))
                .forEach(domain -> suggest.add(domain.getUrl()));
        return suggest;
    }
}
