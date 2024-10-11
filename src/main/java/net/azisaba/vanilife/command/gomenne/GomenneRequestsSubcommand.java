package net.azisaba.vanilife.command.gomenne;

import net.azisaba.vanilife.command.subcommand.Subcommand;
import net.azisaba.vanilife.ui.ImereqUI;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GomenneRequestsSubcommand implements Subcommand
{
    @Override
    public @NotNull String getName()
    {
        return "requests";
    }

    @Override
    public @NotNull Sara getRequirement()
    {
        return Sara.MOD;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return;
        }

        if (! UserUtility.isModerator(player))
        {
            sender.sendMessage(Component.text("You do not have sufficient permission to execute the command.").color(NamedTextColor.RED));
            return;
        }

        if (args.length != 0)
        {
            sender.sendMessage(Component.text("Correct syntax: /" + label + " requests"));
            return;
        }

        new ImereqUI(player);
    }

    @Override
    public @NotNull List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}
