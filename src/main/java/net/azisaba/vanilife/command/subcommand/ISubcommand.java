package net.azisaba.vanilife.command.subcommand;

import net.azisaba.vanilife.user.Sara;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ISubcommand
{
    @NotNull String getName();

    @NotNull Sara getRequirement();

    void onCommand(CommandSender sender, Command command, String label, String[] args);

    @NotNull List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args);
}
