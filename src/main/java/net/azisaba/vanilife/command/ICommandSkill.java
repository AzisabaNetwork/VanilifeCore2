package net.azisaba.vanilife.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public interface ICommandSkill
{
    String getName();

    boolean isOpCommand();

    void onCommand(CommandSender sender, Command command, String label, String[] args);

    ArrayList<String> onTabComplete(CommandSender sender, Command command, String label, String[] args);
}
