package net.azisaba.vanilife.command.gomenne;

import net.azisaba.vanilife.command.subcommand.Subcommand;
import net.azisaba.vanilife.gomenne.Gomenne;
import net.azisaba.vanilife.user.Sara;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GomenneRemoveSubcommand implements Subcommand
{
    @Override
    public @NotNull String getName()
    {
        return "remove";
    }

    @Override
    public @NotNull Sara getRequirement()
    {
        return Sara.MOD;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /" + label + " remove <yomi>").color(NamedTextColor.RED));
            return;
        }

        if (! Gomenne.getDictionary().containsKey(args[0]))
        {
            sender.sendMessage(Component.text(args[0] + " は IME の辞書に登録されていません").color(NamedTextColor.RED));
            return;
        }

        Gomenne.unregister(args[0]);
        sender.sendMessage(Component.text(String.format("IMEから 「%s」を削除しました", args[0])).color(NamedTextColor.GREEN));
    }

    @Override
    public @NotNull List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}
