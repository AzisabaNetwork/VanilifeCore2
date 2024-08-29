package net.azisaba.vanilife.command.skill;

import net.azisaba.vanilife.user.Sara;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public interface ICommandSkill
{
    String getName();

    Sara getRequirement();

    void onCommand(CommandSender sender, Command command, String label, String[] args);

    ArrayList<String> onTabComplete(CommandSender sender, Command command, String label, String[] args);
}
