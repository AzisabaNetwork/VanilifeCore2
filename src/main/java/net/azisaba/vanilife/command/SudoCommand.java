package net.azisaba.vanilife.command;

import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.UserUtility;
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

public class SudoCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length < 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /sudo <command>").color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);

        Sara old = user.getSara();
        Sara permission = UserUtility.calculateSara(player, false);

        if (! permission.isPermission())
        {
            sender.sendMessage(Component.text("昇格可能な権限が見つかりませんでした").color(NamedTextColor.RED));
            return true;
        }

        user.setSara(permission);
        String cmd = String.join(" ", args);

        Bukkit.dispatchCommand(player, cmd);

        user.setSara(old);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! sender.isOp() || args.length == 0)
        {
            return List.of();
        }

        List<String> suggest = new ArrayList<>();

        String commandPart = args[0].toLowerCase();

        for (Command cmd : Bukkit.getServer().getCommandMap().getKnownCommands().values())
        {
            if (cmd.getName().startsWith(commandPart) && args.length == 1)
            {
                suggest.add(cmd.getName());
            }
        }

        return suggest;
    }
}
