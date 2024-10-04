package net.azisaba.vanilife.command;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RealNameCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /realname <nick>").color(NamedTextColor.RED));
            return true;
        }

        Language lang = sender instanceof Player player ? Language.getInstance(player) : Language.getInstance("ja-jp");

        User target = User.getInstances().stream()
                        .filter(u -> u.getNick().equals(args[0]))
                        .reduce((first, second) -> second)
                        .orElse(null);

        if (target == null)
        {
            sender.sendMessage(lang.translate("msg.not-found.user", "name=" + args[0]).color(NamedTextColor.RED));
            return true;
        }

        sender.sendMessage(Component.text().build());
        sender.sendMessage(Component.text(args[0] + "'s real name: ").color(NamedTextColor.GRAY).append(Component.text(target.getPlaneName()).color(NamedTextColor.GREEN)));
        sender.sendMessage(Component.text().build());

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        List<String> suggest = new ArrayList<>();
        
        if (args.length == 1)
        {
            Bukkit.getOnlinePlayers().forEach(player -> suggest.add(User.getInstance(player).getNick()));
        }
        
        return suggest;
    }
}
