package net.azisaba.vanilife.command.gomenne;

import net.azisaba.vanilife.command.subcommand.Subcommand;
import net.azisaba.vanilife.gomenne.Gomenne;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.util.StringUtility;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GomenneAddSubcommand implements Subcommand
{
    @Override
    public @NotNull String getName()
    {
        return "add";
    }

    @Override
    public @NotNull Sara getRequirement()
    {
        return Sara.ADMIN;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 2)
        {
            sender.sendMessage(Component.text("Correct syntax: /" + label + " add <yomi> <kaki>").color(NamedTextColor.RED));
            return;
        }

        if (! StringUtility.isHiragana(args[0]) && ! UserUtility.isAdmin(sender))
        {
            sender.sendMessage(Component.text("「よみ」はひらがなで指定してください").color(NamedTextColor.RED));
            return;
        }

        Gomenne.register(args[0], args[1]);
        sender.sendMessage(Component.text(String.format("IMEに 「%s」を「%s」として登録しました！", args[0], args[1])).color(NamedTextColor.GREEN));
    }

    @Override
    public @NotNull List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}
