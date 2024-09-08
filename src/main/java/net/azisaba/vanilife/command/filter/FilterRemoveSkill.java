package net.azisaba.vanilife.command.filter;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.command.skill.ICommandSkill;
import net.azisaba.vanilife.user.Sara;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FilterRemoveSkill implements ICommandSkill
{
    @Override
    @NotNull
    public String getName()
    {
        return "remove";
    }

    @Override
    @NotNull
    public Sara getRequirement()
    {
        return Sara.MOD;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /filter remove <string>").color(NamedTextColor.RED));
            return;
        }

        if (! Vanilife.filter.getFilters().contains(args[0]))
        {
            sender.sendMessage(Component.text(String.format("%s is not a NG word.", args[0])).color(NamedTextColor.RED));
            return;
        }

        Vanilife.filter.unregister(args[0]);
        sender.sendMessage(Component.text(String.format("%s has been removed from the NG word list.", args[0])).color(NamedTextColor.GREEN));
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return Vanilife.filter.getFilters();
    }
}
