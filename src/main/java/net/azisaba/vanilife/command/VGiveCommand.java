package net.azisaba.vanilife.command;

import net.azisaba.vanilife.item.VanilifeItem;
import net.azisaba.vanilife.item.VanilifeItems;
import net.azisaba.vanilife.util.MathUtility;
import net.azisaba.vanilife.util.PlayerUtility;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.util.Watch;
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

public class VGiveCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (! UserUtility.isAdmin(player))
        {
            sender.sendMessage(Component.text("You do not have sufficient permission to execute the command.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length < 2 || 3 < args.length || (args.length == 3 && ! MathUtility.isInt(args[2])))
        {
            sender.sendMessage(Component.text("Correct syntax: /vgive <player> <item> [amount]").color(NamedTextColor.RED));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null)
        {
            sender.sendMessage(Component.text(args[0] + " は現在オフラインです").color(NamedTextColor.RED));
            return true;
        }

        int amount = args.length == 3 ? Integer.parseInt(args[2]) : 1;

        VanilifeItem item = VanilifeItems.registry.get(args[1]);

        if (item == null)
        {
            sender.sendMessage(Component.text(args[0] + " は未定義のアイテムです").color(NamedTextColor.RED));
            return true;
        }

        PlayerUtility.giveItemStack(target, item.asItemStack(amount));
        sender.sendMessage(Component.text(String.format("%s に %s を%s個与えました", args[0], args[1], amount)));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player) || ! UserUtility.isAdmin(sender))
        {
            return List.of();
        }

        List<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Bukkit.getOnlinePlayers().stream()
                    .filter(player -> ! Watch.isWatcher(player))
                    .forEach(player -> suggest.add(player.getName()));
        }

        if (args.length == 2)
        {
            VanilifeItems.registry.values().forEach(item -> suggest.add(item.getName()));
        }

        return suggest;
    }
}
